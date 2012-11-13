package ru.crazyproger.plugins.webtoper;

import com.google.gson.Gson;
import com.intellij.diagnostic.DiagnosticBundle;
import com.intellij.diagnostic.IdeErrorsDialog;
import com.intellij.diagnostic.ReportMessages;
import com.intellij.diagnostic.errordialog.Attachment;
import com.intellij.errorreport.bean.ErrorBean;
import com.intellij.ide.DataManager;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.idea.IdeaLogger;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.progress.EmptyProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.updateSettings.impl.UpdateSettings;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Consumer;
import com.intellij.util.SystemProperties;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.nls.NlsBundle;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

/**
 * @author crazyproger
 */
public class ErrorSubmitter extends ErrorReportSubmitter {
    public static final String ENCODING = "UTF8";
    public static final String SUBMIT_URL = "https://script.google.com/macros/s/AKfycbxhP9JtHwKbvTFfV-AYgRxXyOTOLH0wgySwtv24lPzmcA5ae2ae/exec";
    public static final String POST_DELIMITER = "&";
    public static final String DONE = "Done";
    private static final String HTTP_CONTENT_LENGTH = "Content-Length";
    private static final String HTTP_CONTENT_TYPE = "Content-Type";
    private static final String HTTP_WWW_FORM = "application/x-www-form-urlencoded";
    private static final String HTTP_POST = "POST";
    private static final String KEY = "0AoJktm18KZeydGNfUnpTUGJPTjhoYXltZlZISVVOTXc";

    // adopted copy-paste from com.intellij.errorreport.itn.ITNProxy
    private static Map<String, String> createParametersFor(ErrorBean error,
                                                           String compilationTimestamp, Application application, ApplicationInfoEx appInfo,
                                                           ApplicationNamesInfo namesInfo,
                                                           UpdateSettings updateSettings) {
        @NonNls Map<String, String> params = new HashMap<String, String>();

        params.put("os.name", SystemProperties.getOsName());

        params.put("java.version", SystemProperties.getJavaVersion());
        params.put("java.vm.vendor", SystemProperties.getJavaVmVendor());

        params.put("app.name", namesInfo.getProductName());
        params.put("app.name.full", namesInfo.getFullProductName());
        params.put("app.name.version", appInfo.getVersionName());
        params.put("app.eap", Boolean.toString(appInfo.isEAP()));
        params.put("app.internal", Boolean.toString(application.isInternal()));
        params.put("app.build", appInfo.getBuild().asString());
        params.put("app.version.major", appInfo.getMajorVersion());
        params.put("app.version.minor", appInfo.getMinorVersion());
        params.put("app.build.date", format(appInfo.getBuildDate()));
        params.put("app.build.date.release", format(appInfo.getMajorReleaseBuildDate()));
        params.put("app.compilation.timestamp", compilationTimestamp);

        params.put("update.channel.status", updateSettings.getSelectedChannelStatus().getCode());
        params.put("update.ignored.builds", StringUtil.join(updateSettings.getIgnoredBuildNumbers(), ","));

        params.put("plugin.name", error.getPluginName());
        params.put("plugin.version", error.getPluginVersion());

        params.put("last.action", error.getLastAction());

        params.put("error.message", error.getMessage());
        params.put("error.stacktrace", error.getStackTrace());

        params.put("error.description", error.getDescription());

        List<Attachment> attachments = error.getAttachments();
        for (int i = 0; i < attachments.size(); i++) {
            Attachment attachment = attachments.get(i);
            params.put("attachment.name" + i, attachment.getName());
            params.put("attachment.value" + i, attachment.getEncodedBytes());
        }

        return params;
    }

    private static String format(Calendar calendar) {
        return calendar == null ? null : Long.toString(calendar.getTime().getTime());
    }

    private static HttpURLConnection post(URL url, byte[] bytes) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(10 * 1000);
        connection.setConnectTimeout(10 * 1000);
        connection.setRequestMethod(HTTP_POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty(HTTP_CONTENT_TYPE, String.format("%s; charset=%s", HTTP_WWW_FORM, ENCODING));
        connection.setRequestProperty(HTTP_CONTENT_LENGTH, Integer.toString(bytes.length));

        OutputStream out = new BufferedOutputStream(connection.getOutputStream());
        try {
            out.write(bytes);
            out.flush();
        } finally {
            out.close();
        }

        return connection;
    }

    private static String toJson(Map<String, String> params) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        return gson.toJson(params);
    }

    private static byte[] join(java.util.List<Pair<String, String>> params) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();

        Iterator<Pair<String, String>> it = params.iterator();

        while (it.hasNext()) {
            Pair<String, String> param = it.next();

            if (StringUtil.isEmpty(param.first))
                throw new IllegalArgumentException(param.toString());

            if (StringUtil.isNotEmpty(param.second))
                builder.append(param.first).append("=").append(URLEncoder.encode(param.second, ENCODING));

            if (it.hasNext())
                builder.append(POST_DELIMITER);
        }

        return builder.toString().getBytes();
    }

    private static String readFrom(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c;
        while ((c = is.read()) != -1) {
            out.write(c);
        }
        String s = out.toString();
        out.close();
        return s;
    }

    @Override
    public String getReportActionText() {
        return NlsBundle.message("nls.report.action.text");
    }

    @Override
    public SubmittedReportInfo submit(IdeaLoggingEvent[] events, Component parentComponent) {
        // obsolete API
        return new SubmittedReportInfo(null, "0", SubmittedReportInfo.SubmissionStatus.FAILED);
    }

    @Override
    public boolean trySubmitAsync(final IdeaLoggingEvent[] events, final String additionalInfo, Component parentComponent, Consumer<SubmittedReportInfo> consumer) {
        final DataContext dataContext = DataManager.getInstance().getDataContext(parentComponent);
        final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        Task.Backgroundable task = new Task.Backgroundable(project, DiagnosticBundle.message("title.submitting.error.report")) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                sendReport(getErrorBean(additionalInfo, events[0]), project);
            }
        };
        if (project == null) {
            task.run(new EmptyProgressIndicator());
        } else {
            ProgressManager.getInstance().run(task);
        }
        return true;
    }

    private void sendReport(ErrorBean errorBean, Project project) {

        @NonNls Map<String, String> errorInfo = createParametersFor(errorBean,
                IdeaLogger.getOurCompilationTimestamp(),
                ApplicationManager.getApplication(),
                (ApplicationInfoEx) ApplicationInfo.getInstance(),
                ApplicationNamesInfo.getInstance(),
                UpdateSettings.getInstance());
        String json;
        try {
            json = toJson(errorInfo);
        } catch (UnsupportedEncodingException e) {
            IdeaLogger.getInstance(this.getClass()).error(e);
            json = "{\"UnsupportedEncodingException\":\"" + e.getMessage() + "\"}";
        }

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<String, String>("data", json));
        params.add(new Pair<String, String>("key", KEY));

        SubmittedReportInfo.SubmissionStatus status = SubmittedReportInfo.SubmissionStatus.FAILED;
        HttpURLConnection connection;
        try {
            connection = post(new URL(SUBMIT_URL), join(params));
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String reply = getReply(connection);
                if (StringUtil.startsWith(reply, DONE)) {
                    status = SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
                } else {
                    IdeaLogger.getInstance(this.getClass()).error("Submission response:" + reply);
                }
            }
        } catch (IOException ignored) {
        }

        showNotification(status, project);
    }

    private String getReply(HttpURLConnection connection) throws IOException {
        InputStream is = new BufferedInputStream(connection.getInputStream());
        try {
            return readFrom(is);
        } finally {
            is.close();
        }
    }

    private ErrorBean getErrorBean(String additionalInfo, IdeaLoggingEvent event) {
        ErrorBean errorBean = new ErrorBean(event.getThrowable(), IdeaLogger.ourLastActionId);
        errorBean.setDescription(additionalInfo);
        errorBean.setMessage(event.getMessage());
        Throwable t = event.getThrowable();
        if (t != null) {
            final PluginId pluginId = IdeErrorsDialog.findPluginId(t);
            if (pluginId != null) {
                final IdeaPluginDescriptor ideaPluginDescriptor = PluginManager.getPlugin(pluginId);
                if (ideaPluginDescriptor != null && !ideaPluginDescriptor.isBundled()) {
                    errorBean.setPluginName(ideaPluginDescriptor.getName());
                    errorBean.setPluginVersion(ideaPluginDescriptor.getVersion());
                }
            }
        }
        return errorBean;
    }

    private void showNotification(final SubmittedReportInfo.SubmissionStatus status, final Project project) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                StringBuilder text = new StringBuilder("<html>");
                if (status == SubmittedReportInfo.SubmissionStatus.FAILED) {
                    text.append(DiagnosticBundle.message("error.list.message.submission.failed"));
                } else {
                    text.append(DiagnosticBundle.message("error.report.gratitude"));
                }
                text.append("</html>");
                NotificationType type = status == SubmittedReportInfo.SubmissionStatus.FAILED
                        ? NotificationType.ERROR
                        : NotificationType.INFORMATION;
                ReportMessages.GROUP.createNotification(ReportMessages.ERROR_REPORT,
                        text.toString(),
                        type, null).setImportant(false).notify(project);
            }
        });
    }
}

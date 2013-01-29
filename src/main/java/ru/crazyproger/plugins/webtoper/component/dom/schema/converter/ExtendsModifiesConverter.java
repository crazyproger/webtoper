package ru.crazyproger.plugins.webtoper.component.dom.schema.converter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazyproger.plugins.webtoper.Utils;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Config;
import ru.crazyproger.plugins.webtoper.component.dom.schema.Scope;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.IdentifiedById;
import ru.crazyproger.plugins.webtoper.component.dom.schema.pseudo.PrimaryElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * todo tests
 * todo refactor
 *
 * @author crazyproger
 */
public class ExtendsModifiesConverter extends ResolvingConverter<PrimaryElement> {
    public static final Pattern EXTENDS_PATTERN = Pattern.compile("\\s*(\\w+)\\s*:\\s*/?(.+)\\s*");

    @NotNull
    @Override
    public Collection<? extends PrimaryElement> getVariants(ConvertContext context) {
        GlobalSearchScope scope = Utils.getWebRootsScope(context.getModule());
        List<DomFileElement<Config>> fileElements = DomService.getInstance().getFileElements(Config.class, context.getProject(), scope);
        final List<PrimaryElement> result = new ArrayList<PrimaryElement>();
        final PrimaryElement currentPrimaryElement = context.getInvocationElement().getParentOfType(PrimaryElement.class, false);
        assert currentPrimaryElement != null;
        for (DomFileElement<Config> fileElement : fileElements) {
            List<Scope> scopes = fileElement.getRootElement().getScopes();
            for (Scope domScope : scopes) {
                domScope.acceptChildren(new DomElementVisitor() {
                    @Override
                    public void visitDomElement(DomElement element) {
                    }

                    public void visitPrimaryElement(PrimaryElement element) {
                        if (currentPrimaryElement.getClass().equals(element.getClass())) {
                            if (StringUtils.isBlank(element.getModifiesValue().getStringValue())) {
                                result.add(element);
                            }
                        }
                    }
                });

            }
        }
        return result;
    }

    @Nullable
    @Override
    public PrimaryElement fromString(@Nullable @NonNls String s, ConvertContext context) {
        // todo parse another variants of references (p40)
        Matcher matcher = ExtendsModifiesConverter.EXTENDS_PATTERN.matcher(s);
        if (!matcher.matches()) return null;

        final Project project = context.getProject();
        String configPath = matcher.group(2);
        VirtualFile config = Utils.findFileInArtifact(configPath, project);
        if (config == null) return null;

        final PsiFile file;
        file = PsiManager.getInstance(project).findFile(config);
        if (!(file instanceof XmlFile)) return null;

        final String componentId = matcher.group(1);
        DomFileElement<Config> fileDescription = DomManager.getDomManager(project).getFileElement((XmlFile) file, Config.class);
        assert fileDescription != null;
        List<Scope> scopes = fileDescription.getRootElement().getScopes();

        final PrimaryElement[] result = {null};
        for (Scope domScope : scopes) {
            domScope.acceptChildren(new DomElementVisitor() {
                @Override
                public void visitDomElement(DomElement element) {
                }

                public void visitPrimaryElement(PrimaryElement element) {
                    if (element instanceof IdentifiedById) {
                        IdentifiedById byId = (IdentifiedById) element;
                        if (componentId.equals(byId.getId().getValue())) {
                            result[0] = element;
                        }

                    }
                }
            });

        }
        return result[0];
    }

    @Nullable
    @Override
    public String toString(@Nullable PrimaryElement primaryElement, ConvertContext context) {
        if (primaryElement == null) {
            return null;
        }
        VirtualFile virtualFile = primaryElement.getXmlTag().getContainingFile().getVirtualFile();
        String pathInArtifact = Utils.findPathInArtifact(virtualFile, context.getModule());
        if (pathInArtifact == null) {
            return null;
        }
        if (primaryElement instanceof IdentifiedById) {
            IdentifiedById byId = (IdentifiedById) primaryElement;
            String id = byId.getId().getValue();
            if (id == null) {
                return null; // bad defined component or with 'modifies'
            }
            return id + ":" + pathInArtifact;
        }
        return pathInArtifact;
    }
}

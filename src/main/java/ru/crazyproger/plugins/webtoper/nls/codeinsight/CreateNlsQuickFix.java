/*
 * Copyright 2013 Vladimir Rudev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.crazyproger.plugins.webtoper.nls.codeinsight;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import ru.crazyproger.plugins.webtoper.WebtoperBundle;
import ru.crazyproger.plugins.webtoper.config.WebtoperFacet;
import ru.crazyproger.plugins.webtoper.nls.NlsUtils;

import java.io.IOException;

class CreateNlsQuickFix implements LocalQuickFix {

    private static final Logger LOG = Logger.getInstance("#" + CreateNlsQuickFix.class.getName());

    private final String nlsName;
    private WebtoperFacet facet;

    public CreateNlsQuickFix(String nlsName, WebtoperFacet facet) {
        this.nlsName = nlsName;
        this.facet = facet;
    }

    @NotNull
    @Override
    public String getName() {
        return WebtoperBundle.message("create.nls.file.quickfix.text", nlsName);
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return WebtoperBundle.message("create.nls.file.quickfix.familyName");
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        if (!facet.isValid()) return;
        final String nameText = nlsName;


        final VirtualFile nlsRoot = facet.getNlsRoot(); // todo #WT-24
        assert nlsRoot != null;
        assert nlsRoot.isValid();

        VirtualFile child = nlsRoot.findFileByRelativePath(NlsUtils.nlsNameToPath(nameText, "/"));
        assert child == null : "QuickFix called on unresolved name, but file exists!";

        final String[] chunks = NlsUtils.nlsNameToPathChunks(nameText);
        if (ArrayUtils.isEmpty(chunks)) return;

        VirtualFile createdFile = ApplicationManager.getApplication().runWriteAction(new Computable<VirtualFile>() {
            @Override
            public VirtualFile compute() {
                try {
                    return createNlsByPath(nlsRoot, chunks);
                } catch (IOException e) {
                    LOG.error("On create nls ''{0}''", e, nameText);
                }
                return null;
            }
        });

        if (createdFile != null) {
            OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, createdFile, 0);
            FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true);
        }
    }

    private VirtualFile createNlsByPath(@NotNull VirtualFile nlsRoot, @NotNull String[] chunks) throws IOException {
        VirtualFile currentFolder = nlsRoot;
        for (int i = 0; i < chunks.length - 1; i++) {
            String chunk = chunks[i];
            VirtualFile nextFolder = currentFolder.findChild(chunk);
            if (nextFolder == null) {
                nextFolder = currentFolder.createChildDirectory(this, chunk);
            }
            currentFolder = nextFolder;
        }
        //noinspection ConstantConditions
        return currentFolder.createChildData(this, ArrayUtil.getLastElement(chunks));
    }
}

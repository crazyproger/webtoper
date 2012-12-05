/*
 * Copyright 2012 Vladimir Rudev
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

package ru.crazyproger.plugins.webtoper;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import com.intellij.util.FileContentUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author crazyproger
 */
public class Utils {
    public static void reparseFilesInRoots(Project project, Iterable<VirtualFile> roots, String extension) {
        List<VirtualFile> files = new LinkedList<VirtualFile>();
        for (VirtualFile file : roots) {
            if (file.isDirectory()) {
                PsiDirectory directory = PsiManager.getInstance(project).findDirectory(file);
                if (directory != null) {
                    GlobalSearchScope scope = GlobalSearchScopes.directoryScope(directory, true);
                    files.addAll(FilenameIndex.getAllFilesByExt(project, extension, scope));
                }
            } else {
                files.add(file);
            }
        }
        FileContentUtil.reparseFiles(project, files, true);
    }
}

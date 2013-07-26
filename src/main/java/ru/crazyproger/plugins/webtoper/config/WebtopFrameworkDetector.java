package ru.crazyproger.plugins.webtoper.config;

import com.google.common.collect.Lists;
import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WebtopFrameworkDetector extends FacetBasedFrameworkDetector<WebtoperFacet, WebtoperFacetConfiguration> {

    public static final String LAYER_FILE_NAME = "app.xml";
    public static final String ROOT_TAG = "config";

    public WebtopFrameworkDetector() {
        super(WebtopFrameworkDetector.class.getSimpleName());
    }

    @Override
    public FacetType<WebtoperFacet, WebtoperFacetConfiguration> getFacetType() {
        return WebtoperFacet.getFacetType();
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return XmlFileType.INSTANCE;
    }

    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
        return FileContentPattern.fileContent().withName(LAYER_FILE_NAME).xmlWithRootTag(ROOT_TAG);
    }

    @NotNull
    @Override
    public List<Pair<WebtoperFacetConfiguration, Collection<VirtualFile>>> createConfigurations(
            @NotNull Collection<VirtualFile> files,
            @NotNull Collection<WebtoperFacetConfiguration> existentFacetConfigurations) {
        List<Pair<WebtoperFacetConfiguration, Collection<VirtualFile>>> result = Lists.newArrayList();
        if (files.isEmpty()) return result;

        for (VirtualFile file : files) {
            VirtualFile fileParent = file.getParent();
            if (isLayerRoot(fileParent, existentFacetConfigurations)) continue;
            WebtoperFacetConfiguration configuration = getFacetType().createDefaultConfiguration();
            configuration.setFacetRoot(fileParent);
            configuration.setSuggestedName(fileParent.getPresentableName());
            result.add(new Pair<WebtoperFacetConfiguration, Collection<VirtualFile>>(configuration, Arrays.asList(file)));
        }

        return result;
    }

    private boolean isLayerRoot(VirtualFile file, Collection<WebtoperFacetConfiguration> existentFacetConfigurations) {
        for (WebtoperFacetConfiguration configuration : existentFacetConfigurations) {
            VirtualFile facetRoot = configuration.getFacetRoot();
            if (file.equals(facetRoot)) {
                return true;
            }
        }
        return false;
    }
}

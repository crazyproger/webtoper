package ru.crazyproger.plugins.webtoper.config;

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import org.jetbrains.annotations.NotNull;

public class WebtopFrameworkDetector extends FacetBasedFrameworkDetector<WebtoperFacet, WebtoperFacetConfiguration> {

    public WebtopFrameworkDetector(String detectorId) {
        super(detectorId);
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
        return null;
    }
}

package de.jensd.fx.glyphs.demo.browser;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphIcons;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 *
 * @author Jens Deters
 */
public class GlyphPack {

    public enum Type {
        FONT_AWESOME, OCTICON, MATERIAL_DESIGN_ICONS, MATERIAL_ICONS, WEATHER_ICONS, ICONS525;
    }

    private final FontInfo fontInfo;
    private final ObservableList<GlyphIcon> glyphNodesList;

    public GlyphPack(FontInfo fontInfo, ObservableList<GlyphIcon> glyphNodesList) {
        this.fontInfo = fontInfo;
        this.glyphNodesList = glyphNodesList;
    }

    public FontInfo getFontInfo() {
        return fontInfo;
    }

    public ObservableList<GlyphIcon> getGlyphNodes() {
        return glyphNodesList;
    }

    public int getNumberOfIcons() {
        return getGlyphNodes().size();
    }

    public String getName() {
        return fontInfo.getName();
    }

    public String getFamiliy() {
        return fontInfo.getFamiliy();
    }

    public String getVersion() {
        return fontInfo.getVersion();
    }

    public String getReleaseDate() {
        return fontInfo.getReleaseDate();
    }

    public String getURL() {
        return fontInfo.getURL();
    }

    public String getDescription() {
        return fontInfo.getDescription();
    }

    public String getWhatsNew() {
        return fontInfo.getWhatsNew();
    }

    @Override
    public String toString() {
        return getName();
    }

}

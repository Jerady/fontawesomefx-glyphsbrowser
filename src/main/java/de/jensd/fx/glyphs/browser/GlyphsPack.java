/**
 * Copyright (c) 2016 Jens Deters http://www.jensd.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */
package de.jensd.fx.glyphs.browser;

import de.jensd.fx.glyphs.GlyphIcon;
import javafx.collections.ObservableList;

/**
 *
 * @author Jens Deters
 */
public class GlyphsPack {

    public enum Type {
        FONT_AWESOME, OCTICON, MATERIAL_DESIGN_ICONS, MATERIAL_ICONS, WEATHER_ICONS, ICONS525;
    }

    private final FontInfo fontInfo;
    private final ObservableList<GlyphIcon> glyphNodesList;

    public GlyphsPack(FontInfo fontInfo, ObservableList<GlyphIcon> glyphNodesList) {
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

    public String getLicense() {
        return fontInfo.getLicense();
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

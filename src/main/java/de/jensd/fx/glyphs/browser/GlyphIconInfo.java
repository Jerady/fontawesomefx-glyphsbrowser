package de.jensd.fx.glyphs.browser;

import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * Copyright (c) 2016 Jens Deters http://www.jensd.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
/**
 *
 * @author Jens Deters
 */
public class GlyphIconInfo {

    private final String glyphNameName;
    private final String glyphUnicode;
    private final String glyphCode;
    private final String glyphFactoryCode;
    private final ObservableList<Node> previewGlyphs;

    public GlyphIconInfo(String glyphNameName, String glyphUnicode, String glyphCode, String glyphFactoryCode, ObservableList<Node> previewGlyphs) {
        this.glyphNameName = glyphNameName;
        this.glyphUnicode = glyphUnicode;
        this.glyphCode = glyphCode;
        this.glyphFactoryCode = glyphFactoryCode;
        this.previewGlyphs = previewGlyphs;
    }

    public String getGlyphNameName() {
        return glyphNameName;
    }

    public String getGlyphUnicode() {
        return glyphUnicode;
    }

    public String getGlyphCode() {
        return glyphCode;
    }

    public String getGlyphFactoryCode() {
        return glyphFactoryCode;
    }

    public ObservableList<Node> getPreviewGlyphs() {
        return previewGlyphs;
    }
}

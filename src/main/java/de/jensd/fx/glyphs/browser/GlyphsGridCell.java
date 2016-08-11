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
import javafx.geometry.Pos;
import org.controlsfx.control.GridCell;

/**
 *
 * @author Jens Deters
 */
public class GlyphsGridCell extends GridCell<GlyphIcon> implements SelectableNode{

    public GlyphsGridCell() {
        init();
    }

    private void init() {
        getStyleClass().add("glyph-grid-cell");
        setAlignment(Pos.CENTER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateItem(GlyphIcon item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(item);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}

package de.jensd.fx.glyphs.demo.browser;

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

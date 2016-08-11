package de.jensd.fx.glyphs.demo.browser;

import de.jensd.fx.glyphs.GlyphIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jens Deters
 */
public class SelectableGlyphView extends VBox implements SelectableNode {

    private BooleanProperty selectedProperty;
    private final GlyphIcon glyphIcon;

    public SelectableGlyphView(GlyphIcon glyphIcon) {
        this.glyphIcon = glyphIcon;
        init();
    }
    
    private void init(){
                getChildren().add(glyphIcon);
        setAlignment(Pos.CENTER);
        resize();
        glyphIcon.glyphSizeProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            resize();
        });
        
        getStyleClass().add("glyph-view");
         setOnMouseClicked((MouseEvent t) -> {
             setSelected(!isSelected());
        });

    }

    private void resize() {
        setPrefHeight(glyphIcon.getGlyphSize().doubleValue() + glyphIcon.getGlyphSize().doubleValue()/2);
        setPrefWidth(glyphIcon.getGlyphSize().doubleValue() + glyphIcon.getGlyphSize().doubleValue()/2);
    }

    @Override
    public boolean isSelected() {
        return selectedProperty().get();
    }

    @Override
    public void setSelected(boolean selected) {
        selectedProperty().set(selected);
    }

    public BooleanProperty selectedProperty() {
        if (selectedProperty == null) {
            selectedProperty = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return selectedProperty;
    }

}

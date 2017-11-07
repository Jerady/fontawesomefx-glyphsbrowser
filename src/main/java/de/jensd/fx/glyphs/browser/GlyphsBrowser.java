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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.controlsfx.control.GridView;

import de.jensd.fx.glyphs.GlyphIcon;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jens Deters
 */
public class GlyphsBrowser extends VBox {

    @FXML
    private Label numberOfIconsLabel;
    @FXML
    private Slider glyphSizeSlider;
    @FXML
    private Label glyphSizeSliderValueLabel;
    @FXML
    private Label fontNameLabel;
    @FXML
    private Label fontFamilyLabel;
    @FXML
    private Label fontVersionLabel;
    @FXML
    private Label fontLicenseLabel;
    @FXML
    private Label fontReleaseDateLabel;
    @FXML
    private Hyperlink fontUrlLabel;
    @FXML
    private Label fontWhatsNewLabel;
    @FXML
    private TextField glyphNameLabel;
    @FXML
    private TextField glyphCodeLabel;
    @FXML
    private TextField glyphFactoryCodeLabel;
    @FXML
    private Button copyCodeButton;
    @FXML
    private Button copyFactoryCodeButton;
    @FXML
    private ListView<GlyphsPack> glyphsPackListView;
    @FXML
    private GridView<GlyphIcon> glyphsGridView;
    @FXML
    private Pane glyphPreviewPane;
    @FXML
    private TextField searchBar;
    @FXML
    private Label searchBarResultsLabel;

    private final GlyphsBrowserAppModel model;

    public GlyphsBrowser(GlyphsBrowserAppModel glyphPacksModel) {
        this.model = glyphPacksModel;
        init();
    }

    private void init() {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(GlyphsBrowserAppModel.RESOURCE_BUNDLE);
            URL fxmlURL = getClass().getResource(GlyphsBrowserAppModel.GLYPH_BROWSER_FXML);
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, resourceBundle);
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(GlyphsBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void initialize() {
        glyphsGridView.setCellFactory((GridView<GlyphIcon> gridView) -> new GlyphsGridCell());
        glyphsGridView.cellHeightProperty().bind(model.glyphSizeProperty());
        glyphsGridView.cellWidthProperty().bind(model.glyphSizeProperty());
        glyphsGridView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getTarget() instanceof GlyphIcon) {
                model.selectedGlyphIconProperty().set((GlyphIcon) event.getTarget());
            }
        });
        fontUrlLabel.setOnAction((ActionEvent t) -> {
            if (model.getHostServices() != null) {
                model.getHostServices().showDocument(fontUrlLabel.getText());
            }
        });
        glyphSizeSlider.valueProperty().bindBidirectional(model.glyphSizeProperty());
        glyphSizeSliderValueLabel.textProperty().bind(glyphSizeSlider.valueProperty().asString("%.0f"));
        glyphsPackListView.setItems(model.getGlyphsPacks());
        glyphsPackListView.itemsProperty().addListener((Observable observable) -> {
            glyphsPackListView.getSelectionModel().selectFirst();
        });
        glyphsPackListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GlyphsPack> observable, GlyphsPack oldValue, GlyphsPack newValue) -> {
           
        	//Reset Search Bar
        	searchBar.setText("");
        	
        	//Update the Browser
        	updateBrowser(glyphsPackListView.getSelectionModel().getSelectedItem());
        });
        glyphsPackListView.getSelectionModel().selectFirst();
        model.selectedGlyphIconProperty().addListener((ObservableValue<? extends GlyphIcon> observable, GlyphIcon oldValue, GlyphIcon newValue) -> {
            Optional<GlyphIconInfo> value = model.getGlyphIconInfo(newValue);
            if (value.isPresent()) {
                showGlyphIconsDetails(value.get());
            }
        });
        copyCodeButton.visibleProperty().bind(glyphCodeLabel.textProperty().isEmpty().not());
        copyFactoryCodeButton.visibleProperty().bind(glyphFactoryCodeLabel.textProperty().isEmpty().not());
        
        //!!!!!!!!!Jens Deters add some comments ma BRO :) !!!!!!
        
		//== searchBar
		searchBar.textProperty().addListener((observable , oldValue , newValue) -> {
			//In case search bar has no text inside
			if (searchBar.getText().isEmpty()) {
				
				//Reset all to visible
				glyphsPackListView.getSelectionModel().getSelectedItem().getGlyphNodes().forEach(glyph -> glyph.setVisible(true));
				
				//Reset Search Bar Found Label
				searchBarResultsLabel.setText("Found : [ All ]");
				
				//Show all the items on the GridView
				updateBrowser(glyphsPackListView.getSelectionModel().getSelectedItem());							
				
			} else { //Let's do some search magic
				glyphsPackListView.getSelectionModel().getSelectedItem().getGlyphNodes().forEach(glyph -> {
					
					//Glyph name contains search bar text ? [ No case sensitive ]
					String searchValue = newValue.toLowerCase(); //Speed improvements
					glyph.setVisible(glyph.getGlyphName().toLowerCase().contains(searchValue)); //visible only if name matches searchValue
				});
				
				
				//Add the new items
				glyphsGridView.setItems(glyphsPackListView.getSelectionModel().getSelectedItem().getGlyphNodes().stream().filter(Node::isVisible)
						.collect(Collectors.toCollection(FXCollections::observableArrayList)));
				searchBarResultsLabel.setText("Found : [ " + glyphsGridView.getItems().size()+" ]");
			}
		});
        
        
    }

    private void showGlyphIconsDetails(GlyphIconInfo glyphIconInfo) {
        if (glyphIconInfo != null) {
            {
                glyphNameLabel.setText(glyphIconInfo.getGlyphNameName());
                glyphCodeLabel.setText(glyphIconInfo.getGlyphCode());
                glyphFactoryCodeLabel.setText(glyphIconInfo.getGlyphFactoryCode());
                glyphPreviewPane.getChildren().setAll(glyphIconInfo.getPreviewGlyphs());
            }
        }
    }

    private void clearGlyphIconsDetails() {
        glyphPreviewPane.getChildren().clear();
        glyphNameLabel.setText("");
        glyphCodeLabel.setText("");
        glyphFactoryCodeLabel.setText("");
    }

    private void updateBrowser(GlyphsPack glyphPack) {
        clearGlyphIconsDetails();
        glyphsGridView.setItems(glyphPack.getGlyphNodes());
        numberOfIconsLabel.setText(glyphPack.getNumberOfIcons() + "");
        fontNameLabel.setText(glyphPack.getName());
        fontFamilyLabel.setText(glyphPack.getFamiliy());
        fontVersionLabel.setText(glyphPack.getVersion());
        fontLicenseLabel.setText(glyphPack.getLicense());
        fontReleaseDateLabel.setText(glyphPack.getReleaseDate());
        fontUrlLabel.setText(glyphPack.getURL());
        fontWhatsNewLabel.setText(glyphPack.getWhatsNew());
        if (!glyphPack.getGlyphNodes().isEmpty()) {
            Optional<GlyphIconInfo> value = model.getGlyphIconInfo(glyphPack.getGlyphNodes().get(0));
            if (value.isPresent()) {
                showGlyphIconsDetails(value.get());
            }
        }
        model.selectedGlyphIconProperty().set(glyphPack.getGlyphNodes().get(0));
        
//        //Lets check what's inside
//        glyphPack.getGlyphNodes().forEach(glyph->{
//        	System.out.println(glyph.getGlyphName());
//        });
    }

    @FXML
    public void onCopyUnicode() {
        final ClipboardContent content = new ClipboardContent();
        content.putString(model.selectedGlyphIconProperty().getValue().unicode());
        model.getClipboard().setContent(content);
    }
    
    @FXML
    public void onCopyCode() {
        final ClipboardContent content = new ClipboardContent();
        content.putString(glyphCodeLabel.getText());
        model.getClipboard().setContent(content);
    }

    @FXML
    public void onCopyFactoryCode() {
        final ClipboardContent content = new ClipboardContent();
        content.putString(glyphFactoryCodeLabel.getText());
        model.getClipboard().setContent(content);
    }

}

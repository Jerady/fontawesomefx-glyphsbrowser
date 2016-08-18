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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import de.jensd.fx.glyphs.icons525.utils.Icon525Factory;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconFactory;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.octicons.utils.OctIconFactory;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import de.jensd.fx.glyphs.weathericons.utils.WeatherIconFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import org.controlsfx.control.GridView;

/**
 *
 * @author Jens Deters
 */
public class GlyphsBrowser extends VBox {

    public final static String FONTAWESOME_PROPERTIES = "/de/jensd/fx/glyphs/fontawesome/fontinfo.properties";
    public final static String ICONS525_PROPERTIES = "/de/jensd/fx/glyphs/icons525/fontinfo.properties";
    public final static String MATERIALDESIGNFONT_PROPERTIES = "/de/jensd/fx/glyphs/materialdesignicons/fontinfo.properties";
    public final static String MATERIALICONS_PROPERTIES = "/de/jensd/fx/glyphs/materialicons/fontinfo.properties";
    public final static String OCTICONS_PROPERTIES = "/de/jensd/fx/glyphs/octicons/fontinfo.properties";
    public final static String WEATHERICONS_PROPERTIES = "/weathericons.fontinfo.properties";

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
    private Label glyphUnicodeLabel;
    @FXML
    private Button copyCodeButton;
    @FXML
    private Button copyFactoryCodeButton;
    @FXML
    private ListView<GlyphPack> glyphsPackListView;
    @FXML
    private GridView<GlyphIcon> glyphsGridView;
    @FXML
    private Pane glyphPreviewPane;

    private final GlyphBrowserAppModel model;

    public GlyphsBrowser(GlyphBrowserAppModel glyphPacksModel) {
        this.model = glyphPacksModel;
        init();
    }

    private void init() {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/messages");
            URL fxmlURL = getClass().getResource("/fxml/glyphs_browser.fxml");
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
        glyphsPackListView.setItems(model.getGlyphPacks());
        glyphsPackListView.itemsProperty().addListener((Observable observable) -> {
            glyphsPackListView.getSelectionModel().selectFirst();
        });
        glyphsPackListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GlyphPack> observable, GlyphPack oldValue, GlyphPack newValue) -> {
            updateBrowser(glyphsPackListView.getSelectionModel().getSelectedItem());
        });
        glyphsPackListView.getSelectionModel().selectFirst();
        model.selectedGlyphIconProperty().addListener((ObservableValue<? extends GlyphIcon> observable, GlyphIcon oldValue, GlyphIcon newValue) -> {
            showGlyphIconsDetails(newValue);
        });
        copyCodeButton.visibleProperty().bind(glyphCodeLabel.textProperty().isEmpty().not());
        copyFactoryCodeButton.visibleProperty().bind(glyphFactoryCodeLabel.textProperty().isEmpty().not());
    }

    private void showGlyphIconsDetails(GlyphIcon glyphIcon) {
        clearGlyphIconsDetails();
        if (glyphIcon != null) {
            String glyphNameName = "";
            String glyphUnicode = "";
            String glyphCode = "";
            String glyphFactoryCode = "";
            if (glyphIcon instanceof FontAwesomeIconView) {
                FontAwesomeIcon icon = FontAwesomeIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : model.getGlyphPreviewSizes()) {
                    glyphPreviewPane.getChildren().add(FontAwesomeIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameName = "FontAwesomeIcon." + glyphIcon.getGlyphName();
                glyphUnicode = icon.unicodeToString();
                glyphCode = "FontAwesomeIconView icon = new FontAwesomeIconView(" + glyphNameName + ");";
                glyphFactoryCode = "Text icon = FontAwesomeIconFactory.get().createIcon(" + glyphNameName + ");";

            } else if (glyphIcon instanceof OctIconView) {
                OctIcon icon = OctIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : model.getGlyphPreviewSizes()) {
                    glyphPreviewPane.getChildren().add(OctIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameName = "OctIcon." + glyphIcon.getGlyphName();
                glyphUnicode = icon.unicodeToString();
                glyphCode = "OctIconView icon = new OctIconView(" + glyphNameName + ");";
            } else if (glyphIcon instanceof MaterialDesignIconView) {
                MaterialDesignIcon icon = MaterialDesignIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : model.getGlyphPreviewSizes()) {
                    glyphPreviewPane.getChildren().add(MaterialDesignIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameName = "MaterialDesignIcon." + glyphIcon.getGlyphName();
                glyphUnicode = icon.unicodeToString();
                glyphCode = "MaterialDesignIconView icon = new MaterialDesignIconView(" + glyphNameName + ");";
            } else if (glyphIcon instanceof MaterialIconView) {
                MaterialIcon icon = MaterialIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : model.getGlyphPreviewSizes()) {
                    glyphPreviewPane.getChildren().add(MaterialIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameName = "MaterialIcon." + glyphIcon.getGlyphName();
                glyphUnicode = icon.unicodeToString();
                glyphCode = "MaterialIconView icon = new MaterialIconView(" + glyphNameName + ");";
            } else if (glyphIcon instanceof Icons525View) {
                Icons525 icon = Icons525.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : model.getGlyphPreviewSizes()) {
                    glyphPreviewPane.getChildren().add(Icon525Factory.get().createIcon(icon, previewSize));
                }
                glyphNameName = "Icons525." + glyphIcon.getGlyphName();
                glyphUnicode = icon.unicodeToString();
                glyphCode = "Icons525View icon = new Icons525IconView(" + glyphNameName + ");";
            } else if (glyphIcon instanceof WeatherIconView) {
                WeatherIcon icon = WeatherIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : model.getGlyphPreviewSizes()) {
                    glyphPreviewPane.getChildren().add(WeatherIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameName = "WeatherIcon." + glyphIcon.getGlyphName();
                glyphUnicode = icon.unicodeToString();
                glyphCode = "WeatherIconView icon = new WeatherIconIconView(" + glyphNameName + ");";
            }
            glyphNameLabel.setText(glyphNameName);
            glyphUnicodeLabel.setText(glyphUnicode);
            glyphCodeLabel.setText(glyphCode);
            glyphFactoryCodeLabel.setText(glyphFactoryCode);
        }
    }

    private void clearGlyphIconsDetails() {
        glyphPreviewPane.getChildren().clear();
        glyphNameLabel.setText("");
        glyphUnicodeLabel.setText("");
        glyphCodeLabel.setText("");
        glyphFactoryCodeLabel.setText("");
    }

    private void updateBrowser(GlyphPack glyphPack) {
        clearGlyphIconsDetails();
        glyphsGridView.setItems(glyphPack.getGlyphNodes());
        numberOfIconsLabel.setText(glyphPack.getNumberOfIcons() + "");
        fontNameLabel.setText(glyphPack.getName());
        fontFamilyLabel.setText(glyphPack.getFamiliy());
        fontVersionLabel.setText(glyphPack.getVersion());
        fontReleaseDateLabel.setText(glyphPack.getReleaseDate());
        fontUrlLabel.setText(glyphPack.getURL());
        fontWhatsNewLabel.setText(glyphPack.getWhatsNew());
        if (!glyphPack.getGlyphNodes().isEmpty()) {
            showGlyphIconsDetails(glyphPack.getGlyphNodes().get(0));
        }
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

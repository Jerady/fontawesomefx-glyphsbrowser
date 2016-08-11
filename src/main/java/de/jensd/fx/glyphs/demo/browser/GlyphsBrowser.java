/**
 * Copyright (c) 2015 Jens Deters http://www.jensd.de
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
package de.jensd.fx.glyphs.demo.browser;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconNameComparator;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import de.jensd.fx.glyphs.icons525.utils.Icon525Factory;
import de.jensd.fx.glyphs.icons525.utils.Icon525NameComparator;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconFactory;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconNameComparator;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconNameComparator;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.octicons.utils.OctIconFactory;
import de.jensd.fx.glyphs.octicons.utils.OctIconNameComparator;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import de.jensd.fx.glyphs.weathericons.utils.WeatherIconFactory;
import de.jensd.fx.glyphs.weathericons.utils.WeatherIconNameComparator;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.HostServices;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
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
    private Label glyphNameLabel;
    @FXML
    private ListView<GlyphPack> glyphsPackListView;
    @FXML
    private GridView<GlyphIcon> glyphsGridView;
    @FXML
    private Pane glyphPreviewPane;

    private ObjectProperty<GlyphIcon> selectedGlyphIconProperty;
    private ObjectProperty<Number> glyphSizeProperty;
    private ObservableList<GlyphPack> glyphPacks;
    private String[] glyphPreviewSizes;
    private HostServices hostServices;

    public GlyphsBrowser() {
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

        glyphPreviewSizes = new String[]{"86px", "66px", "56px", "46px", "36px", "26px", "16px"};

        glyphsGridView.setCellFactory((GridView<GlyphIcon> gridView) -> new GlyphsGridCell());
        glyphsGridView.cellHeightProperty().bind(glyphSizeProperty());
        glyphsGridView.cellWidthProperty().bind(glyphSizeProperty());

        glyphsGridView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getTarget() instanceof GlyphIcon) {
                selectedGlyphIconProperty().set((GlyphIcon) event.getTarget());

            }
        });

        fontUrlLabel.setOnAction((ActionEvent t) -> {
            if (getHostServices() != null) {
                getHostServices().showDocument(fontUrlLabel.getText());
            }
        });

        glyphSizeSlider.valueProperty().bindBidirectional(glyphSizeProperty());
        glyphSizeSliderValueLabel.textProperty().bind(glyphSizeSlider.valueProperty().asString("%.0f"));
        initGlyphPacks();
        glyphsPackListView.setItems(glyphPacks);
        glyphsPackListView.itemsProperty().addListener((Observable observable) -> {
            glyphsPackListView.getSelectionModel().selectFirst();
        });
        glyphsPackListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GlyphPack> observable, GlyphPack oldValue, GlyphPack newValue) -> {
            updateBrowser(glyphsPackListView.getSelectionModel().getSelectedItem());
        });
        glyphsPackListView.getSelectionModel().selectFirst();

        selectedGlyphIconProperty().addListener((ObservableValue<? extends GlyphIcon> observable, GlyphIcon oldValue, GlyphIcon newValue) -> {
            showGlyphIconsDetails(newValue);
        });

    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public ObjectProperty<GlyphIcon> selectedGlyphIconProperty() {
        if (selectedGlyphIconProperty == null) {
            selectedGlyphIconProperty = new SimpleObjectProperty<>();
        }
        return selectedGlyphIconProperty;
    }

    public ObjectProperty<Number> glyphSizeProperty() {
        if (glyphSizeProperty == null) {
            glyphSizeProperty = new SimpleObjectProperty<>(24);
        }
        return glyphSizeProperty;
    }

    private void showGlyphIconsDetails(GlyphIcon glyphIcon) {
        clearGlyphIconsDetails();
        if (glyphIcon != null) {
            if (glyphIcon instanceof FontAwesomeIconView) {
                FontAwesomeIcon icon = FontAwesomeIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : glyphPreviewSizes) {
                    glyphPreviewPane.getChildren().add(FontAwesomeIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameLabel.setText("FontAwesomeIcon." + glyphIcon.getGlyphName());
            } else if (glyphIcon instanceof OctIconView) {
                OctIcon icon = OctIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : glyphPreviewSizes) {
                    glyphPreviewPane.getChildren().add(OctIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameLabel.setText("OctIcon." + glyphIcon.getGlyphName());
            } else if (glyphIcon instanceof MaterialDesignIconView) {
                MaterialDesignIcon icon = MaterialDesignIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : glyphPreviewSizes) {
                    glyphPreviewPane.getChildren().add(MaterialDesignIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameLabel.setText("MaterialDesignIcon." + glyphIcon.getGlyphName());
            } else if (glyphIcon instanceof MaterialIconView) {
                MaterialIcon icon = MaterialIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : glyphPreviewSizes) {
                    glyphPreviewPane.getChildren().add(MaterialIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameLabel.setText("MaterialIcon." + glyphIcon.getGlyphName());
            } else if (glyphIcon instanceof Icons525View) {
                Icons525 icon = Icons525.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : glyphPreviewSizes) {
                    glyphPreviewPane.getChildren().add(Icon525Factory.get().createIcon(icon, previewSize));
                }
                glyphNameLabel.setText("Icons525." + glyphIcon.getGlyphName());
            } else if (glyphIcon instanceof WeatherIconView) {
                WeatherIcon icon = WeatherIcon.valueOf(glyphIcon.getGlyphName());
                for (String previewSize : glyphPreviewSizes) {
                    glyphPreviewPane.getChildren().add(WeatherIconFactory.get().createIcon(icon, previewSize));
                }
                glyphNameLabel.setText("WeatherIcon." + glyphIcon.getGlyphName());
            }
        }
    }

    private void clearGlyphIconsDetails() {
        glyphPreviewPane.getChildren().clear();
        glyphNameLabel.setText("");
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
    }

    private GlyphIcon createIconView(GlyphIcon icon) {
        icon.glyphSizeProperty().bind(glyphSizeProperty);
        return icon;
    }

    private void initGlyphPacks() {
        glyphPacks = FXCollections.observableArrayList();
        List<GlyphIcon> fontAwesomeList = Stream.of(FontAwesomeIcon.values())
                .sorted(new FontAwesomeIconNameComparator())
                .map(i -> createIconView(new FontAwesomeIconView(i)))
                .collect(Collectors.toList());
        glyphPacks.add(new GlyphPack(new FontInfo(FONTAWESOME_PROPERTIES), FXCollections.observableArrayList(fontAwesomeList)));

        List<GlyphIcon> weatherIconsList = Stream.of(WeatherIcon.values())
                .sorted(new WeatherIconNameComparator())
                .map(i -> createIconView(new WeatherIconView(i)))
                .collect(Collectors.toList());
        glyphPacks.add(new GlyphPack(new FontInfo(WEATHERICONS_PROPERTIES), FXCollections.observableArrayList(weatherIconsList)));

        List<GlyphIcon> materialDesignIconsList = Stream.of(MaterialDesignIcon.values())
                .sorted(new MaterialDesignIconNameComparator())
                .map(i -> createIconView(new MaterialDesignIconView(i)))
                .collect(Collectors.toList());
        glyphPacks.add(new GlyphPack(new FontInfo(MATERIALDESIGNFONT_PROPERTIES), FXCollections.observableArrayList(materialDesignIconsList)));

        List<GlyphIcon> materialIconsList = Stream.of(MaterialIcon.values())
                .sorted(new MaterialIconNameComparator())
                .map(i -> createIconView(new MaterialIconView(i)))
                .collect(Collectors.toList());
        glyphPacks.add(new GlyphPack(new FontInfo(MATERIALICONS_PROPERTIES), FXCollections.observableArrayList(materialIconsList)));

        List<GlyphIcon> octIconsList = Stream.of(OctIcon.values())
                .sorted(new OctIconNameComparator())
                .map(i -> createIconView(new OctIconView(i)))
                .collect(Collectors.toList());
        glyphPacks.add(new GlyphPack(new FontInfo(OCTICONS_PROPERTIES), FXCollections.observableArrayList(octIconsList)));

        List<GlyphIcon> icons525List = Stream.of(Icons525.values())
                .sorted(new Icon525NameComparator())
                .map(i -> createIconView(new Icons525View(i)))
                .collect(Collectors.toList());
        glyphPacks.add(new GlyphPack(new FontInfo(ICONS525_PROPERTIES), FXCollections.observableArrayList(icons525List)));

    }

}

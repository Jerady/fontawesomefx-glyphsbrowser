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
import de.jensd.fx.glyphs.emojione.EmojiOne;
import de.jensd.fx.glyphs.emojione.EmojiOneView;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.HostServices;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.Clipboard;

/**
 *
 * @author Jens Deters
 */
public class GlyphsBrowserAppModel {

    public final static String FONTAWESOME_PROPERTIES = "/de/jensd/fx/glyphs/fontawesome/fontinfo.properties";
    public final static String ICONS525_PROPERTIES = "/de/jensd/fx/glyphs/icons525/fontinfo.properties";
    public final static String MATERIALDESIGNFONT_PROPERTIES = "/de/jensd/fx/glyphs/materialdesignicons/fontinfo.properties";
    public final static String MATERIALICONS_PROPERTIES = "/de/jensd/fx/glyphs/materialicons/fontinfo.properties";
    public final static String OCTICONS_PROPERTIES = "/de/jensd/fx/glyphs/octicons/fontinfo.properties";
    public final static String WEATHERICONS_PROPERTIES = "/de/jensd/fx/glyphs/weathericons/fontinfo.properties";
    public final static String EMOJIONE_PROPERTIES = "/de/jensd/fx/glyphs/emojione/fontinfo.properties";
    public final static String APP_VERSION = "v1.3.0";
    public final static String APP_NAME = "FontAwesomeFX 8.15 -- GlyphsBrowser";
    public final static String APP_STYLES = "/styles/iconsbrowser.css";
    public final static String RESOURCE_BUNDLE = "i18n/messages";
    public final static String GLYPH_BROWSER_FXML = "/fxml/glyphs_browser.fxml";
    public final static int DEFAULT_WITH = 1024;
    public final static int DEFAULT_HEIGHT = 600;
    public final static int DEFAULT_GLYPH_SIZE = 24;
    public final static String[] GLYPH_PREVIEW_SIZES = {"8px", "10px", "12px", "16px", "26px", "36px", "46px", "56px", "66px", "86px"};

    private ObservableList<GlyphsPack> glyphsPacks;
    private ObjectProperty<Number> glyphSizeProperty;
    private HostServices hostServices;
    private ObjectProperty<GlyphIcon> selectedGlyphIconProperty;
    private Clipboard clipboard;

    public GlyphsBrowserAppModel() {
        init();
    }

    private void init() {

        List<GlyphIcon> fontAwesomeList = Stream.of(FontAwesomeIcon.values())
                .sorted(new FontAwesomeIconNameComparator())
                .map(i -> createIconView(new FontAwesomeIconView(i)))
                .collect(Collectors.toList());

        List<GlyphIcon> weatherIconsList = Stream.of(WeatherIcon.values())
                .sorted(new WeatherIconNameComparator())
                .map(i -> createIconView(new WeatherIconView(i)))
                .collect(Collectors.toList());

        List<GlyphIcon> materialDesignIconsList = Stream.of(MaterialDesignIcon.values())
                .sorted(new MaterialDesignIconNameComparator())
                .map(i -> createIconView(new MaterialDesignIconView(i)))
                .collect(Collectors.toList());

        List<GlyphIcon> materialIconsList = Stream.of(MaterialIcon.values())
                .sorted(new MaterialIconNameComparator())
                .map(i -> createIconView(new MaterialIconView(i)))
                .collect(Collectors.toList());

        List<GlyphIcon> octIconsList = Stream.of(OctIcon.values())
                .sorted(new OctIconNameComparator())
                .map(i -> createIconView(new OctIconView(i)))
                .collect(Collectors.toList());

        List<GlyphIcon> icons525List = Stream.of(Icons525.values())
                .sorted(new Icon525NameComparator())
                .map(i -> createIconView(new Icons525View(i)))
                .collect(Collectors.toList());

        List<GlyphIcon> emojiOneList = Stream.of(EmojiOne.values())
                .map(i -> createIconView(new EmojiOneView(i)))
                .collect(Collectors.toList());

        getGlyphsPacks().add(new GlyphsPack(new FontInfo(EMOJIONE_PROPERTIES), FXCollections.observableArrayList(emojiOneList)));
        getGlyphsPacks().add(new GlyphsPack(new FontInfo(FONTAWESOME_PROPERTIES), FXCollections.observableArrayList(fontAwesomeList)));
        getGlyphsPacks().add(new GlyphsPack(new FontInfo(ICONS525_PROPERTIES), FXCollections.observableArrayList(icons525List)));
        getGlyphsPacks().add(new GlyphsPack(new FontInfo(MATERIALDESIGNFONT_PROPERTIES), FXCollections.observableArrayList(materialDesignIconsList)));
        getGlyphsPacks().add(new GlyphsPack(new FontInfo(MATERIALICONS_PROPERTIES), FXCollections.observableArrayList(materialIconsList)));
        getGlyphsPacks().add(new GlyphsPack(new FontInfo(OCTICONS_PROPERTIES), FXCollections.observableArrayList(octIconsList)));
        getGlyphsPacks().add(new GlyphsPack(new FontInfo(WEATHERICONS_PROPERTIES), FXCollections.observableArrayList(weatherIconsList)));

    }

    public Clipboard getClipboard() {
        if (clipboard == null) {
            clipboard = Clipboard.getSystemClipboard();
        }
        return clipboard;
    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public ObservableList<GlyphsPack> getGlyphsPacks() {
        if (glyphsPacks == null) {
            glyphsPacks = FXCollections.observableArrayList();
        }
        return glyphsPacks;
    }

    public ObjectProperty<Number> glyphSizeProperty() {
        if (glyphSizeProperty == null) {
            glyphSizeProperty = new SimpleObjectProperty<>(DEFAULT_GLYPH_SIZE);
        }
        return glyphSizeProperty;
    }

    public ObjectProperty<GlyphIcon> selectedGlyphIconProperty() {
        if (selectedGlyphIconProperty == null) {
            selectedGlyphIconProperty = new SimpleObjectProperty<>();
        }
        return selectedGlyphIconProperty;
    }

    private GlyphIcon createIconView(GlyphIcon icon) {
        icon.glyphSizeProperty().bind(glyphSizeProperty());
        return icon;
    }

    public Optional<GlyphIconInfo> getGlyphIconInfo(GlyphIcon glyphIcon) {
        GlyphIconInfo glyphIconInfo = null;
        if (glyphIcon != null) {
            if (glyphIcon instanceof FontAwesomeIconView) {
                FontAwesomeIcon icon = FontAwesomeIcon.valueOf(glyphIcon.getGlyphName());
                ObservableList<Node> preview = FXCollections.observableArrayList();
                for (String previewSize : GLYPH_PREVIEW_SIZES) {
                    preview.add(FontAwesomeIconFactory.get().createIcon(icon, previewSize));
                }
                glyphIconInfo = new GlyphIconInfo(
                        "FontAwesomeIcon." + glyphIcon.getGlyphName(),
                        icon.unicode(),
                        "FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon." + glyphIcon.getGlyphName() + ");",
                        "Text icon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon." + glyphIcon.getGlyphName() + ");",
                        preview);
            } else if (glyphIcon instanceof OctIconView) {
                OctIcon icon = OctIcon.valueOf(glyphIcon.getGlyphName());
                ObservableList<Node> preview = FXCollections.observableArrayList();
                for (String previewSize : GLYPH_PREVIEW_SIZES) {
                    preview.add(OctIconFactory.get().createIcon(icon, previewSize));
                }
                glyphIconInfo = new GlyphIconInfo(
                        "OctIcon." + glyphIcon.getGlyphName(),
                        icon.unicode(),
                        "OctIconView icon = new OctIconView(OctIcon." + glyphIcon.getGlyphName() + ");",
                        "Text icon = OctIconFactory.get().createIcon(OctIcon." + glyphIcon.getGlyphName() + ");",
                        preview);
            } else if (glyphIcon instanceof MaterialDesignIconView) {
                MaterialDesignIcon icon = MaterialDesignIcon.valueOf(glyphIcon.getGlyphName());
                ObservableList<Node> preview = FXCollections.observableArrayList();
                for (String previewSize : GLYPH_PREVIEW_SIZES) {
                    preview.add(MaterialDesignIconFactory.get().createIcon(icon, previewSize));
                }
                glyphIconInfo = new GlyphIconInfo(
                        "MaterialDesignIcon." + glyphIcon.getGlyphName(),
                        icon.unicode(),
                        "MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon." + glyphIcon.getGlyphName() + ");",
                        "Text icon = MaterialDesignIconFactory.get().createIcon(MaterialDesignIcon." + glyphIcon.getGlyphName() + ");",
                        preview);
            } else if (glyphIcon instanceof MaterialIconView) {
                MaterialIcon icon = MaterialIcon.valueOf(glyphIcon.getGlyphName());
                ObservableList<Node> preview = FXCollections.observableArrayList();
                for (String previewSize : GLYPH_PREVIEW_SIZES) {
                    preview.add(MaterialIconFactory.get().createIcon(icon, previewSize));
                }
                glyphIconInfo = new GlyphIconInfo(
                        "MaterialIcon." + glyphIcon.getGlyphName(),
                        icon.unicode(),
                        "MaterialIconView icon = new MaterialIconView(MaterialIcon." + glyphIcon.getGlyphName() + ");",
                        "Text icon = MaterialIconFactory.get().createIcon(MaterialIcon." + glyphIcon.getGlyphName() + ");",
                        preview);
            } else if (glyphIcon instanceof Icons525View) {
                Icons525 icon = Icons525.valueOf(glyphIcon.getGlyphName());
                ObservableList<Node> preview = FXCollections.observableArrayList();
                for (String previewSize : GLYPH_PREVIEW_SIZES) {
                    preview.add(Icon525Factory.get().createIcon(icon, previewSize));
                }
                glyphIconInfo = new GlyphIconInfo(
                        "Icons525." + glyphIcon.getGlyphName(),
                        icon.unicode(),
                        "Icons525View icon = new FontAwesomeIconView(Icons525." + glyphIcon.getGlyphName() + ");",
                        "Text icon = Icons525Factory.get().createIcon(Icons525." + glyphIcon.getGlyphName() + ");",
                        preview);
            } else if (glyphIcon instanceof WeatherIconView) {
                WeatherIcon icon = WeatherIcon.valueOf(glyphIcon.getGlyphName());
                ObservableList<Node> preview = FXCollections.observableArrayList();
                for (String previewSize : GLYPH_PREVIEW_SIZES) {
                    preview.add(WeatherIconFactory.get().createIcon(icon, previewSize));
                }
                glyphIconInfo = new GlyphIconInfo(
                        "WeatherIcon." + glyphIcon.getGlyphName(),
                        icon.unicode(),
                        "WeatherIconView icon = new WeatherIconView(WeatherIcon." + glyphIcon.getGlyphName() + ");",
                        "Text icon = WeatherIconFactory.get().createIcon(WeatherIcon." + glyphIcon.getGlyphName() + ");",
                        preview);
            } else if (glyphIcon instanceof EmojiOneView) {
                EmojiOne icon = EmojiOne.valueOf(glyphIcon.getGlyphName());
                ObservableList<Node> preview = FXCollections.observableArrayList();
                for (String previewSize : GLYPH_PREVIEW_SIZES) {
                    preview.add(WeatherIconFactory.get().createIcon(icon, previewSize));
                }
                glyphIconInfo = new GlyphIconInfo(
                        "EmojiOne." + glyphIcon.getGlyphName(),
                        icon.unicode(),
                        "EmojiOneView icon = new EmojiOneView(WeatherIcon." + glyphIcon.getGlyphName() + ");",
                        "Text icon = EmojiOneViewFactory.get().createIcon(EmojiOne." + glyphIcon.getGlyphName() + ");",
                        preview);
            }
        }
        return Optional.of(glyphIconInfo);
    }

}

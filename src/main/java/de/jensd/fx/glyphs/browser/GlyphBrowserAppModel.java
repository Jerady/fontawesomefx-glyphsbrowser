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
import static de.jensd.fx.glyphs.browser.GlyphsBrowser.FONTAWESOME_PROPERTIES;
import static de.jensd.fx.glyphs.browser.GlyphsBrowser.ICONS525_PROPERTIES;
import static de.jensd.fx.glyphs.browser.GlyphsBrowser.MATERIALDESIGNFONT_PROPERTIES;
import static de.jensd.fx.glyphs.browser.GlyphsBrowser.MATERIALICONS_PROPERTIES;
import static de.jensd.fx.glyphs.browser.GlyphsBrowser.OCTICONS_PROPERTIES;
import static de.jensd.fx.glyphs.browser.GlyphsBrowser.WEATHERICONS_PROPERTIES;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconNameComparator;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import de.jensd.fx.glyphs.icons525.utils.Icon525NameComparator;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconNameComparator;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconNameComparator;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.octicons.utils.OctIconNameComparator;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import de.jensd.fx.glyphs.weathericons.utils.WeatherIconNameComparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.HostServices;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.Clipboard;

/**
 *
 * @author Jens Deters
 */
public class GlyphBrowserAppModel {

    private ObservableList<GlyphPack> glyphPacks;
    private ObjectProperty<Number> glyphSizeProperty;
    private HostServices hostServices;
    private ObjectProperty<GlyphIcon> selectedGlyphIconProperty;
    private String[] glyphPreviewSizes;
    private Clipboard clipboard;
    public static final String APP_VERSION = "1.0";
    public static final String APP_NAME = "FontAwesomeFX 8.12 - GlyphsBrowser";
    public static final String APP_STYLES = "/styles/iconsbrowser.css";
    public static final int DEFAULT_WITH = 1024;
    public static final int DEFAULT_HEIGHT = 600;

    public GlyphBrowserAppModel() {
        init();
    }

    private void init() {

        List<GlyphIcon> fontAwesomeList = Stream.of(FontAwesomeIcon.values())
                .sorted(new FontAwesomeIconNameComparator())
                .map(i -> createIconView(new FontAwesomeIconView(i)))
                .collect(Collectors.toList());
        getGlyphPacks().add(new GlyphPack(new FontInfo(FONTAWESOME_PROPERTIES), FXCollections.observableArrayList(fontAwesomeList)));

        List<GlyphIcon> weatherIconsList = Stream.of(WeatherIcon.values())
                .sorted(new WeatherIconNameComparator())
                .map(i -> createIconView(new WeatherIconView(i)))
                .collect(Collectors.toList());
        getGlyphPacks().add(new GlyphPack(new FontInfo(WEATHERICONS_PROPERTIES), FXCollections.observableArrayList(weatherIconsList)));

        List<GlyphIcon> materialDesignIconsList = Stream.of(MaterialDesignIcon.values())
                .sorted(new MaterialDesignIconNameComparator())
                .map(i -> createIconView(new MaterialDesignIconView(i)))
                .collect(Collectors.toList());
        getGlyphPacks().add(new GlyphPack(new FontInfo(MATERIALDESIGNFONT_PROPERTIES), FXCollections.observableArrayList(materialDesignIconsList)));

        List<GlyphIcon> materialIconsList = Stream.of(MaterialIcon.values())
                .sorted(new MaterialIconNameComparator())
                .map(i -> createIconView(new MaterialIconView(i)))
                .collect(Collectors.toList());
        getGlyphPacks().add(new GlyphPack(new FontInfo(MATERIALICONS_PROPERTIES), FXCollections.observableArrayList(materialIconsList)));

        List<GlyphIcon> octIconsList = Stream.of(OctIcon.values())
                .sorted(new OctIconNameComparator())
                .map(i -> createIconView(new OctIconView(i)))
                .collect(Collectors.toList());
        getGlyphPacks().add(new GlyphPack(new FontInfo(OCTICONS_PROPERTIES), FXCollections.observableArrayList(octIconsList)));

        List<GlyphIcon> icons525List = Stream.of(Icons525.values())
                .sorted(new Icon525NameComparator())
                .map(i -> createIconView(new Icons525View(i)))
                .collect(Collectors.toList());
        getGlyphPacks().add(new GlyphPack(new FontInfo(ICONS525_PROPERTIES), FXCollections.observableArrayList(icons525List)));

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

    public ObservableList<GlyphPack> getGlyphPacks() {
        if (glyphPacks == null) {
            glyphPacks = FXCollections.observableArrayList();
        }
        return glyphPacks;
    }

    public ObjectProperty<Number> glyphSizeProperty() {
        if (glyphSizeProperty == null) {
            glyphSizeProperty = new SimpleObjectProperty<>(24);
        }
        return glyphSizeProperty;
    }

    public ObjectProperty<GlyphIcon> selectedGlyphIconProperty() {
        if (selectedGlyphIconProperty == null) {
            selectedGlyphIconProperty = new SimpleObjectProperty<>();
        }
        return selectedGlyphIconProperty;
    }

    public String[] getGlyphPreviewSizes() {
        if (glyphPreviewSizes == null) {
            glyphPreviewSizes = new String[]{"8px", "10px", "12px", "16px", "26px", "36px", "46px", "56px", "66px", "86px"};
        }
        return glyphPreviewSizes;
    }
    
    

    private GlyphIcon createIconView(GlyphIcon icon) {
        icon.glyphSizeProperty().bind(glyphSizeProperty());
        return icon;
    }

}

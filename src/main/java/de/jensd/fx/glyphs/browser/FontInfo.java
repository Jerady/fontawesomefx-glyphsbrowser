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
import java.util.Properties;

/**
 *
 * @author Jens Deters
 */
public class FontInfo {

    private Properties fontInfoProperties;

    public FontInfo(String pathToFontInfoProperties) {
        fontInfoProperties = new Properties();
        try {
            Optional<URL> url = Optional.ofNullable(FontInfo.class.getResource(pathToFontInfoProperties));
            if (url.isPresent()) {
                this.fontInfoProperties.load(url.get().openStream());
            } else {
                System.out.printf("An error accurred while loading '%s'", pathToFontInfoProperties);
            }
        } catch (IOException ex) {
            System.out.printf("An error accurred while loading '%s': %s", pathToFontInfoProperties, ex.getMessage());
        }
    }

    public String getName() {
        return fontInfoProperties.getProperty("font.name", "no font name");
    }

    public String getFamiliy() {
        return fontInfoProperties.getProperty("font.family", "no font family");
    }

    public String getVersion() {
        return fontInfoProperties.getProperty("font.version", "no font version");
    }

    public String getLicense() {
        return fontInfoProperties.getProperty("font.license", "no font license");
    }

    public String getReleaseDate() {
        return fontInfoProperties.getProperty("font.released", "no font released");
    }

    public String getURL() {
        return fontInfoProperties.getProperty("font.url", "no font url");
    }

    public String getDescription() {
        return fontInfoProperties.getProperty("font.description", "no font description");
    }

    public String getWhatsNew() {
        return fontInfoProperties.getProperty("font.whatsnew", "no font whatsnew");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Fontname     : ").append(getName()).append('\n');
        builder.append("Fontfamily   : ").append(getFamiliy()).append('\n');
        builder.append("Version      : ").append(getVersion()).append('\n');
        builder.append("License      : ").append(getLicense()).append('\n');
        builder.append("Release Date : ").append(getReleaseDate()).append('\n');
        builder.append("URL          : ").append(getURL()).append('\n');
        builder.append("Description  : ").append(getDescription()).append('\n');
        builder.append("What's new   : ").append(getWhatsNew()).append('\n');
        return builder.toString();
    }

}

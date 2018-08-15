package com.klaus3d3.xDripwatchface.resource;

import android.content.res.Resources;
import android.graphics.Typeface;

import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptNumView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * Resource manager for caching purposes
 */
public class ResourceManager {

    public enum Font {
        MULTI_SPACE("fonts/font.ttf"), ICONS_FONT("fonts/faSolid.otf"), MONO_SPACE("fonts/CursedTimerULiL.ttf")/*, ANOTHER_FONT("fonts/myFont.otf")*/;  // More fonts can go here

        private final String path;

        Font(String path) {
            this.path = path;
        }
    }


    private static Map<Font, Typeface> TYPE_FACES = new EnumMap<>(Font.class);

    public static Typeface getTypeFace(final Resources resources, final Font font) {
        Typeface typeface = TYPE_FACES.get(font);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(resources.getAssets(), font.path);
            TYPE_FACES.put(font, typeface);
        }
        return typeface;
    }
}

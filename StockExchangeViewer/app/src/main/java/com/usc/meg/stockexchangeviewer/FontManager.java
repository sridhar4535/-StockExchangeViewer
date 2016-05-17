package com.usc.meg.stockexchangeviewer;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by sbmeg on 02/05/16.
 */
public class FontManager {
    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        Typeface tf = FontCache.get(font, context);
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}




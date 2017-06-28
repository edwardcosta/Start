package com.android.usuario.start.Util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by tulio on 27/06/2017.
 */

public class Fonts {
    public final Typeface OPEN_SANS_REGULAR;
    public final Typeface OPEN_SANS_BOLD;
    public final Typeface OPEN_SANS_ITALIC;
    public final Typeface OPEN_SANS_SEMIBOLD;
    public Fonts(Context context) {
        OPEN_SANS_REGULAR = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        OPEN_SANS_BOLD = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
        OPEN_SANS_ITALIC = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        OPEN_SANS_SEMIBOLD = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
    }
}

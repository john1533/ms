package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.mobcent.lowest.module.place.api.constant.PlaceApiConstant;

public class MCResource {
    private static Object lock = new Object();
    private static MCResource mcResource;
    private String pkg;
    private Resources resources;

    private MCResource(Context context) {
        this.pkg = context.getPackageName();
        this.resources = context.getResources();
    }

    public static MCResource getInstance(Context context) {
        synchronized (lock) {
            if (mcResource == null) {
                mcResource = new MCResource(context.getApplicationContext());
            }
        }
        return mcResource;
    }

    public int getResourcesId(String type, String name) {
        try {
            return this.resources.getIdentifier(name, type, this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getStringId(String name) {
        try {
            return this.resources.getIdentifier(name, "string", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getString(String name) {
        String s = "";
        try {
            return this.resources.getString(getStringId(name));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getColorId(String name) {
        try {
            return this.resources.getIdentifier(name, PlaceApiConstant.COLOR, this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getColor(String name) {
        try {
            return this.resources.getColor(getColorId(name));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getDimenId(String name) {
        try {
            return this.resources.getIdentifier(name, "dimen", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getStyleId(String name) {
        try {
            return this.resources.getIdentifier(name, "style", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getLayoutId(String name) {
        try {
            return this.resources.getIdentifier(name, "layout", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getViewId(String name) {
        try {
            return this.resources.getIdentifier(name, "id", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getAnimId(String name) {
        try {
            return this.resources.getIdentifier(name, "anim", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getArrayId(String name) {
        try {
            return this.resources.getIdentifier(name, "array", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getMenuId(String name){
        try {
            return this.resources.getIdentifier(name, "menu", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getDrawableId(String name) {
        try {
            return this.resources.getIdentifier(name, PlaceApiConstant.DRAWABLE, this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public Drawable getDrawable(String name) {
        try {
            return this.resources.getDrawable(getDrawableId(name));
        } catch (Exception e) {
            return null;
        }
    }

    public int getRawId(String name) {
        try {
            return this.resources.getIdentifier(name, "raw", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getAttrId(String name) {
        try {
            return this.resources.getIdentifier(name, "attr", this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public Drawable getDrawableFromAttr(Resources.Theme theme,String name){
        Drawable drawable = null;
        TypedValue a = new TypedValue();
        theme.resolveAttribute(getAttrId(name), a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            int color = a.data;
            drawable = new ColorDrawable(color);
        } else {
            // windowBackground is not a color, probably a drawable
            drawable = resources.getDrawable(a.resourceId);
        }
        return drawable;
    }

    public Drawable getDrawableFromAttr(Resources.Theme theme,int resId){
        Drawable drawable = null;
        TypedValue a = new TypedValue();
        theme.resolveAttribute(resId, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            int color = a.data;
            drawable = new ColorDrawable(color);
        } else {
            // windowBackground is not a color, probably a drawable
            drawable = resources.getDrawable(a.resourceId);
        }
        return drawable;
    }


    public int getColorFromAttr(Resources.Theme theme,String name){
        int color = 0;
        TypedValue a = new TypedValue();
        theme.resolveAttribute(getAttrId(name), a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            color = a.data;
        }
        return color;
    }
}

package com.mobcent.update.android.util;

import android.content.Context;
import android.content.res.Resources;
import com.mobcent.lowest.module.place.api.constant.PlaceApiConstant;

public class MCUpdateResource {
    private static MCUpdateResource forumResource;
    private String pkg;
    private Resources resources;

    private MCUpdateResource(Context context) {
        this.pkg = context.getPackageName();
        this.resources = context.getResources();
    }

    public static MCUpdateResource getInstance(Context context) {
        if (forumResource == null) {
            forumResource = new MCUpdateResource(context);
        }
        return forumResource;
    }

    protected int getResourcesId(Context context, String type, String name) {
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
        return this.resources.getString(getStringId(name));
    }

    public int getColorId(String name) {
        try {
            return this.resources.getIdentifier(name, PlaceApiConstant.COLOR, this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
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

    public int getDrawableId(String name) {
        try {
            return this.resources.getIdentifier(name, PlaceApiConstant.DRAWABLE, this.pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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
}

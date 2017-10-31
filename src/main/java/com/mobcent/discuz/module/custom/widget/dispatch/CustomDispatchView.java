package com.mobcent.discuz.module.custom.widget.dispatch;

import android.content.Context;
import android.view.View;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.CustomBaseImg;
import com.mobcent.discuz.module.custom.widget.CustomBaseRelativeLayout;
import com.mobcent.discuz.module.custom.widget.CustomBaseText;
import com.mobcent.discuz.module.custom.widget.CustomBtnRound;
import com.mobcent.discuz.module.custom.widget.CustomImgUpDown;
import com.mobcent.discuz.module.custom.widget.CustomListImgLeft;
import com.mobcent.discuz.module.custom.widget.CustomNewsAutoList;
import com.mobcent.discuz.module.custom.widget.CustomOverlayTextDown;
import com.mobcent.discuz.module.custom.widget.CustomSlider;
import com.mobcent.discuz.module.custom.widget.CustomVideoOverlayTextDown;
import com.mobcent.discuz.module.custom.widget.CustomVideoOverlayTextUp;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.custom.widget.layout.CustomLayoutAllOneRow;
import com.mobcent.discuz.module.custom.widget.layout.CustomLayoutOneThree;
import com.mobcent.discuz.module.custom.widget.layout.CustomLayoutOneTwo;
import com.mobcent.lowest.base.utils.MCListUtils;

public class CustomDispatchView implements CustomConstant {
    public static View dispatchLayout(Context context, ConfigComponentModel component, String outSideStyle, boolean isAnim) {
        View v = null;
        String componentStyle = component.getStyle();
        if (MCListUtils.isEmpty(component.getComponentList())) {
            return null;
        }
        if (isOneRow(componentStyle)) {
            v = new CustomLayoutAllOneRow(context);
        } else if (CustomConstant.STYLE_LAYOUT_SLIDER.equals(componentStyle)) {
            v = new CustomSlider(context);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_THREEROW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_THREEROW_ONECOL.equals(componentStyle)) {
            v = new CustomLayoutOneThree(context);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_TWOROW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TWOROW_ONECOL.equals(componentStyle)) {
            v = new CustomLayoutOneTwo(context);
        } else if (CustomConstant.STYLE_LAYOUT_NEWS_AUTO.equals(componentStyle)) {
            View list = new CustomNewsAutoList(context);
            ((CustomNewsAutoList)list).setOutSideStyle(outSideStyle);
            ((CustomNewsAutoList)list).initViews(component, isAnim);
            return list;
        }
        if (v != null) {
            setStyle(outSideStyle, component, (CustomBaseRelativeLayout)v);
            ((CustomBaseRelativeLayout)v).initViews(component);
        }
        return v;
    }

    public static boolean isOneRow(String style) {
        if (style.equals(CustomConstant.STYLE_LAYOUT_ONECOL_HIGH) || style.equals(CustomConstant.STYLE_LAYOUT_ONECOLLOW) || style.equals(CustomConstant.STYLE_LAYOUT_ONECOL_FIXED)) {
            return true;
        }
        if (style.equals(CustomConstant.STYLE_LAYOUT_TWOCOL_LOW) || style.equals(CustomConstant.STYLE_LAYOUT_TWOCOL_MID) || style.equals(CustomConstant.STYLE_LAYOUT_TWOCOL_HIGH)) {
            return true;
        }
        if (style.equals(CustomConstant.STYLE_LAYOUT_THREECOL_LOW) || style.equals(CustomConstant.STYLE_LAYOUT_THREECOL_MID) || style.equals(CustomConstant.STYLE_LAYOUT_THREECOL_HIGH)) {
            return true;
        }
        if (style.equals(CustomConstant.STYLE_LAYOUT_FOURCOL) || style.equals(CustomConstant.STYLE_LAYOUT_THREECOL_MID) || style.equals(CustomConstant.STYLE_LAYOUT_THREECOL_HIGH)) {
            return true;
        }
        if (style.equals(CustomConstant.STYLE_LAYOUT_TEXT_THREE_COL) || style.equals(CustomConstant.STYLE_LAYOUT_TEXT_TWO_COL)) {
            return true;
        }
        if (style.equals(CustomConstant.STYLE_LAYOUT_ONEROW_ONECOL) || style.equals(CustomConstant.STYLE_LAYOUT_ONECOL_ONEROW)) {
            return true;
        }
        return false;
    }

    public static void setStyle(String outSideStyle, ConfigComponentModel component, CustomBaseRelativeLayout v) {
        String componentStyle = component.getStyle();
        if (outSideStyle.equals(CustomConstant.STYLE_LAYOUT_STYLE_IMAGE)) {
            setRatioByImgStyle(componentStyle, v);
            v.setMarginLeftRight(5);
            v.setMarginTopBottom(5);
            v.setMarginInside(5);
        } else if (outSideStyle.equals(CustomConstant.STYLE_LAYOUT_STYLE_LINE)) {
            setRatioByLineStyle(componentStyle, v);
            v.setMarginLeftRight(0);
            v.setMarginTopBottom(0);
            v.setMarginInside(0);
        } else {
            setRatioByDefaultStyle(componentStyle, v);
            v.setMarginLeftRight(12);
            v.setMarginTopBottom(8);
            v.setMarginInside(10);
            if (CustomConstant.STYLE_LAYOUT_TWOCOL_HIGH.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TWOCOL_MID.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TWOCOL_LOW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_FOURCOL.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TEXT_TWO_COL.equals(componentStyle)) {
                v.setMarginInside(12);
            }
        }
        if (CustomConstant.STYLE_LAYOUT_ONECOL_HIGH.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_ONECOLLOW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_ONECOL_FIXED.equals(componentStyle)) {
            v.setMarginLeftRight(0);
        }
        v.setStyle(outSideStyle);
    }

    private static void setRatioByImgStyle(String componentStyle, CustomBaseRelativeLayout v) {
        if (CustomConstant.STYLE_LAYOUT_ONECOL_HIGH.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ONE_HEIGHT);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOLLOW.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ALL_LOW);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_FIXED.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ONE_LOW_FIXED);
        } else if (CustomConstant.STYLE_LAYOUT_TWOCOL_HIGH.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TWOCOL_LOW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TWOCOL_MID.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_ONECOL_ONEROW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_ONEROW_ONECOL.equals(componentStyle)) {
            v.setRatio(0.34375f);
        } else if (CustomConstant.STYLE_LAYOUT_THREECOL_LOW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_THREECOL_MID.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_THREECOL_HIGH.equals(componentStyle)) {
            v.setRatio(0.34375f);
        } else if (CustomConstant.STYLE_LAYOUT_FOURCOL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ALL_LOW);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_TWOROW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TWOROW_ONECOL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_STYLE_IMAGE_ONETWO);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_THREEROW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_ONECOL_THREEROW.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ONE_THREE);
        } else if (CustomConstant.STYLE_LAYOUT_TEXT_TWO_COL.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TEXT_THREE_COL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_STYLE_IMAGE_TEXT);
        }
    }

    private static void setRatioByLineStyle(String componentStyle, CustomBaseRelativeLayout v) {
        setRatioByDefaultStyle(componentStyle, v);
    }

    private static void setRatioByDefaultStyle(String componentStyle, CustomBaseRelativeLayout v) {
        if (CustomConstant.STYLE_LAYOUT_ONECOL_HIGH.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ONE_HEIGHT);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOLLOW.equals(componentStyle)) {
            v.setRatio(0.5f);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_FIXED.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ONE_LOW_FIXED);
        } else if (CustomConstant.STYLE_LAYOUT_TWOCOL_HIGH.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_TWO_HEIGHT);
        } else if (CustomConstant.STYLE_LAYOUT_TWOCOL_LOW.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ALL_LOW);
        } else if (CustomConstant.STYLE_LAYOUT_TWOCOL_MID.equals(componentStyle)) {
            v.setRatio(0.425f);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_ONEROW.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_THREE_MIDDLE);
        } else if (CustomConstant.STYLE_LAYOUT_ONEROW_ONECOL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_THREE_MIDDLE);
        } else if (CustomConstant.STYLE_LAYOUT_THREECOL_LOW.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ALL_LOW);
        } else if (CustomConstant.STYLE_LAYOUT_THREECOL_MID.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_THREE_MIDDLE);
        } else if (CustomConstant.STYLE_LAYOUT_THREECOL_HIGH.equals(componentStyle)) {
            v.setRatio(0.425f);
        } else if (CustomConstant.STYLE_LAYOUT_FOURCOL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ALL_LOW);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_TWOROW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TWOROW_ONECOL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ONE_TWO);
        } else if (CustomConstant.STYLE_LAYOUT_ONECOL_THREEROW.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_THREEROW_ONECOL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_ONE_THREE);
        } else if (CustomConstant.STYLE_LAYOUT_TEXT_THREE_COL.equals(componentStyle) || CustomConstant.STYLE_LAYOUT_TEXT_TWO_COL.equals(componentStyle)) {
            v.setRatio(CustomConstant.RATIO_STYLE_DEFAULT_TEXT);
        }
    }

    public static View dispatchChild(Context context, ConfigComponentModel component, int width, int height) {
        String viewStyle = component.getIconStyle();
        if ("image".equals(viewStyle)) {
            View img = new CustomBaseImg(context);
            img.setTag(component.getIcon());
            return img;
        } else if (CustomConstant.STYLE_COMPONENT_TEXT_IMAGE.equals(viewStyle)) {
            CustomImgUpDown vTemp = new CustomImgUpDown(context);
            vTemp.initViews(component);
            return vTemp;
        } else if (CustomConstant.STYLE_COMPONENT_TEXT_OVER_LAPDOWN.equals(viewStyle)) {
            CustomOverlayTextDown vTemp = new CustomOverlayTextDown(context);
            vTemp.initViews(component);
            return vTemp;
        } else if (CustomConstant.STYLE_COMPONENT_TEXT_OVER_LAP_DOWNVIDEO.equals(viewStyle)) {
            CustomVideoOverlayTextDown vTemp = new CustomVideoOverlayTextDown(context);
            vTemp.initViews(component);
            return vTemp;
        } else if (CustomConstant.STYLE_COMPONENT_TEXT_OVER_LAP_UPVIDEO.equals(viewStyle)) {
            CustomVideoOverlayTextUp vTemp = new CustomVideoOverlayTextUp(context);
            vTemp.initViews(component);
            return vTemp;
        } else if (CustomConstant.STYLE_COMPONENT_CIRCLE.equals(viewStyle)) {
            CustomBtnRound vTemp = new CustomBtnRound(context);
            vTemp.setMinSide(Math.min(width, height));
            vTemp.initViews(component);
            return vTemp;
        } else if ("news".equals(viewStyle)) {
            CustomListImgLeft vTemp = new CustomListImgLeft(context);
            vTemp.initViews(component);
            return vTemp;
        } else if (!"text".equals(viewStyle)) {
            return new View(context);
        } else {
            CustomBaseText vTemp = CustomHelper.createTextOnlyStyle(context);
            vTemp.initViews(component);
            return vTemp;
        }
    }
}

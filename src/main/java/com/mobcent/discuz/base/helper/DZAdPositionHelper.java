package com.mobcent.discuz.base.helper;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCResource;

public class DZAdPositionHelper {
    public static int[] getMainPostPosition(Context context) {
        MCResource resource = MCResource.getInstance(context.getApplicationContext());
        int adPosition1 = Integer.valueOf(resource.getString("mc_forum_main_posts_position")).intValue();
        int adPosition2 = Integer.valueOf(resource.getString("mc_forum_main_posts_position2")).intValue();
        int adPosition3 = Integer.valueOf(resource.getString("mc_forum_main_posts_position3")).intValue();
        return new int[]{adPosition1, adPosition2, adPosition3};
    }

    public static int getImgListTopPosition(Context context) {
        return Integer.valueOf(MCResource.getInstance(context.getApplicationContext()).getString("mc_forum_pic_topic_position")).intValue();
    }
}

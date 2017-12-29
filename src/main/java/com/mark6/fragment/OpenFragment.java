package com.mark6.fragment;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mark6.entity.Daily;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.org.fourtween.utils.ZodiacUtils;
import com.org.fourtween.utils.download.Downloader;
import com.org.fourtween.utils.download.HttpControl;

/**
 * Created by John on 2017/12/20.
 */

public class OpenFragment extends BaseFragment{

    private String[] reds;
    private String[] blues;
    private String[] greens;

    private Daily daily;
    private LinearLayout openNumLayout;

    @Override
    protected String getRootLayoutName() {
        return "open_fragment";
    }

    @Override
    protected void initActions(View view) {

    }

    @Override
    protected void initViews(View view) {
        Log.v("marksix","initViews");
        openNumLayout = (LinearLayout) findViewByName(view,"open_num");
        initData();
    }

    private void initData(){
        reds = activity.getResources().getStringArray(resource.getArrayId("red_ball_array"));
        blues = activity.getResources().getStringArray(resource.getArrayId("blue_ball_array"));
        greens = activity.getResources().getStringArray(resource.getArrayId("green_ball_array"));
        HttpControl httpControl = new HttpControl(resource.getString("open_base_url")+"/op",null,(byte)0,(byte)0);
        Downloader dl = new Downloader(activity,httpControl);
        dl.setCallback(new Downloader.DownLoadedCallback() {
            @Override
            public void onSuccess(Context context, String result) {
                Log.v("marksix",result);
                Gson gson = new Gson();
                daily = gson.fromJson(result, Daily.class);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        openNumLayout.addView(createNumView(daily.getData().getNum1(),false));
                        openNumLayout.addView(createNumView(daily.getData().getNum2(),false));
                        openNumLayout.addView(createNumView(daily.getData().getNum3(),false));
                        openNumLayout.addView(createNumView(daily.getData().getNum4(),false));
                        openNumLayout.addView(createNumView(daily.getData().getNum5(),false));
                        openNumLayout.addView(createNumView(daily.getData().getNum6(),false));
                        openNumLayout.addView(createNumView(null,true));
                        openNumLayout.addView(createNumView(daily.getData().getNum7(),false));
                    }
                });

            }

            @Override
            public void onFail(int code) {

            }
        });
        dl.doDownload();
    }


    private TextView createNumView(String num,boolean isPlus){
        TextView textView = new TextView(activity);
//        for(int i=1;i<=49;i++){
//            Log.v("marksix","--"+i+":"+ZodiacUtils.getZodiaName(i));
//        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(30),dip2px(30));
        lp.setMargins(dip2px(10),0,0,0);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        if(isPlus){
            textView.setBackground(resource.getDrawable("plus"));
            return textView;
        }
        if(contains(reds,num)){
            textView.setBackground(resource.getDrawable("ball_red_tool_lover"));
        }else if(contains(blues,num)){
            textView.setBackground(resource.getDrawable("ball_blue_tool_lover"));
        }else if(contains(greens,num)){
            textView.setBackground(resource.getDrawable("ball_green_tool_lover"));
        }
        textView.setText(num);
        return textView;
    }


    private boolean contains(String[] args,String item){
        if(args == null&&args.length<=0){
            return false;
        }else{
            for (String ele:args){
                if(ele.equals(item)){
                    return true;
                }
            }
        }
        return false;
    }
}

package com.mark6.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mark6.entity.Daily;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.org.fourtween.utils.FileUtils;
import com.org.fourtween.utils.ZodiacUtils;
import com.org.fourtween.utils.download.Downloader;
import com.org.fourtween.utils.download.HttpControl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by John on 2017/12/20.
 */

public class OpenFragment extends BaseFragment{

    private String[] reds;
    private String[] blues;
    private String[] greens;

    private Daily daily;
    private LinearLayout openNumLayout,openTextLayout;

    private String DAILY_INFO_CONFIG  = "daily_info_config";
    Gson gson = new Gson();

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
        openTextLayout = (LinearLayout) findViewByName(view,"open_text") ;
        initData();
    }

    private void initData(){
        reds = activity.getResources().getStringArray(resource.getArrayId("red_ball_array"));
        blues = activity.getResources().getStringArray(resource.getArrayId("blue_ball_array"));
        greens = activity.getResources().getStringArray(resource.getArrayId("green_ball_array"));

        StringBuilder strDaily = FileUtils.readFile(activity.getFileStreamPath(DAILY_INFO_CONFIG),"utf8");
        if(!TextUtils.isEmpty(strDaily)){
            daily = gson.fromJson(strDaily.toString(), Daily.class);
            String strDate = daily.getData().getDate();
            Log.v("marksix","1111111111111");

            if(!TextUtils.isEmpty(strDate)&&isToday(strDate)){
                initOpenNumLayout();
                initOpenTextLayout();
                Log.v("marksix","2222222222222");
                return;
            }
            Log.v("marksix","33333333333333333");
        }
        HttpControl httpControl = new HttpControl(resource.getString("open_base_url")+"/op",null,(byte)0,(byte)0);
        Downloader dl = new Downloader(activity,httpControl);
        dl.setCallback(new Downloader.DownLoadedCallback() {
            @Override
            public void onSuccess(Context context, String result) {
                Log.v("marksix",result);
                if(TextUtils.isEmpty(result)){
                    return ;
                }
                daily = gson.fromJson(result, Daily.class);
                FileUtils.writeFile(context.getFileStreamPath(DAILY_INFO_CONFIG),result,false);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initOpenNumLayout();
                        initOpenTextLayout();
                    }
                });

            }

            @Override
            public void onFail(int code) {

            }
        });
        dl.doDownload();


    }

    private boolean isToday(String strDate){

        SimpleDateFormat sdf = new SimpleDateFormat("HH/DD/MM/yyyy");

        long time = 0;
        try {
            Log.v("marksix","--"+time+"--"+sdf.parse("01/"+strDate));
            time = sdf.parse("01/"+strDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        Calendar today = Calendar.getInstance();
        Calendar tommror = Calendar.getInstance();
        tommror.add(Calendar.DAY_OF_MONTH, 1);
        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        tommror.set(tommror.get(Calendar.YEAR), tommror.get(Calendar.MONTH), tommror.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Log.v("marksix","--"+today.getTime()+"--"+tommror.getTime());

        Log.v("marksix","--"+time+"--"+today.getTimeInMillis()+"--"+tommror.getTimeInMillis());

        return time>=today.getTimeInMillis()&&time<tommror.getTimeInMillis();

    }

    private void initOpenNumLayout(){
        openNumLayout.addView(createNumView(daily.getData().getNum1(),false));
        openNumLayout.addView(createNumView(daily.getData().getNum2(),false));
        openNumLayout.addView(createNumView(daily.getData().getNum3(),false));
        openNumLayout.addView(createNumView(daily.getData().getNum4(),false));
        openNumLayout.addView(createNumView(daily.getData().getNum5(),false));
        openNumLayout.addView(createNumView(daily.getData().getNum6(),false));
        openNumLayout.addView(createNumView(null,true));
        openNumLayout.addView(createNumView(daily.getData().getNum7(),false));
    }

    private TextView createNumView(String num,boolean isPlus){
        TextView textView = new TextView(activity);
//        for(int i=1;i<=49;i++){
//            Log.v("marksix","--"+i+":"+ZodiacUtils.getZodiaName(i));
//        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(30),dip2px(30));

        lp.setMargins(dip2px(10),0,0,0);
        textView.setLayoutParams(lp);
//        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(dip2px(5),dip2px(4),0,0);

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

    private void initOpenTextLayout(){
        openTextLayout.addView(createTextView(daily.getData().getNum1(),false));
        openTextLayout.addView(createTextView(daily.getData().getNum2(),false));
        openTextLayout.addView(createTextView(daily.getData().getNum3(),false));
        openTextLayout.addView(createTextView(daily.getData().getNum4(),false));
        openTextLayout.addView(createTextView(daily.getData().getNum5(),false));
        openTextLayout.addView(createTextView(daily.getData().getNum6(),false));
        openTextLayout.addView(createTextView(null,true));
        openTextLayout.addView(createTextView(daily.getData().getNum7(),false));
    }


    private View createTextView(String num,boolean isPlus){
        TextView textView = new TextView(activity);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(30),dip2px(30));
        lp.setMargins(dip2px(10),0,0,0);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        if(isPlus){
            textView.setText("");
            return textView;
        }

        textView.setText(ZodiacUtils.getZodiaName(Integer.parseInt(num)));
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

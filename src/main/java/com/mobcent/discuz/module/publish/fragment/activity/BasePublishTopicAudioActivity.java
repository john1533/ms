package com.mobcent.discuz.module.publish.fragment.activity;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.UploadFileTask;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils.AudioPlayListener;
import com.mobcent.lowest.android.ui.utils.MCRecordUtils;
import com.mobcent.lowest.android.ui.utils.MCRecordUtils.RecordListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.io.File;
import java.util.List;

public class BasePublishTopicAudioActivity extends BasePublishFaceAndMentionActivty implements RecordListener {
    private TextView additionVoiceText;
    protected int audioDuration;
    private ImageView deleVoiceImg;
    private long endTime;
    protected boolean hasAudio = false;
    private boolean isRecording = false;
    private Handler mHandler = new Handler();
    private int maxAmplitude;
    private ImageView recordImg;
    private MCRecordUtils recordUtils;
    private Button recordingBtn;
    protected int sortId = 0;
    private long startTime;
    private TextView timeText;
    private ImageView voiceImg;

    private class MonitorThread extends Thread {
        private MonitorThread() {
        }

        public void run() {
            while (BasePublishTopicAudioActivity.this.recordUtils != null && BasePublishTopicAudioActivity.this.isRecording) {
                try {
                    final int uv = (BasePublishTopicAudioActivity.this.maxAmplitude * 5) / 32768;
                    BasePublishTopicAudioActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (uv == 0) {
                                BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_1"));
                            } else if (uv <= 3) {
                                BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_2"));
                            } else if (uv <= 6) {
                                BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_3"));
                            } else if (uv <= 8) {
                                BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_4"));
                            } else if (uv <= 10) {
                                BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_5"));
                            } else if (uv <= 12) {
                                BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_6"));
                            } else if (uv >= 17) {
                                BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_6"));
                            }
                        }
                    });
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BasePublishTopicAudioActivity.this.mHandler.post(new Runnable() {
                    public void run() {
                        BasePublishTopicAudioActivity.this.recordImg.setBackgroundDrawable(null);
                    }
                });
            }
        }
    }

    private class TimeThread extends Thread {
        private TimeThread() {
        }

        public void run() {
            while (BasePublishTopicAudioActivity.this.recordUtils != null && BasePublishTopicAudioActivity.this.isRecording) {
                try {
                    BasePublishTopicAudioActivity.this.endTime = System.currentTimeMillis();
                    final int time = (int) ((BasePublishTopicAudioActivity.this.endTime - BasePublishTopicAudioActivity.this.startTime) / 1000);
                    BasePublishTopicAudioActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            BasePublishTopicAudioActivity.this.timeText.setText(time + "\"");
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    protected void initDatas() {
        super.initDatas();
        initRecord();
    }

    protected void initViews() {
        super.initViews();
        this.voiceViewBox = (RelativeLayout) findViewByName("mc_forum_voice_view");
        this.voiceBox = (RelativeLayout) findViewByName("mc_forum_voice_box");
        this.recordImg = (ImageView) findViewByName("mc_forum_recording_img");
        this.recordingBtn = (Button) findViewByName("mc_forum_recording_btn");
        this.voiceImg = (ImageView) findViewByName("mc_forum_voice_img");
        this.additionVoiceText = (TextView) findViewByName("mc_forum_addition_text");
        this.deleVoiceImg = (ImageView) findViewByName("mc_forum_dele_voice_img");
        this.timeText = (TextView) findViewByName("mc_forum_time");
        this.recordingBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_recording2_img2_n"));
    }

    protected void initActions() {
        super.initActions();
        this.voiceBox.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                BasePublishTopicAudioActivity.this.imm.hideSoftInputFromWindow(BasePublishTopicAudioActivity.this.contentEdText.getWindowToken(), 2);
                if (BasePublishTopicAudioActivity.this.boardId <= 0) {
                    MCToastUtils.toastByResName(BasePublishTopicAudioActivity.this, "mc_forum_publish_select_board", 0);
                } else if (BasePublishTopicAudioActivity.this.allowPostAttachment != 1) {
                    MCToastUtils.toastByResName(BasePublishTopicAudioActivity.this, "mc_forum_record_permission", 0);
                } else if (BasePublishTopicAudioActivity.this.hasAudio) {
                    MCAudioUtils.getInstance(BasePublishTopicAudioActivity.this.getApplicationContext()).playAudioOnLine(BasePublishTopicAudioActivity.this.audioPath);
                } else {
                    BasePublishTopicAudioActivity.this.voiceViewBox.setVisibility(0);
                    BasePublishTopicAudioActivity.this.timeText.setText("");
                }
            }
        });
        this.recordingBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    BasePublishTopicAudioActivity.this.recordingBtn.setBackgroundResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img2_h"));
                    BasePublishTopicAudioActivity.this.recording();
                } else if (event.getAction() == 1 || event.getAction() == 3) {
                    BasePublishTopicAudioActivity.this.recordingBtn.setBackgroundResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img2_n"));
                    BasePublishTopicAudioActivity.this.hasAudio = true;
                    BasePublishTopicAudioActivity.this.isPlayVoice(true);
                    BasePublishTopicAudioActivity.this.recorded();
                }
                return true;
            }
        });
        MCAudioUtils.getInstance(getApplicationContext()).registerListener(new AudioPlayListener() {
            public void onAudioStatusChange(String url, String path, int status, int percent) {
                switch (status) {
                    case 2:
                        BasePublishTopicAudioActivity.this.recordImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_recording2_img1_1"));
                        switch (percent % 3) {
                            case 0:
                                BasePublishTopicAudioActivity.this.voiceImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_voice_chat_img1"));
                                return;
                            case 1:
                                BasePublishTopicAudioActivity.this.voiceImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_voice_chat_img2"));
                                return;
                            case 2:
                                BasePublishTopicAudioActivity.this.voiceImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_voice_chat_img3"));
                                return;
                            default:
                                BasePublishTopicAudioActivity.this.voiceImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                                return;
                        }
                    case 6:
                        BasePublishTopicAudioActivity.this.voiceImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                        return;
                    case 7:
                        BasePublishTopicAudioActivity.this.voiceImg.setImageResource(BasePublishTopicAudioActivity.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                        MCToastUtils.toastByResName(BasePublishTopicAudioActivity.this.getApplicationContext(), "mc_forum_audio_play_error", 0);
                        return;
                    default:
                        return;
                }
            }
        });
        this.deleVoiceImg.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                BasePublishTopicAudioActivity.this.isPlayVoice(false);
                BasePublishTopicAudioActivity.this.clearAudioTempFile();
                BasePublishTopicAudioActivity.this.hasAudio = false;
            }
        });
        this.voiceViewBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                BasePublishTopicAudioActivity.this.voiceViewBox.setVisibility(8);
                return true;
            }
        });
    }

    private void initRecord() {
        this.recordUtils = MCRecordUtils.getInastance(this);
        this.recordUtils.setRecordListener(this);
        this.recordUtils.setMaxAudioTime(FinalConstant.MAX_RECORD_TIME);
    }

    public void onRecordStatusChange(String path, int status, int maxAmplitudes) {
        this.maxAmplitude = maxAmplitudes;
        this.audioPath = path;
    }

    private void recording() {
        this.recordUtils.startRecordMp3();
        this.isRecording = true;
        this.startTime = System.currentTimeMillis();
        new MonitorThread().start();
        new TimeThread().start();
    }

    private void recorded() {
        this.hasAudio = true;
        this.endTime = System.currentTimeMillis();
        int time = (int) ((this.endTime - this.startTime) / 1000);
        this.recordUtils.stopRecord();
        this.isRecording = false;
        if (time < 1) {
            toast(this.resource.getString("mc_forum_warn_recorder_min_len"));
            this.hasAudio = false;
            isPlayVoice(false);
            this.voiceViewBox.setVisibility(8);
            return;
        }
        this.audioDuration = time;
    }

    public void isPlayVoice(boolean isPlay) {
        if (isPlay) {
            this.voiceImg.setImageResource(this.resource.getDrawableId("mc_forum_recording2_img5_n"));
            this.additionVoiceText.setText(this.resource.getString("mc_forum_rapid_publish_try_voice"));
            this.deleVoiceImg.setVisibility(0);
            this.voiceViewBox.setVisibility(8);
            return;
        }
        this.voiceImg.setImageResource(this.resource.getDrawableId("mc_forum_publish_icons1_n"));
        this.additionVoiceText.setText(this.resource.getString("mc_forum_rapid_publish_addition_voice"));
        this.deleVoiceImg.setVisibility(8);
    }

    private void clearAudioTempFile() {
        File f = new File(this.audioPath);
        if (f.exists()) {
            f.delete();
        }
    }

    protected void uploadAudio() {
        new UploadFileTask(getApplicationContext(), new String[]{new String(this.audioPath)}, "forum", "audio", this.boardId, (long) this.sortId, 0, new BaseRequestCallback<BaseResultModel<List<UploadPictureModel>>>() {
            public void onPreExecute() {
            }

            public void onPostExecute(BaseResultModel<List<UploadPictureModel>> result) {
                DZToastAlertUtils.toast(BasePublishTopicAudioActivity.this, result);
                String path = "";
                if (result == null || result.getRs() != 1) {
                    BasePublishTopicAudioActivity.this.draftService.saveDraftModel(BasePublishTopicAudioActivity.this.convertDraftModel());
                    MCToastUtils.toastByResName(BasePublishTopicAudioActivity.this.getApplicationContext(), "mc_forum_warn_update_fail");
                    return;
                }
                if (!MCListUtils.isEmpty((List) result.getData()) && ((List) result.getData()).size() > 0) {
                    path = ((UploadPictureModel) ((List) result.getData()).get(0)).getPicPath();
                }
                if (MCStringUtil.isEmpty(path) || !path.startsWith("#")) {
                    BasePublishTopicAudioActivity.this.audioPath = path;
                    BasePublishTopicAudioActivity.this.clearAudioTempFile();
                    BasePublishTopicAudioActivity.this.uploadAudioSucc();
                    return;
                }
                BasePublishTopicAudioActivity.this.publishIng = false;
                String errorCode = path.substring(path.indexOf("#") + 1, path.length());
                if (errorCode != null && !errorCode.equals("PublishTopicActivity")) {
                    MCToastUtils.toastByResName(BasePublishTopicAudioActivity.this.getApplicationContext(), errorCode);
                }
            }
        }).execute(new Void[0]);
    }

    protected void uploadAudioSucc() {
    }

    protected boolean publicTopic() {
        return true;
    }
}

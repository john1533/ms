package com.mobcent.discuz.activity.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper.FinishListener;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.util.DZPoiUtil;
import com.mobcent.discuz.android.util.DZPoiUtil.PoiDelegate;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.publish.adapter.FacePagerAdapter;
import com.mobcent.discuz.module.publish.adapter.FacePagerAdapter.OnFaceImageClickListener;
import com.mobcent.discuz.module.publish.delegate.MentionFriendReturnDelegate;
import com.mobcent.discuz.module.publish.delegate.MentionFriendReturnDelegate.OnMentionChannelListener;
import com.mobcent.discuz.module.publish.delegate.PoiItemDelegate;
import com.mobcent.discuz.module.publish.delegate.PoiItemDelegate.ClickPoiItemLisenter;
import com.mobcent.discuz.module.publish.fragment.activity.MentionFriendFragmentActivity;
import com.mobcent.discuz.module.publish.fragment.activity.PoiFragmentAcitvity;
import com.mobcent.lowest.android.ui.utils.MCRecordUtils;
import com.mobcent.lowest.android.ui.utils.MCRecordUtils.RecordListener;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MCMultiBottomView extends RelativeLayout {
    public static final int AT_ACTION = 6;
    public static final int CAMERA_ACTION = 10;
    private static final int EDIT_ACTION = 5;
    public static final int FACE_ACTION = 2;
    public static final int MORE_ACTION = 1;
    public static final int PIC_ACTION = 7;
    public static final int PIC_TYPE = 2;
    public static final int POI_ACTION = 4;
    public static final int RECORD_ACTION = 3;
    public static final int RECORD_TYPE = 3;
    private static final int RETURN_ACTION = 9;
    public static final int SEND_ACTION = 11;
    public static final int SEND_TYPE = 1;
    public static final int SHARE_ACTION = 8;
    public static final int SHARE_TYPE = 4;
    private final String TAG;
    private Map<String, UserInfoModel> atFriendMap;
    private boolean atPerm;
    private List<UserInfoModel> atUserList;
    private Context context;
    private long endTime;
    private ViewPager facePager;
    private FacePagerAdapter facePagerAdapter;
    private FinishListener finishListener;
    private HeightDelegate heightDelegate;
    private LinearLayout imageBox;
    private HorizontalScrollView imageScrollView;
    private LayoutInflater inflater;
    private int[] innerShow;
    private List<InnerShowModel> innerShowList;
    private boolean isRecording;
    private double latitude;
    private RelativeLayout locationBox;
    private boolean locationOff;
    private MCProgressBar locationProgress;
    private String locationStr;
    private TextView locationText;
    private MCLocationUtil locationUtil;
    private double longitude;
    private Handler mHandler;
    private int mScreenHeight;
    private int maxAmplitude;
    private boolean modifyPicUI;
    private boolean modifyRecordUI;
    private GridView moreGidView;
    private MoreGridViewAdapter moreGridViewAdapter;
    private RelativeLayout multiBottomBox;
    private RelativeLayout multiFaceBox;
    private RelativeLayout multiMoreBox;
    private OnPicDelegate onPicDelegate;
    private OnRecordDelegate onRecordDelegate;
    private OnSendDelegate onSendDelegate;
    private OnShareDelegate onShareDelegate;
    private int[] outerLeftShow;
    private int[] outerRightShow;
    private PhotoManageHelper photoManageHelper;
    public Map<String, PictureModel> picMap;
    private boolean picPerm;
    private List<String> poiList;
    private DZPoiUtil poiUtil;
    private String recordAbsolutePath;
    private RelativeLayout recordBox;
    private Button recordBtn;
    private ImageView recordImage;
    private boolean recordPerm;
    private boolean recordShort;
    private TextView recordTime;
    private MCRecordUtils recordUtil;
    private EditText replyEdit;
    private MCResource resource;
    public SendModel sendModel;
    private boolean sendPerm;
    private Button sendReplyBtn;
    private int showLocation;
    private char spaceChar;
    private long startTime;
    private LinearLayout takeBox;
    private Button takeBtn1;
    private Button takeBtn2;
    private Button takeBtn3;
    private View view;

    public interface HeightDelegate {
        void resetHeight(int i);
    }

    private class MonitorThread extends Thread {
        private MonitorThread() {
        }

        public void run() {
            while (MCMultiBottomView.this.recordUtil != null && MCMultiBottomView.this.isRecording) {
                try {
                    final int uv = (MCMultiBottomView.this.maxAmplitude * 5) / 32768;
                    MCMultiBottomView.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (uv == 0) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_1"));
                            } else if (uv == 1) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_2"));
                            } else if (uv == 2) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_3"));
                            } else if (uv == 3) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_4"));
                            } else if (uv == 4) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_5"));
                            } else if (uv == 5) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_6"));
                            } else if (uv == 6) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_7"));
                            } else if (uv == 7) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_8"));
                            } else if (uv == 8) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_9"));
                            } else if (uv == 9) {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_10"));
                            } else {
                                MCMultiBottomView.this.recordImage.setImageResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_recording_img1_10"));
                            }
                        }
                    });
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            MCMultiBottomView.this.mHandler.post(new Runnable() {
                public void run() {
                    MCMultiBottomView.this.recordImage.setImageDrawable(null);
                }
            });
        }
    }

    private class MoreGridViewAdapter extends BaseAdapter {
        private List<InnerShowModel> innerShowList;
        private MCResource resource;

        public class MoreGridViewAdapterHolder {
            private ImageView imageView;
            private TextView textView;

            public ImageView getImageView() {
                return this.imageView;
            }

            public void setImageView(ImageView imageView) {
                this.imageView = imageView;
            }

            public TextView getTextView() {
                return this.textView;
            }

            public void setTextView(TextView textView) {
                this.textView = textView;
            }
        }

        public void setInnerShowList(List<InnerShowModel> innerShowList) {
            this.innerShowList = innerShowList;
        }

        public MoreGridViewAdapter(List<InnerShowModel> innerShowList) {
            this.innerShowList = innerShowList;
            this.resource = MCResource.getInstance(MCMultiBottomView.this.context.getApplicationContext());
        }

        public int getCount() {
            return this.innerShowList.size();
        }

        public Object getItem(int position) {
            return this.innerShowList.get(position);
        }

        public long getItemId(int position) {
            return (long) ((InnerShowModel) this.innerShowList.get(position)).getAction();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            InnerShowModel innerShowModel = (InnerShowModel) this.innerShowList.get(position);
            convertView = getMoreGridView(convertView);
            MoreGridViewAdapterHolder holder = (MoreGridViewAdapterHolder) convertView.getTag();
            holder.getImageView().setBackgroundResource(this.resource.getDrawableId(innerShowModel.getDrawableId()));
            holder.getTextView().setText(innerShowModel.getTitle());
            return convertView;
        }

        private View getMoreGridView(View convertView) {
            MoreGridViewAdapterHolder holder;
            if (convertView == null) {
                convertView = MCMultiBottomView.this.inflater.inflate(this.resource.getLayoutId("multi_more_grid_view_item"), null);
                holder = new MoreGridViewAdapterHolder();
                initMoreGridViewAdapterHolder(convertView, holder);
                convertView.setTag(holder);
                return convertView;
            }
            holder = (MoreGridViewAdapterHolder) convertView.getTag();
            return convertView;
        }

        private void initMoreGridViewAdapterHolder(View convertView, MoreGridViewAdapterHolder holder) {
            holder.setImageView((ImageView) convertView.findViewById(this.resource.getViewId("item_img")));
            holder.setTextView((TextView) convertView.findViewById(this.resource.getViewId("item_text")));
        }
    }

    public interface OnPicDelegate {
        void sendPic(int i, PicModel picModel);
    }

    public interface OnRecordDelegate {
        void sendRecord(int i, RecordModel recordModel);
    }

    public interface OnSendDelegate {
        void sendReply(int i, SendModel sendModel);
    }

    public interface OnShareDelegate {
        void share(int i);
    }

    private class TimeThread extends Thread {
        private TimeThread() {
        }

        public void run() {
            while (MCMultiBottomView.this.recordUtil != null && MCMultiBottomView.this.isRecording) {
                try {
                    MCMultiBottomView.this.endTime = System.currentTimeMillis();
                    final int time = (int) ((MCMultiBottomView.this.endTime - MCMultiBottomView.this.startTime) / 1000);
                    MCMultiBottomView.this.mHandler.post(new Runnable() {
                        public void run() {
                            MCMultiBottomView.this.recordTime.setText(time + "\"");
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

    private class InnerShowModel extends BaseModel {
        private static final long serialVersionUID = -8107194462694638445L;
        private int action;
        private String drawableId;
        private String title;

        private InnerShowModel() {
        }

        public int getAction() {
            return this.action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public String getDrawableId() {
            return this.drawableId;
        }

        public void setDrawableId(String drawableId) {
            this.drawableId = drawableId;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public class PicModel extends BaseModel {
        private static final long serialVersionUID = -5130706826965291265L;
        private Map<String, PictureModel> selectMap;

        public Map<String, PictureModel> getSelectMap() {
            return this.selectMap;
        }

        public void setSelectMap(Map<String, PictureModel> selectMap) {
            this.selectMap = selectMap;
        }
    }

    public class PoiModel extends BaseModel {
        private static final long serialVersionUID = -5301286964016880049L;
        private boolean isOpen;
        private double latitude;
        private String locationStr;
        private double longitude;

        public String getLocationStr() {
            return this.locationStr;
        }

        public void setLocationStr(String locationStr) {
            this.locationStr = locationStr;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public boolean isOpen() {
            return this.isOpen;
        }

        public void setOpen(boolean isOpen) {
            this.isOpen = isOpen;
        }
    }

    public class RecordModel extends BaseModel {
        private static final long serialVersionUID = 1;
        private String audioPath;

        public String getAudioPath() {
            return this.audioPath;
        }

        public void setAudioPath(String audioPath) {
            this.audioPath = audioPath;
        }
    }

    public class SendModel extends BaseModel {
        private static final long serialVersionUID = 270641792885692821L;
        private PicModel picModel;
        private PoiModel poiModel;
        private RecordModel recordModel;
        private WordModel wordModel;

        public WordModel getWordModel() {
            return this.wordModel;
        }

        public void setWordModel(WordModel wordModel) {
            this.wordModel = wordModel;
        }

        public PicModel getPicModel() {
            return this.picModel;
        }

        public void setPicModel(PicModel picModel) {
            this.picModel = picModel;
        }

        public PoiModel getPoiModel() {
            return this.poiModel;
        }

        public void setPoiModel(PoiModel poiModel) {
            this.poiModel = poiModel;
        }

        public RecordModel getRecordModel() {
            return this.recordModel;
        }

        public void setRecordModel(RecordModel recordModel) {
            this.recordModel = recordModel;
        }
    }

    public class WordModel extends BaseModel {
        private static final long serialVersionUID = 4166765705604527018L;
        private Map<String, UserInfoModel> atFriendMap;
        private String content;

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Map<String, UserInfoModel> getAtFriendMap() {
            return this.atFriendMap;
        }

        public void setAtFriendMap(Map<String, UserInfoModel> atFriendMap) {
            this.atFriendMap = atFriendMap;
        }
    }

    public Map<String, PictureModel> getPicMap() {
        return this.picMap;
    }

    public void setPicMap(Map<String, PictureModel> picMap) {
        this.picMap = picMap;
    }

    public SendModel getSendModel() {
        return this.sendModel;
    }

    public void setSendModel(SendModel sendModel) {
        this.sendModel = sendModel;
    }

    private void showLoading(final MCProgressBar progressBar) {
        this.mHandler.post(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.show();
            }
        });
    }

    private void hideLoading(final MCProgressBar progressBar) {
        this.mHandler.post(new Runnable() {
            public void run() {
                progressBar.hide();
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public MCMultiBottomView(Context context) {
        this(context, null);
    }

    public MCMultiBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "MCMultiBottomView";
        this.locationOff = true;
        this.mHandler = new Handler();
        this.poiList = new ArrayList();
        this.recordShort = false;
        this.recordPerm = true;
        this.picPerm = true;
        this.atPerm = true;
        this.sendPerm = true;
        this.showLocation = 0;
        this.atUserList = new ArrayList();
        this.modifyPicUI = true;
        this.modifyRecordUI = false;
        this.atFriendMap = new LinkedHashMap();
        this.spaceChar = ' ';
        this.outerLeftShow = new int[]{1, 2};
        this.outerRightShow = new int[]{3};
        this.innerShow = new int[]{4, 6, 7, 10};
        this.innerShowList = new ArrayList();
        this.context = context;
        initData();
        initViews();
        initWidgetActions();
    }

    private void initData() {
        this.resource = MCResource.getInstance(this.context.getApplicationContext());
        this.inflater = LayoutInflater.from(this.context.getApplicationContext());
        this.photoManageHelper = new PhotoManageHelper(this.context.getApplicationContext());
        this.photoManageHelper.setMaxSelectNum(5);
        this.photoManageHelper.setCallBackRightNow(!this.modifyPicUI);
        initLocationData();
        initRecord();
        this.picMap = new LinkedHashMap();
    }

    private void initLocationData() {
        this.locationUtil = MCLocationUtil.getInstance(this.context.getApplicationContext());
        this.poiUtil = DZPoiUtil.getInstance(this.context.getApplicationContext());
    }

    private void initRecord() {
        this.recordUtil = MCRecordUtils.getInastance(this.context.getApplicationContext());
        this.recordUtil.setMaxAudioTime(FinalConstant.MAX_RECORD_TIME);
        this.recordUtil.setRecordListener(new RecordListener() {
            public void onRecordStatusChange(String path, int status, int amplitude) {
                MCMultiBottomView.this.maxAmplitude = amplitude;
                if (1 == status && !MCMultiBottomView.this.recordShort) {
                    MCMultiBottomView.this.recordAbsolutePath = path;
                    MCMultiBottomView.this.isRecording = false;
                    if (!MCMultiBottomView.this.modifyRecordUI) {
                        RecordModel recordModel = new RecordModel();
                        recordModel.setAudioPath(MCMultiBottomView.this.recordAbsolutePath);
                        MCMultiBottomView.this.clearRecord();
                        if (MCMultiBottomView.this.onRecordDelegate != null) {
                            MCMultiBottomView.this.onRecordDelegate.sendRecord(3, recordModel);
                        }
                    }
                }
                if (-1 == status) {
                    MCMultiBottomView.this.mHandler.post(new Runnable() {
                        public void run() {
                            MCToastUtils.toast(MCMultiBottomView.this.context.getApplicationContext(), MCStringBundleUtil.resolveString(MCMultiBottomView.this.resource.getStringId("mc_forum_warn_recorder_limit"), String.valueOf(59), MCMultiBottomView.this.context.getApplicationContext()));
                        }
                    });
                }
            }
        });
    }

    private View findViewByName(View rootView, String viewId) {
        return rootView.findViewById(this.resource.getViewId(viewId));
    }

    private void initViews() {
        this.view = this.inflater.inflate(this.resource.getLayoutId("multi_bottom_view"), this, true);
        this.multiBottomBox = (RelativeLayout) findViewByName(this.view, "multi_bottom_box");
        this.multiBottomBox.setBackgroundColor(0);
        this.takeBox = (LinearLayout) findViewByName(this.view, "take_box");
        this.takeBtn1 = (Button) findViewByName(this.view, "take_btn1");
        MCTouchUtil.createTouchDelegate(this.takeBtn1, 10);
        this.takeBtn2 = (Button) findViewByName(this.view, "take_btn2");
        MCTouchUtil.createTouchDelegate(this.takeBtn2, 10);
        this.takeBtn3 = (Button) findViewByName(this.view, "take_btn3");
        MCTouchUtil.createTouchDelegate(this.takeBtn3, 10);
        this.recordBtn = (Button) findViewByName(this.view, "record_btn");
        this.sendReplyBtn = (Button) findViewByName(this.view, "send_reply_btn");
        this.replyEdit = (EditText) findViewByName(this.view, "reply_edit");
        this.imageScrollView = (HorizontalScrollView) findViewByName(this.view, "image_scroll_view");
        this.imageBox = (LinearLayout) findViewByName(this.view, "image_box");
        this.recordBox = (RelativeLayout) findViewByName(this.view, "record_box");
        this.recordImage = (ImageView) findViewByName(this.view, "record_img");
        this.recordTime = (TextView) findViewByName(this.view, "record_time_text");
        initTakeBtnView();
    }

    private void initFaceView() {
        this.multiFaceBox = (RelativeLayout) findViewByName(this.view, "multi_face_box");
        this.facePager = (ViewPager) findViewByName(this.view, "face_pager");
        ImageView select1 = (ImageView) findViewByName(this.view, "indicate_select1");
        ImageView select2 = (ImageView) findViewByName(this.view, "indicate_select2");
        ImageView select3 = (ImageView) findViewByName(this.view, "indicate_select3");
        final ImageView[] selects = new ImageView[]{select1, select2, select3};
        this.facePagerAdapter = new FacePagerAdapter(this.context, DZFaceUtil.getFaceConstant(this.context).getFaceList());
        this.facePager.setAdapter(this.facePagerAdapter);
        this.facePager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                for (int i = 0; i < selects.length; i++) {
                    if (i == position) {
                        selects[i].setImageDrawable(MCMultiBottomView.this.context.getResources().getDrawable(MCMultiBottomView.this.resource.getDrawableId("mc_forum_select2_2")));
                    } else {
                        selects[i].setImageDrawable(MCMultiBottomView.this.context.getResources().getDrawable(MCMultiBottomView.this.resource.getDrawableId("mc_forum_select2_1")));
                    }
                }
            }
        });
        this.facePagerAdapter.setOnFaceImageClickListener(new OnFaceImageClickListener() {
            public void onFaceImageClick(String tag, Drawable drawable) {
                try {
                    SpannableStringBuilder spannable = new SpannableStringBuilder(tag);
                    spannable.setSpan(new ImageSpan(drawable, 0), 0, tag.length(), 33);
                    MCMultiBottomView.this.replyEdit.getText().insert(MCMultiBottomView.this.replyEdit.getSelectionEnd(), spannable);
                } catch (Exception e) {
                }
            }
        });
        this.multiFaceBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void initMoreView() {
        this.multiMoreBox = (RelativeLayout) findViewByName(this.view, "multi_more_box");
        this.moreGidView = (GridView) findViewByName(this.view, "more_grid");
        this.locationBox = (RelativeLayout) findViewByName(this.view, "location_box");
        this.locationProgress = (MCProgressBar) findViewByName(this.view, "location_progress_bar");
        this.locationText = (TextView) findViewByName(this.view, "location_text");
        setInnerShow(this.innerShow);
        this.multiMoreBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.locationBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MCMultiBottomView.this.context.getApplicationContext(), PoiFragmentAcitvity.class);
                intent.putExtra(PoiFragmentAcitvity.POI_LIST, (ArrayList) MCMultiBottomView.this.poiList);
                MCMultiBottomView.this.context.startActivity(intent);
            }
        });
        MentionFriendReturnDelegate.getInstance().setOnLoginChannelListener(new OnMentionChannelListener() {
            public void changeMentionFriend(UserInfoModel mentionFriend) {
                MCMultiBottomView.this.getAtFriendListFromContent();
                if (mentionFriend.getUserId() <= 0) {
                    return;
                }
                if (MCMultiBottomView.this.atFriendMap.size() < 20) {
                    MCMultiBottomView.this.replyEdit.getText().insert(MCMultiBottomView.this.replyEdit.getSelectionEnd(), "@" + mentionFriend.getNickname() + " ");
                    MCMultiBottomView.this.atFriendMap.put(mentionFriend.getNickname(), mentionFriend);
                    return;
                }
                MCToastUtils.toast(MCMultiBottomView.this.context, MCStringBundleUtil.resolveString(MCMultiBottomView.this.resource.getStringId("mc_forum_mention_friend_count"), String.valueOf(20), MCMultiBottomView.this.context));
            }
        });
        this.finishListener = new FinishListener() {
            public void cameraFinish(int type, Map<String, PictureModel> outerSelectMap, String outputPath, Bitmap bitmap) {
                MCMultiBottomView.this.picMap.putAll(outerSelectMap);
                MCMultiBottomView.this.updateImageUI(outerSelectMap);
            }

            public void photoFinish(int type, Map<String, PictureModel> outerSelectMap) {
                MCMultiBottomView.this.picMap.putAll(outerSelectMap);
                MCMultiBottomView.this.updateImageUI(outerSelectMap);
            }
        };
        this.photoManageHelper.registListener(this.finishListener);
        PoiItemDelegate.getInstance().setClickPoiItemLisenter(new ClickPoiItemLisenter() {
            public void clickItem(String poi) {
                MCMultiBottomView.this.locationStr = MCMultiBottomView.this.locationStr + poi;
                MCMultiBottomView.this.locationText.setText(MCMultiBottomView.this.locationStr);
            }
        });
    }

    public void updateImageUI(final Map<String, PictureModel> outerSelectMap) {
        if (this.modifyPicUI) {
            if (outerSelectMap.isEmpty()) {
                this.imageScrollView.setVisibility(View.GONE);
                return;
            }
            this.imageScrollView.setVisibility(View.VISIBLE);
            int i = -1;
            this.imageBox.removeAllViews();
            for (String key : outerSelectMap.keySet()) {
                i++;
                final PictureModel pictureModel = (PictureModel) outerSelectMap.get(key);
                final View photoView = this.inflater.inflate(this.resource.getLayoutId("multi_photo_show_view"), null);
                ImageView normalImg = (ImageView) photoView.findViewById(this.resource.getViewId("normal_img"));
                ImageView closeImg = (ImageView) photoView.findViewById(this.resource.getViewId("close_img"));
                normalImg.setTag(pictureModel.getAbsolutePath());
                loadImage(normalImg);
                final int position = i;
                normalImg.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        MCMultiBottomView.this.photoManageHelper.openPhotoPreview((Activity) MCMultiBottomView.this.context, 2, position, PhotoManageHelper.PREVIEW_NORMAL_ALBUM_PATH);
                    }
                });
                closeImg.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        outerSelectMap.remove(pictureModel.getAbsolutePath());
                        MCMultiBottomView.this.imageBox.removeView(photoView);
                        if (outerSelectMap.isEmpty()) {
                            MCMultiBottomView.this.imageScrollView.setVisibility(View.GONE);
                        }
                    }
                });
                this.imageBox.addView(photoView);
            }
        } else if (!outerSelectMap.isEmpty()) {
            PicModel picModel = new PicModel();
            Map<String, PictureModel> tempMap = new LinkedHashMap();
            for (String key2 : outerSelectMap.keySet()) {
                tempMap.put(key2, outerSelectMap.get(key2));
            }
            picModel.setSelectMap(tempMap);
            clearPic();
            if (this.onPicDelegate != null) {
                this.onPicDelegate.sendPic(2, picModel);
            }
        }
    }

    private void loadImage(ImageView imageView) {
        ImageLoader.getInstance().displayImage((String) imageView.getTag(), imageView);
    }

    public void setShowLocation(int showLocation) {
        this.showLocation = showLocation;
    }

    public void setRecordPerm(boolean recordPerm) {
        this.recordPerm = recordPerm;
    }

    public void setPicPerm(boolean picPerm) {
        this.picPerm = picPerm;
    }

    public void setAtPerm(boolean atPerm) {
        this.atPerm = atPerm;
    }

    public void setSendPerm(boolean sendPerm) {
        this.sendPerm = sendPerm;
    }

    public void setAtUserList(List<UserInfoModel> atUserList) {
        this.atUserList = atUserList;
    }

    @SuppressLint({"NewApi"})
    private void initWidgetActions() {
        this.replyEdit.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MCMultiBottomView.this.onClickAction(5);
                return false;
            }
        });
        this.replyEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                MCMultiBottomView.this.resetEdit();
            }
        });
        this.recordBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    MCMultiBottomView.this.recordBtn.setBackgroundResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_new_chat_input_bar_button1_h"));
                    MCMultiBottomView.this.recordBtn.setText(MCMultiBottomView.this.resource.getString("mc_forum_release_end"));
                    MCMultiBottomView.this.recordBox.setVisibility(View.VISIBLE);
                    MCMultiBottomView.this.recording();
                } else if (event.getAction() == 1 || event.getAction() == 3) {
                    MCMultiBottomView.this.recordBtn.setBackgroundResource(MCMultiBottomView.this.resource.getDrawableId("mc_forum_new_chat_input_bar_button1_n"));
                    MCMultiBottomView.this.recordBtn.setText(MCMultiBottomView.this.resource.getString("mc_forum_press_record"));
                    MCMultiBottomView.this.recordBox.setVisibility(View.GONE);
                    MCMultiBottomView.this.recorded();
                }
                return true;
            }
        });
        this.sendReplyBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!LoginHelper.doInterceptor(MCMultiBottomView.this.context, null, null)) {
                    return;
                }
                if (MCMultiBottomView.this.sendPerm) {
                    SendModel sendModel = new SendModel();
                    String content = MCMultiBottomView.this.replyEdit.getText().toString();
                    if (!MCStringUtil.isEmpty(content)) {
                        WordModel wordModel = new WordModel();
                        wordModel.setContent(content);
                        Map<String, UserInfoModel> tempMap = new LinkedHashMap();
                        for (String key : MCMultiBottomView.this.atFriendMap.keySet()) {
                            tempMap.put(key, MCMultiBottomView.this.atFriendMap.get(key));
                        }
                        wordModel.setAtFriendMap(tempMap);
                        sendModel.setWordModel(wordModel);
                    }
                    if (!MCMultiBottomView.this.picMap.isEmpty()) {
                        PicModel picModel = new PicModel();
                        Map<String, PictureModel> tempMap2 = new LinkedHashMap();
                        for (String key2 : MCMultiBottomView.this.picMap.keySet()) {
                            tempMap2.put(key2, MCMultiBottomView.this.picMap.get(key2));
                        }
                        picModel.setSelectMap(tempMap2);
                        sendModel.setPicModel(picModel);
                    }
                    if (!MCMultiBottomView.this.locationOff) {
                        PoiModel poiModel = new PoiModel();
                        poiModel.setOpen(!MCMultiBottomView.this.locationOff);
                        poiModel.setLocationStr(MCMultiBottomView.this.locationStr);
                        poiModel.setLongitude(MCMultiBottomView.this.longitude);
                        poiModel.setLatitude(MCMultiBottomView.this.latitude);
                        sendModel.setPoiModel(poiModel);
                    }
                    if (!MCStringUtil.isEmpty(MCMultiBottomView.this.recordAbsolutePath)) {
                        RecordModel recordModel = new RecordModel();
                        recordModel.setAudioPath(MCMultiBottomView.this.recordAbsolutePath);
                        sendModel.setRecordModel(recordModel);
                    }
                    if (sendModel.getWordModel() == null && sendModel.getPicModel() == null && sendModel.getRecordModel() == null) {
                        MCToastUtils.toastByResName(MCMultiBottomView.this.context, "mc_forum_publish_min_length_error");
                        return;
                    }
                    MCMultiBottomView.this.clearSend();
                    if (MCMultiBottomView.this.onSendDelegate != null) {
                        MCMultiBottomView.this.onSendDelegate.sendReply(1, sendModel);
                        return;
                    }
                    return;
                }
                MCToastUtils.toastByResName(MCMultiBottomView.this.context.getApplicationContext(), "mc_forum_send_permission");
            }
        });
    }

    private void recording() {
        this.recordUtil.startRecordMp3();
        this.recordShort = false;
        this.isRecording = true;
        this.startTime = System.currentTimeMillis();
        new TimeThread().start();
        new MonitorThread().start();
    }

    private void recorded() {
        this.recordShort = false;
        this.recordUtil.stopRecord();
        this.endTime = System.currentTimeMillis();
        if (((int) ((this.endTime - this.startTime) / 1000)) < 1) {
            this.recordShort = true;
            MCToastUtils.toastByResName(this.context.getApplicationContext(), "mc_forum_warn_recorder_min_len");
        }
    }

    private void onClickAction(int action) {
        boolean isShowMore = false;
        if (action == 1) {
            isShowMore = true;
        } else if (this.innerShow != null && this.innerShow.length != 0) {
            for (int actionId : this.innerShow) {
                if (action == actionId) {
                    isShowMore = true;
                    break;
                }
            }
        }
        if (isShowMore) {
            openMoreViewStub();
        }
        if (action == 2) {
            openFaceViewStub();
        }
        showCurrentViewInverse(action);
        showCurrentView(action);
    }

    private void openMoreViewStub() {
        ViewStub moreStub = (ViewStub) this.view.findViewById(this.resource.getViewId("multi_more_stub"));
        if (moreStub != null) {
            moreStub.setVisibility(View.VISIBLE);
            initMoreView();
        }
    }

    private void openFaceViewStub() {
        ViewStub faceStub = (ViewStub) this.view.findViewById(this.resource.getViewId("multi_face_stub"));
        if (faceStub != null) {
            faceStub.setVisibility(View.VISIBLE);
            initFaceView();
        }
    }

    private void showCurrentViewInverse(int action) {
        if (!(action == 1 || this.multiMoreBox == null || this.multiMoreBox.getVisibility() != View.VISIBLE)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    MCMultiBottomView.this.multiMoreBox.setVisibility(View.GONE);
                }
            });
        }
        if (!(action == 2 || this.multiFaceBox == null || this.multiFaceBox.getVisibility() != View.VISIBLE)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    MCMultiBottomView.this.multiFaceBox.setVisibility(View.GONE);
                }
            });
        }
        if (action != 3 && this.recordBtn.getVisibility() == View.VISIBLE) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    MCMultiBottomView.this.recordBtn.setVisibility(View.GONE);
                    MCMultiBottomView.this.replyEdit.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void resetEdit() {
        if (this.replyEdit.getText().toString().length() > 0) {
            this.sendReplyBtn.setVisibility(View.VISIBLE);
            this.takeBtn3.setVisibility(View.GONE);
            return;
        }
        this.sendReplyBtn.setVisibility(INVISIBLE);
        this.takeBtn3.setVisibility(View.VISIBLE);
    }

    private void showCurrentView(int action) {
        initTakeBtnView();
        if (action == 5) {
            if (this.recordBtn.getVisibility() == View.VISIBLE) {
                this.recordBtn.setVisibility(View.GONE);
            }
            this.replyEdit.requestFocus();
            showSoftKeyboard();
            resetEdit();
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(240);
                return;
            }
            return;
        }
        hideSoftKeyboard();
        if (action == 1) {
            if (LoginHelper.doInterceptor(this.context, null, null)) {
                if (this.multiMoreBox == null) {
                    openMoreViewStub();
                }
                if (this.multiMoreBox.getVisibility() == View.VISIBLE) {
                    this.multiMoreBox.setVisibility(View.GONE);
                } else {
                    this.multiMoreBox.setVisibility(View.VISIBLE);
                    if (this.showLocation == 1) {
                        this.locationOff = true;
                    } else {
                        this.locationOff = false;
                    }
                    showCurrentView(4);
                }
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(220);
            }
        }
        if (action == 2) {
            if (LoginHelper.doInterceptor(this.context, null, null)) {
                if (this.multiFaceBox == null) {
                    openFaceViewStub();
                }
                if (this.multiMoreBox != null && this.multiMoreBox.getVisibility() == VISIBLE) {
                    this.multiMoreBox.setVisibility(View.GONE);
                }
                if (this.multiFaceBox.getVisibility() == VISIBLE) {
                    this.multiFaceBox.setVisibility(View.GONE);
                } else {
                    this.multiFaceBox.setVisibility(View.VISIBLE);
                }
                resetEdit();
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(220);
            }
        }
        if (action == 3) {
            if (LoginHelper.doInterceptor(this.context, null, null)) {
                if (this.multiMoreBox != null && this.multiMoreBox.getVisibility() == VISIBLE) {
                    this.multiMoreBox.setVisibility(View.GONE);
                }
                if (!this.recordPerm) {
                    MCToastUtils.toastByResName(this.context.getApplicationContext(), "mc_forum_record_permission");
                } else if (this.recordBtn.getVisibility() == View.GONE) {
                    this.recordBtn.setVisibility(View.VISIBLE);
                    this.replyEdit.setVisibility(View.GONE);
                    this.sendReplyBtn.setVisibility(INVISIBLE);
                }
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(0);
            }
        }
        if (action == 4) {
            if (LoginHelper.doInterceptor(this.context, null, null)) {
                if (this.multiMoreBox == null) {
                    openMoreViewStub();
                }
                if (this.multiMoreBox != null && this.multiMoreBox.getVisibility() == GONE) {
                    this.multiMoreBox.setVisibility(View.VISIBLE);
                }
                if (this.locationOff) {
                    this.locationOff = false;
                    this.locationBox.setVisibility(View.VISIBLE);
                    showLoading(this.locationProgress);
                    this.locationText.setText(this.resource.getString("mc_forum_obtain_location_info_warn"));

                    this.poiUtil.requestPoi(new PoiDelegate() {
                        public void onReceivePoi(List<String> poi) {
                            MCMultiBottomView.this.poiList.clear();
                            if (!MCListUtils.isEmpty((List) poi)) {
                                MCMultiBottomView.this.poiList.addAll(poi);
                            }
                        }
                    });
                } else {
                    this.locationOff = true;
                    this.locationBox.setVisibility(View.GONE);
                    hideLoading(this.locationProgress);
                    this.longitude = 0.0d;
                    this.latitude = 0.0d;
                    this.locationStr = null;
                }
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(240);
            }
        }
        if (action == 8) {
            if (this.onShareDelegate != null) {
                this.onShareDelegate.share(4);
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(240);
            }
        }
        if (action == 6) {
            if (LoginHelper.doInterceptor(this.context, null, null)) {
                if (this.atPerm) {
                    Intent intent = new Intent(this.context.getApplicationContext(), MentionFriendFragmentActivity.class);
                    intent.putExtra(FinalConstant.POSTS_USER_LIST, (ArrayList) this.atUserList);
                    this.context.startActivity(intent);
                } else {
                    MCToastUtils.toastByResName(this.context.getApplicationContext(), "mc_forum_at_permission");
                }
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(240);
            }
        }
        if (action == 7) {
            if (LoginHelper.doInterceptor(this.context, null, null)) {
                if (this.picPerm) {
                    this.photoManageHelper.openPhotoSelector((Activity) this.context, 2, 1, this.picMap);
                } else {
                    MCToastUtils.toastByResName(this.context.getApplicationContext(), "mc_forum_pic_permission");
                }
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(240);
            }
        }
        if (action == 10) {
            if (LoginHelper.doInterceptor(this.context, null, null)) {
                if (this.picPerm) {
                    this.photoManageHelper.openPhotoGraph((Activity) this.context, 2, 1, this.picMap);
                } else {
                    MCToastUtils.toastByResName(this.context.getApplicationContext(), "mc_forum_pic_permission");
                }
            }
            if (this.heightDelegate != null) {
                this.heightDelegate.resetHeight(240);
            }
        }
        if (action == 9 && this.heightDelegate != null) {
            this.heightDelegate.resetHeight(240);
        }
    }

    private void initTakeBtnView() {
        setOuterLeftShow(this.outerLeftShow);
        setOuterRightShow(this.outerRightShow);
    }

    public void setModifyPicUI(boolean modifyPicUI) {
        this.modifyPicUI = modifyPicUI;
        this.photoManageHelper.setCallBackRightNow(!modifyPicUI);
    }

    public void setModifyRecordUI(boolean modifyRecordUI) {
        this.modifyRecordUI = modifyRecordUI;
    }

    public void getAtFriendListFromContent() {
        String content = this.replyEdit.getText().toString();
        if (content.contains("@")) {
            String[] s = content.split("@");
            for (int j = 0; j < s.length; j++) {
                int endIndex = s[j].indexOf(this.spaceChar);
                String nickname = "";
                if (endIndex > -1) {
                    nickname = s[j].substring(0, endIndex);
                    if (!this.atFriendMap.containsKey(nickname)) {
                        UserInfoModel user = new UserInfoModel();
                        user.setNickname(nickname);
                        this.atFriendMap.put(nickname, user);
                    }
                }
            }
        }
    }

    public void showSoftKeyboard() {
        if (((Activity) this.context).getCurrentFocus() != null) {
            ((InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(((Activity) this.context).getCurrentFocus(), 1);
        }
        this.mScreenHeight = ((WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        MCLogUtil.e("MCMultiBottomView", "mScreenHeight=" + this.mScreenHeight);
    }

    public void hideSoftKeyboard() {
        if (((Activity) this.context).getCurrentFocus() != null) {
            ((InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) this.context).getCurrentFocus().getWindowToken(), 2);
        }
    }

    public void setOuterRightShow(int[] outerRightShow) {
        this.outerRightShow = outerRightShow;
        if (outerRightShow != null && outerRightShow.length != 0) {
            switch (outerRightShow.length) {
                case 0:
                    this.sendReplyBtn.setVisibility(View.VISIBLE);
                    this.takeBtn3.setVisibility(View.GONE);
                    return;
                case 1:
                    if (11 == outerRightShow[0]) {
                        this.sendReplyBtn.setVisibility(View.VISIBLE);
                        this.takeBtn3.setVisibility(View.GONE);
                    } else {
                        this.sendReplyBtn.setVisibility(INVISIBLE);
                        this.takeBtn3.setVisibility(View.VISIBLE);
                    }
                    if (isResetEditAction(outerRightShow[0])) {
                        this.takeBtn3.setTag(new int[]{outerRightShow[0], 5});
                    } else {
                        this.takeBtn3.setTag(new int[]{outerRightShow[0], outerRightShow[0]});
                    }
                    initOuterShow(this.takeBtn3);
                    return;
                default:
                    return;
            }
        }
    }

    public void setOuterLeftShow(int[] outerLeftShow) {
        this.outerLeftShow = outerLeftShow;
        if (outerLeftShow == null || outerLeftShow.length == 0) {
            this.takeBox.setVisibility(View.GONE);
            return;
        }
        switch (outerLeftShow.length) {
            case 0:
                this.takeBox.setVisibility(View.GONE);
                this.takeBtn1.setVisibility(View.GONE);
                this.takeBtn2.setVisibility(View.GONE);
                return;
            case 1:
                this.takeBox.setVisibility(View.VISIBLE);
                this.takeBtn1.setVisibility(View.VISIBLE);
                this.takeBtn2.setVisibility(View.GONE);
                if (isResetEditAction(outerLeftShow[0])) {
                    this.takeBtn1.setTag(new int[]{outerLeftShow[0], 5});
                } else {
                    this.takeBtn1.setTag(new int[]{outerLeftShow[0], outerLeftShow[0]});
                }
                initOuterShow(this.takeBtn1);
                return;
            case 2:
                this.takeBox.setVisibility(View.VISIBLE);
                this.takeBtn1.setVisibility(View.VISIBLE);
                this.takeBtn2.setVisibility(View.VISIBLE);
                if (isResetEditAction(outerLeftShow[0])) {
                    this.takeBtn1.setTag(new int[]{outerLeftShow[0], 5});
                } else {
                    this.takeBtn1.setTag(new int[]{outerLeftShow[0], outerLeftShow[0]});
                }
                if (isResetEditAction(outerLeftShow[1])) {
                    this.takeBtn2.setTag(new int[]{outerLeftShow[1], 5});
                } else {
                    this.takeBtn2.setTag(new int[]{outerLeftShow[1], outerLeftShow[1]});
                }
                initOuterShow(this.takeBtn1);
                initOuterShow(this.takeBtn2);
                return;
            default:
                return;
        }
    }

    private boolean isResetEditAction(int action) {
        switch (action) {
            case 1:
                return false;
            case 8:
                return false;
            default:
                return true;
        }
    }

    private void initOuterShow(Button button) {
        switch (((int[]) button.getTag())[0]) {
            case 1:
                button.setBackgroundResource(this.resource.getDrawableId("mc_forum_chat_icon2"));
                break;
            case 2:
                button.setBackgroundResource(this.resource.getDrawableId("mc_forum_chat_icon1"));
                break;
            case 3:
                button.setBackgroundResource(this.resource.getDrawableId("mc_forum_chat_icon9"));
                break;
            case 5:
                button.setBackgroundResource(this.resource.getDrawableId("mc_forum_chat_icon5"));
                break;
            case 8:
                button.setBackgroundResource(this.resource.getDrawableId("mc_forum_chat_icon8"));
                break;
        }
        initOuterShowAction(button);
    }

    private void initOuterShowAction(final Button button) {
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LoginHelper.doInterceptor(MCMultiBottomView.this.context, null, null)) {

                    int[] actions = (int[])button.getTag();
                    MCMultiBottomView.this.onClickAction(actions[0]);
                    button.setTag(new int[]{actions[1], actions[0]});
                    MCMultiBottomView.this.initOuterShow(button);
                }
            }
        });
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setInnerShow(int[] r6) {
        this.innerShow = r6;
        int r3 = 8;
        List<InnerShowModel> r2 = this.innerShowList;
        r2.clear();
        if(r6!=null&&r6.length!=0){
            for(int i = 0; i < r6.length; i++){
                InnerShowModel r1 = new InnerShowModel();
                switch (r6[i]){
                    case 2:
                        r1.setAction(PIC_TYPE);
                        r1.setTitle(this.resource.getString("mc_forum_posts_face"));
                        r1.setDrawableId("mc_forum_chat_bigicon5_n");
                        break;
                    case 3:
                        continue;
                    case 4:
                        Context appContext = this.context.getApplicationContext();
                        SettingSharePreference sharePreference = new SettingSharePreference(appContext);
                        if(!sharePreference.isLocationOpen(SharedPreferencesDB.getInstance(appContext).getUserId())){
                            continue;
                        }else{
                            r1.setAction(POI_ACTION);
                            r1.setTitle(this.resource.getString("mc_forum_posts_location"));
                            r1.setDrawableId("mc_forum_chat_bigicon3_n");
                        }
                        break;
                    case 5:
                        break;
                    case 6:
                        r1.setAction(6);
                        r1.setTitle(this.resource.getString("mc_forum_friend"));
                        r1.setDrawableId("mc_forum_chat_bigicon2_n");
                        break;
                    case 7:
                        r1.setAction(7);
                        r1.setTitle(this.resource.getString("mc_forum_pic_topic_list"));
                        r1.setDrawableId("mc_forum_chat_bigicon4_n");
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                    case 10:
                        r1.setAction(10);
                        r1.setTitle(this.resource.getString("mc_forum_camera"));
                        r1.setDrawableId("mc_forum_chat_bigicon4_n");
                        break;
                    default:
                        break;
                }
                r2.add(r1);
            }
        }else{
            if(this.multiFaceBox!=null){
                this.multiFaceBox.setVisibility(View.GONE);
            }
            if(this.multiMoreBox!=null){
                this.multiMoreBox.setVisibility(View.GONE);
            }

        }

        if(this.moreGridViewAdapter==null){//L_0x00c1
            this.moreGridViewAdapter = new MoreGridViewAdapter(this.innerShowList);

        }else{//L_0x00dd
            this.moreGridViewAdapter.setInnerShowList(this.innerShowList);
            this.moreGridViewAdapter.notifyDataSetChanged();

        }
        if(this.moreGidView!=null){
            this.moreGidView.setAdapter(this.moreGridViewAdapter);
        }
        this.initInnerShowAction();
        return;
        /*
        r5 = this;
        r3 = 8;
        r5.innerShow = r6;
        r2 = r5.innerShowList;
        r2.clear();
        if (r6 == 0) goto L_0x00af;
    L_0x000b:
        r2 = r6.length;
        if (r2 == 0) goto L_0x00af;
    L_0x000e:
        r0 = 0;
    L_0x000f:
        r2 = r6.length;
        if (r0 >= r2) goto L_0x00c1;
    L_0x0012:
        r1 = new com.mobcent.discuz.activity.view.MCMultiBottomView$InnerShowModel;
        r2 = 0;
        r1.<init>();
        r2 = r6[r0];
        switch(r2) {
            case 2: goto L_0x0025;
            case 3: goto L_0x0022;
            case 4: goto L_0x003a;
            case 5: goto L_0x001d;
            case 6: goto L_0x006e;
            case 7: goto L_0x0083;
            case 8: goto L_0x001d;
            case 9: goto L_0x001d;
            case 10: goto L_0x0098;
            default: goto L_0x001d;
        };
    L_0x001d:
        r2 = r5.innerShowList;
        r2.add(r1);
    L_0x0022:
        r0 = r0 + 1;
        goto L_0x000f;
    L_0x0025:
        r2 = 2;
        r1.setAction(r2);
        r2 = r5.resource;
        r3 = "mc_forum_posts_face";
        r2 = r2.getString(r3);
        r1.setTitle(r2);
        r2 = "mc_forum_chat_bigicon5_n";
        r1.setDrawableId(r2);
        goto L_0x001d;
    L_0x003a:
        r2 = new com.mobcent.discuz.android.db.SettingSharePreference;
        r3 = r5.context;
        r3 = r3.getApplicationContext();
        r2.<init>(r3);
        r3 = r5.context;
        r3 = r3.getApplicationContext();
        r3 = com.mobcent.discuz.android.db.SharedPreferencesDB.getInstance(r3);
        r3 = r3.getUserId();
        r2 = r2.isLocationOpen(r3);
        if (r2 == 0) goto L_0x0022;
    L_0x0059:
        r2 = 4;
        r1.setAction(r2);
        r2 = r5.resource;
        r3 = "mc_forum_posts_location";
        r2 = r2.getString(r3);
        r1.setTitle(r2);
        r2 = "mc_forum_chat_bigicon3_n";
        r1.setDrawableId(r2);
        goto L_0x001d;
    L_0x006e:
        r2 = 6;
        r1.setAction(r2);
        r2 = r5.resource;
        r3 = "mc_forum_friend";
        r2 = r2.getString(r3);
        r1.setTitle(r2);
        r2 = "mc_forum_chat_bigicon2_n";
        r1.setDrawableId(r2);
        goto L_0x001d;
    L_0x0083:
        r2 = 7;
        r1.setAction(r2);
        r2 = r5.resource;
        r3 = "mc_forum_pic_topic_list";
        r2 = r2.getString(r3);
        r1.setTitle(r2);
        r2 = "mc_forum_chat_bigicon4_n";
        r1.setDrawableId(r2);
        goto L_0x001d;
    L_0x0098:
        r2 = 10;
        r1.setAction(r2);
        r2 = r5.resource;
        r3 = "mc_forum_camera";
        r2 = r2.getString(r3);
        r1.setTitle(r2);
        r2 = "mc_forum_chat_bigicon6_n";
        r1.setDrawableId(r2);
        goto L_0x001d;
    L_0x00af:
        r2 = r5.multiFaceBox;
        if (r2 == 0) goto L_0x00b8;
    L_0x00b3:
        r2 = r5.multiFaceBox;
        r2.setVisibility(r3);
    L_0x00b8:
        r2 = r5.multiMoreBox;
        if (r2 == 0) goto L_0x00c1;
    L_0x00bc:
        r2 = r5.multiMoreBox;
        r2.setVisibility(r3);
    L_0x00c1:
        r2 = r5.moreGridViewAdapter;
        if (r2 != 0) goto L_0x00dd;
    L_0x00c5:
        r2 = new com.mobcent.discuz.activity.view.MCMultiBottomView$MoreGridViewAdapter;
        r3 = r5.innerShowList;
        r2.<init>(r3);
        r5.moreGridViewAdapter = r2;
    L_0x00ce:
        r2 = r5.moreGidView;
        if (r2 == 0) goto L_0x00d9;
    L_0x00d2:
        r2 = r5.moreGidView;
        r3 = r5.moreGridViewAdapter;
        r2.setAdapter(r3);
    L_0x00d9:
        r5.initInnerShowAction();
        return;
    L_0x00dd:
        r2 = r5.moreGridViewAdapter;
        r3 = r5.innerShowList;
        r2.setInnerShowList(r3);
        r2 = r5.moreGridViewAdapter;
        r2.notifyDataSetChanged();
        goto L_0x00ce;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.activity.view.MCMultiBottomView.setInnerShow(int[]):void");
    }

    private void initInnerShowAction() {
        if (this.moreGidView != null) {
            this.moreGidView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    MCMultiBottomView.this.showCurrentView(((InnerShowModel) MCMultiBottomView.this.innerShowList.get(position)).getAction());
                }
            });
        }
    }

    public void onDestroy() {
        MentionFriendReturnDelegate.getInstance().setOnLoginChannelListener(null);
        PoiItemDelegate.getInstance().setClickPoiItemLisenter(null);
    }

    public boolean onKeyDown(boolean isAlwaysVisiable) {
        if (this.multiMoreBox != null && this.multiMoreBox.getVisibility() == VISIBLE) {
            onClickAction(9);
            return false;
        } else if (this.multiFaceBox != null && this.multiFaceBox.getVisibility() == VISIBLE) {
            onClickAction(9);
            return false;
        } else if (isAlwaysVisiable || this.multiBottomBox == null || this.multiBottomBox.getVisibility() != VISIBLE) {
            return true;
        } else {
            this.multiBottomBox.setVisibility(View.GONE);
            return false;
        }
    }

    public void setOnSendDelegate(OnSendDelegate onSendDelegate) {
        this.onSendDelegate = onSendDelegate;
    }

    public void setOnPicDelegate(OnPicDelegate onPicDelegate) {
        this.onPicDelegate = onPicDelegate;
    }

    public void setOnRecordDelegate(OnRecordDelegate onRecordDelegate) {
        this.onRecordDelegate = onRecordDelegate;
    }

    public void setOnShareDelegate(OnShareDelegate onShareDelegate) {
        this.onShareDelegate = onShareDelegate;
    }

    private void clearWord() {
        this.replyEdit.setText("");
        this.atFriendMap.clear();
        clearPoi();
    }

    private void clearRecord() {
        this.recordAbsolutePath = null;
    }

    private void clearPic() {
        this.photoManageHelper.onDestroy();
        this.imageBox.removeAllViews();
        this.imageScrollView.setVisibility(View.GONE);
    }

    private void clearPoi() {
        this.locationOff = true;
        this.locationStr = null;
        this.latitude = 0.0d;
        this.longitude = 0.0d;
        if (this.locationBox != null && this.locationBox.getVisibility() == VISIBLE) {
            this.locationBox.setVisibility(View.GONE);
        }
    }

    private void clearSend() {
        clearWord();
        clearRecord();
        clearPic();
    }

    public void requestEditFocus() {
        this.multiBottomBox.setVisibility(View.VISIBLE);
        onClickAction(5);
    }

    public void hidePanel() {
        this.mHandler.post(new Runnable() {
            public void run() {
                MCMultiBottomView.this.onClickAction(9);
            }
        });
    }

    public void setHeightDelegate(HeightDelegate heightDelegate) {
        this.heightDelegate = heightDelegate;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.photoManageHelper.onActivityResult((Activity) this.context, requestCode, resultCode, data);
    }

    public ViewPager getFacePager() {
        return this.facePager;
    }
}

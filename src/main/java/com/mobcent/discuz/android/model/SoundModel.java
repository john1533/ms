package com.mobcent.discuz.android.model;

public class SoundModel extends BaseModel {
    public static final int SOUND_DOWNING = 8;
    public static final int SOUND_FAIL = 5;
    public static final int SOUND_FINISH = 4;
    public static final int SOUND_INIT = 1;
    public static final int SOUND_NONE = 0;
    public static final int SOUND_OS_NO_PAUSE = 7;
    public static final int SOUND_OS_PAUSE = 6;
    public static final int SOUND_PAUSE = 3;
    public static final int SOUND_PLAY = 2;
    private static final long serialVersionUID = 1;
    private int currentPosition;
    private int palyStatus = 0;
    private int playProgress;
    private String soundPath;
    private long soundTime;
    private int type;

    public long getSoundTime() {
        return this.soundTime;
    }

    public void setSoundTime(long soundTime) {
        this.soundTime = soundTime;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getSoundPath() {
        return this.soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public int getPalyStatus() {
        return this.palyStatus;
    }

    public void setPalyStatus(int palyStatus) {
        this.palyStatus = palyStatus;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPlayProgress() {
        return this.playProgress;
    }

    public void setPlayProgress(int playProgress) {
        this.playProgress = playProgress;
    }

    public String toString() {
        return "SoundModel [palyStatus=" + this.palyStatus + ", soundTime=" + this.soundTime + ", currentPosition=" + this.currentPosition + ", soundPath=" + this.soundPath + "]";
    }
}

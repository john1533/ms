package com.org.fourtween.utils.download;

/**
 * Created by John on 2016/5/5.
 */
public class HttpControl {
    private String url;
    private String parm;
    private byte method;//0GET,1POST
    private byte dealType;//0不保存，1保存
    private String data;//post data
    private String savePath;




    public HttpControl(String url, String parm, byte method, byte dealType){
        this.url = url;
        this.parm = parm;
        this.method = method;
        this.dealType = dealType;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParm() {
        return parm;
    }

    public void setParm(String parm) {
        this.parm = parm;
    }

    public byte getMethod() {
        return method;
    }

    public void setMethod(byte method) {
        this.method = method;
    }

    public byte getDealType() {
        return dealType;
    }

    public void setDealType(byte dealType) {

        this.dealType = dealType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFullUrlPath(){
        StringBuffer ret = new StringBuffer(url);
        ret.append("?").append(parm);
        return ret.toString();
    }
}

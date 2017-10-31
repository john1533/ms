package com.mobcent.discuz.android.service.impl.helper;

import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.PayStateConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PayStateModel;
import org.json.JSONObject;

public class PayStateServiceHelper implements PayStateConstant, BaseApiConstant {
    public static BaseResultModel<PayStateModel> controll(String jsonStr) {
        boolean z = true;
        BaseResultModel<PayStateModel> result = new BaseResultModel();
        PayStateModel payStateModel = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                result.setRs(jsonObj.optInt("rs"));
                if (result.getRs() == 1) {
                    PayStateModel payStateModel2 = new PayStateModel();
                    try {
                        payStateModel2.setImgUrl(jsonObj.optString("img_url"));
                        JSONObject dataObj = jsonObj.optJSONObject("data");
                        if (dataObj != null) {
                            payStateModel2.setLoadingPageImage(dataObj.optString("loadingpage_image"));
                            payStateModel2.setWaterMarkImage(dataObj.optString("watermark_image"));
                            payStateModel2.setWeiXinAppKey(dataObj.optString("weixin_appkey"));
                            payStateModel2.setWeixinAppSecret(dataObj.optString("weixin_appsecret"));
                            JSONObject payStateObj = dataObj.optJSONObject(PayStateConstant.PAYSTATE);
                            if (payStateObj != null) {
                                boolean z2;
                                if (payStateObj.optInt("adv") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setAdv(z2);
                                if (payStateObj.optInt("client_manager") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setClientManager(z2);
                                if (payStateObj.optInt("loadingpage") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setLoadingPage(z2);
                                if (payStateObj.optInt("msg_push") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setMsgPush(z2);
                                if (payStateObj.optInt("powerby") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setPowerBy(z2);
                                if (payStateObj.optInt("share_key") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setShareKey(z2);
                                if (payStateObj.optInt("square") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setSquare(z2);
                                if (payStateObj.optInt("watermark") == 1) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                payStateModel2.setWaterMark(z2);
                                if (payStateObj.optInt(PayStateConstant.USER_DEFINED) != 1) {
                                    z = false;
                                }
                                payStateModel2.setUserDefined(z);
                            }
                        }
                        payStateModel = payStateModel2;
                    } catch (Exception e) {
                        payStateModel = payStateModel2;
                        result.setRs(0);
                        return result;
                    }
                }
            }
            result.setData(payStateModel);
        } catch (Exception e2) {
            result.setRs(0);
            return result;
        }
        return result;
    }
}

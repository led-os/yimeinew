package com.handong.framework.account;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

public class AccountHelper {

    private static final String SP_NAME = "AccountHelper";
    private static final String SP_TOKEN = "token";
    private static final String SP_NICKNAME = "nickname";
    private static final String SP_AVATAR = "avatar";
    private static final String SP_GENDER = "gender";
    private static final String SP_USERID = "userid";
    private static final String SP_MOBILE = "SP_MOBILE";
    private static final String SP_SUPPLIERID = "supplierid";//店铺id
    private static final String SP_CITY = "city";//店铺id
    //用户身份 0未登录 1普通用户 2医生 3咨询师 4医院
    private static final String SP_IDENTITY = "identity";
    //医院->座机电话
    private static final String SP_TELEPHONE = "telephone";
    //4中身份用户的 城市id 城市name 经纬度
    private static final String SP_CITY_ID = "city_id";
    //三方登录的信息
    private static final String SP_OPEN_ID = "open_id";
    private static final String SP_ICONURL = "iconurl";
    private static final String SP_UID = "uid";

    private static final String SP_CITY_NAME = "city_name";
    private static final String SP_LATITUDE = "latitude";
    private static final String SP_LONGITUDE = "longitude";

    private static final String SP_YUNXIN_ACCID = "yunxin_accid";
    private static final String SP_YUNXIN_TOKEN = "yunxin_token";

    private static final String SP_ONLINE_STATUS = "SP_ONLINE_STATUS";

    private static final String SP_AUTH_STATUS = "sp_auth_status";

    private static final AccountHelper INSTANCE = new AccountHelper();

    public static AccountHelper getInstance() {
        return INSTANCE;
    }

    private AccountHelper() {

    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }

    public static void login(String token, String userId, String mobile, String gender, String avatar, String nickname, int olstatus) {
        SPUtils.getInstance(SP_NAME).put(SP_TOKEN, token);
        SPUtils.getInstance(SP_NAME).put(SP_USERID, userId);
        SPUtils.getInstance(SP_NAME).put(SP_MOBILE, mobile);
        SPUtils.getInstance(SP_NAME).put(SP_GENDER, gender);
        SPUtils.getInstance(SP_NAME).put(SP_AVATAR, avatar);
        SPUtils.getInstance(SP_NAME).put(SP_NICKNAME, nickname);
        SPUtils.getInstance(SP_NAME).put(SP_ONLINE_STATUS, olstatus);
    }

    public static void saveOther(String open_id, String iconurl, String uid) {
        SPUtils.getInstance(SP_NAME).put(SP_OPEN_ID, open_id);
        SPUtils.getInstance(SP_NAME).put(SP_ICONURL, iconurl);
        SPUtils.getInstance(SP_NAME).put(SP_UID, uid);
    }

    public static void setCity(String city_id, String city_name, String latitude, String longitude) {
        SPUtils.getInstance(SP_NAME).put(SP_CITY_ID, city_id);
        SPUtils.getInstance(SP_NAME).put(SP_CITY_NAME, city_name);
        SPUtils.getInstance(SP_NAME).put(SP_LATITUDE, latitude);
        SPUtils.getInstance(SP_NAME).put(SP_LONGITUDE, longitude);
    }

    public static void setCity(String latitude, String longitude) {
        SPUtils.getInstance(SP_NAME).put(SP_LATITUDE, latitude);
        SPUtils.getInstance(SP_NAME).put(SP_LONGITUDE, longitude);
    }

    public static String getToken() {
        return SPUtils.getInstance(SP_NAME).getString(SP_TOKEN);
    }

    public static String getCity_Id() {
        return SPUtils.getInstance(SP_NAME).getString(SP_CITY_ID);
    }

    public static String getCity_Name() {
        return SPUtils.getInstance(SP_NAME).getString(SP_CITY_NAME);
    }

    public static String getUserId() {
        return SPUtils.getInstance(SP_NAME).getString(SP_USERID);
    }

    public static String getGender() {
        return SPUtils.getInstance(SP_NAME).getString(SP_GENDER);
    }

    public static String getAvatar() {
        return SPUtils.getInstance(SP_NAME).getString(SP_AVATAR);
    }

    public static String getTelephone() {
        return SPUtils.getInstance(SP_NAME).getString(SP_TELEPHONE);
    }

    public static String getOpenId() {
        return SPUtils.getInstance(SP_NAME).getString(SP_OPEN_ID);
    }

    public static String getIconurl() {
        return SPUtils.getInstance(SP_NAME).getString(SP_ICONURL);
    }

    public static String getUid() {
        return SPUtils.getInstance(SP_NAME).getString(SP_UID);
    }

    public static void setTelephone(String telephone) {
        SPUtils.getInstance(SP_NAME).put(SP_TELEPHONE, telephone);
    }

    public static void setAvatar(String avatar) {
        SPUtils.getInstance(SP_NAME).put(SP_AVATAR, avatar);
    }

    public static String getNickname() {
        return SPUtils.getInstance(SP_NAME).getString(SP_NICKNAME);
    }

    public static void setNickname(String nickname) {
        SPUtils.getInstance(SP_NAME).put(SP_NICKNAME, nickname);
    }

    public static void setGender(String nickname) {
        SPUtils.getInstance(SP_NAME).put(SP_GENDER, nickname);
    }

    public static String getMobile() {
        return SPUtils.getInstance(SP_NAME).getString(SP_MOBILE);
    }

    public static void setMobile(String mobile) {
        SPUtils.getInstance(SP_NAME).put(SP_MOBILE, mobile);
    }

    public static void setSupplierId(String supplierId) {
        SPUtils.getInstance(SP_NAME).put(SP_SUPPLIERID, supplierId);
    }

    public static String getSupplierId() {
        return SPUtils.getInstance(SP_NAME).getString(SP_SUPPLIERID);
    }

    public static void setCityName(String cityName) {
        SPUtils.getInstance(SP_NAME).put(SP_CITY, cityName);
    }

    public static String getCityName() {
        return SPUtils.getInstance(SP_NAME).getString(SP_CITY);
    }

    public static void setOLStatus(int status) {
        SPUtils.getInstance(SP_NAME).put(SP_ONLINE_STATUS, status);
    }

    public static int getOLStatus() {
        return SPUtils.getInstance(SP_NAME).getInt(SP_ONLINE_STATUS, 0);
    }

    public static void setAuthStatus(String authStatus) {
        SPUtils.getInstance(SP_NAME).put(SP_AUTH_STATUS, authStatus);
    }

    public static String getAuthStatus() {
        return SPUtils.getInstance(SP_NAME).getString(SP_AUTH_STATUS);
    }

    public static void logout() {

        if (getInstance().onLogoutListener != null) {
            getInstance().onLogoutListener.onPreLogout();
        }

        login(null, null, null, null, null, null, 0);
        setSupplierId(null);
        setCityName(null);
        setCity(null, null, null, null);
        setIdentity(0);
        setOLStatus(0);
        setYunxinAccid("");
        setYunxinToken("");
        setAuthStatus("");
        if (getInstance().onLogoutListener != null) {
            getInstance().onLogoutListener.onLogout();
        }
    }

    public static boolean shouldLogin(Context context) {
        if (!isLogin()) {
            context.startActivity(new Intent(context.getPackageName() + ".com.action.login"));
            return true;
        }

        if (AccountHelper.getIdentity() >= 2 &&
                !TextUtils.equals(AccountHelper.getAuthStatus(), "1")) {
            ToastUtils.showShort("您尚未通过审核，无法操作！");
            return true;
        }

        return false;
    }

    public static boolean shouldLogin(Context context, boolean needCheck) {
        if (!isLogin()) {
            context.startActivity(new Intent(context.getPackageName() + ".com.action.login"));
            return true;
        }
        if (needCheck) {

            if (AccountHelper.getIdentity() >= 2 &&
                    !TextUtils.equals(AccountHelper.getAuthStatus(), "1")) {
                ToastUtils.showShort("您尚未通过审核，无法操作！");
                return true;
            }

        }

        return false;
    }

    public static int getIdentity() {
        return SPUtils.getInstance(SP_NAME).getInt(SP_IDENTITY, 1);
    }

    public static void setIdentity(int identity) {
        SPUtils.getInstance(SP_NAME).put(SP_IDENTITY, identity);
    }

    public static void setYunxinAccid(String accid) {
        SPUtils.getInstance(SP_NAME).put(SP_YUNXIN_ACCID, accid);
    }

    public static String getYunxinAccid() {
        return SPUtils.getInstance(SP_NAME)
                .getString(SP_YUNXIN_ACCID);
    }

    public static void setYunxinToken(String yunxinToken) {
        SPUtils.getInstance(SP_NAME).put(SP_YUNXIN_TOKEN, yunxinToken);
    }

    public static String getYunxinToken() {
        return SPUtils.getInstance(SP_NAME)
                .getString(SP_YUNXIN_TOKEN);
    }

    private OnLogoutListener onLogoutListener;

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    public interface OnLogoutListener {
        void onPreLogout();

        void onLogout();
    }
}
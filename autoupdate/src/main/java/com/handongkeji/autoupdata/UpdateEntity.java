package com.handongkeji.autoupdata;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sangbo on 16-5-19.
 */
public class UpdateEntity {

    public int versionCode = 0;//版本号
    public String isForceUpdate = "0";//是否提示是最新版本
    public String versionName = "";
    public String downUrl = "";
    public String updateLog = "";

    public UpdateEntity(String json) throws JSONException {

        JSONObject jso = new JSONObject(json);
        JSONObject jsonObject = jso.getJSONObject("data");
        String code = jsonObject.getString("version_number");
        if (TextUtils.isEmpty(code) || "null".equals(code)) {
			this.versionCode = 0;
		}else{
			this.versionCode = Integer.valueOf(code);
		}
        this.versionName = jsonObject.getString("name");
        this.isForceUpdate = jsonObject.getString("is_force_update");
        this.downUrl = jsonObject.getString("download_url");
        this.updateLog = jsonObject.getString("content");

    }
}

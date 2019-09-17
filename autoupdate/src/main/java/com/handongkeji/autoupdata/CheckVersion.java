package com.handongkeji.autoupdata;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.handongkeji.autoupdate.R;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * app更新
 * Created by sangbo on 16-5-19.
 */
public class CheckVersion {

    private static final String TAG = "CheckVersion";
    private Context mContext;
    private boolean mIsEnforceCheck = false;
    public String checkUrl;
    private UpdateEntity mUpdateEntity;

    public static void update(Context context, String checkUrl) {
        update(context, checkUrl,false);
    }

    public static void update(Context context, String checkUrl, final boolean isEnforceCheck) {

        final CheckVersion checkVersion = new CheckVersion();
        checkVersion.checkUrl = checkUrl;
        checkVersion.mContext = context;
        checkVersion.mIsEnforceCheck = isEnforceCheck;

        if (TextUtils.isEmpty(checkVersion.checkUrl)) {
            Log.d(TAG, "url不能为空，请设置url");
            return;
        }

        HttpUtils utils = new HttpUtils();
        utils.asyncGet(checkVersion.checkUrl, new HttpUtils.Callback() {
            @Override
            public void onComplete(Response response) {
                Log.d(TAG, "onComplete: " + response.message);
                if (response.status == Response.SUCCESS) {
                    checkVersion.loadOnlineData(response.message);
                } else {
                    if (checkVersion.mIsEnforceCheck) {
                        Toast.makeText(checkVersion.mContext, "更新失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void loadOnlineData(String json) {

        try {
            UpdateEntity updateEntity = new UpdateEntity(json);
            if (updateEntity == null) {
                if (mIsEnforceCheck)
                    Toast.makeText(mContext, "网络信息获取失败，请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            mUpdateEntity = updateEntity;
            int mAppVersionCode = getVersionCode(mContext);

            if (mAppVersionCode < mUpdateEntity.versionCode && mUpdateEntity.isForceUpdate.equals("1")) {
                //启动更新
                AlertUpdate();
            } else {
                if (mIsEnforceCheck) {
                    Toast.makeText(mContext, "当前版本已经是最新版本", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
//            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.getMessage());
        }

    }


    private void AlertUpdate() {

        final Dialog dialog = new Dialog(mContext);
        Window window = dialog.getWindow();
        int[] screenSize = getScreenSize(mContext);
        window.setLayout((int) (screenSize[0] * 0.7), LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        dialog.setContentView(R.layout.umeng_update_dialog);
        TextView tv = (TextView) dialog.findViewById(R.id.umeng_update_content);
        tv.setText("新版本:" + mUpdateEntity.versionName + "\n"
                + "更新内容\r\n" + mUpdateEntity.updateLog
        );
        dialog.findViewById(R.id.umeng_update_id_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.umeng_update_id_ok).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                updateApp();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void updateApp() {
        updateApp(false);
    }

    private void updateApp(boolean isEnforceDown) {

        Intent intent = new Intent(mContext, UpdataService.class);
        try {
            URL url = new URL(mUpdateEntity.downUrl);
            intent.putExtra("downUrl", mUpdateEntity.downUrl);
            mContext.startService(intent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "updateApp: " + e.toString());
        }

    }

    private static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 获得apk版本号
     *
     * @param context
     * @return
     */
    private static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageInfo packInfo = getPackInfo(context);
        if (packInfo != null) {
            versionCode = packInfo.versionCode;
        }
        return versionCode;
    }

    /**
     * 获得apkinfo
     *
     * @param context
     * @return
     */
    private static PackageInfo getPackInfo(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    private static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int[] size = new int[2];
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

}

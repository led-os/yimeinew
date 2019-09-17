package com.chengzi.app.third.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chengzi.app.event.AlipayEvent;
import com.chengzi.app.third.pay.alipay.PayResult;
import com.chengzi.app.third.pay.alipay.SignUtils;
import com.hwangjr.rxbus.RxBus;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/9/18.
 */
public class Alipay {

    private static final String TAG = "Alipay";

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "";

    //    // 商户PID
    // 商户收款账号
    public static final String SELLER = "bjsxwj@163.com";
      public static final String RSA2_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Activity mActivity;
    private int getCount = 0;
    private String orderid;

    public Alipay(Activity activity) {
        mActivity = activity;
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 110:
                    // 支付成功要跳回我的账户
                    Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                    //显示充值成功的页面和需要的操作
                    RxBus.get().post(PayConstants.PAY_SUCCESS, "successZ");
                    break;
                case 111:
                    // 循环结束没成功也要跳回我的账户
                    Toast.makeText(mActivity, "服务器延迟，支付订单正在处理中", Toast.LENGTH_SHORT).show();
//                    RxBus.get().post(PayConstants.RxBusTag.LOGIN_STATUS,"");
                    break;
                case SDK_PAY_FLAG: {
//                    dialog.setMsg("正在完成支付...");
//                    dialog.show();

                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
//                        GetServerStatus();
                        //显示充值成功的页面和需要的操作
                        EventBus.getDefault().post(new AlipayEvent(1));
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new AlipayEvent(0));
                        }
                        //显示充值成功的页面和需要的操作

                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mActivity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    Toast.makeText(mActivity, "其他错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    private boolean check() {
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA2_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(mActivity).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    mActivity.finish();
                }
            }).show();
            return false;
        }
        return true;
    }

    /**
     * 支付宝 回调地址
     */
    public final String PAY_CALLBACK_URL = "https://tapi.incheng.com/paymentServer/tradeNotify";

    public void getOrderInfo(String trade_no, String price, String orderId, String commodityName, String commodityMessage) {

        if (!this.check()) {
            return;
        }

        orderid = orderId;
        //commodityName  commodityMessage   后期可以根据 是打赏还是充值来显示相应的字
        Map<String, String> biz_content = new HashMap<>();
        biz_content.put("subject", trade_no);
        biz_content.put("body", "commodityMessage");
        biz_content.put("out_trade_no", trade_no);
        biz_content.put("total_amount", price);
        biz_content.put("product_code", "QUICK_MSECURITY_PAY");
        biz_content.put("timeout_express", "30m");

        JSONObject jsonObject = new JSONObject(biz_content);
        String s = jsonObject.toString();
        Map<String, String> params = new HashMap<>();
        params.put("app_id", APPID);
        params.put("method", "alipay.trade.app.pay");
        params.put("charset", "utf-8");
        params.put("sign_type", "RSA2");
        params.put("timestamp", getTimestamp());
        params.put("version", "1.0");
        params.put("notify_url", PAY_CALLBACK_URL); //  // TODO: 2017/10/25 0025
        params.put("biz_content", s);

        String orderInfo = buildOrderParam(params);
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        String sign = getSign(params, RSA2_PRIVATE, rsa2);

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&" + sign;

        Log.i(TAG, "getOrderInfo: " + payInfo);

        Schedulers.newThread().createWorker().schedule(() -> pay(payInfo));
    }

    public void payFromServer(String payInfo){
        Schedulers.newThread().createWorker().schedule(() -> pay(payInfo));
    }

    private void pay(String payInfo) {

        PayTask payTask = new PayTask(mActivity);
        String version = payTask.getVersion();
        String result = payTask.pay(payInfo, true);
        Message msg = new Message();
        msg.what = SDK_PAY_FLAG;
        msg.obj = result;
        mHandler.sendMessage(msg);

    }

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    //
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }
}

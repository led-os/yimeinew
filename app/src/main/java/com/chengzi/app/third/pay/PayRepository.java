package com.chengzi.app.third.pay;

import android.app.Activity;
import android.content.Context;

/*import com.yueertang.app.data.entity.BaseEntity;
import com.yueertang.app.data.net.BaseObserver;
import com.yueertang.app.data.net.NetApi;
import com.yueertang.app.kit.CacheKit;
import com.yueertang.app.kit.ToastKit;*/


/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class PayRepository {
    /**
     * 支付方式(类别)：101 、微信 102、支付宝
     *
     * @param context
     * @param params
     * @param callback
     */
    public static final String wxPay = "1";

    public static final String aliPay = "2";

    public static final String ye = "103";

    public static void PayOrder(Context context, String orderpaytype,
                                String systemOrder, String price, String orderid, String ordername, String commodityMessage) {
        switch (orderpaytype) {
            case ye:
                //充值没有余额
//                yuePay(context, systemOrder, 0, "0");
                break;
            case wxPay:     //微信支付
//                WxPay wxPay = new WxPay((Activity) context, context);
//                if (TextUtils.isEmpty(price)) {
//                    price = "0";
//                }
//
//                SharedPreferences sp = context.getSharedPreferences("WXOrderId", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("orderid", orderid + "");
//                editor.commit();
//
//                double p = Double.valueOf(price);
//                WxPayViewModel model = ViewModelFactory.provide((FragmentActivity) context, WxPayViewModel.class);
//                model.placeOrder(((int) (p * 100)) + "", systemOrder);
//                model.placeOrderLiveData.observe((LifecycleOwner) context, wxEntityBaseBean -> {
//                    if (wxEntityBaseBean != null && wxEntityBaseBean.getData() != null) {
//                        wxEntity data = wxEntityBaseBean.getData();
//                        wxPay.pay(data);
//                    }
//                });
                break;
            case aliPay:

                Alipay alipay = new Alipay((Activity) context);
                alipay.getOrderInfo(systemOrder, price, String.valueOf(orderid), ordername, commodityMessage);
                break;
        }
    }
}
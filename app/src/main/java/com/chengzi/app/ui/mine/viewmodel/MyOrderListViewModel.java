package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.ui.mine.bean.GetEvaluationObjectBean;
import com.chengzi.app.ui.mine.bean.SpreadsOrderBean;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.ui.mine.model.ShopCartAndOrderService;
import com.chengzi.app.ui.pay.bean.PaymentBean;
import com.chengzi.app.ui.pay.model.PayService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 普通/拼购订单
 *
 * @ClassName:UserBeautyRaiseViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class MyOrderListViewModel extends BaseViewModel implements IRequest {

    //评论图
    public final ArrayList<String> items = new ArrayList<>();
    //投诉图
    public final ArrayList<String> complaintsitems = new ArrayList<>();

    //订单状态 0-全部 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消（非必须）
    public int status = -1;
    //机构订单状态 0-全部 1-待使用，2-待评价，3-已完成
    public int h_status = -1;
    //1-普通订单和秒杀订单 2-拼购订单 （必须）
    public ObservableField<Integer> type = new ObservableField();

    //获取普通用户的订单列表 订单详情  取消订单  获取评价对象
    public final MutableLiveData<List<UserOrderListBean>> userOrderListLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean<UserOrderListBean>> userOrderDetailsListLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> userOrderCancelListLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean<GetEvaluationObjectBean>> getEvaluationObjectLive = new MutableLiveData<>();
    //    订单评价 订单投诉(普通用户/医院)
    public final MutableLiveData<ResponseBean> orderEvaluationLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> complaintEventLiveData = new MutableLiveData<>();
    // Order - 补差价下单（冀）
    public final MutableLiveData<ResponseBean<SpreadsOrderBean>> spreadsOrderLiveData = new MutableLiveData<>();

    ///*医院*/ 确认使用
    public final MutableLiveData<ResponseBean> orderConfirmUseLive = new MutableLiveData<>();

    private final ShopCartAndOrderService mineService;

    public MyOrderListViewModel() {
        mineService = Api.getApiService(ShopCartAndOrderService.class);
    }


    //订单列表
    @Override
    public void onRequest(int currentPage, int pageSize) {
//        ToastUtils.showShort(type + " --" + status);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", AccountHelper.getToken());
        map.put("user_id", AccountHelper.getUserId());
        map.put("type", String.valueOf(type.get()));
        map.put("limit", String.valueOf(pageSize));
        map.put("page", String.valueOf(currentPage));
        if (status != -1) {     ///普通用户必传
            map.put("status", String.valueOf(status));
        }
        if (h_status != -1) {   //医院必传
            map.put("h_status", String.valueOf(h_status));
        }
        mineService.userOrderList(map)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<UserOrderListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<UserOrderListBean>> pageBeanResponseBean) {
                        userOrderListLive.postValue(pageBeanResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        userOrderListLive.postValue(new ArrayList<>());
                    }
                });
    }

    /**
     * 订单详情
     *
     * @param orderid 订单id
     */
    public void userOrderDetails(String orderid) {
        mineService.userOrderDetails(AccountHelper.getToken(), AccountHelper.getUserId(), orderid)
                .subscribe(new SimpleObserver<ResponseBean<UserOrderListBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserOrderListBean> listResponseBean) {
                        userOrderDetailsListLive.postValue(listResponseBean);
                    }
                });
    }

    /**
     * 取消订单
     *
     * @param orderid 订单id
     */
    public void userOrderCancel(String orderid) {
        mineService.userOrderCancel(AccountHelper.getToken(), AccountHelper.getUserId(), orderid)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean listResponseBean) {
                        userOrderCancelListLive.postValue(listResponseBean);
                    }
                });
    }

    /**
     * 获取评价对象
     *
     * @param orderid 订单id
     */
    public void getEvaluationObject(String orderid) {
        mineService.getEvaluationObject(AccountHelper.getToken(), AccountHelper.getUserId(), orderid)
                .subscribe(new SimpleObserver<ResponseBean<GetEvaluationObjectBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<GetEvaluationObjectBean> listResponseBean) {
                        getEvaluationObjectLive.postValue(listResponseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        getEvaluationObjectLive.postValue(null);
                    }
                });
    }

    /**
     * Order - 确认使用
     *
     * @param orderid 订单id
     *                type-->1普通订单,拼购,秒杀 2美人筹
     */
    public void orderConfirmUse(String orderid) {
        mineService.orderConfirmUse(AccountHelper.getToken(), AccountHelper.getUserId(), orderid, "1", "1")
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean listResponseBean) {
                        orderConfirmUseLive.postValue(listResponseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        ResponseBean listResponseBean = new ResponseBean<>();
                        listResponseBean.setStatus(code);
                        listResponseBean.setMessage(errorMsg);
                        orderConfirmUseLive.postValue(listResponseBean);
                    }
                });
    }


    /**
     * 普通用户-订单评价
     *
     * @param order_id           订单Id
     * @param doctor_skill       医生技术分 （订单指定了医生，用户给医生评价时必须）
     * @param doctor_major       医生专业分 （订单指定了医生，用户给医生评价时必须）
     * @param consultant_service 咨询师服务分 （订单指定了咨询师，用户给咨询师评价时必须）
     * @param consultant_major   咨询师专业分 （订单指定了咨询师，用户给咨询师评价时必须）
     * @param content            用户评价的内容 （非必须）
     * @param //image            用户评价的图片 （非必须，最多9张）
     * @param //user_grade       医院给用户的评分 （医院给用户评价时必须）
     * @param //c_content        医院给用户评价的内容 （非必须）
     */
    public void orderEvaluation(String order_id, String doctor_skill, String doctor_major, String consultant_service,
                                String consultant_major, String content) {//, String image, String user_grade, String c_content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", AccountHelper.getToken());
        map.put("user_id", AccountHelper.getUserId());
        map.put("order_id", order_id);
        if (!TextUtils.isEmpty(doctor_skill) && !doctor_skill.equals("0"))
            map.put("doctor_skill", doctor_skill);
        if (!TextUtils.isEmpty(doctor_major) && !doctor_major.equals("0"))
            map.put("doctor_major", doctor_major);
        if (!TextUtils.isEmpty(consultant_service) && !consultant_service.equals("0"))
            map.put("consultant_service", consultant_service);
        if (!TextUtils.isEmpty(consultant_major) && !consultant_major.equals("0"))
            map.put("consultant_major", consultant_major);
        if (!TextUtils.isEmpty(content))
            map.put("content", content);

        if (!items.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String item : items) {
                sb.append(item).append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length()-1);
            }
            map.put("image_arr",sb.toString());
        }

        mineService.orderEvaluation(map, items).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean stringResponseBean) {
                orderEvaluationLive.postValue(stringResponseBean);
            }
        });
    }

    /**
     * 医院-订单评价
     *
     * @param order_id   订单Id
     * @param user_grade 医院给用户的评分 （医院给用户评价时必须）
     * @param c_content  医院给用户评价的内容 （非必须）
     */
    public void orderEvaluation(String order_id, String user_grade, String c_content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", AccountHelper.getToken());
        map.put("user_id", AccountHelper.getUserId());
        map.put("order_id", order_id);
        if (!TextUtils.isEmpty(user_grade))
            map.put("user_grade", user_grade);
        if (!TextUtils.isEmpty(c_content))
            map.put("c_content", c_content);
        mineService.orderEvaluation(map).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean stringResponseBean) {
                orderEvaluationLive.postValue(stringResponseBean);
            }
        });
    }

    /**
     * 订单投诉(普通用户 医院)
     *
     * @param order_id     订单Id
     * @param content      投诉内容 （必传）
     * @param complaint_id 被投诉人id（必传）(多个以逗号隔开)
     */
    public void complaintEvent(String order_id, String content, String complaint_id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", AccountHelper.getToken());
        map.put("user_id", AccountHelper.getUserId());
        map.put("order_id", order_id);
        map.put("content", content);
        if (!TextUtils.isEmpty(complaint_id)) {
            complaint_id = complaint_id.substring(0, complaint_id.length() - 1);
        }
        map.put("complaint_id", complaint_id);

        if (!complaintsitems.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String item : complaintsitems) {
                sb.append(item).append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length()-1);
            }
            map.put("image_arr",sb.toString());
        }

        mineService.complaintEvent(map, complaintsitems).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean stringResponseBean) {
                complaintEventLiveData.postValue(stringResponseBean);
            }
        });
    }

    /**
     * Order - 补差价下单（冀）
     * * @param token
     * *  @param user_id
     *
     * @param order_id     订单id
     * @param payment_type 支付方式 1-微信 2-支付宝（
     * @param spreads      补差金额，单位元
     */
    public void spreadsOrder(String order_id, String payment_type, String spreads) {
        mineService.spreadsOrder(AccountHelper.getToken(), AccountHelper.getUserId(), order_id, payment_type, spreads)
                .subscribe(new SimpleObserver<ResponseBean<SpreadsOrderBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<SpreadsOrderBean> stringResponseBean) {
                        spreadsOrderLiveData.postValue(stringResponseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        spreadsOrderLiveData.postValue(null);
                    }
                });
    }

    public final MutableLiveData<PaymentBean> payLive = new MutableLiveData<>();

    public void payment(String payWay, String prepaylog_id) {   //  用此id 发起支付
        String paymentType = TextUtils.equals(payWay, Sys.wxPay) ? "wechat_app" : "ali_app";
        Api.getApiService(PayService.class).payment(prepaylog_id, paymentType)
                .subscribe(new SimpleObserver<ResponseBean<PaymentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PaymentBean> paymentBeanResponseBean) {
                        payLive.postValue(paymentBeanResponseBean.getData());
                    }
                });
    }

    public void payNotify(String prepaylog_id){
        Api.getApiService(PayService.class).payNotify(prepaylog_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }
}
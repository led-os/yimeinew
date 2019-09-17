package com.chengzi.app.ui.account.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Pair;

import com.chengzi.app.ui.account.bean.AuditInfoBean;
import com.chengzi.app.ui.account.bean.BankCityBean;
import com.chengzi.app.ui.account.bean.BankNameBean;
import com.chengzi.app.ui.account.bean.HospitalCertifiedBean;
import com.chengzi.app.ui.account.model.AccountService;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;

import java.util.HashMap;

/**
 * @Desc:
 * @ClassName:HospitalCertifiedViewModel
 * @PackageName:com.yimei.app.ui.account.viewmodel
 * @Create On 2019/4/30 0030
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class HospitalCertifiedViewModel2 extends BaseViewModel {

    public ObservableField<String> real_name = new ObservableField<>();         //  医院名称
    public ObservableField<String> hospital_opreation_name = new ObservableField<>();   //  医院运营人姓名

    public ObservableField<String> city_id = new ObservableField<>();           //  所在城市id
    public ObservableField<String> hosipital_type = new ObservableField<>();    //  医院所属类型

    // -----企业相关信息-----
    public ObservableField<String> companyName = new ObservableField<>();       //  企业工商注册名称
    public ObservableField<String> creditCode = new ObservableField<>();        //  企业统一社会信用代码
    public ObservableField<String> companyEmail = new ObservableField<>();      //  企业相关邮箱

    public Pair<String,String> registerAmount;                                 //  企业注册资金
    public Pair<String,String> staffSize;                                      //  员工规模
    public Pair<String,String> officeSpace;                                    //  办公面积

    public ObservableField<String> bussiness_aqtitude_img = new ObservableField<>();    //  医院营业执照
    public ObservableField<String> permission = new ObservableField<>();                //  医疗机构许可证
    // -----企业相关信息-----

    //------企业法人身份信息------
    public ObservableField<String> legalPersonName = new ObservableField<>();   //  法人姓名
    public ObservableField<String> legalPersonIdcardNumber = new ObservableField<>();   //  法人身份证号
    public String idcardStartDate;
    public String idcardEndDate;
    public ObservableField<String> card_front = new ObservableField<>();        //  身份证正面
    public ObservableField<String> card_reverse = new ObservableField<>();      //  身份证反面

    public ObservableField<String> cardUrl = new ObservableField<>("....");  //  手持身份证照片 去掉此参数
    //------企业法人身份信息------


    //-----企业银行账号------
    public ObservableField<String> bankCardNumber = new ObservableField<>();   //  开户账号
    public BankNameBean bankData;
    public BankCityBean province;
    public BankCityBean city;
    public ObservableField<String> bankBranchInfo = new ObservableField<>();   //  开户银行支行信息
    public ObservableField<String> reservedMobile = new ObservableField<>();   //  开户银行预留手机号

    public ObservableField<String> bankPermit = new ObservableField<>();        //  开户行许可证
    public ObservableField<String> bankCardFront = new ObservableField<>();     //  银行卡正面
    //-----企业银行账号------

    public ObservableField<String> guangshen_aqtitude_img = new ObservableField<>();    //  广审表

    public ObservableBoolean agreement = new ObservableBoolean(true);

    public MutableLiveData<ResponseBean> responseLiveData = new MutableLiveData<>();
    public MutableLiveData<AuditInfoBean> auditInfoLive = new MutableLiveData<>();
    public final MutableLiveData<HospitalCertifiedBean> authenticationLiveData = new MutableLiveData<>();


    public void user_audit() {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("card", cardUrl.get())                                     //手持身份证   （必传）
                .put("card_front", card_front.get())                            //   身份证正面  （必）
                .put("card_reverse", card_reverse.get())                        //   身份证反面  （必）
                .put("guangshen_aqtitude_img", guangshen_aqtitude_img.get())    // 广审表（非）-->改为必传2019-6-25
                .put("bussiness_aqtitude_img", bussiness_aqtitude_img.get())    //   医院营业执照  （必传）
                .put("permission", permission.get())                            // 医院医疗机构许可证  （必传）
                .put("hospital_name", real_name.get())                          //  医院名称
                .put("hospital_opreation_name", hospital_opreation_name.get())  // 运营人姓名   （必）
                .put("city_id", city_id.get())                                  //  城市ID
                .put("hosipital_type", hosipital_type.get())                    //医院类型     （必传 ）1民营机构 2公司机构 3品牌连锁 4生活美容机构
                .put("enterprise_name",companyName.get())                       //  企业工商注册名
                .put("enterprise_origin_code",creditCode.get())                 //  统一社会信用代码
                .put("company_email",companyEmail.get())                        //  邮箱
                .put("company_open_account",bankPermit.get())                   //  开户许可证
                .put("company_bank_card",bankCardFront.get())                   //  对公账户银行卡
                .put("company_bank_card_num",bankCardNumber.get())              //  对公账户卡号
                .put("company_bank_mobile",reservedMobile.get())                //  对公账户预留手机号
                .put("company_bank_code",bankData.getCode())                    //  对公账户银行代码
                .put("company_bank_province_code",province.getCode())           //  对公账户省代码
                .put("company_bank_city_code",city.getCode())                   //  对公账户市代码
                .put("legal_name",legalPersonName.get())                        //  法人姓名
                .put("legal_id_card_num",legalPersonIdcardNumber.get())         //  身份证
                .put("legal_id_card_effective_date_start",idcardStartDate)      //  身份证有效期开始
                .put("legal_id_card_effective_date_end",idcardEndDate)          //  身份证有效期结束
                .put("risk_info[register_amount]",registerAmount.second)
                .put("risk_info[office_space]",officeSpace.second)
                .put("risk_info[staff_size]",staffSize.second)
                .put("company_branch_bank_name",bankBranchInfo.get())
                .params();
        Api.getApiService(AccountService.class).user_audit(params)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        responseLiveData.postValue(responseBean);
                    }
                });
    }

    public void getAuditInfo() {
        Api.getApiService(AccountService.class)
                .getAuditInfo()
                .subscribe(new SimpleObserver<ResponseBean<AuditInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<AuditInfoBean> auditInfoBeanResponseBean) {
                        auditInfoLive.postValue(auditInfoBeanResponseBean.getData());
                    }
                });
    }

    public void authentication() {
        Api.getApiService(AccountService.class).authentication(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<HospitalCertifiedBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<HospitalCertifiedBean> responseBean) {
                        authenticationLiveData.postValue(responseBean.getData());
                    }
                });
    }
}
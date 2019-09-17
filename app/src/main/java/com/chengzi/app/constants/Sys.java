package com.chengzi.app.constants;

public class Sys {
    public static final String LOADING = "加载中...";
    public static final int SUCCESS_STATUS = 200;
    //用户身份 1普通用户 2医生 3咨询师 4医院
    public static final int TYPE_USER = 1;
    public static final int TYPE_DOCTOR = 2;
    public static final int TYPE_COUNSELOR = 3;
    public static final int TYPE_HOSPITAL = 4;

    //设置电话 -->医院的电话
    public static String SETTING_KEFU_PHONE = "4008208820";

    public static final String wxPay = "1";
    public static final String aliPay = "2";

    public static final String EXTRA = "extra";
    public static final String EXTRA_FOR_CATEGORY = "extra_for_category";
    public static final String EXTRA_OP_CATEGORY = "extra_op_category";

    public static final int TYPE_PRIVATE_REFER = 0x01;  //  私享咨询
    public static final int TYPE_DIAGNOSE_ONLINE = 0x02;  //  在线诊断

    public static final int REQUEST_CODE_PIC = 0x01;        //相册
    public static final int REQUEST_CODE_CAMERA = 0x02;     //拍照

    public static final String EXTRA_QUESTION_STATUS = "extra_question_status";
    public static final int EXTRA_STATUS_ALL = 0x00;         //    全部问题
    public static final int EXTRA_STATUS_RESOLVED = 0x01;    //   已解决的问题
    public static final int EXTRA_STATUS_UNRESOLVED = 0x02;  //  未解决的问题
    public static final String EXTRA_SEARCH_KEYWORD = "extra_search_keyword";


    public static final String EXTRA_FANS_TYPE = "extra_fans_type";  //  粉丝 或者 关注
    public static final int EXTRA_FANS = 0x00;   //  粉丝
    public static final int EXTRA_ATTENTION = 0x01;   //  关注


    public static final String REGISTER_UM_DEVICE_TOKEN = "REGISTER_UM_DEVICE_TOKEN";

    public static final String EXTRA_TARGET_ID = "extra_target_id";
    public static final String EXTRA_TARGET_TYPE = "extra_target_type";

    public static final String EXTRA_COMMENT_ID = "extra_comment_id";
    public static final String EXTRA_COMMENT_TYPE = "extra_comment_type";

    public static final String EXTRA_HOSPITAL_ID = "extra_hospital_id";
    public static final String EXTRA_USER_TYPE = "extra_user_type";

}
package com.chengzi.app.ui.bean.account;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.handong.framework.account.AccountHelper;

/**
 * @Desc:
 * @ClassName:UserInfoBean
 * @PackageName:com.yimei.app.ui.bean.account
 * @Create On 2019/4/24 0024
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class UserInfoBean {

    @SerializedName(value = "user_id", alternate = {"id"})
    private String user_id;
    private String name;        // 姓名
    private String image;       // 头像
    private String mobile;
    private String password;
    private String grade;       // 评分
    private String skill_grade;
    private String gender;      // 性别 0女 1男
    private String birthday;
    private String autograph;   //个性签名
    private int type;
    private int is_frozen;
    private String is_sign;     //今天是否已签到，false未签到，true已签到  -->是否已签到，0-未签到 1-已签到
    private String city_id;
    private String city_name;   //医院所属城市
    private int is_VIP;
    private String vip_starttime;
    private String vip_endtime;
    private String auth_status;      // 认证状态  0待审核 1审核通过 2已拒绝
    private String orange_create = "0";    // 橙子信用分
    private String institution;
    private String work_year;
    private String begoodat;
    private String synopsis;
    private String qualnumber;
    private String telephone;      //医院(机构) 固定电话
    private String school;
    private String skilled;
    private String wx_id;
    private int status;
    private String last_time;
    private int count;
    private String recomm_mobile;
    private String device_id;
    private String token;
    private String true_name;
    private String open_id;
    private String preset_amount;
    private String unionid;
    private String address;
    private String longitude;
    private String latitude;
    private int online_state;
    private String create_time;
    private String update_time;
    private String delete_time;

    private String hospital_opreation_name; //医院经营者姓名


    private String occupation_class;

    private String rank;
    private String begoodat_name;

    private String yunxin_accid;
    private String yunxin_token;

    private String jump;    //0-认证页  1-首页

    public String getBegoodat_name() {
        return !TextUtils.isEmpty(begoodat_name) ? begoodat_name : "请选择";
    }

    public void setBegoodat_name(String begoodat_name) {
        this.begoodat_name = begoodat_name;
    }

    public String getUser_id() {
        return user_id == null ? "" : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
//        return name == null ? "" : name;
        if (AccountHelper.isLogin()) {
            return name == null ? "" : name;
        }
        return "未登录";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrade() {
        return grade == null ? "" : grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSkill_grade() {
        return skill_grade == null ? "" : skill_grade;
    }

    public void setSkill_grade(String skill_grade) {
        this.skill_grade = skill_grade;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday == null ? "" : birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAutograph() {
//        return TextUtils.isEmpty(autograph) ? "这里显示个性签名内容…" : autograph;
        return TextUtils.isEmpty(autograph) ? "" : autograph;
    }

    public String getAutographName() {
        return TextUtils.isEmpty(autograph) ? "" : autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_frozen() {
        return is_frozen;
    }

    public void setIs_frozen(int is_frozen) {
        this.is_frozen = is_frozen;
    }

    public String getIs_sign() {    // 是否已签到，0-未签到 1-已签到
        return !TextUtils.isEmpty(is_sign) ? is_sign : "0";
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return !TextUtils.isEmpty(city_name) ? city_name : "";
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(int is_VIP) {
        this.is_VIP = is_VIP;
    }

    public String getVip_starttime() {
        return vip_starttime == null ? "" : vip_starttime;
    }

    public void setVip_starttime(String vip_starttime) {
        this.vip_starttime = vip_starttime;
    }

    public String getVip_endtime() {
        return vip_endtime == null ? "" : vip_endtime;
    }

    public void setVip_endtime(String vip_endtime) {
        this.vip_endtime = vip_endtime;
    }

    public String getAuth_status() {
        return !TextUtils.isEmpty(auth_status) ? auth_status : "3";
    }

    public void setAuth_status(String auth_status) {
        this.auth_status = auth_status;
    }

    public String getOrange_create() {
        return !TextUtils.isEmpty(orange_create) ? orange_create : "0";

    }

    public void setOrange_create(String orange_create) {
        this.orange_create = orange_create;
    }

    public String getInstitution() {
        return institution == null ? "" : institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getWork_year() {
        return work_year == null ? "0" : work_year;
    }

    public void setWork_year(String work_year) {
        this.work_year = work_year;
    }

    public String getBegoodat() {
        return begoodat == null ? "" : begoodat;
    }

    public void setBegoodat(String begoodat) {
        this.begoodat = begoodat;
    }

    public String getSynopsis() {
        return synopsis == null ? "" : synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getQualnumber() {
        return qualnumber == null ? "" : qualnumber;
    }

    public void setQualnumber(String qualnumber) {
        this.qualnumber = qualnumber;
    }

    public String getTelephone() {
        return !TextUtils.isEmpty(telephone) ? telephone : "";
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSchool() {
        return school == null ? "" : school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSkilled() {
        return skilled == null ? "" : skilled;
    }

    public void setSkilled(String skilled) {
        this.skilled = skilled;
    }

    public String getWx_id() {
        return wx_id == null ? "" : wx_id;
    }

    public void setWx_id(String wx_id) {
        this.wx_id = wx_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLast_time() {
        return last_time == null ? "" : last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRecomm_mobile() {
        return recomm_mobile == null ? "" : recomm_mobile;
    }

    public void setRecomm_mobile(String recomm_mobile) {
        this.recomm_mobile = recomm_mobile;
    }

    public String getDevice_id() {
        return device_id == null ? "" : device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTrue_name() {
        return true_name == null ? "" : true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getOpen_id() {
        return open_id == null ? "" : open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getPreset_amount() {
        return preset_amount == null ? "" : preset_amount;
    }

    public void setPreset_amount(String preset_amount) {
        this.preset_amount = preset_amount;
    }

    public String getUnionid() {
        return unionid == null ? "" : unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude == null ? "" : longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude == null ? "" : latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getOnline_state() {
        return online_state;
    }

    public void setOnline_state(int online_state) {
        this.online_state = online_state;
    }

    public String getOccupation_class() {
        return occupation_class == null ? "" : occupation_class;
    }

    public void setOccupation_class(String occupation_class) {
        this.occupation_class = occupation_class;
    }

    public String getRank() {
        return rank == null ? "" : rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCreate_time() {
        return create_time == null ? "" : create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time == null ? "" : update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getDelete_time() {
        return delete_time == null ? "" : delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getHospital_opreation_name() {
        return !TextUtils.isEmpty(hospital_opreation_name) ? hospital_opreation_name : "";
    }

    public void setHospital_opreation_name(String hospital_opreation_name) {
        this.hospital_opreation_name = hospital_opreation_name;
    }

    public String getYunxin_accid() {
        return yunxin_accid;
    }

    public void setYunxin_accid(String yunxin_accid) {
        this.yunxin_accid = yunxin_accid;
    }

    public String getYunxin_token() {
        return yunxin_token;
    }

    public void setYunxin_token(String yunxin_token) {
        this.yunxin_token = yunxin_token;
    }

    public String getJump() {
        return !TextUtils.isEmpty(jump) ? jump : "0";
    }

    public void setJump(String jump) {
        this.jump = jump;
    }
}

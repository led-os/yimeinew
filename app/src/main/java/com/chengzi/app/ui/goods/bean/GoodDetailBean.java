package com.chengzi.app.ui.goods.bean;

import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.utils.ScoreUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodDetailBean {

    private String id;              // 商品id
    private String user_id;         // 所属机构id
    private String city_id;         // 所属机构的所属城市的id
    private String name;            // 商品名称
    private String category_id;     // 一级分类id
    private String category_id2;     // 二级分类id
    private String category_id3;    // 三级分类id
    private String buy_price;       // 抢购价
    private String spell_price;     // 团购价
    private String doctor_id;
    private String counselling_id;
    private Object video_image;
    private String goods_info;          // 商品详情
    private int status;                 // 状态 1-上架  2-下架
    private int feature;                // 是否是特色商品，默认1-非特色  2-特色
    private String grade;               // 评分
    private String sales_volume;        // 销量
    private String preset_amount;
    private int is_extension;           // 是否推广 1-是 2-否
    private int is_killing;             // 是否是秒杀商品 0-不是，1-是
    private String kill_price;          // 秒杀价格
    private int kill_time;              //  秒杀开始时间 如 10:00 -- 12:00 ,返回10
    private long kill_remain_time;       // 秒杀中的剩余时间，秒数
    private long current_time;              //  服务器当前时间
    private HospitalBean hospital_data;      // 绑定的机构信息
    private String logo;                // 商品封面
    private String organization_name;       // 商品所属机构名称
    private int is_VIP;                     // VIP 0 否 1是
    private List<String> image;                 // 商品轮播图
    private List<DoctorBean> doctor_data;
    private List<DoctorBean> counselling_data;
    private List<GoodBean> hot_data;
    private List<GoodBean> recommend_data;
    private List<BannerBean> images_arr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_id2() {
        return category_id2;
    }

    public void setCategory_id2(String category_id2) {
        this.category_id2 = category_id2;
    }

    public String getCategory_id3() {
        return category_id3;
    }

    public void setCategory_id3(String category_id3) {
        this.category_id3 = category_id3;
    }

    public String getBuy_price() {
        return buy_price == null ? (buy_price = "") : buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public String getSpell_price() {
        return spell_price == null ? (spell_price = "") : spell_price;
    }

    public void setSpell_price(String spell_price) {
        this.spell_price = spell_price;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getCounselling_id() {
        return counselling_id;
    }

    public void setCounselling_id(String counselling_id) {
        this.counselling_id = counselling_id;
    }

    public Object getVideo_image() {
        return video_image;
    }

    public void setVideo_image(Object video_image) {
        this.video_image = video_image;
    }

    public String getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(String goods_info) {
        this.goods_info = goods_info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFeature() {
        return feature;
    }

    public void setFeature(int feature) {
        this.feature = feature;
    }

    public String getGrade() {
        return ScoreUtils.processScore(grade);
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }

    public String getPreset_amount() {
        return preset_amount;
    }

    public void setPreset_amount(String preset_amount) {
        this.preset_amount = preset_amount;
    }

    public int getIs_extension() {
        return is_extension;
    }

    public void setIs_extension(int is_extension) {
        this.is_extension = is_extension;
    }

    public int getIs_killing() {
        return is_killing;
    }

    public void setIs_killing(int is_killing) {
        this.is_killing = is_killing;
    }

    public String getKill_price() {
        return kill_price == null ? (kill_price = "") : kill_price;
    }

    public void setKill_price(String kill_price) {
        this.kill_price = kill_price;
    }

    public int getKill_time() {
        return kill_time;
    }

    public void setKill_time(int kill_time) {
        this.kill_time = kill_time;
    }

    public long getKill_remain_time() {
        return kill_remain_time;
    }

    public void setKill_remain_time(long kill_remain_time) {
        this.kill_remain_time = kill_remain_time;
    }

    public HospitalBean getHospital_data() {
        return hospital_data;
    }

    public void setHospital_data(HospitalBean hospital_data) {
        this.hospital_data = hospital_data;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public int getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(int is_VIP) {
        this.is_VIP = is_VIP;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<DoctorBean> getDoctor_data() {
        return doctor_data == null ? (doctor_data = new ArrayList<>()) : doctor_data;
    }

    public void setDoctor_data(List<DoctorBean> doctor_data) {
        this.doctor_data = doctor_data;
    }

    public List<DoctorBean> getCounselling_data() {
        return counselling_data == null ? (counselling_data = new ArrayList<>()) : counselling_data;
    }

    public void setCounselling_data(List<DoctorBean> counselling_data) {
        this.counselling_data = counselling_data;
    }

    public List<GoodBean> getHot_data() {
        return hot_data;
    }

    public void setHot_data(List<GoodBean> hot_data) {
        this.hot_data = hot_data;
    }

    public List<GoodBean> getRecommend_data() {
        return recommend_data;
    }

    public void setRecommend_data(List<GoodBean> recommend_data) {
        this.recommend_data = recommend_data;
    }

    public List<BannerBean> getImages_arr() {
        return images_arr;
    }

    public void setImages_arr(List<BannerBean> images_arr) {
        this.images_arr = images_arr;
    }

    public long getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(long current_time) {
        this.current_time = current_time;
    }
}

package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.handongkeji.utils.FormatUtil;

import java.io.Serializable;


/**
 * 案例管理列表的Bean类
 *
 * @ClassName:CaseManageListBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/24 002413:44
 * @Site:http://www.handongkeji.com
 * @author: wanghongfu
 * @Copyrights 2019/4/24 0024 handongkeji All rights reserved.
 */
public class CaseManageListBean implements Serializable {

    private String id;          //案例搜索的案例id
    private String cases_id; // 案例ID
    private String user_id; // 用户ID
    private String cate_id;     // 分类id
    private String content;// 内容
    private String catename; // 第三级分类名称
    private int like_number;  // 点赞数
    private String comment_number; // 评论数
    private String forward_number;  // 转发数
    private String view_number;// 浏览数
    private String pre_img; // 术前照
    private String after_img; // 术后照

    //案例管理   默认不可点赞
    private boolean isLike = false; // 当前用户是否点赞
    private HospitalInfoBean hospital_info;

    //案例推广
    private boolean is_extension;// 是否开启搜索推广
    private String preset_amount;// 搜索推广消费限额

    public HospitalInfoBean getHospital_info() {
        return hospital_info;
    }

    public void setHospital_info(HospitalInfoBean hospital_info) {
        this.hospital_info = hospital_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCases_id() {
        return cases_id;
    }

    public void setCases_id(String cases_id) {
        this.cases_id = cases_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public int getLike_number() {
        return !TextUtils.isEmpty(String.valueOf(like_number)) ? like_number : 0;
    }

    public void setLike_number(int like_number) {
        this.like_number = like_number;
    }

    public String getComment_number() {
        return !TextUtils.isEmpty(comment_number) ? comment_number : "0";
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public String getForward_number() {
        return !TextUtils.isEmpty(forward_number) ? forward_number : "0";
    }

    public void setForward_number(String forward_number) {
        this.forward_number = forward_number;
    }

    public String getView_number() {
        return !TextUtils.isEmpty(view_number) ? view_number : "0";
    }

    public void setView_number(String view_number) {
        this.view_number = view_number;
    }

    public String getPre_img() {
        return pre_img;
    }

    public void setPre_img(String pre_img) {
        this.pre_img = pre_img;
    }

    public String getAfter_img() {
        return after_img;
    }

    public void setAfter_img(String after_img) {
        this.after_img = after_img;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isIs_extension() {
        return is_extension;
    }

    public void setIs_extension(boolean is_extension) {
        this.is_extension = is_extension;
    }

    public String getPreset_amount() {
//        return preset_amount;
        return !TextUtils.isEmpty(preset_amount)? FormatUtil.format2Decimal(Double.parseDouble(preset_amount)):"0.00";
    }

    public void setPreset_amount(String preset_amount) {
        this.preset_amount = preset_amount;
    }


    public class HospitalInfoBean {
        /**
         * id : 47
         * name : 彭彭咨询师
         * image : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/155857911259047
         * is_VIP : 1
         * grade : 8.1
         * is_Extension : true
         */

        private String id;
        private String name;
        private String image;
        private String is_VIP;
        private String grade;
        private boolean is_Extension;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getIs_VIP() {
            return is_VIP;
        }

        public void setIs_VIP(String is_VIP) {
            this.is_VIP = is_VIP;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public boolean isIs_Extension() {
            return is_Extension;
        }

        public void setIs_Extension(boolean is_Extension) {
            this.is_Extension = is_Extension;
        }
    }
}
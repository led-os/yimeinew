package com.chengzi.app.ui.homepage.bean;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.chengzi.app.ui.discover.bean.ContentBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.utils.GsonHelper;

import java.lang.reflect.Type;
import java.util.List;

public class ClickLikeListBean {

    /**
     * user_id : 6
     * create_time : 2006-05-12 05:40
     * find_id : 1
     * find_image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * find_content : 世界辣么大，我想去探探
     * user_info : {"user_id":6,"name":"北京美容解放医院","image":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","is_VIP":0,"grade":"8.4","level_name":"","city_id":110100,"gender":null,"birthday":null,"user_type":4}
     */

    private String user_id;          // 点赞者的用户id
    private String create_time;      // 点赞时间
    private String find_id;          // 点赞的帖子id
    private String find_image;       // 帖子封面
    private String find_content;     // 帖子内容
    private SpannableStringBuilder contentBuilder;
    private UserInfoEntity user_info;// 点赞者的用户信息

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return !TextUtils.isEmpty(create_time) ? create_time : "";
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFind_id() {
        return find_id;
    }

    public void setFind_id(String find_id) {
        this.find_id = find_id;
    }

    public String getFind_image() {
        return find_image;
    }

    public void setFind_image(String find_image) {
        this.find_image = find_image;
    }

    public String getFind_content() {
        return find_content;
    }

    public SpannableStringBuilder getContentBuilder() {
        if (contentBuilder == null) {
            contentBuilder = new SpannableStringBuilder();
            if (!TextUtils.isEmpty(find_content)) {
                Type type = new TypeToken<List<ContentBean>>() {
                }.getType();
                List<ContentBean> list = GsonHelper.getDefault().fromJson(find_content, type);
                if (list != null && !list.isEmpty()) {
                    for (ContentBean contentBean : list) {
                        if (contentBean.getType() == 0) {
                            contentBuilder.append(contentBean.getText());
                        } else {
                            int start = contentBuilder.length();
                            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#47B6E2"));
                            contentBuilder.append(contentBean.getText(), fcs, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    String userId = contentBean.getUserId();
                                    int userType = contentBean.getUserType();
                                    switch (userType) {
                                        case 1:
                                            UserHomePageActivity.start(widget.getContext(), userId);
                                            break;
                                        case 2:
                                            DoctorHomePageActivity.startDoctor(widget.getContext(), userId);
                                            break;
                                        case 3:
                                            DoctorHomePageActivity.startCounselor(widget.getContext(), userId);
                                            break;
                                        case 4:
                                            HospitalHomePageActivity.start(widget.getContext(), userId);
                                            break;
                                    }
                                }

                                @Override
                                public void updateDrawState(@NonNull TextPaint ds) {
//                                    super.updateDrawState(ds);
                                }
                            };

                            contentBuilder.setSpan(clickableSpan, start, contentBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            contentBuilder.append(" ");
                        }
                    }
                }
            }
        }
        return contentBuilder;
    }

    public void setFind_content(String find_content) {
        this.find_content = find_content;
    }

    public UserInfoEntity getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoEntity user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoEntity {
        /**
         * user_id : 6
         * name : 北京美容解放医院
         * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
         * is_VIP : 0
         * grade : 8.4
         * level_name :
         * city_id : 110100
         * gender : null
         * birthday : null
         * user_type : 4
         */

        private String user_id;    // 点赞者的用户id，同外面
        private String name;       // 点赞者昵称
        private String image;      // 点赞者头像
        private String is_VIP;     // 是否是vip 0-否 1-是
        private String grade;      // 点赞者评分
        private String level_name; // 点赞者职称
        private String city_id;    // 点赞者所属城市id
        private String gender;     // 点赞者性别 0-女 1-男
        private String birthday;   // 点赞者生日 字符串非时间戳
        private String user_type;  // 点赞者用户类型1-用户 2-医生 3-咨询师 4-机构

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return !TextUtils.isEmpty(name) ? name : "";
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

        public String getLevel_name() {
            return level_name;
        }

        public void setLevel_name(String level_name) {
            this.level_name = level_name;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}

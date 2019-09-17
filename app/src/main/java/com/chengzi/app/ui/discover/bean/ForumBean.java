package com.chengzi.app.ui.discover.bean;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.chengzi.app.utils.IntTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.utils.GsonHelper;
import com.chengzi.app.utils.NumberShowUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ForumBean {

    private String id;        //  帖子id
    @SerializedName(value = "user_id", alternate = {"uid"})
    private String user_id;     //  用户id
    @JsonAdapter(IntTypeAdapter.class)
    @SerializedName(value = "user_type", alternate = {"userType", "type"})
    private int user_type;      //  用户类型
    private String content;     //  内容
    private SpannableStringBuilder contentBuilder;
    private List<String> pics;  //  图片
    private String video;       //  视频
    private String likes_num;      //  点赞数
    @SerializedName(value = "comment_count", alternate = {"comments_count", "comment_num"})
    private String comment_count;  //评论数
    private String collection_num;   //  收藏数
    private String forward_num;     //  转发数
    //    private String type;            //   身份类型
    @SerializedName(value = "title", alternate = {"level"})
    private String title;         //   身份头衔
    private String username;      //   用户名
    private int is_follow;        //    是否关注   0 否  1 是
    private String autograph;     //    用户签名

    @SerializedName(value = "image", alternate = {"imgge", "usericon"})
    private String image;       //      头像
    private String time;         //   距离发帖时间
    private long create_time;
    private String resource_type;  //   资源类型  1 图片   2视频

    private int is_like;    //  是否点赞    0否  1 是
    @SerializedName(value = "is_collect", alternate = {"is_collection"})
    private int is_collect;     //  是否收藏    0否  1 是

    public ForumBean(String username, String content, String time,
                     String collection_num, String comments_count, String forward_num, String likes_num) {
        this.username = username;
        this.content = content;
        this.time = time;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes_num() {
        return NumberShowUtils.processNumber(likes_num);
    }

    public void setLikes_num(String likes_num) {
        this.likes_num = likes_num;
    }

    public String getCollection_num() {
        return NumberShowUtils.processNumber(collection_num);
    }

    public void setCollection_num(String collection_num) {
        this.collection_num = collection_num;
    }

    public String getForward_num() {
        return NumberShowUtils.processNumber(forward_num);
    }

    public void setForward_num(String forward_num) {
        this.forward_num = forward_num;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getComment_count() {
        return NumberShowUtils.processNumber(comment_count);
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getPics() {
        return pics == null ? (pics = new ArrayList<>()) : pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public SpannableStringBuilder getContentBuilder() {
        if (contentBuilder == null) {
            contentBuilder = new SpannableStringBuilder();
            if (!TextUtils.isEmpty(content)) {
                Type type = new TypeToken<List<ContentBean>>() {
                }.getType();
                List<ContentBean> list = GsonHelper.getDefault().fromJson(content, type);
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
}

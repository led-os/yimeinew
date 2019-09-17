package com.chengzi.app.ui.message.bean;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.chengzi.app.ui.discover.bean.ContentBean;
import com.chengzi.app.utils.GsonHelper;

import java.lang.reflect.Type;
import java.util.List;

public class CommentMsgBean {

    private String from_uid;
    private String to_id;
    private String to_uid;
    private String content;
    private String reply;
    private String create_time;
    private String data_resoure;
    private String username;
    private String usericon;
    private int user_type;   //  用户类型
    private String level;
    @SerializedName(value = "publish_user")
    private Publisher publisher;
    private PublishContent publish_content;

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getData_resoure() {
        return data_resoure;
    }

    public void setData_resoure(String data_resoure) {
        this.data_resoure = data_resoure;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public PublishContent getPublish_content() {
        return publish_content;
    }

    public void setPublish_content(PublishContent publish_content) {
        this.publish_content = publish_content;
    }

    public static class Publisher {
        private String id;
        private String name;
        private String image;

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
    }

    public static class PublishContent {
        private String id;
        private String content;
        private String category_name;
        private String category_id;
        private String image;

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public SpannableStringBuilder getContentBuilder() {
            SpannableStringBuilder contentBuilder = new SpannableStringBuilder();
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

//                            ClickableSpan clickableSpan = new ClickableSpan() {
//                                @Override
//                                public void onClick(@NonNull View widget) {
//                                    String userId = contentBean.getUserId();
//                                    int userType = contentBean.getUserType();
//                                    switch (userType) {
//                                        case 1:
//                                            UserHomePageActivity.start(widget.getContext(), userId);
//                                            break;
//                                        case 2:
//                                            DoctorHomePageActivity.startDoctor(widget.getContext(), userId);
//                                            break;
//                                        case 3:
//                                            DoctorHomePageActivity.startCounselor(widget.getContext(), userId);
//                                            break;
//                                        case 4:
//                                            HospitalHomePageActivity.start(widget.getContext(), userId);
//                                            break;
//                                    }
//                                }
//
//                                @Override
//                                public void updateDrawState(@NonNull TextPaint ds) {
////                                    super.updateDrawState(ds);
//                                }
//                            };

//                            contentBuilder.setSpan(clickableSpan, start, contentBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            contentBuilder.append(" ");
                        }
                    }
                }
            }
            return contentBuilder;
        }
    }
}

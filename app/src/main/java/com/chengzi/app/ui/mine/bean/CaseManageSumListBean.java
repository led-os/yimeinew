package com.chengzi.app.ui.mine.bean;

/**
 * 自己构造的模型类
 *
 * @ClassName:CaseManageSumListBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/24 002414:31
 * @Site:http://www.handongkeji.com
 * @author: wanghongfu
 * @Copyrights 2019/4/24 0024 handongkeji All rights reserved.
 */
public class CaseManageSumListBean {
    private int status;
    private int id;
    private int user_id;
    private int cate_id;
    private String content;
    private String catename;
    private int like_number;
    private int ascomment_number;
    private int forward_number;
    private int view_number;
    private String PreoperativeImg;
    private String PostoperativeImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCatename() {
        return catename == null ? "" : catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public int getLike_number() {
        return like_number;
    }

    public void setLike_number(int like_number) {
        this.like_number = like_number;
    }

    public int getAscomment_number() {
        return ascomment_number;
    }

    public void setAscomment_number(int ascomment_number) {
        this.ascomment_number = ascomment_number;
    }

    public int getForward_number() {
        return forward_number;
    }

    public void setForward_number(int forward_number) {
        this.forward_number = forward_number;
    }

    public int getView_number() {
        return view_number;
    }

    public void setView_number(int view_number) {
        this.view_number = view_number;
    }

    public String getPreoperativeImg() {
        return PreoperativeImg == null ? "" : PreoperativeImg;
    }

    public void setPreoperativeImg(String preoperativeImg) {
        PreoperativeImg = preoperativeImg;
    }

    public String getPostoperativeImg() {
        return PostoperativeImg == null ? "" : PostoperativeImg;
    }

    public void setPostoperativeImg(String postoperativeImg) {
        PostoperativeImg = postoperativeImg;
    }

    public CaseManageSumListBean(int id, int user_id, int cate_id, String content, String catename, int like_number, int ascomment_number, int forward_number, int view_number, String preoperativeImg, String postoperativeImg) {
        this.id = id;
        this.user_id = user_id;
        this.cate_id = cate_id;
        this.content = content;
        this.catename = catename;
        this.like_number = like_number;
        this.ascomment_number = ascomment_number;
        this.forward_number = forward_number;
        this.view_number = view_number;
        PreoperativeImg = preoperativeImg;
        PostoperativeImg = postoperativeImg;
    }
}

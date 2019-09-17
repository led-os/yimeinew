package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class LookSheetNumBean {

    /**
     * id : 4
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * name : 阿姐
     * type : 3
     * rank : VIP咨询师
     * sheetNum : 1
     */

    private String id;
    private String image;
    private String name;
    private String type;

    /*职称*/
    @SerializedName(value = "rank", alternate = {"occupation_name"})
    private String rank;
    /*当前排名/对话量 */
    @SerializedName(value = "sheetNum", alternate = {"rank_num"})
    private String sheetNum;

    //竞对分析专有
    private String true_name;   //真实姓名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return !TextUtils.isEmpty(name) ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return !TextUtils.isEmpty(type) ? type : "";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRank() {
        return !TextUtils.isEmpty(rank) ? rank : "";
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSheetNum() {
        return !TextUtils.isEmpty(sheetNum) ? sheetNum : "";
    }

    public void setSheetNum(String sheetNum) {
        this.sheetNum = sheetNum;
    }

    public String getTrue_name() {
        return !TextUtils.isEmpty(true_name) ? true_name : "";
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }
}
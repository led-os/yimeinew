package com.chengzi.app.ui.mine.bean;

public class GetDownloadUrlQrcodeBean {

    /**
     * id : 14
     * version_number : 888666
     * low_version_number : 01
     * name : 测试新版本
     * download_url : http://www.aliyun.com
     * qrcode_image : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/2019/05/21/3813580b3bca4f7583cc329d61813bd865fb15862e34a4.png
     * is_force_update : 1
     * content : 官儿告知
     * create_time : 1558404595
     */

    private int id;
    private String version_number;
    private String low_version_number;
    private String name;
    private String download_url;
    private String qrcode_image;
    private int is_force_update;
    private String content;
    private int create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion_number() {
        return version_number;
    }

    public void setVersion_number(String version_number) {
        this.version_number = version_number;
    }

    public String getLow_version_number() {
        return low_version_number;
    }

    public void setLow_version_number(String low_version_number) {
        this.low_version_number = low_version_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getQrcode_image() {
        return qrcode_image;
    }

    public void setQrcode_image(String qrcode_image) {
        this.qrcode_image = qrcode_image;
    }

    public int getIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(int is_force_update) {
        this.is_force_update = is_force_update;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}

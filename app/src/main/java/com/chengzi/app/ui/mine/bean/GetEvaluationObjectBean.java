package com.chengzi.app.ui.mine.bean;

public class GetEvaluationObjectBean {
    /**
     * doctor_data : {"id":14,"type":2,"image":"http:///public/uploads/2019-04-12/5cb00bbd0bf3b.jpg","name":"笑笑二号","occupation_class":"主治医师"}
     * counselling_data : {"id":4,"type":3,"image":"http:///public/uploads/2019-04-15/5cb3ed045e351.jpg","name":"阿姐","occupation_class":"普通咨询师"}
     */

    //用户评价：医生 咨询师
    //用户投诉：医生 咨询师 医院
    //机构投诉：普通用户
    private DoctorDataEntity user_data;         //普通用户
    private DoctorDataEntity doctor_data;       //医生
    private DoctorDataEntity counselling_data;  //咨询师
    private DoctorDataEntity hospital_data;     //医院

    public DoctorDataEntity getUser_data() {
        return user_data;
    }

    public void setUser_data(DoctorDataEntity user_data) {
        this.user_data = user_data;
    }

    public DoctorDataEntity getDoctor_data() {
        return doctor_data;
    }

    public void setDoctor_data(DoctorDataEntity doctor_data) {
        this.doctor_data = doctor_data;
    }

    public DoctorDataEntity getCounselling_data() {
        return counselling_data;
    }

    public void setCounselling_data(DoctorDataEntity counselling_data) {
        this.counselling_data = counselling_data;
    }

    public DoctorDataEntity getHospital_data() {
        return hospital_data;
    }

    public void setHospital_data(DoctorDataEntity hospital_data) {
        this.hospital_data = hospital_data;
    }

    public static class DoctorDataEntity { // 医生/咨询师信息
        /**
         * id : 14
         * type : 2
         * image : http:///public/uploads/2019-04-12/5cb00bbd0bf3b.jpg
         * name : 笑笑二号
         * occupation_class : 主治医师
         */

        private String id;      // 用户uid
        private String type;    // 用户类型 1-用户 2-医生 3-咨询师 4-机构
        private String image;   // 用户头像
        private String name;    // 用户昵称
        private String occupation_class;  // 用户职称

        private boolean isChoose = false;

        public boolean getIsChoose() {
            return isChoose;
        }

        public void setIsChoose(boolean choose) {
            isChoose = choose;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOccupation_class() {
            return occupation_class;
        }

        public void setOccupation_class(String occupation_class) {
            this.occupation_class = occupation_class;
        }
    }
}

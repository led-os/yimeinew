package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.util.List;

public class CompetitiveAnalysisBean {


    /**
     * hospitalName : {"name":"北京美容解放医院"}
     * city : {"city_name":"北京","city_id":110100}
     * cityRankings : 5
     * data : [{"id":16,"name":"咨询师一号","true_name":"小仙咨询师","occupation_name":"普通咨询师","image":"","rank_num":1},{"id":11,"name":"阿姐","true_name":"小仙咨询师","occupation_name":"VIP咨询师","image":"","rank_num":5},{"id":17,"name":"咨询师二号","true_name":"小仙咨询师","occupation_name":"普通咨询师","image":"","rank_num":6}]
     */

    private HospitalNameEntity hospitalName;    //医院
    private CityEntity city;        //城市
    private String cityRankings;            //排名
    private List<LookSheetNumBean> doctorRanking;
    private List<LookSheetNumBean> consultantRanking;

    public HospitalNameEntity getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(HospitalNameEntity hospitalName) {
        this.hospitalName = hospitalName;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public String getCityRankings() {
        return !TextUtils.isEmpty(cityRankings) ? cityRankings : "";
    }

    public void setCityRankings(String cityRankings) {
        this.cityRankings = cityRankings;
    }

    public List<LookSheetNumBean> getDoctorRanking() {
        return doctorRanking;
    }

    public void setDoctorRanking(List<LookSheetNumBean> doctorRanking) {
        this.doctorRanking = doctorRanking;
    }

    public List<LookSheetNumBean> getConsultantRanking() {
        return consultantRanking;
    }

    public void setConsultantRanking(List<LookSheetNumBean> consultantRanking) {
        this.consultantRanking = consultantRanking;
    }

    public static class HospitalNameEntity {
        /**
         * name : 北京美容解放医院
         */

        private String name;

        public String getName() {
            return !TextUtils.isEmpty(name) ? name : "";
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class CityEntity {
        /**
         * city_name : 北京
         * city_id : 110100
         */

        private String city_name;
        private String city_id;

        public String getCity_name() {
            return !TextUtils.isEmpty(city_name) ? city_name : "";
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }
    }
}

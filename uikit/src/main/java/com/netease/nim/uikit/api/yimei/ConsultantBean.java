package com.netease.nim.uikit.api.yimei;

public class ConsultantBean {

    private ConsultantsAccidBean consultants_accid;

    public ConsultantsAccidBean getConsultants_accid() {
        return consultants_accid;
    }

    public void setConsultants_accid(ConsultantsAccidBean consultants_accid) {
        this.consultants_accid = consultants_accid;
    }

    public static class ConsultantsAccidBean {

        /**
         * id : 72
         * yunxin_accid : offline_v4_72
         */

        private String id;
        private String yunxin_accid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getYunxin_accid() {
            return yunxin_accid;
        }

        public void setYunxin_accid(String yunxin_accid) {
            this.yunxin_accid = yunxin_accid;
        }
    }
}

package com.chengzi.app.ui.account.bean;

import com.google.gson.JsonObject;

public class CompanyBean {


    /**
     * register_amount : {"label":"注册资金","code":"REGISTER_AMOUNT","options":{"A":"人民币 500 万以上，或等值外币","B":"人民币 100 万-500 万(含)，或等值外币","C":"人民币 100 万以下或等值外币及自然人商户"}}
     * staff_size : {"label":"公司员工规模","code":"STAFF_SIZE","options":{"A":"100 人以上","B":"50~100 人(含)","C":"50 人以下"}}
     * office_space : {"label":"办公面积","code":"OFFICE_SPACE","options":{"A":"500 平米以上","B":"200-500 平米(含)","C":"200 平米以下"}}
     */

    private RegisterAmountBean register_amount;
    private StaffSizeBean staff_size;
    private OfficeSpaceBean office_space;

    public RegisterAmountBean getRegister_amount() {
        return register_amount;
    }

    public void setRegister_amount(RegisterAmountBean register_amount) {
        this.register_amount = register_amount;
    }

    public StaffSizeBean getStaff_size() {
        return staff_size;
    }

    public void setStaff_size(StaffSizeBean staff_size) {
        this.staff_size = staff_size;
    }

    public OfficeSpaceBean getOffice_space() {
        return office_space;
    }

    public void setOffice_space(OfficeSpaceBean office_space) {
        this.office_space = office_space;
    }

    public static class RegisterAmountBean {
        /**
         * label : 注册资金
         * code : REGISTER_AMOUNT
         * options : {"A":"人民币 500 万以上，或等值外币","B":"人民币 100 万-500 万(含)，或等值外币","C":"人民币 100 万以下或等值外币及自然人商户"}
         */

        private String label;
        private String code;
        private JsonObject options;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public JsonObject getOptions() {
            return options;
        }

        public void setOptions(JsonObject options) {
            this.options = options;
        }

    }

    public static class StaffSizeBean {
        /**
         * label : 公司员工规模
         * code : STAFF_SIZE
         * options : {"A":"100 人以上","B":"50~100 人(含)","C":"50 人以下"}
         */

        private String label;
        private String code;
        private JsonObject options;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public JsonObject getOptions() {
            return options;
        }

        public void setOptions(JsonObject options) {
            this.options = options;
        }

    }

    public static class OfficeSpaceBean {
        /**
         * label : 办公面积
         * code : OFFICE_SPACE
         * options : {"A":"500 平米以上","B":"200-500 平米(含)","C":"200 平米以下"}
         */

        private String label;
        private String code;
        private JsonObject options;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public JsonObject getOptions() {
            return options;
        }

        public void setOptions(JsonObject options) {
            this.options = options;
        }

    }
}

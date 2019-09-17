package com.chengzi.app.ui.home.bean;

import com.google.gson.annotations.SerializedName;

public class GetCustomerServiceStaffBean {

    @SerializedName(value = "customer_service_staff_accid")
    private CustomerServiceStaffAccidBean customer;

    public CustomerServiceStaffAccidBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerServiceStaffAccidBean customer) {
        this.customer = customer;
    }

    public static class CustomerServiceStaffAccidBean {

        private int id;
        private String yunxin_accid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

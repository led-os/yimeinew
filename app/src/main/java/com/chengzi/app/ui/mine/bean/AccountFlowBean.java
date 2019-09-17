package com.chengzi.app.ui.mine.bean;

import java.util.List;

/**
 * @Desc:
 * @ClassName:AccountFlowBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/26 0026
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class AccountFlowBean {

    /**
     * total : 1
     * per_page : 10
     * current_page : 1
     * last_page : 1
     * data : [{"id":16,"user_id":2,"money":"422","change_type":1,"create_time":"2019-04-01 12:04:22","update_time":null,"delete_time":null}]
     */

    private int total;
    private String per_page;
    private int current_page;
    private int last_page;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 16
         * user_id : 2
         * money : 422
         * change_type : 1
         * create_time : 2019-04-01 12:04:22
         * update_time : null
         * delete_time : null
         */

        private String id;
        private String user_id;
        private String money;
        private int change_type;
        private String create_time;
        private String update_time;
        private String delete_time;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id == null ? "" : user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMoney() {
            return money == null ? "" : money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getChange_type() {
            return change_type;
        }

        public void setChange_type(int change_type) {
            this.change_type = change_type;
        }

        public String getCreate_time() {
            return create_time == null ? "" : create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time == null ? "" : update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getDelete_time() {
            return delete_time == null ? "" : delete_time;
        }

        public void setDelete_time(String delete_time) {
            this.delete_time = delete_time;
        }
    }
}

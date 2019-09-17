package com.chengzi.app.third.pay;

public class PreOrderResult {


    /**
     * data : {"orderid":15,"userid":84,"ordername":null,"orderprice":0.1,"orderstatus":0,"ordercreatetime":1516181187365,"outtradeno":null,"systemtradeno":"151618118736518","orderpaytype":1,"usermobile":null,"ordermembers":0,"orderisdel":0}
     * message : 操作成功!
     * status : 1
     */


        private int orderid;
        private int userid;
        private String ordername;
        private double orderprice;
        private int orderstatus;
        private long ordercreatetime;
        private String outtradeno;
        private String systemtradeno;
        private int orderpaytype;
        private String usermobile;
        private int ordermembers;
        private int orderisdel;

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getOrdername() {
            return ordername;
        }

        public void setOrdername(String ordername) {
            this.ordername = ordername;
        }

        public double getOrderprice() {
            return orderprice;
        }

        public void setOrderprice(double orderprice) {
            this.orderprice = orderprice;
        }

        public int getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(int orderstatus) {
            this.orderstatus = orderstatus;
        }

        public long getOrdercreatetime() {
            return ordercreatetime;
        }

        public void setOrdercreatetime(long ordercreatetime) {
            this.ordercreatetime = ordercreatetime;
        }

        public String getOuttradeno() {
            return outtradeno;
        }

        public void setOuttradeno(String outtradeno) {
            this.outtradeno = outtradeno;
        }

        public String getSystemtradeno() {
            return systemtradeno;
        }

        public void setSystemtradeno(String systemtradeno) {
            this.systemtradeno = systemtradeno;
        }

        public int getOrderpaytype() {
            return orderpaytype;
        }

        public void setOrderpaytype(int orderpaytype) {
            this.orderpaytype = orderpaytype;
        }

        public String getUsermobile() {
            return usermobile;
        }

        public void setUsermobile(String usermobile) {
            this.usermobile = usermobile;
        }

        public int getOrdermembers() {
            return ordermembers;
        }

        public void setOrdermembers(int ordermembers) {
            this.ordermembers = ordermembers;
        }

        public int getOrderisdel() {
            return orderisdel;
        }

        public void setOrderisdel(int orderisdel) {
            this.orderisdel = orderisdel;
        }
}

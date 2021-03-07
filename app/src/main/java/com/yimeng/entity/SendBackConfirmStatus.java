package com.yimeng.entity;

public class SendBackConfirmStatus {


    /**
     * status : 1
     * msg : 退货申请成功，回邮！
     * data : {"refundState":"refund_success","orderNo":null,"address":null,"shopName":null,"username":null,"mobile":null,"productName":null,"remark":null,"logisticsType":"5624668138428469866","logisticsNo":null}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * refundState : refund_success
         * orderNo : null
         * address : null
         * shopName : null
         * username : null
         * mobile : null
         * productName : null
         * remark : null
         * logisticsType : 5624668138428469866
         * logisticsNo : null
         */

        private String refundState;
        private Object orderNo;
        private Object address;
        private Object shopName;
        private Object username;
        private Object mobile;
        private Object productName;
        private Object remark;
        private String logisticsType;
        private Object logisticsNo;

        public String getRefundState() {
            return refundState;
        }

        public void setRefundState(String refundState) {
            this.refundState = refundState;
        }

        public Object getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Object orderNo) {
            this.orderNo = orderNo;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getShopName() {
            return shopName;
        }

        public void setShopName(Object shopName) {
            this.shopName = shopName;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getProductName() {
            return productName;
        }

        public void setProductName(Object productName) {
            this.productName = productName;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getLogisticsType() {
            return logisticsType;
        }

        public void setLogisticsType(String logisticsType) {
            this.logisticsType = logisticsType;
        }

        public Object getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(Object logisticsNo) {
            this.logisticsNo = logisticsNo;
        }
    }
}

package com.yimeng.entity;

public class QrCodeResult {

    /**
     * status : 1
     * msg : null
     * data : {"id":175,"rewardNo":"202008111853314365541","payStatus":"nopay","rewardType":"paymentToMerchant","rewardTypeNo":null,"memberNo":"ANDROID2020081009555702800000002","receiveMemberNo":"IOS201906191850023351444","rewardAmt":1000,"feeAmt":0,"remark":null,"createTime":null,"updateTime":null,"payType":null,"memberName":null,"mobileNo":null,"shopNo":null,"shopType":null,"shopName":null}
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

    public Object getMsg() {
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
         * id : 175
         * rewardNo : 202008111853314365541
         * payStatus : nopay
         * rewardType : paymentToMerchant
         * rewardTypeNo : null
         * memberNo : ANDROID2020081009555702800000002
         * receiveMemberNo : IOS201906191850023351444
         * rewardAmt : 1000
         * feeAmt : 0
         * remark : null
         * createTime : null
         * updateTime : null
         * payType : null
         * memberName : null
         * mobileNo : null
         * shopNo : null
         * shopType : null
         * shopName : null
         */

        private int id;
        private String rewardNo;
        private String payStatus;
        private String rewardType;
        private Object rewardTypeNo;
        private String memberNo;
        private String receiveMemberNo;
        private int rewardAmt;
        private int feeAmt;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRewardNo() {
            return rewardNo;
        }

        public void setRewardNo(String rewardNo) {
            this.rewardNo = rewardNo;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getRewardType() {
            return rewardType;
        }

        public void setRewardType(String rewardType) {
            this.rewardType = rewardType;
        }

        public Object getRewardTypeNo() {
            return rewardTypeNo;
        }

        public void setRewardTypeNo(Object rewardTypeNo) {
            this.rewardTypeNo = rewardTypeNo;
        }

        public String getMemberNo() {
            return memberNo;
        }

        public void setMemberNo(String memberNo) {
            this.memberNo = memberNo;
        }

        public String getReceiveMemberNo() {
            return receiveMemberNo;
        }

        public void setReceiveMemberNo(String receiveMemberNo) {
            this.receiveMemberNo = receiveMemberNo;
        }

        public int getRewardAmt() {
            return rewardAmt;
        }

        public void setRewardAmt(int rewardAmt) {
            this.rewardAmt = rewardAmt;
        }

        public int getFeeAmt() {
            return feeAmt;
        }

        public void setFeeAmt(int feeAmt) {
            this.feeAmt = feeAmt;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", rewardNo='" + rewardNo + '\'' +
                    ", payStatus='" + payStatus + '\'' +
                    ", rewardType='" + rewardType + '\'' +
                    ", rewardTypeNo=" + rewardTypeNo +
                    ", memberNo='" + memberNo + '\'' +
                    ", receiveMemberNo='" + receiveMemberNo + '\'' +
                    ", rewardAmt=" + rewardAmt +
                    ", feeAmt=" + feeAmt +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "QrCodeResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

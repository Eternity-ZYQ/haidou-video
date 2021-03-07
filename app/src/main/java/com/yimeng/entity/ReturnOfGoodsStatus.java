package com.yimeng.entity;

import java.io.Serializable;

public class ReturnOfGoodsStatus implements Serializable{

    /**
     * status : 1
     * msg : 不存在的退货
     * data : {"refundState":"refund_defult","orderNo":null,"address":null,"shopName":null,"mobile":null,"productName":null,"remark":null,"logisticsType":null,"logisticsNo":null}
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

    public static class DataBean implements Serializable{

        /**
         * refundState 状态说明
         * refund_defult:默认无申请
         * refund:申请中
         * refund_complete:申请成功,待回邮
         * refund_express:已回邮
         * refund_refuse:退货失败
         **/
        private String refundState;
        private String remark;
        private String logisticsType;
        private String logisticsNo;

        public String getRefundState() {
            return refundState;
        }

        public void setRefundState(String refundState) {
            this.refundState = refundState;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getLogisticsType() {
            return logisticsType;
        }

        public void setLogisticsType(String logisticsType) {
            this.logisticsType = logisticsType;
        }

        public String getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(String logisticsNo) {
            this.logisticsNo = logisticsNo;
        }
    }
}

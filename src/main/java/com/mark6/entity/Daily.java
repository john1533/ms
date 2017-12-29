package com.mark6.entity;

/**
 * Created by John on 2017/12/20.
 */

public class Daily {
    /**
     * successful : true
     * msg : ok
     * data : {"date":"09/12/2017","id":"17/144","num1":"22","num2":"24","num3":"29","num4":"35","num5":"38","num6":"48","num7":"30","totalAmount":"53,979,761","amount1":"-","amount2":"$564,550","amount3":"$100,360","count1":"-","count2":"4.5","count3":"67.5","nextSerial":"17/145","nextDate":"12/12/2017","ext":"$37,406,204","expect":"$46,000,000"}
     */

    private boolean successful;
    private String msg;
    private DataBean data;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
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
         * date : 09/12/2017
         * id : 17/144
         * num1 : 22
         * num2 : 24
         * num3 : 29
         * num4 : 35
         * num5 : 38
         * num6 : 48
         * num7 : 30
         * totalAmount : 53,979,761
         * amount1 : -
         * amount2 : $564,550
         * amount3 : $100,360
         * count1 : -
         * count2 : 4.5
         * count3 : 67.5
         * nextSerial : 17/145
         * nextDate : 12/12/2017
         * ext : $37,406,204
         * expect : $46,000,000
         */

        private String date;
        private String id;
        private String num1;
        private String num2;
        private String num3;
        private String num4;
        private String num5;
        private String num6;
        private String num7;
        private String totalAmount;
        private String amount1;
        private String amount2;
        private String amount3;
        private String count1;
        private String count2;
        private String count3;
        private String nextSerial;
        private String nextDate;
        private String ext;
        private String expect;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNum1() {
            return num1;
        }

        public void setNum1(String num1) {
            this.num1 = num1;
        }

        public String getNum2() {
            return num2;
        }

        public void setNum2(String num2) {
            this.num2 = num2;
        }

        public String getNum3() {
            return num3;
        }

        public void setNum3(String num3) {
            this.num3 = num3;
        }

        public String getNum4() {
            return num4;
        }

        public void setNum4(String num4) {
            this.num4 = num4;
        }

        public String getNum5() {
            return num5;
        }

        public void setNum5(String num5) {
            this.num5 = num5;
        }

        public String getNum6() {
            return num6;
        }

        public void setNum6(String num6) {
            this.num6 = num6;
        }

        public String getNum7() {
            return num7;
        }

        public void setNum7(String num7) {
            this.num7 = num7;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getAmount1() {
            return amount1;
        }

        public void setAmount1(String amount1) {
            this.amount1 = amount1;
        }

        public String getAmount2() {
            return amount2;
        }

        public void setAmount2(String amount2) {
            this.amount2 = amount2;
        }

        public String getAmount3() {
            return amount3;
        }

        public void setAmount3(String amount3) {
            this.amount3 = amount3;
        }

        public String getCount1() {
            return count1;
        }

        public void setCount1(String count1) {
            this.count1 = count1;
        }

        public String getCount2() {
            return count2;
        }

        public void setCount2(String count2) {
            this.count2 = count2;
        }

        public String getCount3() {
            return count3;
        }

        public void setCount3(String count3) {
            this.count3 = count3;
        }

        public String getNextSerial() {
            return nextSerial;
        }

        public void setNextSerial(String nextSerial) {
            this.nextSerial = nextSerial;
        }

        public String getNextDate() {
            return nextDate;
        }

        public void setNextDate(String nextDate) {
            this.nextDate = nextDate;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getExpect() {
            return expect;
        }

        public void setExpect(String expect) {
            this.expect = expect;
        }
    }
}

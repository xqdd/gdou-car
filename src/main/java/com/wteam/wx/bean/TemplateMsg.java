package com.wteam.wx.bean;

public class TemplateMsg {


    private String touser;

    private String template_id;

    private String url;

    private Miniprogram miniprogram;

    private Data data;


    public class Miniprogram {
        private String appid;
        private String pagepath;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public void setPagepath(String pagepath) {
            this.pagepath = pagepath;
        }

        public Miniprogram(String appid, String pagepath) {
            this.appid = appid;
            this.pagepath = pagepath;
        }

        public Miniprogram() {
        }

        @Override
        public String toString() {
            return "Miniprogram{" +
                    "appid='" + appid + '\'' +
                    ", pagepath='" + pagepath + '\'' +
                    '}';
        }
    }



    public static class Data{
        private DataValue first;
        private DataValue keyword1;
        private DataValue keyword2;
        private DataValue keyword3;
        private DataValue keyword4;
        private DataValue keyword5;
        private DataValue keyword6;
        private DataValue remark;

        public DataValue getFirst() {
            return first;
        }

        public void setFirst(DataValue first) {
            this.first = first;
        }

        public DataValue getKeyword1() {
            return keyword1;
        }

        public void setKeyword1(DataValue keyword1) {
            this.keyword1 = keyword1;
        }

        public DataValue getKeyword2() {
            return keyword2;
        }

        public void setKeyword2(DataValue keyword2) {
            this.keyword2 = keyword2;
        }

        public DataValue getKeyword3() {
            return keyword3;
        }

        public void setKeyword3(DataValue keyword3) {
            this.keyword3 = keyword3;
        }

        public DataValue getKeyword4() {
            return keyword4;
        }

        public void setKeyword4(DataValue keyword4) {
            this.keyword4 = keyword4;
        }

        public DataValue getRemark() {
            return remark;
        }

        public void setRemark(DataValue remark) {
            this.remark = remark;
        }

        public DataValue getKeyword5() {
            return keyword5;
        }

        public void setKeyword5(DataValue keyword5) {
            this.keyword5 = keyword5;
        }

        public DataValue getKeyword6() {
            return keyword6;
        }

        public void setKeyword6(DataValue keyword6) {
            this.keyword6 = keyword6;
        }

        public Data() {
        }

        public Data(DataValue first, DataValue keyword1, DataValue keyword2, DataValue keyword3, DataValue keyword4, DataValue remark) {
            this.first = first;
            this.keyword1 = keyword1;
            this.keyword2 = keyword2;
            this.keyword3 = keyword3;
            this.keyword4 = keyword4;
            this.remark = remark;
        }

        public Data(DataValue first, DataValue keyword1, DataValue keyword2, DataValue keyword3, DataValue keyword4, DataValue keyword5, DataValue remark) {
            this.first = first;
            this.keyword1 = keyword1;
            this.keyword2 = keyword2;
            this.keyword3 = keyword3;
            this.keyword4 = keyword4;
            this.keyword5 = keyword5;
            this.remark = remark;
        }


        public Data(DataValue first, DataValue keyword1, DataValue keyword2, DataValue keyword3, DataValue keyword4, DataValue keyword5, DataValue keyword6, DataValue remark) {
            this.first = first;
            this.keyword1 = keyword1;
            this.keyword2 = keyword2;
            this.keyword3 = keyword3;
            this.keyword4 = keyword4;
            this.keyword5 = keyword5;
            this.keyword6 = keyword6;
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "first=" + first +
                    ", keyword1=" + keyword1 +
                    ", keyword2=" + keyword2 +
                    ", keyword3=" + keyword3 +
                    ", keyword4=" + keyword4 +
                    ", remark=" + remark +
                    '}';
        }
    }

    public static class DataValue{
        private String value;
        private String color;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public DataValue(String value) {
            this.value = value;
        }

        public DataValue(String value, String color) {
            this.value = value;
            this.color = color;
        }

        public DataValue() {
        }

        @Override
        public String toString() {
            return "DataValue{" +
                    "value='" + value + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Miniprogram getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(Miniprogram miniprogram) {
        this.miniprogram = miniprogram;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "TemplateMsg{" +
                "touser='" + touser + '\'' +
                ", template_id='" + template_id + '\'' +
                ", url='" + url + '\'' +
                ", miniprogram=" + miniprogram +
                ", data=" + data +
                '}';
    }
}

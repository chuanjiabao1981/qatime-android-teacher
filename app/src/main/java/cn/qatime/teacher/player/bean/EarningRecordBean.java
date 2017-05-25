package cn.qatime.teacher.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/5/25 10:26
 * @Description:
 */
public class EarningRecordBean {

    /**
     * status : 1
     * data : [{"id":16038,"amount":"0.36","change_type":"account","created_at":"2017-05-25T02:00:21.113+08:00","target_type":"一对一课程","target_id":166},{"id":16018,"amount":"68.71","change_type":"account","created_at":"2017-05-25T02:00:20.308+08:00","target_type":"直播课程","target_id":354},{"id":16001,"amount":"0.0","change_type":"account","created_at":"2017-05-24T02:00:21.408+08:00","target_type":"直播课程","target_id":359},{"id":15991,"amount":"0.0","change_type":"account","created_at":"2017-05-24T02:00:21.052+08:00","target_type":"直播课程","target_id":359},{"id":15971,"amount":"0.0","change_type":"account","created_at":"2017-05-24T02:00:20.442+08:00","target_type":"直播课程","target_id":359},{"id":15935,"amount":"4.41","change_type":"account","created_at":"2017-05-21T02:00:22.102+08:00","target_type":"一对一课程","target_id":162},{"id":15925,"amount":"12.33","change_type":"account","created_at":"2017-05-21T02:00:21.896+08:00","target_type":"直播课程","target_id":358},{"id":15905,"amount":"12.33","change_type":"account","created_at":"2017-05-21T02:00:21.613+08:00","target_type":"直播课程","target_id":358},{"id":15885,"amount":"12.33","change_type":"account","created_at":"2017-05-21T02:00:21.195+08:00","target_type":"直播课程","target_id":358},{"id":15875,"amount":"4.33","change_type":"account","created_at":"2017-05-21T02:00:20.925+08:00","target_type":"一对一课程","target_id":79},{"id":15845,"amount":"0.0","change_type":"account","created_at":"2017-05-20T02:00:21.312+08:00","target_type":"一对一课程","target_id":78},{"id":15835,"amount":"4.31","change_type":"account","created_at":"2017-05-20T02:00:21.000+08:00","target_type":"一对一课程","target_id":161},{"id":15798,"amount":"2.8","change_type":"account","created_at":"2017-05-19T02:00:21.443+08:00","target_type":"一对一课程","target_id":77},{"id":15788,"amount":"12.08","change_type":"account","created_at":"2017-05-19T02:00:20.378+08:00","target_type":"直播课程","target_id":357},{"id":15778,"amount":"12.08","change_type":"account","created_at":"2017-05-19T02:00:20.212+08:00","target_type":"直播课程","target_id":357},{"id":15768,"amount":"12.08","change_type":"account","created_at":"2017-05-19T02:00:19.999+08:00","target_type":"直播课程","target_id":357},{"id":15746,"amount":"1.34","change_type":"account","created_at":"2017-05-18T02:00:21.425+08:00","target_type":"一对一课程","target_id":76},{"id":15726,"amount":"12.42","change_type":"account","created_at":"2017-05-18T02:00:20.449+08:00","target_type":"直播课程","target_id":360},{"id":15716,"amount":"12.42","change_type":"account","created_at":"2017-05-18T02:00:20.291+08:00","target_type":"直播课程","target_id":360},{"id":15706,"amount":"12.42","change_type":"account","created_at":"2017-05-18T02:00:19.981+08:00","target_type":"直播课程","target_id":360},{"id":15686,"amount":"0.0","change_type":"account","created_at":"2017-05-17T02:00:21.532+08:00","target_type":"一对一课程","target_id":75},{"id":15656,"amount":"79.89","change_type":"account","created_at":"2017-05-16T15:38:45.082+08:00","target_type":"视频课","target_id":8},{"id":15618,"amount":"0.63","change_type":"account","created_at":"2017-05-15T02:00:19.731+08:00","target_type":"一对一课程","target_id":73},{"id":15608,"amount":"4.32","change_type":"account","created_at":"2017-05-14T02:00:20.429+08:00","target_type":"一对一课程","target_id":72},{"id":15555,"amount":"0.0","change_type":"account","created_at":"2017-05-11T15:17:36.034+08:00","target_type":"视频课","target_id":3},{"id":15517,"amount":"19.61","change_type":"account","created_at":"2017-05-07T02:00:19.909+08:00","target_type":"直播课程","target_id":344},{"id":15507,"amount":"19.61","change_type":"account","created_at":"2017-05-07T02:00:19.662+08:00","target_type":"直播课程","target_id":344},{"id":15491,"amount":"52.07","change_type":"account","created_at":"2017-05-05T15:34:30.275+08:00","target_type":"直播课程","target_id":343},{"id":15481,"amount":"52.07","change_type":"account","created_at":"2017-05-05T15:34:30.056+08:00","target_type":"直播课程","target_id":343},{"id":15467,"amount":"60.0","change_type":"account","created_at":"2017-05-05T15:06:54.198+08:00","target_type":"视频课","target_id":26}]
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 16038
         * amount : 0.36
         * change_type : account
         * created_at : 2017-05-25T02:00:21.113+08:00
         * target_type : 一对一课程
         * target_id : 166
         */

        private int id;
        private String amount;
        private String change_type;
        private String created_at;
        private String target_type;
        private int target_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getChange_type() {
            return change_type;
        }

        public void setChange_type(String change_type) {
            this.change_type = change_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public int getTarget_id() {
            return target_id;
        }

        public void setTarget_id(int target_id) {
            this.target_id = target_id;
        }
    }
}

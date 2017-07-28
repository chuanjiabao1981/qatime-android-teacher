package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/5/19 15:20
 * @Describe
 */

public class MyExclusiveCourseBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":1,"name":"测试专属课1","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"},"subject":"数学","grade":"高一","status":"published","teacher_name":"王志成","price":200,"current_price":200,"sell_type":"charge","view_tickets_count":0,"events_count":3,"closed_events_count":0,"start_at":null,"end_at":null,"objective":"测试专属课1","suit_crowd":"测试专属课1","description":"<p>测试专属课1测试专属课1<br><\/p>","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":false},"teacher_percentage":70}]
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
         * id : 1
         * name : 测试专属课1
         * publicizes_url : {"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"}
         * subject : 数学
         * grade : 高一
         * status : published
         * teacher_name : 王志成
         * price : 200
         * current_price : 200
         * sell_type : charge
         * view_tickets_count : 0
         * events_count : 3
         * closed_events_count : 0
         * start_at : null
         * end_at : null
         * objective : 测试专属课1
         * suit_crowd : 测试专属课1
         * description : <p>测试专属课1测试专属课1<br></p>
         * icons : {"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":false}
         * teacher_percentage : 70
         */

        private int id;
        private String name;
        private PublicizesUrlBean publicizes_url;
        private String subject;
        private String grade;
        private String status;
        private String teacher_name;
        private String price;
        private String current_price;
        private String sell_type;
        private int view_tickets_count;
        private int events_count;
        private int closed_events_count;
        private String start_at;
        private String end_at;
        private String objective;
        private String suit_crowd;
        private String description;
        private IconsBean icons;
        private int teacher_percentage;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PublicizesUrlBean getPublicizes_url() {
            return publicizes_url;
        }

        public void setPublicizes_url(PublicizesUrlBean publicizes_url) {
            this.publicizes_url = publicizes_url;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getSell_type() {
            return sell_type;
        }

        public void setSell_type(String sell_type) {
            this.sell_type = sell_type;
        }

        public int getView_tickets_count() {
            return view_tickets_count;
        }

        public void setView_tickets_count(int view_tickets_count) {
            this.view_tickets_count = view_tickets_count;
        }

        public int getEvents_count() {
            return events_count;
        }

        public void setEvents_count(int events_count) {
            this.events_count = events_count;
        }

        public int getClosed_events_count() {
            return closed_events_count;
        }

        public void setClosed_events_count(int closed_events_count) {
            this.closed_events_count = closed_events_count;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public String getObjective() {
            return objective;
        }

        public void setObjective(String objective) {
            this.objective = objective;
        }

        public String getSuit_crowd() {
            return suit_crowd;
        }

        public void setSuit_crowd(String suit_crowd) {
            this.suit_crowd = suit_crowd;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public IconsBean getIcons() {
            return icons;
        }

        public void setIcons(IconsBean icons) {
            this.icons = icons;
        }

        public int getTeacher_percentage() {
            return teacher_percentage;
        }

        public void setTeacher_percentage(int teacher_percentage) {
            this.teacher_percentage = teacher_percentage;
        }

        public static class PublicizesUrlBean {
            /**
             * app_info : http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png
             * list : http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png
             * info : http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png
             */

            private String app_info;
            private String list;
            private String info;

            public String getApp_info() {
                return app_info;
            }

            public void setApp_info(String app_info) {
                this.app_info = app_info;
            }

            public String getList() {
                return list;
            }

            public void setList(String list) {
                this.list = list;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }

        public static class IconsBean {
            /**
             * refund_any_time : true
             * coupon_free : true
             * cheap_moment : false
             * join_cheap : false
             * free_taste : false
             */

            private boolean refund_any_time;
            private boolean coupon_free;
            private boolean cheap_moment;
            private boolean join_cheap;
            private boolean free_taste;

            public boolean isRefund_any_time() {
                return refund_any_time;
            }

            public void setRefund_any_time(boolean refund_any_time) {
                this.refund_any_time = refund_any_time;
            }

            public boolean isCoupon_free() {
                return coupon_free;
            }

            public void setCoupon_free(boolean coupon_free) {
                this.coupon_free = coupon_free;
            }

            public boolean isCheap_moment() {
                return cheap_moment;
            }

            public void setCheap_moment(boolean cheap_moment) {
                this.cheap_moment = cheap_moment;
            }

            public boolean isJoin_cheap() {
                return join_cheap;
            }

            public void setJoin_cheap(boolean join_cheap) {
                this.join_cheap = join_cheap;
            }

            public boolean isFree_taste() {
                return free_taste;
            }

            public void setFree_taste(boolean free_taste) {
                this.free_taste = free_taste;
            }
        }
    }
}

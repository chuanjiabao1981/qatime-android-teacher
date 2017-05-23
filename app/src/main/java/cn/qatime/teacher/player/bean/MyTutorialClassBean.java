package cn.qatime.teacher.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/5/19 15:20
 * @Describe
 */

public class MyTutorialClassBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":61,"name":"再来一个辅导班","subject":"化学","grade":"高二","teacher_name":"王志成","price":10,"current_price":10,"chat_team_id":"","chat_team_owner":"","buy_tickets_count":5,"status":"published","preset_lesson_count":1,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"closed_lessons_count":0,"started_lessons_count":0,"live_start_time":"2016-11-22 09:00","live_end_time":"2016-11-22 10:00","objective":null,"suit_crowd":null,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b876ab65a61aaa1ae47597ef144b9f45.png","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":false},"off_shelve":false,"push_address":"rtmp://pa0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419?wsSecret=a3c84d0ecfdeb7434ffaa534607b9e8f&wsTime=1471330308","board_push_stream":"rtmp://pa0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419?wsSecret=a3c84d0ecfdeb7434ffaa534607b9e8f&wsTime=1471330308","camera_push_stream":"rtmp://pa0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97?wsSecret=74c57894244754864cd0f07cc25ba4be&wsTime=1477448251","board":"rtmp://pa0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419?wsSecret=a3c84d0ecfdeb7434ffaa534607b9e8f&wsTime=1471330308","camera":"rtmp://pa0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97?wsSecret=74c57894244754864cd0f07cc25ba4be&wsTime=1477448251"}]
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
         * id : 61
         * name : 再来一个辅导班
         * subject : 化学
         * grade : 高二
         * teacher_name : 王志成
         * price : 10
         * current_price : 10
         * chat_team_id :
         * chat_team_owner :
         * buy_tickets_count : 5
         * status : published
         * preset_lesson_count : 1
         * completed_lesson_count : 0
         * taste_count : 0
         * completed_lessons_count : 0
         * closed_lessons_count : 0
         * started_lessons_count : 0
         * live_start_time : 2016-11-22 09:00
         * live_end_time : 2016-11-22 10:00
         * objective : null
         * suit_crowd : null
         * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b876ab65a61aaa1ae47597ef144b9f45.png
         * icons : {"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":false}
         * off_shelve : false
         * push_address : rtmp://pa0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419?wsSecret=a3c84d0ecfdeb7434ffaa534607b9e8f&wsTime=1471330308
         * board_push_stream : rtmp://pa0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419?wsSecret=a3c84d0ecfdeb7434ffaa534607b9e8f&wsTime=1471330308
         * camera_push_stream : rtmp://pa0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97?wsSecret=74c57894244754864cd0f07cc25ba4be&wsTime=1477448251
         * board : rtmp://pa0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419?wsSecret=a3c84d0ecfdeb7434ffaa534607b9e8f&wsTime=1471330308
         * camera : rtmp://pa0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97?wsSecret=74c57894244754864cd0f07cc25ba4be&wsTime=1477448251
         */

        private int id;
        private String name;
        private String subject;
        private String grade;
        private String teacher_name;
        private int price;
        private double current_price;
        private String chat_team_id;
        private String chat_team_owner;
        private int buy_tickets_count;
        private String status;
        private int preset_lesson_count;
        private int completed_lesson_count;
        private int taste_count;
        private int completed_lessons_count;
        private int closed_lessons_count;
        private int started_lessons_count;
        private String live_start_time;
        private String live_end_time;
        private Object objective;
        private Object suit_crowd;
        private String publicize;
        private IconsBean icons;
        private boolean off_shelve;
        private String push_address;
        private String board_push_stream;
        private String camera_push_stream;
        private String board;
        private String camera;

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

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public double getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(double current_price) {
            this.current_price = current_price;
        }

        public String getChat_team_id() {
            return chat_team_id;
        }

        public void setChat_team_id(String chat_team_id) {
            this.chat_team_id = chat_team_id;
        }

        public String getChat_team_owner() {
            return chat_team_owner;
        }

        public void setChat_team_owner(String chat_team_owner) {
            this.chat_team_owner = chat_team_owner;
        }

        public int getBuy_tickets_count() {
            return buy_tickets_count;
        }

        public void setBuy_tickets_count(int buy_tickets_count) {
            this.buy_tickets_count = buy_tickets_count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getPreset_lesson_count() {
            return preset_lesson_count;
        }

        public void setPreset_lesson_count(int preset_lesson_count) {
            this.preset_lesson_count = preset_lesson_count;
        }

        public int getCompleted_lesson_count() {
            return completed_lesson_count;
        }

        public void setCompleted_lesson_count(int completed_lesson_count) {
            this.completed_lesson_count = completed_lesson_count;
        }

        public int getTaste_count() {
            return taste_count;
        }

        public void setTaste_count(int taste_count) {
            this.taste_count = taste_count;
        }

        public int getCompleted_lessons_count() {
            return completed_lessons_count;
        }

        public void setCompleted_lessons_count(int completed_lessons_count) {
            this.completed_lessons_count = completed_lessons_count;
        }

        public int getClosed_lessons_count() {
            return closed_lessons_count;
        }

        public void setClosed_lessons_count(int closed_lessons_count) {
            this.closed_lessons_count = closed_lessons_count;
        }

        public int getStarted_lessons_count() {
            return started_lessons_count;
        }

        public void setStarted_lessons_count(int started_lessons_count) {
            this.started_lessons_count = started_lessons_count;
        }

        public String getLive_start_time() {
            return live_start_time;
        }

        public void setLive_start_time(String live_start_time) {
            this.live_start_time = live_start_time;
        }

        public String getLive_end_time() {
            return live_end_time;
        }

        public void setLive_end_time(String live_end_time) {
            this.live_end_time = live_end_time;
        }

        public Object getObjective() {
            return objective;
        }

        public void setObjective(Object objective) {
            this.objective = objective;
        }

        public Object getSuit_crowd() {
            return suit_crowd;
        }

        public void setSuit_crowd(Object suit_crowd) {
            this.suit_crowd = suit_crowd;
        }

        public String getPublicize() {
            return publicize;
        }

        public void setPublicize(String publicize) {
            this.publicize = publicize;
        }

        public IconsBean getIcons() {
            return icons;
        }

        public void setIcons(IconsBean icons) {
            this.icons = icons;
        }

        public boolean isOff_shelve() {
            return off_shelve;
        }

        public void setOff_shelve(boolean off_shelve) {
            this.off_shelve = off_shelve;
        }

        public String getPush_address() {
            return push_address;
        }

        public void setPush_address(String push_address) {
            this.push_address = push_address;
        }

        public String getBoard_push_stream() {
            return board_push_stream;
        }

        public void setBoard_push_stream(String board_push_stream) {
            this.board_push_stream = board_push_stream;
        }

        public String getCamera_push_stream() {
            return camera_push_stream;
        }

        public void setCamera_push_stream(String camera_push_stream) {
            this.camera_push_stream = camera_push_stream;
        }

        public String getBoard() {
            return board;
        }

        public void setBoard(String board) {
            this.board = board;
        }

        public String getCamera() {
            return camera;
        }

        public void setCamera(String camera) {
            this.camera = camera;
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

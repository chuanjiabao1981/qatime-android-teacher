package cn.qatime.player.bean;

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
     * data : [{"id":117,"name":"五月十三直播课","subject":"数学","grade":"高一","teacher_name":"王志成","price":100,"current_price":0,"taste_count":3,"live_start_time":"2017-05-16 16:57","live_end_time":"2017-05-23 06:05","teacher_percentage":50,"publicize":"http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png"}]
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
         * id : 117
         * name : 五月十三直播课
         * subject : 数学
         * grade : 高一
         * teacher_name : 王志成
         * price : 100
         * current_price : 0
         * taste_count : 3
         * live_start_time : 2017-05-16 16:57
         * live_end_time : 2017-05-23 06:05
         * teacher_percentage : 50
         * publicize : http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png
         */

        private int id;
        private int buy_tickets_count;
        private int preset_lesson_count;
        private int completed_lesson_count;
        private String name;
        private String subject;
        private String grade;
        private String teacher_name;
        private double price;
        private double current_price;
        private int taste_count;
        private String live_start_time;
        private String live_end_time;
        private int teacher_percentage;
        private String publicize;

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

        public int getBuy_tickets_count() {
            return buy_tickets_count;
        }

        public void setBuy_tickets_count(int buy_tickets_count) {
            this.buy_tickets_count = buy_tickets_count;
        }

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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(double current_price) {
            this.current_price = current_price;
        }

        public int getTaste_count() {
            return taste_count;
        }

        public void setTaste_count(int taste_count) {
            this.taste_count = taste_count;
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

        public int getTeacher_percentage() {
            return teacher_percentage;
        }

        public void setTeacher_percentage(int teacher_percentage) {
            this.teacher_percentage = teacher_percentage;
        }

        public String getPublicize() {
            return publicize;
        }

        public void setPublicize(String publicize) {
            this.publicize = publicize;
        }
    }
}

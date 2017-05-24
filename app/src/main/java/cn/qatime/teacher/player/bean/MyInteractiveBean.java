package cn.qatime.teacher.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/5/24 11:13
 * @Describe
 */

public class MyInteractiveBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":17,"name":"移动端测试","subject":"生物","grade":"高三","price":"91.0","lessons_count":10,"live_start_time":"2017-05-18 16:44","teacher_percentage":50,"publicize_list_url":"http://testing.qatime.cn/assets/interactive_courses/list_default-a8c48d575db6899ee0af76dc21a3fddb.png"}]
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
         * id : 17
         * name : 移动端测试
         * subject : 生物
         * grade : 高三
         * price : 91.0
         * lessons_count : 10
         * live_start_time : 2017-05-18 16:44
         * teacher_percentage : 50
         * publicize_list_url : http://testing.qatime.cn/assets/interactive_courses/list_default-a8c48d575db6899ee0af76dc21a3fddb.png
         */

        private int id;
        private String name;
        private String subject;
        private String grade;
        private String price;
        private int lessons_count;
        private String live_start_time;
        private int teacher_percentage;
        private String publicize_list_url;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getLessons_count() {
            return lessons_count;
        }

        public void setLessons_count(int lessons_count) {
            this.lessons_count = lessons_count;
        }

        public String getLive_start_time() {
            return live_start_time;
        }

        public void setLive_start_time(String live_start_time) {
            this.live_start_time = live_start_time;
        }

        public int getTeacher_percentage() {
            return teacher_percentage;
        }

        public void setTeacher_percentage(int teacher_percentage) {
            this.teacher_percentage = teacher_percentage;
        }

        public String getPublicize_list_url() {
            return publicize_list_url;
        }

        public void setPublicize_list_url(String publicize_list_url) {
            this.publicize_list_url = publicize_list_url;
        }
    }
}

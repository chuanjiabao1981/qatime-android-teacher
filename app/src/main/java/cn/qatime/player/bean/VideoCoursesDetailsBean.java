package cn.qatime.player.bean;

import java.util.List;

import libraryextra.bean.IconsBean;
import libraryextra.bean.TeacherBean;
import libraryextra.bean.VideoLessonsBean;

/**
 * @author Tianhaoranly
 * @date 2017/5/17 17:18
 * @Description:
 */
public class VideoCoursesDetailsBean {


    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private VideoCourseBean video_course;
        private Object ticket;

        public VideoCourseBean getVideo_course() {
            return video_course;
        }

        public void setVideo_course(VideoCourseBean video_course) {
            this.video_course = video_course;
        }

        public Object getTicket() {
            return ticket;
        }

        public void setTicket(Object ticket) {
            this.ticket = ticket;
        }

        public static class VideoCourseBean {

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private String publicize;
            private TeacherBean teacher;
            private String price;
            private int buy_tickets_count;
            private String status;
            private String description;
            private int video_lessons_count;
            private int taste_count;
            private String objective;
            private String suit_crowd;
            private String sell_type;
            private int total_duration;
            private IconsBean icons;
            private List<String> tag_list;
            private List<VideoLessonsBean> video_lessons;

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

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }

            public TeacherBean getTeacher() {
                return teacher;
            }

            public void setTeacher(TeacherBean teacher) {
                this.teacher = teacher;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getVideo_lessons_count() {
                return video_lessons_count;
            }

            public void setVideo_lessons_count(int video_lessons_count) {
                this.video_lessons_count = video_lessons_count;
            }

            public int getTaste_count() {
                return taste_count;
            }

            public void setTaste_count(int taste_count) {
                this.taste_count = taste_count;
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

            public String getSell_type() {
                return sell_type;
            }

            public void setSell_type(String sell_type) {
                this.sell_type = sell_type;
            }

            public int getTotal_duration() {
                return total_duration;
            }

            public void setTotal_duration(int total_duration) {
                this.total_duration = total_duration;
            }

            public IconsBean getIcons() {
                return icons;
            }

            public void setIcons(IconsBean icons) {
                this.icons = icons;
            }

            public List<String> getTag_list() {
                return tag_list;
            }

            public void setTag_list(List<String> tag_list) {
                this.tag_list = tag_list;
            }

            public List<VideoLessonsBean> getVideo_lessons() {
                return video_lessons;
            }

            public void setVideo_lessons(List<VideoLessonsBean> video_lessons) {
                this.video_lessons = video_lessons;
            }

        }

    }
}

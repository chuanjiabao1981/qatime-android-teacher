package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/26 16:23
 * @Description
 */
public class ClassTimeTableBean implements Serializable {
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

        private String date;
        private List<LessonsBean> lessons;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<LessonsBean> getLessons() {
            return lessons;
        }

        public void setLessons(List<LessonsBean> lessons) {
            this.lessons = lessons;
        }

        public static class LessonsBean {

            private String board;
            private String board_pull_stream;
            private String camera;
            private String camera_pull_stream;
            private String chat_team_id;
            private String class_date;
            private String course_id;
            private String course_name;
            private String course_publicize;
            private String grade;
            private int id;
            private int left_replay_times;
            private String lesson_name;
            private String live_time;
            private String modal_type;
            private String name;
            private int product_id;
            private String pull_address;
            private boolean replayable;
            private String status;
            private String subject;
            private String teacher_name;

            public String getBoard() {
                return board;
            }

            public void setBoard(String board) {
                this.board = board;
            }

            public String getBoard_pull_stream() {
                return board_pull_stream;
            }

            public void setBoard_pull_stream(String board_pull_stream) {
                this.board_pull_stream = board_pull_stream;
            }

            public String getCamera() {
                return camera;
            }

            public void setCamera(String camera) {
                this.camera = camera;
            }

            public String getCamera_pull_stream() {
                return camera_pull_stream;
            }

            public void setCamera_pull_stream(String camera_pull_stream) {
                this.camera_pull_stream = camera_pull_stream;
            }

            public String getChat_team_id() {
                return chat_team_id;
            }

            public void setChat_team_id(String chat_team_id) {
                this.chat_team_id = chat_team_id;
            }

            public String getClass_date() {
                return class_date;
            }

            public void setClass_date(String class_date) {
                this.class_date = class_date;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public String getCourse_name() {
                return course_name;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public String getCourse_publicize() {
                return course_publicize;
            }

            public void setCourse_publicize(String course_publicize) {
                this.course_publicize = course_publicize;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLeft_replay_times() {
                return left_replay_times;
            }

            public void setLeft_replay_times(int left_replay_times) {
                this.left_replay_times = left_replay_times;
            }

            public String getLesson_name() {
                return lesson_name;
            }

            public void setLesson_name(String lesson_name) {
                this.lesson_name = lesson_name;
            }

            public String getLive_time() {
                return live_time;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public String getModal_type() {
                return modal_type;
            }

            public void setModal_type(String modal_type) {
                this.modal_type = modal_type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }


            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getPull_address() {
                return pull_address;
            }

            public void setPull_address(String pull_address) {
                this.pull_address = pull_address;
            }

            public boolean isReplayable() {
                return replayable;
            }

            public void setReplayable(boolean replayable) {
                this.replayable = replayable;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }
        }
    }
}

package cn.qatime.player.bean;

import java.util.List;

import libraryextra.bean.ChatTeamBean;

/**
 * @author Tianhaoranly
 * @date 2017/5/17 16:47
 * @Description:
 */
public class MyVideoBean {


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

        private int buy_tickets_count;
        private ChatTeamBean chat_team;
        private int closed_lessons_count;
        private int completed_lesson_count;
        private int completed_lessons_count;
        private String current_price;
        private String description;
        private String grade;
        private IconsBean icons;
        private int id;
        private int lesson_count;
        private String name;
        private String objective;
        private int preset_lesson_count;
        private String price;
        private String publicize;
        private String sell_type;
        private String status;
        private String subject;
        private String suit_crowd;
        private String teacher_percentage;
        private int taste_count;
        private TeacherBean teacher;
        private String teacher_name;
        private int total_duration;
        private int video_lessons_count;
        private List<String> tag_list;
        private List<VideoLessonsBean> video_lessons;

        public String getTeacher_percentage() {
            return teacher_percentage;
        }

        public void setTeacher_percentage(String teacher_percentage) {
            this.teacher_percentage = teacher_percentage;
        }

        public int getBuy_tickets_count() {
            return buy_tickets_count;
        }

        public void setBuy_tickets_count(int buy_tickets_count) {
            this.buy_tickets_count = buy_tickets_count;
        }

        public ChatTeamBean getChat_team() {
            return chat_team;
        }

        public void setChat_team(ChatTeamBean chat_team) {
            this.chat_team = chat_team;
        }

        public int getClosed_lessons_count() {
            return closed_lessons_count;
        }

        public void setClosed_lessons_count(int closed_lessons_count) {
            this.closed_lessons_count = closed_lessons_count;
        }

        public int getCompleted_lesson_count() {
            return completed_lesson_count;
        }

        public void setCompleted_lesson_count(int completed_lesson_count) {
            this.completed_lesson_count = completed_lesson_count;
        }

        public int getCompleted_lessons_count() {
            return completed_lessons_count;
        }

        public void setCompleted_lessons_count(int completed_lessons_count) {
            this.completed_lessons_count = completed_lessons_count;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public IconsBean getIcons() {
            return icons;
        }

        public void setIcons(IconsBean icons) {
            this.icons = icons;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLesson_count() {
            return lesson_count;
        }

        public void setLesson_count(int lesson_count) {
            this.lesson_count = lesson_count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getObjective() {
            return objective;
        }

        public void setObjective(String objective) {
            this.objective = objective;
        }

        public int getPreset_lesson_count() {
            return preset_lesson_count;
        }

        public void setPreset_lesson_count(int preset_lesson_count) {
            this.preset_lesson_count = preset_lesson_count;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPublicize() {
            return publicize;
        }

        public void setPublicize(String publicize) {
            this.publicize = publicize;
        }

        public String getSell_type() {
            return sell_type;
        }

        public void setSell_type(String sell_type) {
            this.sell_type = sell_type;
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

        public String getSuit_crowd() {
            return suit_crowd;
        }

        public void setSuit_crowd(String suit_crowd) {
            this.suit_crowd = suit_crowd;
        }

        public int getTaste_count() {
            return taste_count;
        }

        public void setTaste_count(int taste_count) {
            this.taste_count = taste_count;
        }

        public TeacherBean getTeacher() {
            return teacher;
        }

        public void setTeacher(TeacherBean teacher) {
            this.teacher = teacher;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public int getTotal_duration() {
            return total_duration;
        }

        public void setTotal_duration(int total_duration) {
            this.total_duration = total_duration;
        }

        public int getVideo_lessons_count() {
            return video_lessons_count;
        }

        public void setVideo_lessons_count(int video_lessons_count) {
            this.video_lessons_count = video_lessons_count;
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


        public static class IconsBean {

            private boolean cheap_moment;
            private boolean coupon_free;
            private boolean free_taste;

            public boolean isCheap_moment() {
                return cheap_moment;
            }

            public void setCheap_moment(boolean cheap_moment) {
                this.cheap_moment = cheap_moment;
            }

            public boolean isCoupon_free() {
                return coupon_free;
            }

            public void setCoupon_free(boolean coupon_free) {
                this.coupon_free = coupon_free;
            }

            public boolean isFree_taste() {
                return free_taste;
            }

            public void setFree_taste(boolean free_taste) {
                this.free_taste = free_taste;
            }
        }

        public static class TeacherBean {

            private String avatar_url;
            private String birthday;
            private String category;
            private int city;
            private String desc;
            private String email;
            private String ex_big_avatar_url;
            private String gender;
            private int id;
            private String login_mobile;
            private String name;
            private String nick_name;
            private int province;
            private int school;
            private int school_id;
            private String subject;
            private String teaching_years;
            private List<String> grade_range;

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public int getCity() {
                return city;
            }

            public void setCity(int city) {
                this.city = city;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getEx_big_avatar_url() {
                return ex_big_avatar_url;
            }

            public void setEx_big_avatar_url(String ex_big_avatar_url) {
                this.ex_big_avatar_url = ex_big_avatar_url;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLogin_mobile() {
                return login_mobile;
            }

            public void setLogin_mobile(String login_mobile) {
                this.login_mobile = login_mobile;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public int getProvince() {
                return province;
            }

            public void setProvince(int province) {
                this.province = province;
            }

            public int getSchool() {
                return school;
            }

            public void setSchool(int school) {
                this.school = school;
            }

            public int getSchool_id() {
                return school_id;
            }

            public void setSchool_id(int school_id) {
                this.school_id = school_id;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getTeaching_years() {
                return teaching_years;
            }

            public void setTeaching_years(String teaching_years) {
                this.teaching_years = teaching_years;
            }

            public List<String> getGrade_range() {
                return grade_range;
            }

            public void setGrade_range(List<String> grade_range) {
                this.grade_range = grade_range;
            }
        }

        public static class VideoLessonsBean {

            private int id;
            private String name;
            private int pos;
            private int real_time;
            private String status;
            private boolean tastable;
            private VideoBean video;
            private int video_course_id;

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

            public int getPos() {
                return pos;
            }

            public void setPos(int pos) {
                this.pos = pos;
            }

            public int getReal_time() {
                return real_time;
            }

            public void setReal_time(int real_time) {
                this.real_time = real_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public boolean isTastable() {
                return tastable;
            }

            public void setTastable(boolean tastable) {
                this.tastable = tastable;
            }

            public VideoBean getVideo() {
                return video;
            }

            public void setVideo(VideoBean video) {
                this.video = video;
            }

            public int getVideo_course_id() {
                return video_course_id;
            }

            public void setVideo_course_id(int video_course_id) {
                this.video_course_id = video_course_id;
            }

            public static class VideoBean {
                /**
                 * capture : http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b52302e0bb7426234aedd2b865f6f442.jpg
                 * duration : 2
                 * format_tmp_duration : 00:00:02
                 * id : 7827
                 * name_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/6fd2a77380df1551ab2edc58cf173bb3.mp4
                 * tmp_duration : 2
                 * token : 1493709746400
                 * video_type : mp4
                 */

                private String capture;
                private int duration;
                private String format_tmp_duration;
                private int id;
                private String name_url;
                private int tmp_duration;
                private String token;
                private String video_type;

                public String getCapture() {
                    return capture;
                }

                public void setCapture(String capture) {
                    this.capture = capture;
                }

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public String getFormat_tmp_duration() {
                    return format_tmp_duration;
                }

                public void setFormat_tmp_duration(String format_tmp_duration) {
                    this.format_tmp_duration = format_tmp_duration;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName_url() {
                    return name_url;
                }

                public void setName_url(String name_url) {
                    this.name_url = name_url;
                }

                public int getTmp_duration() {
                    return tmp_duration;
                }

                public void setTmp_duration(int tmp_duration) {
                    this.tmp_duration = tmp_duration;
                }

                public String getToken() {
                    return token;
                }

                public void setToken(String token) {
                    this.token = token;
                }

                public String getVideo_type() {
                    return video_type;
                }

                public void setVideo_type(String video_type) {
                    this.video_type = video_type;
                }
            }
        }
    }
}

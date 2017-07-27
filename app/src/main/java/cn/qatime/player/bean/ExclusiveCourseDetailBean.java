package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

public class ExclusiveCourseDetailBean implements Serializable {

    /**
     * status : 1
     * data : {"customized_group":{"id":1,"name":"测试专属课1","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"},"subject":"数学","grade":"高一","status":"published","teacher_name":"王志成","price":200,"current_price":200,"view_tickets_count":0,"events_count":3,"closed_events_count":0,"start_at":null,"end_at":null,"objective":"测试专属课1","suit_crowd":"测试专属课1","description":"<p>测试专属课1测试专属课1<br><\/p>","teacher":{"id":2489,"name":"王志成","nick_name":"luke测试","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/a9df861997945a0086ad8483d7039f2c.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_a9df861997945a0086ad8483d7039f2c.jpg","login_mobile":"13121249326","email":"wchtest001@163.com","is_guest":false,"teaching_years":"within_three_years","category":"初中","subject":"政治","grade_range":["二年级","三年级","四年级","五年级","六年级",""],"gender":"male","birthday":"1991-06-18","province":1,"city":1,"school":17,"school_name":"阳泉实验中学","school_id":17,"desc":"注意啦"},"offline_lessons":[{"id":3,"name":"公开课","class_date":"2017-07-29","start_time":"05:10","end_time":"06:10","status":"init","class_address":"一号楼"}],"scheduled_lessons":[{"id":2,"name":"第二季","class_date":"2017-07-28","start_time":"15:35","end_time":"16:20","status":"init"},{"id":1,"name":"第一节","class_date":"2017-07-27","start_time":"13:30","end_time":"14:15","status":"init"}]}}
     */

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
        /**
         * customized_group : {"id":1,"name":"测试专属课1","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"},"subject":"数学","grade":"高一","status":"published","teacher_name":"王志成","price":200,"current_price":200,"view_tickets_count":0,"events_count":3,"closed_events_count":0,"start_at":null,"end_at":null,"objective":"测试专属课1","suit_crowd":"测试专属课1","description":"<p>测试专属课1测试专属课1<br><\/p>","teacher":{"id":2489,"name":"王志成","nick_name":"luke测试","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/a9df861997945a0086ad8483d7039f2c.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_a9df861997945a0086ad8483d7039f2c.jpg","login_mobile":"13121249326","email":"wchtest001@163.com","is_guest":false,"teaching_years":"within_three_years","category":"初中","subject":"政治","grade_range":["二年级","三年级","四年级","五年级","六年级",""],"gender":"male","birthday":"1991-06-18","province":1,"city":1,"school":17,"school_name":"阳泉实验中学","school_id":17,"desc":"注意啦"},"offline_lessons":[{"id":3,"name":"公开课","class_date":"2017-07-29","start_time":"05:10","end_time":"06:10","status":"init","class_address":"一号楼"}],"scheduled_lessons":[{"id":2,"name":"第二季","class_date":"2017-07-28","start_time":"15:35","end_time":"16:20","status":"init"},{"id":1,"name":"第一节","class_date":"2017-07-27","start_time":"13:30","end_time":"14:15","status":"init"}]}
         */

        private CustomizedGroupBean customized_group;

        public CustomizedGroupBean getCustomized_group() {
            return customized_group;
        }

        public void setCustomized_group(CustomizedGroupBean customized_group) {
            this.customized_group = customized_group;
        }

        public static class CustomizedGroupBean {
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
             * view_tickets_count : 0
             * events_count : 3
             * closed_events_count : 0
             * start_at : null
             * end_at : null
             * objective : 测试专属课1
             * suit_crowd : 测试专属课1
             * description : <p>测试专属课1测试专属课1<br></p>
             * teacher : {"id":2489,"name":"王志成","nick_name":"luke测试","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/a9df861997945a0086ad8483d7039f2c.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_a9df861997945a0086ad8483d7039f2c.jpg","login_mobile":"13121249326","email":"wchtest001@163.com","is_guest":false,"teaching_years":"within_three_years","category":"初中","subject":"政治","grade_range":["二年级","三年级","四年级","五年级","六年级",""],"gender":"male","birthday":"1991-06-18","province":1,"city":1,"school":17,"school_name":"阳泉实验中学","school_id":17,"desc":"注意啦"}
             * offline_lessons : [{"id":3,"name":"公开课","class_date":"2017-07-29","start_time":"05:10","end_time":"06:10","status":"init","class_address":"一号楼"}]
             * scheduled_lessons : [{"id":2,"name":"第二季","class_date":"2017-07-28","start_time":"15:35","end_time":"16:20","status":"init"},{"id":1,"name":"第一节","class_date":"2017-07-27","start_time":"13:30","end_time":"14:15","status":"init"}]
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
            private int view_tickets_count;
            private int events_count;
            private int closed_events_count;
            private String start_at;
            private String end_at;
            private String objective;
            private String suit_crowd;
            private String sell_type;
            private String description;
            private TeacherBean teacher;
            private List<OfflineLessonsBean> offline_lessons;
            private List<ScheduledLessonsBean> scheduled_lessons;
            private IconsBean icons;

            public IconsBean getIcons() {
                return icons;
            }

            public void setIcons(IconsBean icons) {
                this.icons = icons;
            }

            public String getSell_type() {
                return sell_type;
            }

            public void setSell_type(String sell_type) {
                this.sell_type = sell_type;
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

            public TeacherBean getTeacher() {
                return teacher;
            }

            public void setTeacher(TeacherBean teacher) {
                this.teacher = teacher;
            }

            public List<OfflineLessonsBean> getOffline_lessons() {
                return offline_lessons;
            }

            public void setOffline_lessons(List<OfflineLessonsBean> offline_lessons) {
                this.offline_lessons = offline_lessons;
            }

            public List<ScheduledLessonsBean> getScheduled_lessons() {
                return scheduled_lessons;
            }

            public void setScheduled_lessons(List<ScheduledLessonsBean> scheduled_lessons) {
                this.scheduled_lessons = scheduled_lessons;
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

            public static class TeacherBean {
                /**
                 * id : 2489
                 * name : 王志成
                 * nick_name : luke测试
                 * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/a9df861997945a0086ad8483d7039f2c.jpg
                 * ex_big_avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_a9df861997945a0086ad8483d7039f2c.jpg
                 * login_mobile : 13121249326
                 * email : wchtest001@163.com
                 * is_guest : false
                 * teaching_years : within_three_years
                 * category : 初中
                 * subject : 政治
                 * grade_range : ["二年级","三年级","四年级","五年级","六年级",""]
                 * gender : male
                 * birthday : 1991-06-18
                 * province : 1
                 * city : 1
                 * school : 17
                 * school_name : 阳泉实验中学
                 * school_id : 17
                 * desc : 注意啦
                 */

                private int id;
                private String name;
                private String nick_name;
                private String avatar_url;
                private String ex_big_avatar_url;
                private String login_mobile;
                private String email;
                private boolean is_guest;
                private String teaching_years;
                private String category;
                private String subject;
                private String gender;
                private String birthday;
                private int province;
                private int city;
                private int school;
                private String school_name;
                private int school_id;
                private String desc;
                private List<String> grade_range;

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

                public String getNick_name() {
                    return nick_name;
                }

                public void setNick_name(String nick_name) {
                    this.nick_name = nick_name;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public String getEx_big_avatar_url() {
                    return ex_big_avatar_url;
                }

                public void setEx_big_avatar_url(String ex_big_avatar_url) {
                    this.ex_big_avatar_url = ex_big_avatar_url;
                }

                public String getLogin_mobile() {
                    return login_mobile;
                }

                public void setLogin_mobile(String login_mobile) {
                    this.login_mobile = login_mobile;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public boolean isIs_guest() {
                    return is_guest;
                }

                public void setIs_guest(boolean is_guest) {
                    this.is_guest = is_guest;
                }

                public String getTeaching_years() {
                    return teaching_years;
                }

                public void setTeaching_years(String teaching_years) {
                    this.teaching_years = teaching_years;
                }

                public String getCategory() {
                    return category;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public String getSubject() {
                    return subject;
                }

                public void setSubject(String subject) {
                    this.subject = subject;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }

                public int getProvince() {
                    return province;
                }

                public void setProvince(int province) {
                    this.province = province;
                }

                public int getCity() {
                    return city;
                }

                public void setCity(int city) {
                    this.city = city;
                }

                public int getSchool() {
                    return school;
                }

                public void setSchool(int school) {
                    this.school = school;
                }

                public String getSchool_name() {
                    return school_name;
                }

                public void setSchool_name(String school_name) {
                    this.school_name = school_name;
                }

                public int getSchool_id() {
                    return school_id;
                }

                public void setSchool_id(int school_id) {
                    this.school_id = school_id;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public List<String> getGrade_range() {
                    return grade_range;
                }

                public void setGrade_range(List<String> grade_range) {
                    this.grade_range = grade_range;
                }
            }

            public static class OfflineLessonsBean {
                /**
                 * id : 3
                 * name : 公开课
                 * class_date : 2017-07-29
                 * start_time : 05:10
                 * end_time : 06:10
                 * status : init
                 * class_address : 一号楼
                 */

                private int id;
                private String name;
                private String class_date;
                private String start_time;
                private String end_time;
                private String status;
                private String class_address;

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

                public String getClass_date() {
                    return class_date;
                }

                public void setClass_date(String class_date) {
                    this.class_date = class_date;
                }

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getClass_address() {
                    return class_address;
                }

                public void setClass_address(String class_address) {
                    this.class_address = class_address;
                }
            }

            public static class ScheduledLessonsBean {
                /**
                 * id : 2
                 * name : 第二季
                 * class_date : 2017-07-28
                 * start_time : 15:35
                 * end_time : 16:20
                 * status : init
                 */

                private int id;
                private String name;
                private String class_date;
                private String start_time;
                private String end_time;
                private String status;

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

                public String getClass_date() {
                    return class_date;
                }

                public void setClass_date(String class_date) {
                    this.class_date = class_date;
                }

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
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

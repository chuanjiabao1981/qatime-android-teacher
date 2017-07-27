package cn.qatime.player.utils;

import android.os.Environment;

/**
 * @author luntify
 * @date 2016/8/10 10:28
 * @Description 常量类
 */
public class Constant {

    public static String APP_ID = "wxf2dfbeb5f641ce40";//微信appid
    public static String phoneNumber = "400-838-8010";
    public static final String CACHEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qatime_teacher/images";

    public static int REQUEST = 0;
    public static int RESPONSE = 1;
    public static int REQUEST_EXIT_LOGIN = 0x1001;
    public static int RESPONSE_EXIT_LOGIN = 0x1002;
    public static int REQUEST_CAMERA = 0x1003;
    public static int RESPONSE_CAMERA = 0x1004;
    public static int PHOTO_CROP = 0x1005;
    public static int REQUEST_PICTURE_SELECT = 0x1006;
    public static int RESPONSE_PICTURE_SELECT = 0x1007;
    public static int RESPONSE_HEAD_SCULPTURE = 0x1008;
    public static int REGIST = 0x1009;
    public static int RESPONSE_CITY_SELECT = 0x1011;
    public static int CHANGE_PAY_PSW = 0x1012;
    public static final int QRCODE_SUCCESS = 0x1013;
    public static int REQUEST_REGION_SELECT = 0x1014;
    public static int RESPONSE_REGION_SELECT = 0x1015;
    public static int REQUEST_SCHOOL_SELECT = 0x1016;
    public static int RESPONSE_SCHOOL_SELECT = 0x1017;

    public static class CourseStatus {
        public static String finished = "finished";
        public static String rejected = "rejected";//审核被拒绝
        public static String init = "init";//招生中
        public static String published = "published";//招生中
        public static String teaching = "teaching";//已开课
        public static String completed = "completed";//已完成
    }

    public static class CoursesType {
        public static String courses = "courses";//直播课
        public static String interactive = "interactive";//一对一
        public static String exclusive = "exclusive";//专属课
    }
}

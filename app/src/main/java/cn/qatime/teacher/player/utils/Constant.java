package cn.qatime.teacher.player.utils;

import android.os.Environment;

/**
 * @author luntify
 * @date 2016/8/10 10:28
 * @Description 常量类
 */
public class Constant {

    public static String phoneNumber = "400-838-8010";
    public static final String CACHEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qatime/teacher";

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
    public static int  RESPONSE_SCHOOL_SELECT = 0x1017;
}

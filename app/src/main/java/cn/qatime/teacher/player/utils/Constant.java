package cn.qatime.teacher.player.utils;

import android.os.Environment;

/**
 * @author luntify
 * @date 2016/8/10 10:28
 * @Description 常量类
 */
public class Constant {


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
}

package cn.qatime.player.utils;

import com.orhanobut.logger.Logger;

import java.util.Iterator;
import java.util.Map;

import custom.Configure;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2016/10/25 11:20
 * @Describe
 */
public class UrlUtils {

    private static String baseUrl = Configure.isDebug ? "http://testing.qatime.cn/" : "http://qatime.cn/";
    //登录
    public static String urlLogin = baseUrl + "api/v1/sessions";
    //辅导班
    public static String urlRemedialClass = baseUrl + "api/v1/live_studio/courses";
    //专属课
    public static String urlExclusiveCourse = baseUrl + "api/v1/live_studio/customized_groups/";
    //获取验证码
    public static String urlGetCode = baseUrl + "api/v1/captcha";
    //教师信息
    public static String urlTeacherInformation = baseUrl + "api/v1/teachers/";
    //一对一
    public static String urlInteractCourses = baseUrl + "api/v1/live_studio/interactive_courses/";
    //注册
    public static String urlRegister = baseUrl + "api/v1/user/register";
    //检测
    public static String urlUserCheck = baseUrl + "api/v1/user/check";
    //个人信息
    public static String urlPersonalInformation = baseUrl + "api/v1/teachers/";
    //基础信息
    public static String urlAppconstantInformation = baseUrl + "api/v1/app_constant";
    //找回密码
    public static String urlfindPassword = baseUrl + "api/v1/password";
    //我的
    public static String urlTeachers = baseUrl + "api/v1/live_studio/teachers/";
    //课件
    public static String urlGroups = baseUrl + "api/v1/live_studio/groups/";
    //用户信息
    public static String urlUser = baseUrl + "api/v1/users/";
    //文件资源
    public static String urlFiles = baseUrl + "api/v1/resource/";
    //检查更新
    public static String urlcheckUpdate = baseUrl + "api/v1/system/check_update";
    //账户充值
    public static String urlpayment = baseUrl + "api/v1/payment/users/";
    //獲取支付结果
    public static String urlPayResult = baseUrl + "api/v1/payment/orders/";
    //资产账户
    public static String cashAccounts = baseUrl + "/api/v1/payment/cash_accounts/";
    //视频课
    public static String urlVideoCourses = baseUrl + "api/v1/live_studio/video_courses/";
    //课程表
    public static String urlScheduleData = baseUrl + "api/v2/live_studio/teachers/";
    public static String urlLiveStudio=baseUrl + "api/v1/live_studio/";


    public static String getUrl(String function, Map<String, String> params) {
        String url = function + "?" + Map2String(params);
        if (url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }
        Logger.e("request*******" + url);
        return url;
    }

    /***
     * 对内使用
     *
     * @param map
     * @return
     */
    private static String Map2String(Map<String, String> map) {
        Map.Entry entry;
        StringBuilder sb = new StringBuilder();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            if (!StringUtils.isNullOrBlanK(entry.getKey()) && !StringUtils.isNullOrBlanK(entry.getValue())) {
                sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
            }
        }
        return sb.toString();
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}

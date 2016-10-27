package cn.qatime.teacher.player.utils;

import java.util.Iterator;
import java.util.Map;

import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2016/10/25 11:20
 * @Describe
 */
public class UrlUtils {
    public static boolean isDebug = true;

    public static String baseUrl = isDebug ? "http://testing.qatime.cn/" : "http://qatime.cn/";
    //云信key
    public static String appKey = isDebug ? "2a24ca70e580cab2bef58b1e62478f9f" : "4fe3a3fba0a40a00daf011049a29d995";
    //登录
    public static String urlLogin = baseUrl + "api/v1/sessions";
    //获取验证码
    public static String urlGetCode = baseUrl + "api/v1/captcha";
    //注册
    public static String urlRegister = baseUrl + "api/v1/user/register";
    //个人信息
    public static String urlPersonalInformation = baseUrl + "api/v1/teachers/";
    //找回密码
    public static String urlfindPassword = baseUrl + "api/v1/password";
    //我的辅导班列表
    public static String urlMyRemedialClass = baseUrl + "api/v1/live_studio/teachers/";
    //用户信息
    public static String urlUser = baseUrl + "api/v1/users/";
    //检查更新
    public static String urlcheckUpdate = baseUrl + "api/v1/system/check_update";

    public static String getUrl(String function, Map<String, String> params) {
        String url;
        url = function + "?" + Map2String(params);
        url.trim();
        if (url.endsWith("&")) {
            url.trim().substring(0, url.length() - 1);
        }
        if (url.endsWith("&")) {
            url.substring(0, url.length() - 1);
        }
//        LogUtils.e("请求地址------    " + url);
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
}

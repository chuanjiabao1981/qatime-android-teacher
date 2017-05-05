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
    //基础信息
    public static String urlAppconstantInformation = baseUrl + "api/v1/app_constant";
    //找回密码
    public static String urlfindPassword = baseUrl + "api/v1/password";
    //我的辅导班列表
    public static String urlMyRemedialClass = baseUrl + "api/v1/live_studio/teachers/";
    //用户信息
    public static String urlUser = baseUrl + "api/v1/users/";
    //检查更新
    public static String urlcheckUpdate = baseUrl + "api/v1/system/check_update";
    //账户充值
    public static String urlpayment = baseUrl + "api/v1/payment/users/";
    //獲取支付结果
    public static String urlPayResult = baseUrl + "api/v1/payment/orders/";
    //资产账户
    public static String cashAccounts = baseUrl + "/api/v1/payment/cash_accounts/";

    public static String getUrl(String function, Map<String, String> params) {
        String url = function + "?" + Map2String(params);
        if (url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }
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

package cn.qatime.teacher.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.config.UserPreferences;
import cn.qatime.teacher.player.im.cache.TeamDataCache;
import cn.qatime.teacher.player.im.cache.UserInfoCache;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.SPUtils;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.bean.Profile;
import libraryextra.utils.CheckUtil;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CheckView;
import libraryextra.view.CustomProgressDialog;

/**
 * 登陆页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private int errornum = 0;
    private CheckView checkview;
    private int[] checkNum = null;
    private View checklayout;
    private EditText checkcode;
    private Button login;
    private CustomProgressDialog progress;
    private Profile profile;

    //    private IWXAPI api;
    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EventBus.getDefault().register(this);
//        api = WXAPIFactory.createWXAPI(this, null);
//        api.registerApp(Constant.APP_ID);

        checklayout = findViewById(R.id.checklayout);
        checkview = (CheckView) findViewById(R.id.checkview);
        checkcode = (EditText) findViewById(R.id.checkcode);
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);
        TextView register = (TextView) findViewById(R.id.register);
        TextView enter = (TextView) findViewById(R.id.enter);
        View loginerror = findViewById(R.id.login_error);//忘记密码
        findViewById(R.id.wechat_login).setOnClickListener(this);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        enter.setOnClickListener(this);
        loginerror.setOnClickListener(this);
        checkview.setOnClickListener(this);

        username.setOnClickListener(this);
        password.setOnClickListener(this);


        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    password.setText("");
                }else {
                    if(StringUtils.isNullOrBlanK(username.getText().toString().trim())){
                        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlUserCheck + "?account=" + username.getText().toString().trim(), null, new VolleyListener(LoginActivity.this) {
                        @Override
                        protected void onTokenOut() {

                        }

                        @Override
                        protected void onSuccess(JSONObject response) {
                            try {
                                if (response.getBoolean("data")) {
                                } else {
                                    Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        protected void onError(JSONObject response) {
                            Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                        }
                    }, new VolleyErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                        }
                    });
                    addToRequestQueue(request);
                }
            }
        });

//        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    if(StringUtils.isNullOrBlanK(username.getText().toString().trim())){
//                        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlUserCheck + "?account=" + username.getText().toString().trim(), null, new VolleyListener(LoginActivity.this) {
//                        @Override
//                        protected void onTokenOut() {
//
//                        }
//
//                        @Override
//                        protected void onSuccess(JSONObject response) {
//                            try {
//                                if (response.getBoolean("data")) {
//                                } else {
//                                    Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        protected void onError(JSONObject response) {
//                            Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
//                        }
//                    }, new VolleyErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    addToRequestQueue(request);
//                }
//            }
//        });

        if (!StringUtils.isNullOrBlanK(SPUtils.get(LoginActivity.this, "username", ""))) {
            username.setText(SPUtils.get(LoginActivity.this, "username", "").toString());
        }
        String sign = getIntent().getStringExtra("sign");
        if (!StringUtils.isNullOrBlanK(sign)) {
            if (sign.equals("exit_login")) {//从系统设置退出登录页面跳转而来，清除用户登录信息
                password.setText("");
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login://登陆
                login.setClickable(false);
                login();
                break;
            case R.id.register://注册
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                intent.putExtra("register_action",Constant.REGIST_1);
//                startActivityForResult(intent, Constant.REGIST_1);
                startActivityForResult(intent, Constant.REGIST);
                break;
            case R.id.login_error://忘记密码
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.checkview://重新换验证码
                initCheckNum();
                break;
            case R.id.enter://直接进入
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.wechat_login://微信登录
//                if (!api.isWXAppInstalled()) {
//                    Toast.makeText(this, R.string.login_failed_wechat_not_installed, Toast.LENGTH_SHORT).show();
//                } else if (!api.isWXAppSupportAPI()) {
//                    Toast.makeText(this, R.string.login_failed_wechat_not_support, Toast.LENGTH_SHORT).show();
//                } else {
//                    SendAuth.Req req = new SendAuth.Req();
//                    req.scope = "snsapi_userinfo";
//                    req.state = "wechat_info";
//                    api.sendReq(req);
//                }
//                break;
        }
    }

    private void login() {
        if (TextUtils.isEmpty(username.getText().toString().trim())) {
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            login.setClickable(true);
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString().trim())) {
            Toast.makeText(this, getResources().getString(R.string.password_can_not_be_empty), Toast.LENGTH_SHORT).show();
            login.setClickable(true);
            return;
        }
        if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
            Toast.makeText(this, R.string.password_format_error, Toast.LENGTH_SHORT).show();
            login.setClickable(true);
            return;
        }
        if (checklayout.getVisibility() == View.VISIBLE) {
            if (!CheckUtil.checkNum(checkcode.getText().toString().trim(), checkNum)) {
                Toast.makeText(this, R.string.verification_code_is_incorrect, Toast.LENGTH_SHORT).show();
                initCheckNum();
                checkcode.setText("");
                return;
            }
        }
        progress = DialogUtils.startProgressDialog(progress, LoginActivity.this, getResourceString(R.string.landing));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        Map<String, String> map = new HashMap<>();
        map.put("login_account", username.getText().toString().trim());
        map.put("password", password.getText().toString().trim());
        map.put("client_type", "app");
        map.put("client_cate", "teacher_live");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlLogin, map), null,
                new VolleyListener(LoginActivity.this) {
                    @Override
                    protected void onTokenOut() {
                        login.setClickable(true);
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        login.setClickable(true);
                        try {
                            JSONObject data = response.getJSONObject("data");
                            if (data.has("result")) {
                                DialogUtils.dismissDialog(progress);
                                if (data.getString("result") != null && data.getString("result").equals("failed")) {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.account_or_password_error), Toast.LENGTH_SHORT).show();
                                    BaseApplication.clearToken();
                                    login.setClickable(true);
                                    password.setText("");
                                    //当密码错误5次以上，开始使用验证码
                                    errornum++;
                                    if (errornum >= 5) {
                                        checklayout.setVisibility(View.VISIBLE);
                                        initCheckNum();
                                    }
                                }
                            } else {
                                SPUtils.put(LoginActivity.this, "username", username.getText().toString());
                                profile = JsonUtils.objectFromJson(response.toString(), Profile.class);
//                                if (profile != null && profile.getData() != null && profile.getData().getUser() != null && profile.getData().getUser().getId() != 0) {
//                                    PushAgent.getInstance(LoginActivity.this).addAlias(String.valueOf(profile.getData().getUser().getId()), "student", new UTrack.ICallBack() {
//                                        @Override
//                                        public void onMessage(boolean b, String s) {
//
//                                        }
//                                    });
//                                    String deviceToken = PushAgent.getInstance(LoginActivity.this).getRegistrationId();
//                                    if (!StringUtils.isNullOrBlanK(deviceToken)) {
//                                        Map<String, String> m = new HashMap<>();
//                                        m.put("user_id", String.valueOf(profile.getData().getUser().getId()));
//                                        m.put("device_token", deviceToken);
//                                        m.put("device_model", Build.MODEL);
//                                        try {
//                                            m.put("app_name", URLEncoder.encode(AppUtils.getAppName(LoginActivity.this), "UTF-8"));
//                                        } catch (UnsupportedEncodingException e) {
//                                            e.printStackTrace();
//                                        }
//                                        m.put("app_version", AppUtils.getVersionName(LoginActivity.this));
//                                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlDeviceInfo, m), null,
//                                                new VolleyListener(LoginActivity.this) {
//
//                                                    @Override
//                                                    protected void onSuccess(JSONObject response) {
//                                                    }
//
//                                                    @Override
//                                                    protected void onError(JSONObject response) {
//
//                                                    }
//
//                                                    @Override
//                                                    protected void onTokenOut() {
//                                                        tokenOut();
//                                                    }
//
//                                                }, new VolleyErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError volleyError) {
//                                                super.onErrorResponse(volleyError);
//                                            }
//                                        });
//                                        addToRequestQueue(request);
//                                    }
//                                }
                                if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {
                                    //登录成功且有个人信息  设置profile
                                    BaseApplication.setProfile(profile);
                                    SPUtils.put(LoginActivity.this, "username", username.getText().toString());
                                    loginAccount();//登陆云信
                                } else {
                                    //没有数据或token
                                }
                            }
                        } catch (JSONException e) {
                            DialogUtils.dismissDialog(progress);
                            BaseApplication.clearToken();
                        }


                    }

                    @Override
                    protected void onError(JSONObject response) {
                        DialogUtils.dismissDialog(progress);
                        BaseApplication.clearToken();
                        login.setClickable(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DialogUtils.dismissDialog(progress);
                BaseApplication.clearToken();
                Toast.makeText(LoginActivity.this, getResourceString(R.string.after_try_again), Toast.LENGTH_SHORT).show();
                login.setClickable(true);
                password.setText("");
                //当密码错误5次以上，开始使用验证码
                errornum++;
                if (errornum >= 5) {
                    checklayout.setVisibility(View.VISIBLE);
                    initCheckNum();
                }
            }
        });
        addToRequestQueue(request);
    }

    /**
     * 登陆云信
     */
    private void loginAccount() {
        String account = BaseApplication.getAccount();
        String token = BaseApplication.getAccountToken();

        if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
            NIMClient.getService(AuthService.class).login(new LoginInfo(account, token)).setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo o) {
                    DialogUtils.dismissDialog(progress);
                    Logger.e("云信登录成功" + o.getAccount());
                    // 初始化消息提醒
                    NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                    NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                    //缓存
                    UserInfoCache.getInstance().clear();
                    TeamDataCache.getInstance().clear();

                    UserInfoCache.getInstance().buildCache();
                    TeamDataCache.getInstance().buildCache();

                    UserInfoCache.getInstance().registerObservers(true);
                    TeamDataCache.getInstance().registerObservers(true);
//                  FriendDataCache.getInstance().registerObservers(true);
                }

                @Override
                public void onFailed(int code) {
                    DialogUtils.dismissDialog(progress);
//                    BaseApplication.clearToken();
                    profile.getData().setRemember_token("");
                    SPUtils.putObject(LoginActivity.this, "profile", profile);
                    Logger.e(code + "code");
//                    if (code == 302 || code == 404) {
//                        Toast.makeText(LoginActivity.this, R.string.account_or_password_error, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this, getResourceString(R.string.login_failed) + code, Toast.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    DialogUtils.dismissDialog(progress);
                    Logger.e(throwable.getMessage());
//                    BaseApplication.clearToken();
                    profile.getData().setRemember_token("");
                    SPUtils.putObject(LoginActivity.this, "profile", profile);
                }
            });
        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        DialogUtils.dismissDialog(progress);
        finish();
    }

    /**
     * 刷新验证码
     */
    private void initCheckNum() {
        login.setClickable(true);
        checkNum = CheckUtil.getCheckNum();
        checkview.setCheckNum(checkNum);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if ((requestCode == Constant.REGIST_1||requestCode == Constant.REGIST_2) && resultCode == Constant.RESPONSE) {
        if (requestCode == Constant.REGIST && resultCode == Constant.REGIST) {
            finish();
        }
    }

    /**
     * 微信註冊
     *
     * @param code 微信登錄嗎
     */
    @Subscribe
    public void onEvent(String code) {
//        Map<String, String> map = new HashMap<>();
//        map.put("code", code);
//        map.put("client_cate", "teacher_live");
//        map.put("client_type", "app");
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlLogin + "/wechat", map), null, new VolleyListener(LoginActivity.this) {
//            @Override
//            protected void onTokenOut() {
//                tokenOut();
//            }
//
//            @Override
//            protected void onSuccess(JSONObject response) {
//                try {
//                    if (response.has("data")) {
//                        if (response.getJSONObject("data").has("remember_token")) {//返回登錄信息
//                            Profile data = JsonUtils.objectFromJson(response.toString(), Profile.class);
//                            if (data != null && data.getData() != null && !StringUtils.isNullOrBlanK(data.getData().getRemember_token())) {
//                                BaseApplication.setProfile(data);
//                                SPUtils.put(LoginActivity.this, "username", username.getText().toString());
//                                loginAccount();//登陆云信
//                            } else {
//                                //没有数据或没有token
//                            }
//
//                        } else {
//                            String openid = response.getJSONObject("data").getString("openid");
//                            Intent intent = new Intent(LoginActivity.this, WeChatBindActivity.class);
//                            intent.putExtra("openid", openid);
//                            intent.putExtra("register_action",Constant.REGIST_1);
//                            startActivityForResult(intent, Constant.REGIST_1);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onError(JSONObject response) {
//
//            }
//        }, new VolleyErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                super.onErrorResponse(volleyError);
//                Logger.e(volleyError.getMessage());
//            }
//        });
//        addToRequestQueue(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

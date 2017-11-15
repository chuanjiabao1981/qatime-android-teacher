package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2016/8/17.
 */
public class BindEmailActivity extends BaseActivity implements View.OnClickListener {
    private TimeCount time;
    private TextView textGetcode;
    private Button buttonOver;
    private EditText inputNewEmail;
    private EditText code;
    private String captchaEmail;


    private void assignViews() {
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        inputNewEmail = (EditText) findViewById(R.id.input_new_email);
        buttonOver = (Button) findViewById(R.id.button_over);

        inputNewEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmail(inputNewEmail.getText().toString().trim())) {
                    if (!time.ticking)
                        textGetcode.setEnabled(true);
                } else {
                    textGetcode.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);

        initView();
        time = new TimeCount(60000, 1000);
    }

    @Override
    public int getContentView() {
        return 0;
    }

    private void initView() {
        setTitle(getResources().getString(R.string.bind_email));
        assignViews();

        inputNewEmail.setHint(StringUtils.getSpannedString(this, R.string.hint_input_email));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_code));

        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_getcode:
                captchaEmail = inputNewEmail.getText().toString().trim();
                if (!StringUtils.isEmail(captchaEmail)) { //邮箱
                    Toast.makeText(this, getResources().getString(R.string.email_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("send_to", captchaEmail);
                map.put("key", "change_email_captcha");

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_failed), Toast.LENGTH_LONG).show();
                    }
                }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));

                time.start();
                break;
            case R.id.button_over:
                if (!StringUtils.isEmail(inputNewEmail.getText().toString().trim())) { //邮箱
                    Toast.makeText(this, getResources().getString(R.string.email_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!inputNewEmail.getText().toString().trim().equals(captchaEmail)) { //验证手机是否一致
                    Toast.makeText(this, getResources().getString(R.string.captcha_email_has_changed), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(code.getText().toString())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = this.code.getText().toString().trim();
                map = new HashMap<>();
                map.put("id", "" + BaseApplication.getInstance().getUserId());
                map.put("email", captchaEmail);
                map.put("captcha_confirmation", code);

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getInstance().getUserId() + "/email", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证成功");
                        BaseApplication.getInstance().getProfile().getData().getUser().setEmail(captchaEmail);
                        BaseApplication.getInstance().setProfile(BaseApplication.getInstance().getProfile());
                        Toast.makeText(BindEmailActivity.this, getResourceString(R.string.bind_email_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BindEmailActivity.this, SecurityManagerActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    protected void onError(JSONObject response) {
                        try {
                            JSONObject error = response.getJSONObject("error");
                            if (error.getString("msg").contains("Captcha confirmation")) {
                                Toast.makeText(BindEmailActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BindEmailActivity.this, getResourceString(R.string.phone_already_bind), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));

                break;
        }
    }


    class TimeCount extends CountDownTimer {
        public boolean ticking;

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            ticking = false;
            textGetcode.setText(getResources().getString(R.string.getcode));
            textGetcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            ticking = true;
            textGetcode.setEnabled(false);//防止重复点击
            textGetcode.setText(millisUntilFinished / 1000 + getResourceString(R.string.time_after_acquisition));
        }
    }
}

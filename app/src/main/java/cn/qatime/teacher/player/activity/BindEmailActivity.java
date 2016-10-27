package cn.qatime.teacher.player.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.UrlUtils;
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
    private EditText confirmNewEmail;
    private EditText code;


    private void assignViews() {

        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        inputNewEmail = (EditText) findViewById(R.id.input_new_email);
        confirmNewEmail = (EditText) findViewById(R.id.confirm_new_email);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        time = new TimeCount(60000, 1000);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_bind_email;
    }

    private void initView() {
        setTitle(getResources().getString(R.string.bind_email));
        assignViews();

        inputNewEmail.setHint(StringUtils.getSpannedString(this, R.string.hint_input_email));
        confirmNewEmail.setHint(StringUtils.getSpannedString(this, R.string.hint_input_again));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_code));

        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String email1 = inputNewEmail.getText().toString().trim();
        String email2 = confirmNewEmail.getText().toString().trim();

        switch (v.getId()) {
            case R.id.text_getcode:
                if (!StringUtils.isEmail(email1)) { //邮箱
                    Toast.makeText(this, getResources().getString(R.string.email_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email1.equals(email2)) {
                    Toast.makeText(this, getResources().getString(R.string.email_confirm_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("send_to", email1);
                map.put("key", "change_email_captcha");

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证码发送成功" + email1 + "---" + response.toString());
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

                if (!StringUtils.isEmail(email1)) { //邮箱
                    Toast.makeText(this, getResources().getString(R.string.email_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email1.equals(email2)) {
                    Toast.makeText(this, getResources().getString(R.string.email_confirm_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(code.getText().toString())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                String code = this.code.getText().toString().trim();

                map = new HashMap<>();
                map.put("id", "" + BaseApplication.getUserId());
                map.put("email", email1);
                map.put("captcha_confirmation", code);

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/email", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {


                        Logger.e("验证成功");
                        Toast.makeText(BindEmailActivity.this, getResourceString(R.string.bind_email_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BindEmailActivity.this, SecurityManagerActivity.class);
                        startActivity(intent);


                    }

                    @Override
                    protected void onError(JSONObject response) {
                        try {
                            JSONObject error = response.getJSONObject("error");
                            if (error.getString("msg").contains("与确认值不匹配")) {
                                Toast.makeText(BindEmailActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BindEmailActivity.this, getResourceString(R.string.email_already_bind), Toast.LENGTH_SHORT).show();
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
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            textGetcode.setText(getResources().getString(R.string.getcode));
            textGetcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            textGetcode.setEnabled(false);//防止重复点击
            textGetcode.setText(millisUntilFinished / 1000 + getResourceString(R.string.time_after_acquisition));
        }
    }
}

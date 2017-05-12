package cn.qatime.teacher.player.activity;


import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2016/8/17.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView forgetPassword;
    private EditText password;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Button buttonOver;
    private String password1;
    private String password2;
    private String password3;


    private void assignViews() {
        password = (EditText) findViewById(R.id.password);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_change_password;
    }

    private void initView() {
        setTitle(getResources().getString(R.string.change_password));
        assignViews();

        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_current_password));
        newPassword.setHint(StringUtils.getSpannedString(this, R.string.hint_input_new_password));
        confirmNewPassword.setHint(StringUtils.getSpannedString(this, R.string.hint_confirm_new_password));
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password2 = newPassword.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password3 = confirmNewPassword.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forgetPassword.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        password1 = password.getText().toString().trim();
//        password2 = newPassword.getText().toString().trim();
//        password3 = confirmNewPassword.getText().toString().trim();
        switch (v.getId()) {
            case R.id.forget_password:
                Intent intent = new Intent(this, ForgetPasswordActivity.class);
                intent.putExtra("status_login", true);
                startActivityForResult(intent, Constant.REQUEST_EXIT_LOGIN);
                break;
            case R.id.button_over:
                if (!(StringUtils.isGoodPWD(password1) || StringUtils.isGoodPWD(password2) || StringUtils.isGoodPWD(password3))) {
                    Toast.makeText(this, getResources().getString(R.string.password_6_16), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password2.equals(password3)) {
                    Toast.makeText(this, getResources().getString(R.string.password_and_repassword_are_incongruous), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("id", "" + BaseApplication.getUserId());
                map.put("current_password", password1);
                map.put("password", password2);
                map.put("password_confirmation", password3);

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/password", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证成功");
                        Toast.makeText(ChangePasswordActivity.this, getResourceString(R.string.change_password_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        intent.putExtra("sign", "exit_login");
                        startActivity(intent);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(ChangePasswordActivity.this, getResourceString(R.string.password_error), Toast.LENGTH_SHORT).show();
                    }
                }, new VolleyErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_EXIT_LOGIN && resultCode == Constant.RESPONSE_EXIT_LOGIN) {
            setResult(Constant.RESPONSE_EXIT_LOGIN);
            finish();
        }
    }
}

package cn.qatime.teacher.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.BusEvent;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CustomKeyboard;
import libraryextra.view.PayEditText;


public class PayPSWChangeActivity extends BaseActivity implements View.OnClickListener {
    private PayEditText payEditText;
    private CustomKeyboard customKeyboard;
    private Button over;
    private String tempPassword;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };
    private EditText editText;

    private void assignViews() {
        payEditText = (PayEditText) findViewById(R.id.PayEditText_pay);
        over = (Button) findViewById(R.id.over);
        customKeyboard = (CustomKeyboard) findViewById(R.id.KeyboardView_pay);


        setTitle(getString(R.string.change_new_pay_password));
        payEditText.setOnClickListener(this);
        over.setOnClickListener(this);
        over.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pay_psw);
        assignViews();
        setSubView();
        initEvent();
    }

    @Override
    public int getContentView() {
        return 0;
    }


    private void setSubView() {
        //设置键盘
        customKeyboard.setKeyboardKeys(KEY);
    }

    private void initEvent() {
        customKeyboard.setOnClickKeyboardListener(new CustomKeyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    payEditText.add(value);
                } else if (position == 11) {
                    payEditText.remove();
                }
            }
        });

        /**
         * 当密码输入完成时的回调
         */
        payEditText.setOnInputChangeListener(new PayEditText.OnInputChangeListener() {
            @Override
            public void onInputFinished(String password) {
                if (over.getVisibility() == View.INVISIBLE) {
                    //下一步
                    tempPassword = password;
                    payEditText.clear();
                    over.setVisibility(View.VISIBLE);
                    over.setEnabled(false);
                    setTitle(getString(R.string.confirm_new_pay_password));
                } else {
                    over.setEnabled(true);
                }
            }

            @Override
            public void onInputAdd(String value) {

            }

            @Override
            public void onInputRemove() {
                over.setEnabled(false);
            }
        });
    }

    @Override
    public void backClick(View v) {
        if (over.getVisibility() == View.INVISIBLE) {
            super.backClick(v);
        } else {
            reset();
        }
    }

    @Override
    public void onBackPressed() {
        if (over.getVisibility() == View.INVISIBLE) {
            super.onBackPressed();
        } else {
            reset();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.over:
                if (!tempPassword.equals(payEditText.getText())) {
                    Toast.makeText(PayPSWChangeActivity.this, R.string.password_and_repassword_are_incongruous, Toast.LENGTH_SHORT).show();
                    reset();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("pament_password", tempPassword);
                map.put("ticket_token", getIntent().getStringExtra("ticket_token"));
                DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.cashAccounts + BaseApplication.getUserId() + "/password", map), null,
                        new VolleyListener(PayPSWChangeActivity.this) {
                            @Override
                            protected void onSuccess(JSONObject response) {
                                Toast.makeText(PayPSWChangeActivity.this, R.string.change_pay_password_success, Toast.LENGTH_SHORT).show();
                                BaseApplication.getCashAccount().getData().setPassword_set_at(System.currentTimeMillis()/1000);
                                EventBus.getDefault().post(BusEvent.PAY_PASSWORD_CHANGE);
                                EventBus.getDefault().post(BusEvent.REFRESH_CASH_ACCOUNT);
                                finish();
                            }

                            protected void onError(JSONObject response) {
                                     try {
                                    int errorCode = response.getJSONObject("error").getInt("code");
                                    if (errorCode == 2007) {
                                        Toast.makeText(PayPSWChangeActivity.this, R.string.token_error, Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onTokenOut() {
                                tokenOut();
                            }
                        }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);

                    }
                });
                addToRequestQueue(request);
                break;
            case R.id.PayEditText_pay:
                customKeyboard.setVisibility(customKeyboard.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                break;
        }
    }

    private void reset() {
        //重置
        over.setVisibility(View.INVISIBLE);
        tempPassword = "";
        payEditText.clear();
        setTitle(getString(R.string.change_new_pay_password));
    }
}

package cn.qatime.teacher.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CustomKeyboard;
import libraryextra.view.PayEditText;


public class PayPSWVerifyActivity extends BaseActivity implements View.OnClickListener {
    private PayEditText payEditText;
    private CustomKeyboard customKeyboard;
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };
    private View forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setSubView();
        initEvent();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pay_password;
    }

    private void initView() {
        setTitle(getString(R.string.verify_pay_password));
        payEditText = (PayEditText) findViewById(R.id.PayEditText_pay);
        customKeyboard = (CustomKeyboard) findViewById(R.id.KeyboardView_pay);
        forget = findViewById(R.id.forget_pay_password);

        forget.setOnClickListener(this);

        payEditText.setOnClickListener(this);
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("current_pament_password", password);
                DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.cashAccounts + BaseApplication.getUserId() + "/password/ticket_token", map), null,
                        new VolleyListener(PayPSWVerifyActivity.this) {
                            @Override
                            protected void onSuccess(JSONObject response) {
                                Intent intent = new Intent(PayPSWVerifyActivity.this, PayPSWChangeActivity.class);
                                try {
                                    intent.putExtra("ticket_token", response.getString("data"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                                finish();
                            }

                            protected void onError(JSONObject response) {
                                payEditText.clear();
                                try {
                                    int errorCode = response.getJSONObject("error").getInt("code");
                                    if (errorCode == 2005) {
                                        Toast.makeText(PayPSWVerifyActivity.this, R.string.password_error, Toast.LENGTH_SHORT).show();
                                    } else if (errorCode == 2006) {
                                        Toast.makeText(PayPSWVerifyActivity.this, R.string.pay_password_not_set, Toast.LENGTH_SHORT).show();
                                    } else if (errorCode == 2008) {
                                        Toast.makeText(PayPSWVerifyActivity.this, R.string.pay_password_not_enough_24, Toast.LENGTH_SHORT).show();
                                    }else if (errorCode == 2009) {
                                        Toast.makeText(PayPSWVerifyActivity.this, R.string.pay_password_too_many_mistake, Toast.LENGTH_SHORT).show();
                                    }  else {
                                        Toast.makeText(PayPSWVerifyActivity.this,R.string.server_error, Toast.LENGTH_SHORT).show();
                                    }

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
            }

            @Override
            public void onInputAdd(String value) {

            }

            @Override
            public void onInputRemove() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.CHANGE_PAY_PSW) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PayEditText_pay:
                customKeyboard.setVisibility(customKeyboard.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                break;
            case R.id.forget_pay_password:
                startActivityForResult(new Intent(this, PayPSWForgetActivity.class), Constant.REQUEST);
                break;
        }
    }
}

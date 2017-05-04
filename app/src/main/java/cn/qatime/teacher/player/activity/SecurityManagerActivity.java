package cn.qatime.teacher.player.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class SecurityManagerActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout bindPhoneNumber;
    private TextView phoneNumberM;
    private LinearLayout bindEmail;
    private TextView email;
    private LinearLayout changePassword;
    private LinearLayout changePayPassword;
    private AlertDialog alertDialog;

    private void assignViews() {
        bindPhoneNumber = (LinearLayout) findViewById(R.id.bind_phone_number);
        bindEmail = (LinearLayout) findViewById(R.id.bind_email);
        email = (TextView) findViewById(R.id.email);
        phoneNumberM = (TextView) findViewById(R.id.phone_number_m);
        changePassword = (LinearLayout) findViewById(R.id.change_password);
        changePayPassword = (LinearLayout) findViewById(R.id.change_pay_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_security_manager;
    }

    private void initData() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                Logger.e("学生信息：  " + response.toString());
                PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                setValue(bean);
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(SecurityManagerActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);

    }

    private void setValue(PersonalInformationBean bean) {

        String email = bean.getData().getEmail();
        if (email != null) {
            this.email.setText("" + email);
            this.email.setTextColor(0xff333333);
        } else {
            this.email.setText(getResourceString(R.string.not_bind));
            this.email.setTextColor(Color.RED);
        }

        String loginMobile = bean.getData().getLogin_mobile();
        if (loginMobile != null) {
            phoneNumberM.setText("" + loginMobile);
            phoneNumberM.setTextColor(0xff333333);
        } else {
            phoneNumberM.setText(getResourceString(R.string.not_bind));
            phoneNumberM.setTextColor(Color.RED);
        }
    }


    private void initView() {
        setTitle(getResources().getString(R.string.security_management));
        assignViews();

        bindPhoneNumber.setOnClickListener(this);
        bindEmail.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        changePayPassword.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_phone_number://绑定手机
                Intent intent = new Intent(this, VerifyPhoneActivity.class);
                intent.putExtra("next", "phone");
                startActivity(intent);
                break;
            case R.id.bind_email://绑定邮箱
                intent = new Intent(this, VerifyPhoneActivity.class);
                intent.putExtra("next", "email");
                startActivity(intent);
                break;
            case R.id.change_password://修改密码
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.change_pay_password://修改支付密码
//                dialogNotify();
                break;
        }
    }
    private void dialogNotify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.change_pay_password_notify);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setText(R.string.continue_anyway);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                changePayPSW();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    private void changePayPSW() {
//        PayPopView payPopView = new PayPopView("","",getWindow());
//        payPopView.showPop();
        if (BaseApplication.getCashAccount() != null && BaseApplication.getCashAccount().getData() != null) {
            if (BaseApplication.getCashAccount().getData().isHas_password()) {
                startActivity(new Intent(this, PayPSWVerifyActivity.class));
            } else {
                startActivity(new Intent(this, PayPSWForgetActivity.class));
            }
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            Logger.e("未获取到支付密码状态");
        }
    }
}

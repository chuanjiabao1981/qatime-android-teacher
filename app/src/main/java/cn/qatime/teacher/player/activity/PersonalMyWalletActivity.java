package cn.qatime.teacher.player.activity;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 15:52
 * @Description:
 */
public class PersonalMyWalletActivity extends BaseActivity implements View.OnClickListener {
    private TextView balance;
    private TextView yuan;
    private TextView consumption;
    private LinearLayout consumptionRecord;
    private LinearLayout withdrawRecord;
    private TextView phone;
    private TextView withdrawCash;
    private Dialog alertDialog;
    DecimalFormat df = new DecimalFormat("#.00");

    private void assignViews() {
        balance = (TextView) findViewById(R.id.balance);
        yuan = (TextView) findViewById(R.id.yuan);
        consumption = (TextView) findViewById(R.id.consumption);
        consumptionRecord = (LinearLayout) findViewById(R.id.consumption_record);
        withdrawRecord = (LinearLayout) findViewById(R.id.withdraw_record);
        phone = (TextView) findViewById(R.id.phone);
        withdrawCash = (TextView) findViewById(R.id.withdraw_cash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getResourceString(R.string.wallet_manager));
        assignViews();
        initData();
        phone.setOnClickListener(this);
        consumptionRecord.setOnClickListener(this);
        withdrawRecord.setOnClickListener(this);
        withdrawCash.setOnClickListener(this);
        SPUtils.put(PersonalMyWalletActivity.this, "balance", balance.getText().toString());
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_my_wallet;
    }

    private void initData() {
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlpayment + BaseApplication.getUserId() + "/cash", null, new VolleyListener(this) {

            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    String price = df.format(Double.valueOf(response.getJSONObject("data").getString("balance")));
                    if (price.startsWith(".")) {
                        price = "0" + price;
                    }
                    balance.setText(price);
                    String price1 = df.format(Double.valueOf(response.getJSONObject("data").getString("total_expenditure")));
                    if (price1.startsWith(".")) {
                        price1 = "0" + price1;
                    }
                    consumption.setText(price1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(PersonalMyWalletActivity.this, getResourceString(R.string.get_wallet_info_error), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PersonalMyWalletActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialog == null) {
                    View view = View.inflate(PersonalMyWalletActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + phone.getText() + "?");
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button confirm = (Button) view.findViewById(R.id.confirm);
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
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                            if (ActivityCompat.checkSelfPermission(PersonalMyWalletActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(PersonalMyWalletActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            startActivity(intent);
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PersonalMyWalletActivity.this);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else {
                    alertDialog.show();
                }
                break;

            case R.id.withdraw_cash:
                Intent intent = new Intent(this, WithdrawCash1Activity.class);
                intent.putExtra("balance", balance.getText().toString());
                startActivityForResult(intent, Constant.REQUEST);
                break;

            case R.id.withdraw_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                break;
            case R.id.consumption_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 2);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

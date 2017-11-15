package cn.qatime.player.activity;


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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.utils.SPUtils;
import libraryextra.bean.CashAccountBean;

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
        consumption = (TextView) findViewById(R.id.consumption);
        consumptionRecord = (LinearLayout) findViewById(R.id.consumption_record);
        withdrawRecord = (LinearLayout) findViewById(R.id.withdraw_record);
        phone = (TextView) findViewById(R.id.phone);
        withdrawCash = (TextView) findViewById(R.id.withdraw_cash);
        setRightText("说明", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyWalletActivity.this, WalletExplainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setTitle(getResourceString(R.string.wallet_manager));
        assignViews();
        initData();
        phone.setOnClickListener(this);
        consumptionRecord.setOnClickListener(this);
        withdrawRecord.setOnClickListener(this);
        withdrawCash.setOnClickListener(this);
        SPUtils.put(PersonalMyWalletActivity.this, "balance", balance.getText().toString());
    }

    @Subscribe
    public void onEvent(BusEvent event) {
        if (event == BusEvent.ON_REFRESH_CASH_ACCOUNT)
            initData();
    }


    @Override
    public int getContentView() {
        return R.layout.activity_personal_my_wallet;
    }

    private void initData() {
        CashAccountBean cashAccount = BaseApplication.getInstance().getCashAccount();
        if (cashAccount != null && cashAccount.getData() != null) {
            String price = cashAccount.getData().getBalance();
            if (price.startsWith(".")) {
                price = "0" + price;
            }
            balance.setText("￥" + price);
            String price1 = cashAccount.getData().getTotal_income();
            if (price1.startsWith(".")) {
                price1 = "0" + price1;
            }
            consumption.setText(getString(R.string.account_income) + price1);
        }
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
                Intent intent = new Intent(this, WithdrawCashActivity.class);
                startActivity(intent);
                break;

            case R.id.withdraw_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 0);
                startActivity(intent);
                break;
            case R.id.consumption_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

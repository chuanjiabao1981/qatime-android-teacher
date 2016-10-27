package cn.qatime.teacher.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.utils.Constant;


/**
 * @author Tianhaoranly
 * @date 2016/10/17 17:05
 * @Description:
 */
public class WithdrawResultActivity extends BaseActivity implements View.OnClickListener {
    private TextView id;
    private TextView time;
    private TextView mode;
    private TextView amount;
    //    private TextView commission;
    private Button withdrawCashConfirm;
    private TextView phone;
    private AlertDialog alertDialogPhone;

    private void assignViews() {
        id = (TextView) findViewById(R.id.id);
        time = (TextView) findViewById(R.id.time);
        mode = (TextView) findViewById(R.id.mode);
        amount = (TextView) findViewById(R.id.amount);
//        commission = (TextView) findViewById(R.id.commission);
        amount.setText(getIntent().getStringExtra("amount"));
        mode.setText(getPayType(getIntent().getStringExtra("pay_type")));
        id.setText(getIntent().getStringExtra("id"));
        time.setText(getIntent().getStringExtra("create_at"));

        withdrawCashConfirm = (Button) findViewById(R.id.withdraw_cash_confirm);
        phone = (TextView) findViewById(R.id.phone);
        phone.setOnClickListener(this);
        withdrawCashConfirm.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResourceString(R.string.recharge_confirm));
        assignViews();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_withdraw_result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialogPhone == null) {
                    View view = View.inflate(WithdrawResultActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + phone.getText() + "?");
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button confirm = (Button) view.findViewById(R.id.confirm);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogPhone.dismiss();
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogPhone.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                            startActivity(intent);
                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawResultActivity.this);
                    alertDialogPhone = builder.create();
                    alertDialogPhone.show();
                    alertDialogPhone.setContentView(view);
                } else {
                    alertDialogPhone.show();
                }
                break;
            case R.id.withdraw_cash_confirm:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        setResult(Constant.RESPONSE);
        super.finish();
    }

    private String getPayType(String pay_type) {
        switch (pay_type) {
            case "bank":
                return "银行卡";
            case "alipay":
                return "支付宝";
        }
        return "银行卡";
    }
}

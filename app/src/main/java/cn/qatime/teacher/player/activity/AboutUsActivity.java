package cn.qatime.teacher.player.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;

/**
 * @author luntify
 * @date 2016/8/15 21:07
 * @Description
 */
public class AboutUsActivity extends BaseActivity {

    private View call;
    private TextView phone;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_about_us;
    }

    private void initView() {
        setTitle(getResources().getString(R.string.about_us));
        call = findViewById(R.id.call_phone);
        phone = (TextView) findViewById(R.id.phone);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View view = View.inflate(AboutUsActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                        if (ActivityCompat.checkSelfPermission(AboutUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(AboutUsActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(intent);
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutUsActivity.this);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setContentView(view);
//                WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//                attributes.width= ScreenUtils.getScreenWidth(getApplicationContext())- DensityUtils.dp2px(getApplicationContext(),20)*2;
//                alertDialog.getWindow().setAttributes(attributes);
            }
        });
    }
}

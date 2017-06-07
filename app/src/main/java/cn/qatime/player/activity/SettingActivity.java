package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;

/**
 * @author Tianhaoranly
 * @date 2016/10/26 19:05
 * @Description:
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout security;
    private LinearLayout setting;
    private TextView newVersion;

    private void assignViews() {
        security = (LinearLayout) findViewById(R.id.security);
        setting = (LinearLayout) findViewById(R.id.setting);
        newVersion = (TextView) findViewById(R.id.new_version);
        newVersion.setVisibility(BaseApplication.newVersion ? View.VISIBLE : View.INVISIBLE);
        security.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置");
        assignViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.security:// 安全管理
                Intent intent = new Intent(this, SecurityManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:// 设置
                intent = new Intent(this, SystemSettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}

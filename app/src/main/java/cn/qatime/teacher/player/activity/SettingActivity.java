package cn.qatime.teacher.player.activity;

import android.os.Bundle;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;

/**
 * @author Tianhaoranly
 * @date 2016/10/26 19:05
 * @Description:
 */
public class SettingActivity extends BaseActivity {


    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置中心");
    }
}

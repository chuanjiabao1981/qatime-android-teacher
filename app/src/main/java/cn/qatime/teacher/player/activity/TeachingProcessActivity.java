package cn.qatime.teacher.player.activity;


import android.os.Bundle;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;

/**
 * Created by lenovo on 2016/8/22.
 */
public class TeachingProcessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    @Override
    public int getContentView() {
        return R.layout.activity_teaching_process;
    }


    private void initView() {
        setTitle(getResourceString(R.string.learning_process));
    }
}

package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * Created by lenovo on 2017/8/31.
 */

public class ExclusiveFilesUploadActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentView() {
        return R.layout.activity_exclusive_files_upload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("选择来源");
        getIntent().getIntExtra("id",0);
        findViewById(R.id.local).setOnClickListener(this);
        findViewById(R.id.server).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.local:
                intent.setClass(this,LocalFilesUploadActivity.class);
                break;
            case R.id.server:
                intent.setClass(this,PersonalMyFilesActivity.class);
                break;
        }
        intent.putExtra("id",getIntent().getIntExtra("id",0));
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(0);
        finish();
    }
}

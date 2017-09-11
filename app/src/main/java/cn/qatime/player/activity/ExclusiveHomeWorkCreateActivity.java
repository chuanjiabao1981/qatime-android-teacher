package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.StringUtils;

/**
 * Created by lenovo on 2017/9/8.
 */

public class ExclusiveHomeWorkCreateActivity extends BaseActivity implements View.OnClickListener {

    private EditText title;

    @Override
    public int getContentView() {
        return R.layout.acitivty_homework_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置标题");
        title = (EditText) findViewById(R.id.homework_title);
        findViewById(R.id.bottom_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_button:
                String title = this.title.getText().toString().trim();
                if(StringUtils.isNullOrBlanK(title)){
                    Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this,ExclusiveHomeWorkItemsAddActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("courseId",getIntent().getIntExtra("courseId",0));
                startActivityForResult(intent, Constant.REQUEST);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(Constant.RESPONSE);
        finish();
    }
}

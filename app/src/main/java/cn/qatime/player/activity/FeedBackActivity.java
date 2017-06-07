package cn.qatime.player.activity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.StringUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class FeedBackActivity extends BaseActivity {

    private EditText opinion;
    private EditText contact;
    private Button buttonOver;

    private void assignViews() {
        opinion = (EditText) findViewById(R.id.opinion);
        contact = (EditText) findViewById(R.id.contact);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_feedback;
    }

    private void initData() {

    }

    private void initView() {
        setTitle(getResourceString(R.string.feedback));
        assignViews();
        opinion.setHint(StringUtils.getSpannedString(this, R.string.hint_opinion));
        contact.setHint(StringUtils.getSpannedString(this, R.string.hint_contact));
    }
}

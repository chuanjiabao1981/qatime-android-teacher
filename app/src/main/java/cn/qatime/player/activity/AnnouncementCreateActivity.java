package cn.qatime.player.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/5/19 17:36
 * @Describe
 */

public class AnnouncementCreateActivity extends BaseActivity implements View.OnClickListener {
    private EditText content;
    private int id;
    private String baseUrl;

    private void assignViews() {
        content = (EditText) findViewById(R.id.content);
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        TextView submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_announcement_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getIntExtra("id", 0);
        String type = getIntent().getStringExtra("type");
        if (!StringUtils.isNullOrBlanK(type)) {
            if (type.equals(Constant.CoursesType.courses  )) {
                baseUrl = UrlUtils.urlRemedialClass + "/";
            } else if (type.equals(Constant.CoursesType.interactive)) {
                baseUrl = UrlUtils.urlInteractCourses;
            }
        }
        assignViews();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                commitData();
                break;
        }
    }

    private void commitData() {
        if (id != 0 && !StringUtils.isNullOrBlanK(content.getText().toString())) {
            Map<String, String> map = new HashMap<>();
            try {
                map.put("content", URLEncoder.encode(content.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(baseUrl + id + "/announcements", map), null,
                    new VolleyListener(AnnouncementCreateActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            Toast.makeText(AnnouncementCreateActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
                            setResult(Constant.RESPONSE);
                            finish();
                        }

                        @Override
                        protected void onError(JSONObject response) {
                        }

                        @Override
                        protected void onTokenOut() {
                            tokenOut();
                        }
                    }, new VolleyErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    super.onErrorResponse(volleyError);
                }
            });
            addToRequestQueue(request);
        } else {
            Toast.makeText(this, "公告不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
    }
}

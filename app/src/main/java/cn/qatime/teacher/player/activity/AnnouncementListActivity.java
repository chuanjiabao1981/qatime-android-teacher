package cn.qatime.teacher.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Announcements;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/5/19 14:38
 * @Describe
 */

public class AnnouncementListActivity extends BaseActivity implements View.OnClickListener {
    private CommonAdapter<Announcements.DataBean.AnnouncementsBean> adapter;
    private int id;
    private List<Announcements.DataBean.AnnouncementsBean> items = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_announcement_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("公告");
        initView();
        id = getIntent().getIntExtra("id", 0);
        initData();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        View empty = View.inflate(this, R.layout.empty_view, null);
        TextView textEmpty = (TextView) empty.findViewById(R.id.text_empty);
        textEmpty.setText(R.string.no_course_announcements);
        listView.setEmptyView(empty);
        adapter = new CommonAdapter<Announcements.DataBean.AnnouncementsBean>(this, items, R.layout.item_announcement_list) {
            @Override
            public void convert(ViewHolder helper, Announcements.DataBean.AnnouncementsBean item, int position) {
                helper.setText(R.id.time, item.getEdit_at());
                helper.setText(R.id.content, item.getAnnouncement());
            }
        };
        listView.setAdapter(adapter);
        findViewById(R.id.create).setOnClickListener(this);
    }

    private void initData() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id + "/realtime", null,
                    new VolleyListener(AnnouncementListActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            Announcements data = JsonUtils.objectFromJson(response.toString(), Announcements.class);
                            if (data != null) {
                                if (data.getData() != null) {
                                    items.clear();
                                    items.addAll(data.getData().getAnnouncements());
                                }
                                adapter.notifyDataSetChanged();
                            }
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AnnouncementListActivity.this, AnnouncementCreateActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, Constant.REQUEST);
    }
}

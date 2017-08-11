package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.Team;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Announcements;
import libraryextra.utils.StringUtils;
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
    private String baseUrl;
    private String type;
    private String teamId;

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
        teamId = getIntent().getStringExtra("teamId");
        // TODO: 2017/8/11  课程teamId获取
        type = getIntent().getStringExtra("type");
//        if (!StringUtils.isNullOrBlanK(type)) {
//            if (type.equals(Constant.CoursesType.courses)) {
//                baseUrl = UrlUtils.urlRemedialClass+"/";
//            } else if (type.equals(Constant.CoursesType.interactive)) {
//                baseUrl = UrlUtils.urlInteractCourses;
//            }else if(type.equals(Constant.CoursesType.exclusive)){
//                baseUrl = UrlUtils.urlExclusiveCourse;
//            }
//        }
//        initData();
//        getAnnouncementsData();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        ViewGroup parent = (ViewGroup) listView.getParent();
        View inflate = View.inflate(this, R.layout.empty_view, null);
        TextView textEmpty = (TextView) inflate.findViewById(R.id.text_empty);
        textEmpty.setText(R.string.no_course_announcements);
        parent.addView(inflate, parent.indexOfChild(listView) + 1);
        listView.setEmptyView(inflate);
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
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(baseUrl+ id + "/realtime", null,
                    new VolleyListener(AnnouncementListActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
//                            Announcements data = JsonUtils.objectFromJson(response.toString(), Announcements.class);
//                            if (data != null) {
//                                if (data.getData() != null) {
//                                    items.clear();
//                                    items.addAll(data.getData().getAnnouncements());
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
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
        intent.putExtra("type", type);
        startActivityForResult(intent, Constant.REQUEST);
    }

    private void getAnnouncementsData() {
        Team team = TeamDataCache.getInstance().getTeamById(teamId);
        if (team != null) {
            updateAnnouncement(team);
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateAnnouncement(result);
                    } else {
//                        Toast.makeText(AnnouncementListActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateAnnouncement(Team result) {
        String announcement = result.getAnnouncement();
//        // 每次仅修改群的一个属性，可修改的属性包括：群名，介绍，公告，验证类型等。
//        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.Announcement, value)
//                .setCallback(new RequestCallback<Void>() {
//
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                    }
//
//                    @Override
//                    public void onFailed(int i) {
//
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//
//                    }
//                });
    }

}

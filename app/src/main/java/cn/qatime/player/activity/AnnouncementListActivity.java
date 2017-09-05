package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
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
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Announcements;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/5/19 14:38
 * @Describe
 */

public class AnnouncementListActivity extends BaseActivity implements View.OnClickListener {
    private int id;
    private String type;
    private String teamId;
    private TextView desc;

    @Override
    public int getContentView() {
        return R.layout.activity_announcement_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("公告");
        id = getIntent().getIntExtra("id", 0);
        teamId = getIntent().getStringExtra("teamId");
        type = getIntent().getStringExtra("type");
        desc = (TextView) findViewById(R.id.describe);
        View create =findViewById(R.id.create);
        create.setOnClickListener(this);
        if(StringUtils.isNullOrBlanK(teamId)){
            return;
        }
        getAnnouncementsData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getAnnouncementsData();
//        String content = data.getStringExtra("content");
//        if(StringUtils.isNullOrBlanK(content)){
//            return;
//        }
//        desc.setText(content);
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
        desc.setText(announcement);
    }

}

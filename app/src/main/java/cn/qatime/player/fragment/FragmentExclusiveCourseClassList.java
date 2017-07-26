package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Lessons;
import libraryextra.bean.RemedialClassDetailBean;

import static cn.qatime.player.R.id.status;

public class FragmentExclusiveCourseClassList extends BaseFragment {
    private CommonAdapter<Lessons> adapter;
    private List<Lessons> list = new ArrayList<>();

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private RemedialClassDetailBean.Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exclusive_course_class_list, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        ListView listView = (ListView) view.findViewById(R.id.list1);
        ListView listView2 = (ListView) view.findViewById(R.id.list2);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<Lessons>(getActivity(), list, R.layout.item_fragment_remedial_class_detail3) {

            @Override
            public void convert(ViewHolder holder, Lessons item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getLive_time());
                if (item.getStatus().equals("missed")) {
                    holder.setText(status, getResourceString(R.string.class_missed));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(status, getResourceString(R.string.class_ready));
                } else if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("closed")) {//已直播
                    holder.setText(status, getResourceString(R.string.class_closed));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(status, getResourceString(R.string.class_teaching));
                } else {//closed finished billing completed
                    holder.setText(status, getResourceString(R.string.class_over));//已结束
                }
                try {
                    holder.setText(R.id.class_date, format.format(parse.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                holder.setText(R.id.view_playback, getString(R.string.playback_count, item.getLeft_replay_times()));
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                    holder.getView(R.id.view_playback).setVisibility(data.getIs_bought() && item.isReplayable() ? View.VISIBLE : View.GONE);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                    holder.getView(R.id.view_playback).setVisibility(View.GONE);
                }

            }
        };
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter);
    }

    private boolean isFinished(Lessons item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }

    public void setData(RemedialClassDetailBean data) {
        if (data != null && data.getData() != null) {
            this.data = data.getData();
            list.clear();
            list.addAll(data.getData().getLessons());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}



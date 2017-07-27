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
import cn.qatime.player.bean.ExclusiveCourseDetailBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

import static cn.qatime.player.R.id.status;

public class FragmentExclusiveCourseClassList extends BaseFragment {
    private CommonAdapter<ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean> adapterOnLine;
    private CommonAdapter<ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean> adapterOffLine;
    private List<ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean> listOnLine = new ArrayList<>();
    private List<ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean> listOffLine = new ArrayList<>();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private ExclusiveCourseDetailBean.DataBean data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exclusive_course_class_list, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        ListView listViewOnLine = (ListView) view.findViewById(R.id.list1);
        listViewOnLine.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapterOnLine = new CommonAdapter<ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean>(getActivity(), listOnLine, R.layout.item_fragment_exclusive_course_list_online) {
            @Override
            public void convert(ViewHolder holder, ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getStart_time() + " " + item.getEnd_time());
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
                    holder.setText(R.id.class_date, format.format(format.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }

            }
        };
        listViewOnLine.setAdapter(adapterOnLine);


        ListView listViewOffLine = (ListView) view.findViewById(R.id.list2);
        listViewOffLine.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapterOffLine = new CommonAdapter<ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean>(getActivity(), listOffLine, R.layout.item_fragment_exclusive_course_list_offline) {
            @Override
            public void convert(ViewHolder holder, ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getStart_time() + " " + item.getEnd_time());
                holder.setText(R.id.address, "上课地点：" + item.getClass_address());
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
                    holder.setText(R.id.class_date, format.format(format.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.address)).setTextColor(0xff999999);
                    ((TextView) holder.getView(status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.address)).setTextColor(0xff666666);
                    ((TextView) holder.getView(status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }

            }
        };
        listViewOffLine.setAdapter(adapterOffLine);
    }

    private boolean isFinished(ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }

    private boolean isFinished(ExclusiveCourseDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }

    public void setData(ExclusiveCourseDetailBean data) {
        if (data != null && data.getData() != null) {
            this.data = data.getData();
            listOnLine.clear();
            listOnLine.addAll(data.getData().getCustomized_group().getScheduled_lessons());
            listOffLine.clear();
            listOffLine.addAll(data.getData().getCustomized_group().getOffline_lessons());
            if (adapterOnLine != null) {
                adapterOnLine.notifyDataSetChanged();
            }
            if (adapterOffLine != null) {
                adapterOffLine.notifyDataSetChanged();
            }
        }
    }
}



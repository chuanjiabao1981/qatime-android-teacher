package cn.qatime.teacher.player.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseFragment;
import cn.qatime.teacher.player.bean.VideoCoursesDetailsBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.VideoLessonsBean;

/**
 * @author Tianhaoranly
 * @date 2017/5/17 17:11
 * @Description:
 */
public class FragmentVideoCoursesClassList extends BaseFragment {
    private List<VideoLessonsBean> list = new ArrayList<>();
    private CommonAdapter<VideoLessonsBean> adapter;
    private VideoCoursesDetailsBean data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_class_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<VideoLessonsBean>(getActivity(), list, R.layout.item_fragment_video_courses_class_list) {

            @Override
            public void convert(ViewHolder holder, VideoLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.time, "时长" + item.getVideo().getFormat_tmp_duration());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }


    public void setData(VideoCoursesDetailsBean data) {
        this.data = data;
        list.clear();
        list.addAll(data.getData().getVideo_course().getVideo_lessons());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}

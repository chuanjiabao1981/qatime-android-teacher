package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.LocalFilesUploadActivity;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.FileUtil;

/**
 * Created by lenovo on 2017/8/28.
 */

public class FragmentUploadFilesVideo extends BaseFragment {


    private PullToRefreshListView listView;
    private List<File> list = new ArrayList<>();
    public ListViewSelectAdapter adapter;
    public LocalFilesUploadActivity activity ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_files, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initOver = true;
        onShow();
    }

    private void initView() {
        activity = (LocalFilesUploadActivity) getActivity();
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new ListViewSelectAdapter<File>(getActivity(), list, R.layout.item_file_upload_manager,activity.singleMode) {
            @Override
            public void convert(ViewHolder holder, File item, int position) {
                holder.setText(R.id.name, getItem(position).getName());
                holder.setText(R.id.size, DataCleanUtils.getFormatSize(item.length()));
            }
        };
        adapter.setSelectListener(new ListViewSelectAdapter.SelectChangeListener<File>() {
            @Override
            public void update(File item, boolean isChecked) {
                activity.update(item,isChecked);
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!adapter.isCheckboxShow()) {

                } else {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkedView);
                    checkBox.setChecked(!checkBox.isChecked());
                }
            }
        });
        adapter.showCheckbox(true);
//        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                activity.updateCheckbox();
//                return true;
//            }
//        });
    }
    public void onShow() {
        if (!isLoad) {
            if (initOver) {
                initData();
            } else {
                super.onShow();
            }
        }
    }
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.clear();
                list.addAll( FileUtil.getSpecificTypeOfFile(getActivity(), new String[]{"mp4"}));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}

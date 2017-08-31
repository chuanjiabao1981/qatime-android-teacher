package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.FilesUploadActivity;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.adapter.MyExpandableListViewAdapter;
import cn.qatime.player.base.BaseFragment;
import libraryextra.utils.FileUtil;

/**
 * Created by lenovo on 2017/8/28.
 */

public class FragmentUploadFilesDoc extends BaseFragment {

    public MyExpandableListViewAdapter expandAdapter;
    public FilesUploadActivity activity ;
    private ExpandableListView listExpand;
    private ArrayList<String> groupList;
    private ArrayList<List<File>> childList;
    private List<File> word =  new ArrayList<>();
    private List<File> excel =  new ArrayList<>();
    private List<File> ppt =  new ArrayList<>();
    private  List<File> pdf =  new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_files_doc, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (FilesUploadActivity) getActivity();
        initView();
        initOver = true;
        onShow();
    }

    private void initView() {
        listExpand =(ExpandableListView)findViewById(R.id.list_expand);
        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        groupList.add("Word");
        groupList.add("Excel");
        groupList.add("PPT");
        groupList.add("PDF");
        childList.add(word);
        childList.add(excel);
        childList.add(ppt);
        childList.add(pdf);
        expandAdapter = new MyExpandableListViewAdapter(getActivity(),listExpand, groupList, childList);
        listExpand.setAdapter(expandAdapter);
        //重写OnGroupClickListener，实现当展开时，ExpandableListView不自动滚动
        listExpand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    //第二个参数false表示展开时是否触发默认滚动动画
                    parent.expandGroup(groupPosition, false);
                }
                //telling the listView we have handled the group click, and don't want the default actions.
                return true;
            }
        });

        expandAdapter.setSelectListener(new MyExpandableListViewAdapter.SelectChangeListener() {
            @Override
            public void update(File item, boolean isChecked) {
                activity.update(item,isChecked);
            }
        });
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
                word.addAll(FileUtil.getSpecificTypeOfFile(activity, new String[]{"doc", "docx"}));
                excel.addAll(FileUtil.getSpecificTypeOfFile(activity, new String[]{"xls", "xlsx"}));
                ppt.addAll(FileUtil.getSpecificTypeOfFile(activity, new String[]{"ppt"}));
                pdf.addAll(FileUtil.getSpecificTypeOfFile(activity, new String[]{"pdf"}));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        expandAdapter.refresh();
                    }
                });
            }
        }).start();
    }
}

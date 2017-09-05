package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.LocalFilesUploadActivity;
import cn.qatime.player.activity.PersonalMyFilesActivity;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.adapter.MyExpandableListViewAdapter;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.utils.ImageUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/28.
 */

public class FragmentMyFilesDoc extends BaseFragment {

    public MyExpandableListViewAdapter expandAdapter;
    public PersonalMyFilesActivity activity ;
    private ExpandableListView listExpand;
    private ArrayList<String> groupList;
    private ArrayList<List<MyFilesBean.DataBean>> childList;
    private List<MyFilesBean.DataBean> word =  new ArrayList<>();
    private List<MyFilesBean.DataBean> excel =  new ArrayList<>();
    private List<MyFilesBean.DataBean> ppt =  new ArrayList<>();
    private  List<MyFilesBean.DataBean> pdf =  new ArrayList<>();
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_files_doc, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (PersonalMyFilesActivity) getActivity();
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
        expandAdapter = new MyExpandableListViewAdapter<MyFilesBean.DataBean>(getActivity(),listExpand, groupList, childList,activity.singleMode){

            @Override
            public void convert(ViewHolder holder, MyFilesBean.DataBean item, int groupPosition, int childPosition) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.size, DataCleanUtils.getFormatSize(Double.valueOf(item.getFile_size())));
                holder.setText(R.id.time, "上传时间:" + parse.format(new Date(item.getCreated_at()*1000)));

                if (item.getExt_name().equals("doc") || item.getExt_name().equals("docx")) {
                    holder.setImageResource(R.id.image, R.mipmap.word);
                } else if (item.getExt_name().equals("xls") || item.getExt_name().equals("xlsx")) {
                    holder.setImageResource(R.id.image, R.mipmap.excel);
                }else if (item.getExt_name().equals("pdf")) {
                    holder.setImageResource(R.id.image, R.mipmap.pdf);
                }else {
                    holder.setImageResource(R.id.image, R.mipmap.unknown);
                }


            }
        };
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
        listExpand.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                if (!expandAdapter.isCheckboxShow()) {

                } else {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkedView);
                    checkBox.setChecked(!checkBox.isChecked());
                }
                return true;
            }
        });
        expandAdapter.setSelectListener(new MyExpandableListViewAdapter.SelectChangeListener<MyFilesBean.DataBean>(){
            @Override
            public void update(MyFilesBean.DataBean item, boolean isChecked) {
                activity.update(item,isChecked);
            }
        });
        listExpand.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                activity.updateCheckbox();
                return true;
            }
        });
    }

    public void onShow() {
        if (!isLoad) {
            if (initOver) {
                initData(1);
            } else {
                super.onShow();
            }
        }
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("cate", "document");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlFiles + "teachers/" + BaseApplication.getUserId() + "/files", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        word.clear();
                        excel.clear();
                        ppt.clear();
                        pdf.clear();
                        try {
                            MyFilesBean data = JsonUtils.objectFromJson(response.toString(), MyFilesBean.class);
                            if (data != null) {
                                for (MyFilesBean.DataBean item : data.getData()) {
                                    if(item.getExt_name().equals("doc")||item.getExt_name().equals("docx")){
                                        word.add(item);
                                    }else if(item.getExt_name().equals("xls")||item.getExt_name().equals("xlsx")){
                                        excel.add(item);
                                    }else if(item.getExt_name().equals("ppt")){
                                        ppt.add(item);
                                    }else if(item.getExt_name().equals("pdf")){
                                        pdf.add(item);
                                    }

                                }
                                expandAdapter.refresh();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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

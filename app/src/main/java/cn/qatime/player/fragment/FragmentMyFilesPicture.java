package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.PersonalMyFilesActivity;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/28.
 */

public class FragmentMyFilesPicture extends BaseFragment {
    private PullToRefreshListView listView;
    public ListViewSelectAdapter<MyFilesBean.DataBean> adapter;
    private List<MyFilesBean.DataBean> list = new ArrayList<>();
    private PersonalMyFilesActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_files, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initOver = true;
        onShow();
    }

    private void initView() {
        activity = (PersonalMyFilesActivity) getActivity();
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new ListViewSelectAdapter<MyFilesBean.DataBean>(getActivity(), list, R.layout.item_personal_my_files,activity.singleMode) {
            @Override
            public void convert(ViewHolder holder, MyFilesBean.DataBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.size, DataCleanUtils.getFormatSize(Double.valueOf(item.getFile_size())));
            }
        };
        adapter.setSelectListener(new ListViewSelectAdapter.SelectChangeListener<MyFilesBean.DataBean>() {
            @Override
            public void update(MyFilesBean.DataBean item, boolean isChecked) {
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
        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        map.put("cate", "picture");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlFiles + "teachers/" + BaseApplication.getUserId() + "/files", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        list.clear();
                        try {
                            MyFilesBean data = JsonUtils.objectFromJson(response.toString(), MyFilesBean.class);
                            if (data != null) {
                                list.addAll(data.getData());
                                adapter.notifyDataSetChanged();
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

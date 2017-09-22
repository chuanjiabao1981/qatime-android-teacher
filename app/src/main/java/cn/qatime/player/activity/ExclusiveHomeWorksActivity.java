package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.qatime.player.R;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.MyHomeWorksBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/9/8.
 * 专属课作业列表
 */

public class ExclusiveHomeWorksActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    public ListViewSelectAdapter<MyHomeWorksBean.DataBean> adapter;
    private List<MyHomeWorksBean.DataBean> list = new ArrayList();
    private Set<MyHomeWorksBean.DataBean> selectSet = new HashSet<>();
    private TextView rightText;
    private Button bottom;
    private boolean isShowCheckbox;
    public final boolean singleMode = true;
    private int courseId;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int getContentView() {
        return R.layout.activity_exclusive_homeworks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId = getIntent().getIntExtra("courseId", 0);
        initView();
        initData();
    }


    private void initView() {
        setTitle("已布置作业");
        bottom = (Button) findViewById(R.id.bottom_button);
        bottom.setOnClickListener(this);

        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new ListViewSelectAdapter<MyHomeWorksBean.DataBean>(this, list, R.layout.item_homeworks_assign, singleMode) {
            @Override
            public void convert(ViewHolder holder, MyHomeWorksBean.DataBean item, int position) {
                long time = item.getCreated_at() * 1000L;
                holder.setText(R.id.name, item.getTitle())
                        .setText(R.id.submitted_num, item.getTasks_count()+"人已交")
                        .setText(R.id.time,"发布时间"+parse.format(new Date(time)));
            }
        };
//        adapter.setSelectListener(new ListViewSelectAdapter.SelectChangeListener<MyHomeWorksBean.DataBean>() {
//            @Override
//            public void update(MyHomeWorksBean.DataBean item, boolean isChecked) {
//                ExclusiveHomeWorksActivity.this.update(item, isChecked);
//            }
//        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExclusiveHomeWorksActivity.this,HomeWorkDetailActivity.class);
                intent.putExtra("id",list.get(position-1).getId());
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
//        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                updateCheckbox();
//                return true;
//            }
//        });
    }


    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlGroups + courseId + "/homeworks", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        list.clear();
                        try {
                            MyHomeWorksBean data = JsonUtils.objectFromJson(response.toString(), MyHomeWorksBean.class);
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

    @Override
    public void onBackPressed() {
        if (isShowCheckbox) {
            updateCheckbox();
            return;
        }
        super.onBackPressed();
    }


    public void update(MyHomeWorksBean.DataBean item, boolean isChecked) {
        if (isChecked) {
            if (singleMode) {
                selectSet.clear();
            }
            selectSet.add(item);
        } else {
            selectSet.remove(item);
        }
        adapter.updateItem(item, isChecked);


        if (selectSet.size() <= 0) {
            bottom.setText("删除");
        } else {
            bottom.setText("删除(" + selectSet.size() + ")");
        }

    }

    public void updateCheckbox() {
        isShowCheckbox = !isShowCheckbox;
        adapter.showCheckbox(isShowCheckbox);


        if (isShowCheckbox) {
            rightText.setVisibility(View.VISIBLE);
            if (selectSet.size() <= 0) {
                bottom.setText("删除");
            } else {
                bottom.setText("删除(" + selectSet.size() + ")");
            }
        } else {
            rightText.setVisibility(View.GONE);
            bottom.setText("创建新作业");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_button:
                if (isShowCheckbox) {
                    if (selectSet.size() > 0) {
                        deleteSelect();
                    }
                } else {
                    Intent intent = new Intent(this, ExclusiveHomeWorkCreateActivity.class);
                    intent.putExtra("courseId", courseId);
                    startActivityForResult(intent, 0);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initData();
    }

    private void deleteSelect() {
        Object[] items = selectSet.toArray();
        selectSet.clear();
        final MyHomeWorksBean.DataBean item = (MyHomeWorksBean.DataBean) items[0];
        if (item != null) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.DELETE, UrlUtils.urlGroups + courseId + "/files/" + item.getId()
                    , null, new VolleyListener(ExclusiveHomeWorksActivity.this) {
                @Override
                protected void onTokenOut() {
                    tokenOut();
                }

                @Override
                protected void onSuccess(JSONObject response) {
                    Logger.e("删除成功");
                    update(item, false);
                    initData();
                }

                @Override
                protected void onError(JSONObject response) {

                }
            }, new VolleyErrorListener());
            addToRequestQueue(request);
        }
    }
}

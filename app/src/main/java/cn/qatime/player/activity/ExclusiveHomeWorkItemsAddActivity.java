package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.qatime.player.R;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.AttachmentsBean;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.HomeWorkItemBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/9/8.
 */

public class ExclusiveHomeWorkItemsAddActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    public ListViewSelectAdapter<HomeWorkItemBean> adapter;
    private List<HomeWorkItemBean> list = new ArrayList();
    private Set<HomeWorkItemBean> selectSet = new HashSet<>();
    private TextView rightText;
    private Button bottom;
    private boolean isShowCheckbox;
    public final boolean singleMode = true;
    private int courseId;


    @Override
    public int getContentView() {
        return R.layout.activity_exclusive_homework_items;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId = getIntent().getIntExtra("courseId", 0);
        initView();
    }


    private void initView() {
        setTitle("添加内容");
        rightText = (TextView) findViewById(R.id.right_text);
        rightText.setText("添加");
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExclusiveHomeWorkItemsAddActivity.this, HomeWorkItemsAddActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
        bottom = (Button) findViewById(R.id.bottom_button);
        bottom.setOnClickListener(this);

        listView = (PullToRefreshListView) findViewById(R.id.list);
        View inflate = View.inflate(this, R.layout.empty_view, null);
        TextView empty = (TextView) inflate.findViewById(R.id.text_empty);
        empty.setText("尚未添加任何内容");
        listView.setEmptyView(inflate);
        adapter = new ListViewSelectAdapter<HomeWorkItemBean>(this, list, R.layout.item_homework, singleMode) {
            @Override
            public void convert(ViewHolder holder, HomeWorkItemBean item, int position) {
                String num = position + 1 + "";
                if (position < 10) {
                    num = 0 + num;
                }
                holder.setText(R.id.num, num)
                        .setText(R.id.content, item.content);
            }
        };
//        adapter.setSelectListener(new ListViewSelectAdapter.SelectChangeListener<HomeWorkItemBean>() {
//            @Override
//            public void update(HomeWorkItemBean item, boolean isChecked) {
//                ExclusiveHomeWorksActivity.this.update(item, isChecked);
//            }
//        });
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
//        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                updateCheckbox();
//                return true;
//            }
//        });
    }


    @Override
    public void onBackPressed() {
        if (isShowCheckbox) {
            updateCheckbox();
            return;
        }
        super.onBackPressed();
    }


    public void update(HomeWorkItemBean item, boolean isChecked) {
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
            bottom.setText("发布作业");
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
                    //发布作业
                    issueHomework();
                }
                break;
        }
    }

    private void issueHomework() {
        if (list.size() <= 0) {
            Toast.makeText(this, "请添加作业内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("title", getIntent().getStringExtra("title"));
        map.put("task_items_attributes", getContentString());
        JSONObject obj = new JSONObject(map);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlGroups + courseId + "/homeworks", obj,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        setResult(Constant.RESPONSE);
                        finish();
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

    private String getContentString() {
        StringBuilder sb = new StringBuilder("[");
        for (HomeWorkItemBean homeWorkItemBean : list) {
            sb.append("{\"body\":\"")
                    .append(homeWorkItemBean.content)
            .append("\"");
            if ((homeWorkItemBean.audioAttachment!=null&&homeWorkItemBean.audioAttachment.id != null) || homeWorkItemBean.imageItems.size() > 0) {
                sb.append(",")
                        .append("\"quotes_attributes\":[");
                for (AttachmentsBean attachment : homeWorkItemBean.imageItems) {
                    sb.append("{\"attachment_id\":\"")
                            .append(attachment.id)
                            .append("\"},");
                }
                if (homeWorkItemBean.audioAttachment!=null&&homeWorkItemBean.audioAttachment.id != null) {
                    sb.append("{\"attachment_id\":\"")
                            .append(homeWorkItemBean.audioAttachment.id)
                            .append("\"},");
                }
                sb.setCharAt(sb.length() - 1, ']');
            }
            sb.append("},");
        }
        sb.setCharAt(sb.length() - 1, ']');
        Logger.e("sbsb",sb.toString());
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            HomeWorkItemBean item = (HomeWorkItemBean) data.getSerializableExtra("item");
            if (item != null) {
                list.add(item);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void deleteSelect() {
        Object[] items = selectSet.toArray();
        selectSet.clear();
        final HomeWorkItemBean item = (HomeWorkItemBean) items[0];
        if (item != null) {
//            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.DELETE, UrlUtils.urlGroups + courseId + "/files/" + item.getId()
//                    , null, new VolleyListener(ExclusiveHomeWorkItemsAddActivity.this) {
//                @Override
//                protected void onTokenOut() {
//                    tokenOut();
//                }
//
//                @Override
//                protected void onSuccess(JSONObject response) {
//                    Logger.e("删除成功");
//                    update(item, false);
//                    initData();
//                }
//
//                @Override
//                protected void onError(JSONObject response) {
//
//                }
//            }, new VolleyErrorListener());
//            addToRequestQueue(request);
        }
    }
}

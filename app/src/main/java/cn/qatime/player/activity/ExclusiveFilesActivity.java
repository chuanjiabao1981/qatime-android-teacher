package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
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
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.utils.MyVideoThumbLoader;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/15.
 */

public class ExclusiveFilesActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    public ListViewSelectAdapter<MyFilesBean.DataBean> adapter;
    private List<MyFilesBean.DataBean> list = new ArrayList<>();
    private Set<MyFilesBean.DataBean> selectSet = new HashSet<>();
    private TextView rightText;
    private ImageView rightImage;
    private Button bottom;
    private boolean isShowCheckbox;
    public final boolean singleMode = true;
    private int courseId;

    @Override
    public int getContentView() {
        return R.layout.activity_exclusive_files;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId = getIntent().getIntExtra("id", 0);
        initView();
        initData();
    }


    private void initView() {
        setTitle("课件列表");
        rightText = (TextView) findViewById(R.id.right_text);
        rightImage = (ImageView) findViewById(R.id.right);
        rightText.setText("取消");
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckbox();
            }
        });
        rightImage.setImageResource(R.mipmap.calendar);
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckbox();
            }
        });
        bottom = (Button) findViewById(R.id.bottom_button);
        bottom.setOnClickListener(this);

        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new ListViewSelectAdapter<MyFilesBean.DataBean>(this, list, R.layout.item_personal_my_files, singleMode) {
            public MyVideoThumbLoader mVideoThumbLoader = new MyVideoThumbLoader();

            @Override
            public void convert(ViewHolder holder, MyFilesBean.DataBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.size, DataCleanUtils.getFormatSize(Double.valueOf(item.getFile_size())));
                if (item.getExt_name().equals("doc") || item.getExt_name().equals("docx")) {
                    holder.setImageResource(R.id.image, R.mipmap.word);
                } else if (item.getExt_name().equals("xls") || item.getExt_name().equals("xlsx")) {
                    holder.setImageResource(R.id.image, R.mipmap.excel);
                }else if (item.getExt_name().equals("pdf")) {
                    holder.setImageResource(R.id.image, R.mipmap.pdf);
                } else if (item.getExt_name().equals("mp4")) {
                    mVideoThumbLoader.showThumbByAsyncTask(item.getFile_url(), (ImageView) holder.getView(R.id.image));
//                    holder.setImageBitmap(R.id.image, ImageUtil.getVideoThumbnail(item.getFile_url()));
                } else if (item.getExt_name().equals("jpg") || item.getExt_name().equals("png")) {
                    Glide.with(ExclusiveFilesActivity.this).load(item.getFile_url()).placeholder(R.mipmap.unknown).centerCrop().crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.image)));
                } else {
                    holder.setImageResource(R.id.image, R.mipmap.unknown);
                }
            }
        };
        adapter.setSelectListener(new ListViewSelectAdapter.SelectChangeListener<MyFilesBean.DataBean>() {
            @Override
            public void update(MyFilesBean.DataBean item, boolean isChecked) {
                ExclusiveFilesActivity.this.update(item, isChecked);
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
                updateCheckbox();
                return true;
            }
        });
    }


    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("cate", "");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlGroups + courseId + "/files", map), null,
                new VolleyListener(this) {
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

    @Override
    public void onBackPressed() {
        if (isShowCheckbox) {
            updateCheckbox();
            return;
        }
        super.onBackPressed();
    }


    public void update(MyFilesBean.DataBean item, boolean isChecked) {
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
            rightImage.setVisibility(View.GONE);
            if (selectSet.size() <= 0) {
                bottom.setText("删除");
            } else {
                bottom.setText("删除(" + selectSet.size() + ")");
            }
        } else {
            rightImage.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.GONE);
            bottom.setText("添加新文件");
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
                    Intent intent = new Intent(this, ExclusiveFilesUploadActivity.class);
                    intent.putExtra("id", courseId);
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
        final MyFilesBean.DataBean item = (MyFilesBean.DataBean) items[0];
        if (item != null) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.DELETE, UrlUtils.urlGroups + courseId + "/files/" + item.getId()
                    , null, new VolleyListener(ExclusiveFilesActivity.this) {
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

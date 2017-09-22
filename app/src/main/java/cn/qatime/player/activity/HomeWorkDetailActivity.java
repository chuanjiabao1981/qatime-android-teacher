package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.HomeWorkItemBean;
import cn.qatime.player.bean.HomeworkDetailBean;
import cn.qatime.player.bean.MyHomeWorksBean;
import cn.qatime.player.bean.StudentHomeWorksBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.ListViewForScrollView;

/**
 * Created by lenovo on 2017/9/11.
 */

public class HomeWorkDetailActivity extends BaseActivity {

    private StudentHomeWorksBean.DataBean item;
    private TextView homeworkTitle;
    private TextView createTime;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private CommonAdapter<HomeworkDetailBean> adapter;
    private List<HomeworkDetailBean> list;
    private HomeworkDetailBean doingItem;
    private List<HomeWorkItemBean> correctList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        initView();
        String id = getIntent().getStringExtra("id");
        if (StringUtils.isNullOrBlanK(id)) {
            initData();
        } else {
            initDataById(id);
        }
    }

    @Override
    public int getContentView() {
        return 0;
    }

    private void correctHomework() {
        if (correctList.size() < item.getItems().size()) {
            Toast.makeText(this, "请先添加全部批改", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("task_items_attributes", getContentStringWithParentId());
        JSONObject obj = new JSONObject(map);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlLiveStudio + "student_homeworks/" + item.getId() + "/corrections", obj,
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

    private void reCorrectHomework() {
        if (correctList.size() <= 0) {
            Toast.makeText(this, "未添加修改内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("task_items_attributes", getContentStringWithId());
        JSONObject obj = new JSONObject(map);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PATCH, UrlUtils.urlLiveStudio + "corrections/" + item.getCorrection().getId(), obj,
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

    private String getContentStringWithId() {
        StringBuilder sb = new StringBuilder("[");
        for (HomeWorkItemBean homeWorkItemBean : correctList) {
            sb.append("{\"id\":")
                    .append(homeWorkItemBean.parent_id)
                    .append(",\"body\":\"")
                    .append(homeWorkItemBean.content)
                    .append("\"},");
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    private String getContentStringWithParentId() {
        StringBuilder sb = new StringBuilder("[");
        for (HomeWorkItemBean homeWorkItemBean : correctList) {
            sb.append("{\"parent_id\":")
                    .append(homeWorkItemBean.parent_id)
                    .append(",\"body\":\"")
                    .append(homeWorkItemBean.content)
                    .append("\"},");
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    private void initDataById(String id) {
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlHomeworks + id, null, new VolleyListener(HomeWorkDetailActivity.this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                item = new StudentHomeWorksBean.DataBean();
                try {
                    MyHomeWorksBean.DataBean homeworkBean = JsonUtils.objectFromJson(response.getJSONObject("data").getString("homework"), MyHomeWorksBean.DataBean.class);
                    item.setHomework(homeworkBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (item.getHomework() == null) {
                    Toast.makeText(HomeWorkDetailActivity.this, "作业获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                homeworkTitle.setText(item.getHomework().getTitle());
                setTitle(item.getHomework().getTitle());
                long time = item.getHomework().getCreated_at() * 1000L;
                createTime.setText("创建时间 " + parse.format(new Date(time)));
                //融合
                List<MyHomeWorksBean.DataBean.ItemsBean> homeworks = item.getHomework().getItems();
                for (MyHomeWorksBean.DataBean.ItemsBean homework : homeworks) {
                    HomeworkDetailBean homeworkDetailBean = new HomeworkDetailBean();
                    homeworkDetailBean.homework = homework;
                    list.add(homeworkDetailBean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(HomeWorkDetailActivity.this, "作业获取失败", Toast.LENGTH_SHORT).show();

            }
        }, new VolleyErrorListener()));
    }


    private void initData() {
        item = (StudentHomeWorksBean.DataBean) getIntent().getSerializableExtra("item");
        if (item.getStatus().equals("submitted") || item.getStatus().equals("resolved")) {
            setRightText("提交", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getStatus().equals("submitted")) {
                        correctHomework();
                    } else if (item.getStatus().equals("resolved")) {
                        reCorrectHomework();
                    }
                }
            });
            findViewById(R.id.right_text).setVisibility(View.VISIBLE);
        }
        homeworkTitle.setText(item.getTitle());
        setTitle(item.getTitle());
        long time = item.getHomework().getCreated_at() * 1000L;
        createTime.setText("创建时间 " + parse.format(new Date(time)));
        //融合
        List<MyHomeWorksBean.DataBean.ItemsBean> homeworks = item.getHomework().getItems();
        List<StudentHomeWorksBean.DataBean.ItemsBean> items = item.getItems();
        List<StudentHomeWorksBean.DataBean.CorrectionBean.ItemsBean> corrections = null;
        if (item.getCorrection() != null) {
            corrections = item.getCorrection().getItems();
        }
//        答案结果以及批改一一对应的写法
        for (MyHomeWorksBean.DataBean.ItemsBean homework : homeworks) {
            HomeworkDetailBean homeworkDetailBean = new HomeworkDetailBean();
            homeworkDetailBean.homework = homework;
            if (items != null) {
                for (StudentHomeWorksBean.DataBean.ItemsBean itemsBean : items) {
                    if (itemsBean.getParent_id() == homework.getId()) {
                        homeworkDetailBean.answer = itemsBean;
                        if (corrections != null) {
                            for (StudentHomeWorksBean.DataBean.CorrectionBean.ItemsBean correction : corrections) {
                                if (correction.getParent_id() == itemsBean.getId()) {
                                    homeworkDetailBean.correction = correction;
                                }
                            }
                        }
                    }
                }
            }
            list.add(homeworkDetailBean);
        }


//        //答案结果按顺序取值
//        for (MyHomeWorksBean.DataBean.ItemsBean homework : homeworks) {
//            HomeworkDetailBean homeworkDetailBean = new HomeworkDetailBean();
//            homeworkDetailBean.homework = homework;
//            list.add(homeworkDetailBean);
//        }
//        for (int i = 0; i < homeworks.size(); i++) {
//            if (items != null && i < items.size()) {
//                list.get(i).answer = items.get(i);
//            }
//            if (corrections != null && i < corrections.size()) {
//                list.get(i).correction = corrections.get(i);
//            }
//        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            HomeWorkItemBean item = (HomeWorkItemBean) data.getSerializableExtra("item");
            if (item != null) {
                if (doingItem.correction == null) {
                    doingItem.correction = new StudentHomeWorksBean.DataBean.CorrectionBean.ItemsBean();
                }
                doingItem.correction.setBody(item.content);
                correctList.remove(item);//去掉旧数据防止重复id
                correctList.add(item);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        homeworkTitle = (TextView) findViewById(R.id.homework_title);
        createTime = (TextView) findViewById(R.id.create_time);

        list = new ArrayList<>();
        ListViewForScrollView listView = (ListViewForScrollView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new CommonAdapter<HomeworkDetailBean>(this, list, R.layout.item_homework_detail) {

            @Override
            public void convert(ViewHolder holder, final HomeworkDetailBean item, int position) {
                String num = position + 1 + "";
                if (position < 10) {
                    num = 0 + num;
                }
                holder.setText(R.id.num, num)
                        .setText(R.id.title, item.homework.getBody());

                TextView correctHomework = holder.getView(R.id.correct_homework);
                if(StringUtils.isNullOrBlanK(HomeWorkDetailActivity.this.item.getStatus())){
                    return;
                }
                if (HomeWorkDetailActivity.this.item.getStatus().equals("submitted") || HomeWorkDetailActivity.this.item.getStatus().equals("resolved")) {
                    View answerLayout = holder.getView(R.id.answer_layout);
                    answerLayout.setVisibility(View.VISIBLE);
                    correctHomework.setVisibility(View.VISIBLE);
                    correctHomework.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeWorkDetailActivity.this, HomeWorkItemEditActivity.class);
                            doingItem = item;
                            if (HomeWorkDetailActivity.this.item.getStatus().equals("submitted")) {
                                if (item.answer != null) {
                                    intent.putExtra("parent_id", doingItem.answer.getId());
                                } else {//理论上不会出现   id为空
                                    Toast.makeText(mContext, "该结果不支持批改", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else if (HomeWorkDetailActivity.this.item.getStatus().equals("resolved")) {
                                if (doingItem.correction != null) {
                                    intent.putExtra("parent_id", doingItem.correction.getId());
                                } else {//理论上不会出现   id为空
                                    Toast.makeText(mContext, "该结果不支持重新批改", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            startActivityForResult(intent, Constant.REQUEST);
                        }
                    });

                    if (HomeWorkDetailActivity.this.item.getStatus().equals("submitted")) {//已做但是未未批改
                        if (item.answer != null) {//已做并且答案不为空    有id
                            if (!StringUtils.isNullOrBlanK(item.answer.getBody())) {//内容不为空
                                holder.setText(R.id.answer_content, item.answer.getBody());
                            } else {
                                holder.setText(R.id.answer_content, "无");
                            }
                        } else {//已做但是答案为空  理论上不会出现   answer为空id为空
                            correctHomework.setVisibility(View.GONE);//answer的id为空没办法批改
                            holder.setText(R.id.answer_content, "无");
                        }

                        if (item.correction != null) {//已添加批改但未提交   此时item没有id
                            correctHomework.setText("重新批改");
                            View correctionLayout = holder.getView(R.id.correction_layout);
                            correctionLayout.setVisibility(View.VISIBLE);
                            holder.setText(R.id.correction_content, StringUtils.isNullOrBlanK(item.correction.getBody()) ? "无" : item.correction.getBody());
                        }
                    } else if (HomeWorkDetailActivity.this.item.getStatus().equals("resolved")) {//已经批改过了
                        if (item.answer != null) {//已做并且答案不为空    有id
                            if (!StringUtils.isNullOrBlanK(item.answer.getBody())) {//内容不为空
                                holder.setText(R.id.answer_content, item.answer.getBody());
                            } else {
                                holder.setText(R.id.answer_content, "无");
                            }
                        } else {//已做但是答案为空  理论上不会出现   answer为空id为空
//                            correctHomework.setVisibility(View.GONE);//answer的id为空但是correction的id未知，如果有可以批改
                            holder.setText(R.id.answer_content, "无");
                        }
                        View correctionLayout = holder.getView(R.id.correction_layout);
                        correctionLayout.setVisibility(View.VISIBLE);
                        correctHomework.setText("重新批改");
                        if (item.correction != null) {//已批改并且结果不为空    有id
                            if (!StringUtils.isNullOrBlanK(item.correction.getBody())) {//内容不为空
                                holder.setText(R.id.correction_content, item.correction.getBody());
                            } else {
                                holder.setText(R.id.correction_content, "无");
                            }
                        } else {//已批改的状态但是没有批改结果  理论上不会出现   correction为空id为空
                            correctHomework.setVisibility(View.GONE);
                            holder.setText(R.id.correction_content, "无");
                        }
                    }

                }
            }
        };
        listView.setAdapter(adapter);
    }
}

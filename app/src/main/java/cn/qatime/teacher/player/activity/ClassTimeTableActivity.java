package cn.qatime.teacher.player.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.ClassTimeTableBean;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.MonthDateView;

public class ClassTimeTableActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private List<ClassTimeTableBean.DataBean> totalList = new ArrayList<>();
    private CommonAdapter<ClassTimeTableBean.DataBean.LessonsBean> adapter;
    private List<Integer> alertList = new ArrayList<>();
    private MonthDateView monthDateView;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private String date = parse.format(new Date());
    private List<ClassTimeTableBean.DataBean.LessonsBean> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.all_course));
        initview();
        initData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_class_time_table;
    }

    /**
     * 获取联网数据
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isNullOrBlanK(date)) {
            map.put("month", date);
        }
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlTeachers + BaseApplication.getUserId() + "/schedule", map), null,
                new VolleyListener(ClassTimeTableActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        totalList.clear();
                        try {
                            ClassTimeTableBean data = JsonUtils.objectFromJson(response.toString(), ClassTimeTableBean.class);
                            totalList.addAll(data.getData());
                            alertList.clear();
                            for (int i = 0; i < totalList.size(); i++) {
                                alertList.add(parse.parse(totalList.get(i).getDate()).getDate());
                            }
                            monthDateView.setDaysHasThingList(alertList);
                            filterList();
                            listView.onRefreshComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    protected void onError(JSONObject response) {
                        String label = DateUtils.formatDateTime(ClassTimeTableActivity.this, System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
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

    private void filterList() {
        itemList.clear();
        for (int i = 0; i < totalList.size(); i++) {
            Logger.e(date + "--------" + totalList.get(i).getDate());
            if (date.equals(totalList.get(i).getDate())) {
                itemList.addAll(totalList.get(i).getLessons());
                break;
            }
        }
        Logger.e(itemList.size() + "******************");
        adapter.notifyDataSetChanged();
    }


    private void initview() {
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        listView.setEmptyView(View.inflate(ClassTimeTableActivity.this, R.layout.empty_view, null));
        adapter = new CommonAdapter<ClassTimeTableBean.DataBean.LessonsBean>(this, itemList, R.layout.item_class_time_table) {
            @Override
            public void convert(ViewHolder helper, final ClassTimeTableBean.DataBean.LessonsBean item, int position) {
                Glide.with(ClassTimeTableActivity.this).load(item.getCourse_publicize()).placeholder(R.mipmap.error_header_rect).centerCrop().crossFade().dontAnimate().into((ImageView) helper.getView(R.id.image));
////                helper.setText(R.id.course, item.getCourse_name());
                helper.setText(R.id.classname, item.getName());
                try {
                    Date date = parse.parse(item.getClass_date());
                    helper.setText(R.id.class_date, getMonth(date.getMonth()) + "-" + getDay(date.getDate()) + "  ");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                helper.setText(R.id.status, getStatus(item.getStatus()));
                helper.setText(R.id.live_time, item.getLive_time());
                helper.setText(R.id.grade, item.getGrade());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.teacher, "/" + item.getTeacher_name());
                if ("LiveStudio::Lesson".equals(itemList.get(position).getModal_type())) {
                    helper.getView(R.id.modal_type).setBackgroundColor(0xffff4856);
                    helper.setText(R.id.modal_type, "直播课");
                } else if ("LiveStudio::InteractiveLesson".equals(itemList.get(position).getModal_type())) {
                    helper.getView(R.id.modal_type).setBackgroundColor(0xff4856ff);
                    helper.setText(R.id.modal_type, "一对一");
                }
            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });

        ImageView ivLeft = (ImageView) findViewById(R.id.iv_left);
        ImageView ivRight = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        monthDateView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtils.dp2px(this, 35) * 6));
        TextView tvDate = (TextView) findViewById(R.id.date_text);
        View tvToday = findViewById(R.id.date_operator_ll);
        monthDateView.setTextView(tvDate, null);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvToday.setOnClickListener(this);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                getDate();
                filterList();
            }
        });
        monthDateView.setOnCalendarPageChangeListener(new MonthDateView.OnCalendarPageChangeListener() {
            @Override
            public void onPageChange(int type) {
                cancelAll(this);//旧请求取消
                getDate();
                initData();
            }
        });
    }

    private void getDate() {
        date = monthDateView.getmSelYear() + "-" + (monthDateView.getmSelMonth() + 1 < 10 ? "0" + (monthDateView.getmSelMonth() + 1) : monthDateView.getmSelMonth() + 1) + "-" +
                (monthDateView.getmSelDay() < 10 ? "0" + monthDateView.getmSelDay() : monthDateView.getmSelDay());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                monthDateView.onLeftClick();
                getDate();
                initData();
                break;
            case R.id.iv_right:
                monthDateView.onRightClick();
                getDate();
                initData();
                break;
            case R.id.date_operator_ll:
                monthDateView.setTodayToView();
                getDate();
                initData();
                break;
        }
    }


    private String getDay(int day) {
        if (day < 10) {
            return "0" + day;
        }
        return String.valueOf(day);
    }

    private String getMonth(int month) {
        month += 1;
        if (month < 10) {
            return "0" + month;
        }
        return String.valueOf(month);
    }

    private String getStatus(String status) {
        if (status.equals("missed")) {//待补课
            return getResourceString(R.string.class_wait);
        } else if (status.equals("init")) {//未开始
            return getResourceString(R.string.class_init);
        } else if (status.equals("ready")) {//待开课
            return getResourceString(R.string.class_ready);
        } else if (status.equals("teaching")) {//直播中
            return getResourceString(R.string.class_teaching);
        } else if (status.equals("closed")) {//已直播
            return getResourceString(R.string.class_closed);
        } else if (status.equals("paused")) {//直播中
            return getResourceString(R.string.class_teaching);
        } else {
            return getResourceString(R.string.class_over);//已结束
        }
    }
}

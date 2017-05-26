package cn.qatime.teacher.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseFragmentActivity;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.fragment.FragmentClassDetailClassInfo;
import cn.qatime.teacher.player.fragment.FragmentClassDetailClassList;
import cn.qatime.teacher.player.fragment.FragmentClassDetailTeacherInfo;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SimpleViewPagerIndicator;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private TextView name;
    private TextView title;
    private RemedialClassDetailBean data;
    private ViewPager mViewPager;
    private int pager = 0;
    TextView price;
    TextView studentnumber;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#.00");
    private TextView transferPrice;
    private TextView refundAnyTime;
    private TextView freeTaste;
    private TextView couponFree;
    private TextView joinCheap;

    private TextView progress;
    private TextView status;
    private TextView timeToStart;
    private View layoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getIntent().getIntExtra("id", 0);//联网id
        pager = getIntent().getIntExtra("pager", 0);
        initView();
        if (id == 0) {
            Toast.makeText(this, getResources().getString(R.string.no_tutorial_classes_ID), Toast.LENGTH_SHORT).show();
            return;
        }
        initData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_remedial_class_detail;
    }


    public void initView() {
        name = (TextView) findViewById(R.id.name);

        fragBaseFragments.add(new FragmentClassDetailClassInfo());
        fragBaseFragments.add(new FragmentClassDetailTeacherInfo());
        fragBaseFragments.add(new FragmentClassDetailClassList());

        refundAnyTime = (TextView) findViewById(R.id.refund_any_time);
        freeTaste = (TextView) findViewById(R.id.free_taste);
        couponFree = (TextView) findViewById(R.id.coupon_free);
        joinCheap = (TextView) findViewById(R.id.join_cheap);
        progress = (TextView) findViewById(R.id.progress);
        timeToStart = (TextView) findViewById(R.id.time_to_start);
        status = (TextView) findViewById(R.id.status);
        layoutView = findViewById(R.id.layout_view);

        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        transferPrice = (TextView) findViewById(R.id.transfer_price);
        studentnumber = (TextView) findViewById(R.id.student_number);
        findViewById(R.id.announcement).setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teacher_detail), getString(R.string.course_arrangement)};
        mIndicator.setTitles(mTitles);
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragBaseFragments.get(position);
            }
        };

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mIndicator.select(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mIndicator.setOnItemClickListener(new SimpleViewPagerIndicator.OnItemClickListener() {
            @Override
            public void OnClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(pager);
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id, null,
                new VolleyListener(RemedialClassDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);

                        if (data != null && data.getData() != null && data.getData().getLive_start_time() != null) {
                            status.setText(getStatus(data.getData().getStatus()));
                            name.setText(data.getData().getName());
                            title.setText(data.getData().getName());
                            studentnumber.setText(getString(R.string.student_number, data.getData().getBuy_tickets_count()));
                            String price;
                            if (Constant.CourseStatus.finished.equals(data.getData().getStatus()) || Constant.CourseStatus.completed.equals(data.getData().getStatus())) {
                                price = df.format(data.getData().getPrice());
                            } else {
                                price = df.format(data.getData().getCurrent_price());
                            }
                            if (price.startsWith(".")) {
                                price = "0" + price;
                            }
                            RemedialClassDetailActivity.this.price.setText("￥" + price);
                            if (Constant.CourseStatus.teaching.equals(data.getData().getStatus())) {
                                transferPrice.setVisibility(View.VISIBLE);
                            } else {
                                transferPrice.setVisibility(View.GONE);
                            }
                            if (data.getData().getIcons() != null) {
                                if (!data.getData().getIcons().isCoupon_free()) {
                                    couponFree.setVisibility(View.GONE);
                                }
                                if (!data.getData().getIcons().isRefund_any_time()) {
                                    refundAnyTime.setVisibility(View.GONE);
                                }
                                if (!data.getData().getIcons().isFree_taste()) {
                                    freeTaste.setVisibility(View.GONE);
                                }
                                if (!data.getData().getIcons().isJoin_cheap()) {
                                    joinCheap.setVisibility(View.GONE);
                                }
                            }
                            try {
                                if ("init".equals(data.getData().getStatus()) || "published".equals(data.getData().getStatus())) {
                                    long time = parse.parse(data.getData().getLive_start_time()).getTime() - System.currentTimeMillis();
                                    int value = 0;
                                    if (time > 0) {
                                        value = (int) (time / (1000 * 3600 * 24));
                                    }
                                    timeToStart.setVisibility(View.VISIBLE);
                                    progress.setVisibility(View.GONE);
                                    if (value != 0) {
                                        timeToStart.setText("[" + getResources().getString(R.string.item_to_start_main) + value + getResources().getString(R.string.item_day) + "]");
                                    } else {
                                        timeToStart.setText(R.string.ready_to_start);
                                    }
                                    layoutView.setBackgroundColor(0xff00d564);
                                } else if ("teaching".equals(data.getData().getStatus())) {
                                    progress.setVisibility(View.VISIBLE);
                                    timeToStart.setVisibility(View.GONE);
                                    layoutView.setBackgroundColor(0xff00a0e9);
                                    progress.setText(getString(R.string.progress_live, data.getData().getClosed_lessons_count(), data.getData().getPreset_lesson_count()));
                                } else if (Constant.CourseStatus.finished.equals(data.getData().getStatus()) || Constant.CourseStatus.completed.equals(data.getData().getStatus())) {
                                    progress.setVisibility(View.VISIBLE);
                                    timeToStart.setVisibility(View.GONE);
                                    layoutView.setBackgroundColor(0xff999999);
                                    progress.setText(getString(R.string.progress_live, data.getData().getClosed_lessons_count(), data.getData().getPreset_lesson_count()));
                                } else {
                                    layoutView.setVisibility(View.INVISIBLE);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            ((FragmentClassDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentClassDetailTeacherInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentClassDetailClassList) fragBaseFragments.get(2)).setData(data);

                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }

                , new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private String getStatus(String status) {
        if (status == null) {
            return getString(R.string.recruiting);
        }
        if (status.equals("published")) {
            return getString(R.string.recruiting);
        } else if (status.equals("init")) {
            return getString(R.string.recruiting);
        } else if (status.equals("teaching")) {
            return getString(R.string.teaching);
        } else if (status.equals(Constant.CourseStatus.completed) || status.equals(Constant.CourseStatus.finished)) {//未开始
            return getString(R.string.completed);
        }
        return getString(R.string.recruiting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.announcement:
                Intent intent = new Intent(RemedialClassDetailActivity.this, AnnouncementListActivity.class);
                intent.putExtra("id", data.getData().getId());
                intent.putExtra("type", Constant.CoursesType.courses);
                startActivity(intent);
                break;
        }
    }
}

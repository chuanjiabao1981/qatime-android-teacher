package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.ExclusiveCourseDetailBean;
import cn.qatime.player.bean.ExclusiveLessonPlayInfoBean;
import cn.qatime.player.fragment.FragmentExclusiveCourseClassInfo;
import cn.qatime.player.fragment.FragmentExclusiveCourseClassList;
import cn.qatime.player.fragment.FragmentExclusiveCourseTeacherInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.utils.DateUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class ExclusiveCourseDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private TextView name;
    private TextView title;
    private ExclusiveCourseDetailBean data;
    private ViewPager mViewPager;
    private int pager = 0;
    TextView price;
    TextView studentnumber;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#.00");
    private TextView refundAnyTime;
    private TextView freeTaste;
    private TextView couponFree;
    private TextView joinCheap;

    private TextView progress;
    private TextView status;
    private TextView timeToStart;
    private View layoutView;
    private String teamId;
    private PopupWindow pop;
    private ExclusiveLessonPlayInfoBean playInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getIntent().getIntExtra("id", 0);//联网id
        teamId = getIntent().getStringExtra("teamId");
        pager = getIntent().getIntExtra("pager", 0);
        initView();
        if (id == 0) {
            Toast.makeText(this, getResources().getString(R.string.no_tutorial_classes_ID), Toast.LENGTH_SHORT).show();
            return;
        }
        initData();
        initMenu();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_exclusive_course_detail;
    }


    public void initView() {
        name = (TextView) findViewById(R.id.name);

        fragBaseFragments.add(new FragmentExclusiveCourseClassInfo());
        fragBaseFragments.add(new FragmentExclusiveCourseTeacherInfo());
        fragBaseFragments.add(new FragmentExclusiveCourseClassList());

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
        DaYiJsonObjectRequest requestMember = new DaYiJsonObjectRequest(UrlUtils.urlExclusiveCourse + "/" + id + "/play", null,
                new VolleyListener(ExclusiveCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        playInfo = JsonUtils.objectFromJson(response.toString(), ExclusiveLessonPlayInfoBean.class);
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
        addToRequestQueue(requestMember);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlExclusiveCourse + id + "/detail", null,
                new VolleyListener(ExclusiveCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), ExclusiveCourseDetailBean.class);

                        if (data != null && data.getData() != null && data.getData().getCustomized_group() != null) {
                            status.setText(getStatus(data.getData().getCustomized_group().getStatus()));
                            name.setText(data.getData().getCustomized_group().getName());
                            title.setText(data.getData().getCustomized_group().getName());
                            studentnumber.setText(getString(R.string.student_number, data.getData().getCustomized_group().getView_tickets_count()));
                            String price = data.getData().getCustomized_group().getPrice();
                            if (price.startsWith(".")) {
                                price = "0" + price;
                            }
                            ExclusiveCourseDetailActivity.this.price.setText("free".equals(data.getData().getCustomized_group().getSell_type()) ? "免费" : ("￥" + price));

                            if (data.getData().getCustomized_group().getIcons() != null) {
                                if (!data.getData().getCustomized_group().getIcons().isRefund_any_time()) {
                                    refundAnyTime.setVisibility(View.GONE);
                                }
                                if (!data.getData().getCustomized_group().getIcons().isCoupon_free()) {
                                    couponFree.setVisibility(View.GONE);
                                }
                            }
                            try {
                                if ("init".equals(data.getData().getCustomized_group().getStatus()) || "published".equals(data.getData().getCustomized_group().getStatus())) {
                                    int value = DateUtils.daysBetween(Long.valueOf(data.getData().getCustomized_group().getStart_at())*1000, System.currentTimeMillis());
                                    progress.setVisibility(View.GONE);
                                    if (value > 0) {
                                        timeToStart.setVisibility(View.VISIBLE);
                                        timeToStart.setText("[" + getResources().getString(R.string.item_to_start_main) + value + getResources().getString(R.string.item_day) + "]");
                                    } else {
                                        timeToStart.setVisibility(View.GONE);
//                                        timeToStart.setText(R.string.ready_to_start);
                                    }
                                    layoutView.setBackgroundColor(0xff00d564);
                                } else if ("teaching".equals(data.getData().getCustomized_group().getStatus())) {
                                    progress.setVisibility(View.VISIBLE);
                                    timeToStart.setVisibility(View.GONE);
                                    layoutView.setBackgroundColor(0xff00a0e9);
                                    progress.setText(getString(R.string.progress_live, data.getData().getCustomized_group().getClosed_events_count(), data.getData().getCustomized_group().getEvents_count()));
                                } else if (Constant.CourseStatus.finished.equals(data.getData().getCustomized_group().getStatus()) || Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {
                                    progress.setVisibility(View.VISIBLE);
                                    timeToStart.setVisibility(View.GONE);
                                    layoutView.setBackgroundColor(0xff999999);
                                    progress.setText(getString(R.string.progress_live, data.getData().getCustomized_group().getClosed_events_count(), data.getData().getCustomized_group().getEvents_count()));
                                } else {
                                    layoutView.setVisibility(View.INVISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            ((FragmentExclusiveCourseClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentExclusiveCourseTeacherInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentExclusiveCourseClassList) fragBaseFragments.get(2)).setData(data);

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


    private void initMenu() {
        if (pop == null) {
            setRightImage(R.mipmap.exclusive_menu, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.showAsDropDown(v);
                    backgroundAlpha(0.9f);
                }
            });
            View popView = View.inflate(this, R.layout.exclusive_pop_menu, null);

            View menu1 = popView.findViewById(R.id.menu_1);
            View menu2 = popView.findViewById(R.id.menu_2);
            View menu3 = popView.findViewById(R.id.menu_3);
            View menu4 = popView.findViewById(R.id.menu_4);
            View menu5 = popView.findViewById(R.id.menu_5);

            menu3.setVisibility(View.GONE);
            menu1.setOnClickListener(this);
            menu2.setOnClickListener(this);
            menu3.setOnClickListener(this);
            menu4.setOnClickListener(this);
            menu5.setOnClickListener(this);
            pop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });
        }
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_1:
                pop.dismiss();
                break;
            case R.id.menu_2:
                intent = new Intent(this,ExclusiveFilesActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_3:
                pop.dismiss();
                break;
            case R.id.menu_4:
                intent = new Intent(this,ExclusiveStudentHomeWorksActivity.class);
                intent.putExtra("courseId",id);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_5:
                if(playInfo==null){
                    Toast.makeText(this, "未获取到聊天群组", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this,MembersActivity.class);
                intent.putExtra("courseId", id);
                startActivity(intent);
                pop.dismiss();
                pop.dismiss();
                break;
            case R.id.announcement:
                if(playInfo==null){
                    Toast.makeText(this, "未获取到群组信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                 intent = new Intent(ExclusiveCourseDetailActivity.this, AnnouncementListActivity.class);
                intent.putExtra("id", data.getData().getCustomized_group().getId());
                intent.putExtra("teamId", playInfo.getData().getCustomized_group().getChat_team().getTeam_id());
                intent.putExtra(  "type", Constant.CoursesType.exclusive);
                startActivity(intent);
                break;
        }
    }
}

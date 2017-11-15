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
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.LiveLessonDetailBean;
import cn.qatime.player.bean.RemedialClassPlayInfoBean;
import cn.qatime.player.fragment.FragmentClassDetailClassInfo;
import cn.qatime.player.fragment.FragmentClassDetailClassList;
import cn.qatime.player.fragment.FragmentClassDetailTeacherInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.utils.DateUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private TextView name;
    //    private TextView title;
    private LiveLessonDetailBean data;
    private ViewPager mViewPager;
    private int pager = 0;
    TextView price;
    TextView studentnumber;
    //    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
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
    private RemedialClassPlayInfoBean playInfo;
    private PopupWindow pop;

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

    /**
     * @param status 课程状态
     */
    private void initMenu(String status) {
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

            if (Constant.CourseStatus.completed.equals(status)) {
                menu1.setVisibility(View.GONE);
            }
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menu1.setOnClickListener(this);
            menu5.setOnClickListener(this);
            pop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1);
                }
            });
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
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

//        title = (TextView) findViewById(R.id.title);
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
        DaYiJsonObjectRequest requestMember = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id + "/play_info", null,
                new VolleyListener(RemedialClassDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        playInfo = JsonUtils.objectFromJson(response.toString(), RemedialClassPlayInfoBean.class);
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
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id + "/detail", null,
                new VolleyListener(RemedialClassDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), LiveLessonDetailBean.class);
                        if (data != null && data.getData() != null && data.getData().getCourse() != null) {
                            initMenu(data.getData().getCourse().getStatus());
                            status.setText(getStatus(data.getData().getCourse().getStatus()));
                            name.setText(data.getData().getCourse().getName());
                            setTitle(data.getData().getCourse().getName());
                            studentnumber.setText(getString(R.string.student_number, data.getData().getCourse().getBuy_tickets_count()));

                            if (Constant.CourseStatus.published.equals(data.getData().getCourse().getStatus())) {
                                layoutView.setBackgroundColor(0xff00d564);
                                int value = 0;
                                try {
                                    value = DateUtils.daysBetween(data.getData().getCourse().getLive_start_time(), System.currentTimeMillis());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                progress.setVisibility(View.GONE);
                                if (value > 0) {
                                    timeToStart.setVisibility(View.VISIBLE);
                                    timeToStart.setText("[" + getResources().getString(R.string.item_to_start_main) + value + getResources().getString(R.string.item_day) + "]");
                                } else {
                                    timeToStart.setVisibility(View.GONE);
                                }
                            } else if (Constant.CourseStatus.teaching.equals(data.getData().getCourse().getStatus())) {
                                layoutView.setBackgroundColor(0xff00a0e9);
                                timeToStart.setVisibility(View.GONE);
                                progress.setVisibility(View.VISIBLE);
                                progress.setText(getString(R.string.progress_live, data.getData().getCourse().getClosed_lessons_count(), data.getData().getCourse().getLessons_count()));
                            } else if (Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {
                                layoutView.setBackgroundColor(0xff999999);
                                timeToStart.setVisibility(View.GONE);
                                progress.setVisibility(View.VISIBLE);
                                progress.setText(getString(R.string.progress_live, data.getData().getCourse().getClosed_lessons_count(), data.getData().getCourse().getLessons_count()));
                            } else {
                                layoutView.setVisibility(View.GONE);
                            }

                            if (data.getData().getCourse().getSell_type().equals("charge")) {
                                String priceStr;
                                if (Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {
                                    priceStr = df.format(data.getData().getCourse().getPrice());
                                } else {
                                    priceStr = df.format(data.getData().getCourse().getCurrent_price());
                                }
                                if (priceStr.startsWith(".")) {
                                    priceStr = "0" + priceStr;
                                }
                                price.setText("￥" + priceStr);
                                if (Constant.CourseStatus.teaching.equals(data.getData().getCourse().getStatus())) {
                                    transferPrice.setVisibility(View.VISIBLE);
                                } else {
                                    transferPrice.setVisibility(View.GONE);
                                }

                            } else if (data.getData().getCourse().getSell_type().equals("free")) {
                                price.setText("免费");
                            }

                            if (data.getData().getCourse().getIcons() != null) {
                                if (!data.getData().getCourse().getIcons().isCoupon_free()) {
                                    couponFree.setVisibility(View.GONE);
                                }
                                if (!data.getData().getCourse().getIcons().isRefund_any_time()) {
                                    refundAnyTime.setVisibility(View.GONE);
                                }
                                if (!data.getData().getCourse().getIcons().isFree_taste()) {
                                    freeTaste.setVisibility(View.GONE);
                                }
                                if (!data.getData().getCourse().getIcons().isJoin_cheap()) {
                                    joinCheap.setVisibility(View.GONE);
                                }
                            }
                        }

                        ((FragmentClassDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                        ((FragmentClassDetailTeacherInfo) fragBaseFragments.get(1)).setData(data);
                        ((FragmentClassDetailClassList) fragBaseFragments.get(2)).setData(data);

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
                if (playInfo == null) {
                    Toast.makeText(this, "未获取到群组信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(RemedialClassDetailActivity.this, AnnouncementListActivity.class);
                intent.putExtra("id", data.getData().getCourse().getId());
                intent.putExtra("teamId", playInfo.getData().getChat_team().getTeam_id());
                intent.putExtra("type", Constant.CoursesType.courses);
                startActivity(intent);
                break;
            case R.id.menu_1:

                if (playInfo == null || playInfo.getData() == null || playInfo.getData().getChat_team() == null || StringUtils.isNullOrBlanK(playInfo.getData().getChat_team().getTeam_id())) {
                    Toast.makeText(this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(RemedialClassDetailActivity.this, MessageActivity.class);
                intent.putExtra("sessionId", playInfo.getData().getChat_team().getTeam_id());
                intent.putExtra("sessionType", SessionTypeEnum.None);
                intent.putExtra("name", data.getData().getCourse().getName());
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_5:
                if (playInfo == null || playInfo.getData() == null || playInfo.getData().getChat_team() == null) {
                    Toast.makeText(this, "未获取到聊天群组", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(RemedialClassDetailActivity.this, MembersActivity.class);
                intent.putExtra("members", playInfo.getData().getChat_team());
                startActivity(intent);
                pop.dismiss();
                break;
        }
    }
}

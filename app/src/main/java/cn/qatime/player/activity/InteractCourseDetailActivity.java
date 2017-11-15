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
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.fragment.FragmentInteractDetailClassInfo;
import cn.qatime.player.fragment.FragmentInteractDetailClassList;
import cn.qatime.player.fragment.FragmentInteractDetailTeachersInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class InteractCourseDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private TextView name;
    private TextView title;
    private InteractCourseDetailBean data;
    private ViewPager mViewPager;
    private int pager = 0;
    TextView price;
    DecimalFormat df = new DecimalFormat("#.00");
    private TextView refundAnyTime;
    private TextView couponFree;
    private PopupWindow pop;
    private InteractCourseDetailBean playInfo;

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
        return R.layout.activity_interact_course_detail;
    }

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

        fragBaseFragments.add(new FragmentInteractDetailClassInfo());
        fragBaseFragments.add(new FragmentInteractDetailTeachersInfo());
        fragBaseFragments.add(new FragmentInteractDetailClassList());

        refundAnyTime = (TextView) findViewById(R.id.refund_any_time);
        couponFree = (TextView) findViewById(R.id.coupon_free);

        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        findViewById(R.id.announcement).setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teachers_detail), getString(R.string.course_arrangement)};
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


        mViewPager.setOffscreenPageLimit(2);
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

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(pager);
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + id + "/detail", null,
                new VolleyListener(InteractCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);

                        if (data != null && data.getData() != null && data.getData().getInteractive_course().getLive_start_time() != null) {
                            initMenu(data.getData().getInteractive_course().getStatus());
                            name.setText(data.getData().getInteractive_course().getName());
                            title.setText(data.getData().getInteractive_course().getName());
                            String price;
                            price = df.format(Double.valueOf(data.getData().getInteractive_course().getPrice()));
                            if (price.startsWith(".")) {
                                price = "0" + price;
                            }
                            InteractCourseDetailActivity.this.price.setText("￥" + price);

                            ((FragmentInteractDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentInteractDetailTeachersInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentInteractDetailClassList) fragBaseFragments.get(2)).setData(data);
                            if (data.getData().getInteractive_course().getIcons() != null) {
                                if (!data.getData().getInteractive_course().getIcons().isRefund_any_time()) {
                                    refundAnyTime.setVisibility(View.GONE);
                                }

                                if (!data.getData().getInteractive_course().getIcons().isCoupon_free()) {
                                    couponFree.setVisibility(View.GONE);
                                }
                            }

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
        DaYiJsonObjectRequest requestPlay = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + id + "/detail", null,
                new VolleyListener(InteractCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        playInfo = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);

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
        addToRequestQueue(requestPlay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.announcement:
                if (StringUtils.isNullOrBlanK(data.getData().getInteractive_course().getChat_team().getTeam_id())) {
                    Toast.makeText(this, "未获取到群组信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(InteractCourseDetailActivity.this, AnnouncementListActivity.class);
                intent.putExtra("id", data.getData().getInteractive_course().getId());
                intent.putExtra("teamId", data.getData().getInteractive_course().getChat_team().getTeam_id());
                intent.putExtra("type", Constant.CoursesType.interactive);
                startActivity(intent);
                break;
            case R.id.menu_1:

                if (playInfo == null || playInfo.getData() == null || playInfo.getData().getInteractive_course() == null || playInfo.getData().getInteractive_course().getChat_team() == null ||
                        StringUtils.isNullOrBlanK(playInfo.getData().getInteractive_course().getChat_team().getTeam_id())) {
                    Toast.makeText(this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(InteractCourseDetailActivity.this, MessageActivity.class);
                intent.putExtra("sessionId", playInfo.getData().getInteractive_course().getChat_team().getTeam_id());
                intent.putExtra("sessionType", SessionTypeEnum.None);
                intent.putExtra("name", data.getData().getInteractive_course().getName());
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_5:
                if (playInfo == null || playInfo.getData() == null || playInfo.getData().getInteractive_course() == null || playInfo.getData().getInteractive_course().getChat_team() == null) {
                    Toast.makeText(this, "未获取到聊天群组", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(InteractCourseDetailActivity.this, MembersActivity.class);
                intent.putExtra("members", playInfo.getData().getInteractive_course().getChat_team());
                startActivity(intent);
                pop.dismiss();
                break;
        }
    }
}

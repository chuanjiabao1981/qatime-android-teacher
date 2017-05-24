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
import java.util.ArrayList;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseFragmentActivity;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.fragment.FragmentInteractDetailClassInfo;
import cn.qatime.teacher.player.fragment.FragmentInteractDetailClassList;
import cn.qatime.teacher.player.fragment.FragmentInteractDetailTeachersInfo;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SimpleViewPagerIndicator;

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
    private TextView joinCheap;

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


    public void initView() {
        name = (TextView) findViewById(R.id.name);

        fragBaseFragments.add(new FragmentInteractDetailClassInfo());
        fragBaseFragments.add(new FragmentInteractDetailTeachersInfo());
        fragBaseFragments.add(new FragmentInteractDetailClassList());

        refundAnyTime = (TextView) findViewById(R.id.refund_any_time);
        joinCheap = (TextView) findViewById(R.id.join_cheap);

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
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + "/" + id, null,
                new VolleyListener(InteractCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);

                        if (data != null && data.getData() != null && data.getData().getLive_start_time() != null) {
                            name.setText(data.getData().getName());
                            title.setText(data.getData().getName());
                            String price;
                            price = df.format(Double.valueOf(data.getData().getPrice()));
                            if (price.startsWith(".")) {
                                price = "0" + price;
                            }
                            InteractCourseDetailActivity.this.price.setText("￥" + price);

                            ((FragmentInteractDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentInteractDetailTeachersInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentInteractDetailClassList) fragBaseFragments.get(2)).setData(data);

                            if (data.getData().getIcons() != null) {
                                if (!data.getData().getIcons().isRefund_any_time()) {
                                    refundAnyTime.setVisibility(View.INVISIBLE);
                                }
                                if (!data.getData().getIcons().isJoin_cheap()) {
                                    joinCheap.setVisibility(View.INVISIBLE);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.announcement:
                Intent intent = new Intent(InteractCourseDetailActivity.this, AnnouncementListActivity.class);
                intent.putExtra("id", data.getData().getId());
                intent.putExtra("type", Constant.CoursesType.interactive);
                startActivity(intent);
                break;
        }
    }
}

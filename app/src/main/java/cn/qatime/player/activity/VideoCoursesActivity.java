package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import cn.qatime.player.fragment.FragmentVideoCoursesClassInfo;
import cn.qatime.player.fragment.FragmentVideoCoursesClassList;
import cn.qatime.player.fragment.FragmentVideoCoursesTeacherInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/5/17 17:11
 * @Description:
 */
public class VideoCoursesActivity extends BaseActivity {
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private ViewPager mViewPager;
    private int id;
    private VideoCoursesDetailsBean data;
    private DecimalFormat df = new DecimalFormat("#.00");
    private TextView name;
    private TextView price;
    private TextView transferPrice;
    private TextView studentNumber;
    private TextView freeTaste;
    private TextView joinCheap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses);
        id = getIntent().getIntExtra("id", 0);//联网id
        initView();
        initData();
    }

    @Override
    public int getContentView() {
        return 0;
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlVideoCourses + id + "/detail", null,
                new VolleyListener(VideoCoursesActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), VideoCoursesDetailsBean.class);

                        if (data != null && data.getData() != null && data.getData().getVideo_course() != null) {
                            name.setText(data.getData().getVideo_course().getName());
                            setTitle(data.getData().getVideo_course().getName());
                            studentNumber.setText("学习人数" + data.getData().getVideo_course().getBuy_tickets_count());

                            if (data.getData().getVideo_course().getSell_type().equals("charge")) {
                                String price = df.format(Float.valueOf(data.getData().getVideo_course().getPrice()));
                                if (price.startsWith(".")) {
                                    price = "0" + price;
                                }
                                VideoCoursesActivity.this.price.setText("￥" + price);
                                if (Constant.CourseStatus.teaching.equals(data.getData().getVideo_course().getStatus())) {
                                    transferPrice.setVisibility(View.VISIBLE);
                                } else {
                                    transferPrice.setVisibility(View.GONE);
                                }

                            } else if (data.getData().getVideo_course().getSell_type().equals("free")) {
                                transferPrice.setText("免费");
                                transferPrice.setVisibility(View.VISIBLE);
                                price.setVisibility(View.GONE);
                            }

                            if (data.getData().getVideo_course().getIcons() != null) {
                                if (!data.getData().getVideo_course().getIcons().isFree_taste()) {
                                    freeTaste.setVisibility(View.GONE);
                                }
                                if (!data.getData().getVideo_course().getIcons().isJoin_cheap()) {
                                    joinCheap.setVisibility(View.GONE);
                                }
                            }
                            ((FragmentVideoCoursesClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentVideoCoursesTeacherInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentVideoCoursesClassList) fragBaseFragments.get(2)).setData(data);

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

    private void initView() {
        freeTaste = (TextView) findViewById(R.id.free_taste);
        joinCheap = (TextView) findViewById(R.id.join_cheap);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        transferPrice = (TextView) findViewById(R.id.transfer_price);
        studentNumber = (TextView) findViewById(R.id.student_number);


        fragBaseFragments.add(new FragmentVideoCoursesClassInfo());
        fragBaseFragments.add(new FragmentVideoCoursesTeacherInfo());
        fragBaseFragments.add(new FragmentVideoCoursesClassList());

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teacher_detail), "上课安排"};
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

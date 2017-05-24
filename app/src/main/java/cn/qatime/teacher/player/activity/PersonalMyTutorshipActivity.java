package cn.qatime.teacher.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseFragment;
import cn.qatime.teacher.player.base.BaseFragmentActivity;
import cn.qatime.teacher.player.fragment.FragmentTutorshipEnrollment;
import cn.qatime.teacher.player.fragment.FragmentTutorshipInClass;
import cn.qatime.teacher.player.fragment.FragmentTutorshipOver;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * 我的辅导
 */
public class PersonalMyTutorshipActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的直播课");
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_my_tutorship;
    }


    private void initView() {
        fragBaseFragments.add(new FragmentTutorshipEnrollment());
        fragBaseFragments.add(new FragmentTutorshipInClass());
        fragBaseFragments.add(new FragmentTutorshipOver());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, 0xffff5842);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tableout_personal_my_tutor, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(3);
        ((BaseFragment) fragBaseFragments.get(0)).onShow();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(0);
            }
        }, 200);
    }

}

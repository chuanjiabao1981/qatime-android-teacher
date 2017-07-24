package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.FragmentExclusiveAll;
import cn.qatime.player.fragment.FragmentExclusiveInClass;
import cn.qatime.player.fragment.FragmentExclusiveOver;
import cn.qatime.player.fragment.FragmentExclusivePublished;
import libraryextra.view.FragmentLayoutWithLine;

public class PersonalMyExclusiveActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的专属课");
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_my_exclusive;
    }


    private void initView() {
//        Exclusive
        fragBaseFragments.add(new FragmentExclusiveAll());
        fragBaseFragments.add(new FragmentExclusivePublished());
        fragBaseFragments.add(new FragmentExclusiveInClass());
        fragBaseFragments.add(new FragmentExclusiveOver());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, getResources().getColor(R.color.colorPrimary));
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_personal_my_exclusive, 0x0311);
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

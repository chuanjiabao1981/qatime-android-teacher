package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.fragment.FragmentClassTableClosed;
import cn.qatime.player.fragment.FragmentClassTableUnclosed;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author lungtify
 * @Time 2016/10/27 10:20
 * @Describe
 */
public class ClassTableActivity extends BaseActivity {

    FragmentLayoutWithLine fragmentlayout;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("课程表");
        setRightImage(R.mipmap.calendar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassTableActivity.this, ClassTimeTableActivity.class));
            }
        });

        fragBaseFragments.add(new FragmentClassTableUnclosed());
        fragBaseFragments.add(new FragmentClassTableClosed());


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
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_class_timetable, 0x0911);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(0);
            }
        }, 200);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_class_table;
    }
}

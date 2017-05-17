package cn.qatime.teacher.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseFragmentActivity;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * 我的一对一
 */
public class PersonalMyInteractActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的一对一");

        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_my_interact;
    }

    private void initView() {
        findViewById(R.id.right).setVisibility(View.GONE);

        fragBaseFragments.add(new FragmentInteractStudying());
        fragBaseFragments.add(new FragmentInteractOver());

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

//                if (position == 4) {
//                    findViewById(R.id.right).setVisibility(View.VISIBLE);
//                } else {
//                    findViewById(R.id.right).setVisibility(View.GONE);
//                }
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_personal_my_interact, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(0);
            }
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

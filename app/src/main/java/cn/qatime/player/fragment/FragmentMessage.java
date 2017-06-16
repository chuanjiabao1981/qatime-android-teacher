package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author lungtify
 * @Time 2016/10/25 14:46
 * @Describe
 */
public class FragmentMessage extends BaseFragment {
    FragmentLayoutWithLine fragmentlayout;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        fragBaseFragments.add(new FragmentNews());
        fragBaseFragments.add(new FragmentSystemNews());


        fragmentlayout = (FragmentLayoutWithLine) view.findViewById(R.id.fragmentlayout);

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
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_fragment_news, 0x0912);

    }

    /**
     * 云信发来的sessionid
     *
     * @param message 会话
     */
    public void setMessage(IMMessage message) {
        if (fragBaseFragments != null && fragBaseFragments.size() > 0) {
            ((FragmentNews) fragBaseFragments.get(0)).setMessage(message);

        }
    }
}

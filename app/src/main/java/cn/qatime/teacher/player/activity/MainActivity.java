package cn.qatime.teacher.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseFragmentActivity;
import cn.qatime.teacher.player.fragment.FragmentMessage;
import cn.qatime.teacher.player.fragment.FragmentPersonalCenter;
import cn.qatime.teacher.player.utils.Constant;
import libraryextra.utils.StringUtils;
import libraryextra.view.FragmentLayout;

public class MainActivity extends BaseFragmentActivity {


    private int[] tab_img = {R.id.tab_img1, R.id.tab_img2};
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private int tabImages[][] = {
            {R.mipmap.tab_message_1, R.mipmap.tab_message_2},
            {R.mipmap.tab_person_1, R.mipmap.tab_person_2}};


    FragmentLayout fragmentlayout;
    public ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File(Constant.CACHEPATH);
        if (!file.mkdirs()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    private void initView() {
        //重置
        fragBaseFragments.clear();
        if (fragmentlayout != null) {
            fragmentlayout.reset();
        }

        //添加fragment
        fragBaseFragments.add(new FragmentMessage());
        fragBaseFragments.add(new FragmentPersonalCenter());

        fragmentlayout = (FragmentLayout) findViewById(R.id.fragmentlayout);
        fragmentlayout.setScorllToNext(false);
        fragmentlayout.setScorll(false);
        fragmentlayout.setWhereTab(0);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayout.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xffafaa9a);
                ((ImageView) lastTabView.findViewById(tab_img[lastPosition])).setImageResource(tabImages[lastPosition][1]);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xfff45050);
                ((ImageView) currentTabView.findViewById(tab_img[position])).setImageResource(tabImages[position][0]);
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout, 0x1000);
        fragmentlayout.getViewPager().setOffscreenPageLimit(1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!StringUtils.isNullOrBlanK(intent.getStringExtra("out")) || (!StringUtils.isNullOrBlanK(intent.getStringExtra("sign")))) {
            Intent start = new Intent(this, LoginActivity.class);
            if (!StringUtils.isNullOrBlanK(intent.getStringExtra("sign"))) {
                start.putExtra("sign", intent.getStringExtra("sign"));
            }
            startActivity(start);
            finish();
        } else {
            //云信通知消息
            setIntent(intent);
            parseIntent();
        }
    }

    private void parseIntent() {
        Intent intent = getIntent();
        /**     * 解析通知栏发来的云信消息     */
        if (intent != null && intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            if (messages != null && messages.size() == 1) {
                final IMMessage message = messages.get(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (message != null) {
                            if (fragmentlayout != null) {
                                fragmentlayout.setCurrenItem(2);
                            }
                            if (((FragmentMessage) fragBaseFragments.get(0)) != null) {
                                ((FragmentMessage) fragBaseFragments.get(0)).setMessage(message);
                            }
                        }
                    }
                }, 500);
            }
        }
//        else if (intent != null && intent.hasExtra("type") && intent.getStringExtra("type").equals("system_message")) {//转到系统消息页面
//            if (fragmentlayout != null) {
//                fragmentlayout.setCurrenItem(2);
//            }
//            if (((FragmentMessage) fragBaseFragments.get(0)) != null) {
//                Logger.e("main转到系统消息");
//                ((FragmentMessage) fragBaseFragments.get(0)).toSystemMessage();
//            }
//        }
    }

    boolean flag = false;

    @Override
    public void onBackPressed() {
        if (!flag) {
            Toast toast = Toast.makeText(this, getResources().getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            flag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag = false;
                }
            }, 2500);
        } else {
            this.finish();
        }
    }

}

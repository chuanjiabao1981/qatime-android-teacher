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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.base.BaseFragmentActivity;
import cn.qatime.teacher.player.bean.BusEvent;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.fragment.FragmentMessage;
import cn.qatime.teacher.player.fragment.FragmentPersonalCenter;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.bean.CashAccountBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyListener;
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
        EventBus.getDefault().register(this);
        checkUserInfo();
        initView();
    }

    /**
     * 检查用户信息是否完整
     */
    private void checkUserInfo() {
//        if (BaseApplication.isLogined()) {

        DaYiJsonObjectRequest request1 = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null, new VolleyListener(MainActivity.this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                Logger.e(bean.toString());
                int school = bean.getData().getSchool();
                String url = bean.getData().getAvatar_url();
                String name = bean.getData().getName();
                String category = bean.getData().getCategory();
                String subject = bean.getData().getSubject();
                String province = bean.getData().getProvince();
                String city = bean.getData().getCity();
                String teaching_years = bean.getData().getTeaching_years();
                String desc = bean.getData().getDesc();
                if (school == 0 ||
                        StringUtils.isNullOrBlanK(url) ||
                        StringUtils.isNullOrBlanK(name) ||
                        StringUtils.isNullOrBlanK(category) ||
                        StringUtils.isNullOrBlanK(subject) ||
                        StringUtils.isNullOrBlanK(province) ||
                        StringUtils.isNullOrBlanK(city) ||
                        StringUtils.isNullOrBlanK(teaching_years) ||
                        StringUtils.isNullOrBlanK(desc)
                        ) {

                    Toast.makeText(MainActivity.this, getResourceString(R.string.please_set_information), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, RegisterPerfectActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(MainActivity.this, getResourceString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                BaseApplication.clearToken();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                BaseApplication.clearToken();
            }
        });
        addToRequestQueue(request1);
//        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    private void initView() {
        refreshCashAccount();

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
//        checkUserInfo();
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

    @Subscribe
    public void onEvent(BusEvent event) {
        if (event == BusEvent.REFRESH_CASH_ACCOUNT) {
            refreshCashAccount();
        }
    }

    private void refreshCashAccount() {
            addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlpayment + BaseApplication.getUserId() + "/cash", null, new VolleyListener(MainActivity.this) {

                @Override
                protected void onTokenOut() {

                }

                @Override
                protected void onSuccess(JSONObject response) {
                    CashAccountBean cashAccount = JsonUtils.objectFromJson(response.toString(), CashAccountBean.class);
                    BaseApplication.setCashAccount(cashAccount);
                    EventBus.getDefault().post(BusEvent.ON_REFRESH_CASH_ACCOUNT);
                }

                @Override
                protected void onError(JSONObject response) {
                    Toast.makeText(MainActivity.this, getResourceString(R.string.get_wallet_info_error), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

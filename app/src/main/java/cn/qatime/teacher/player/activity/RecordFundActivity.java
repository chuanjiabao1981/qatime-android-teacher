package cn.qatime.teacher.player.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseFragment;
import cn.qatime.teacher.player.base.BaseFragmentActivity;
import cn.qatime.teacher.player.fragment.FundRecordEarningF;
import cn.qatime.teacher.player.fragment.FundRecordWithdrawCashF;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 17:14
 * @Description:
 */
public class RecordFundActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int page;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.my_record_fund));
        setRightImage(R.mipmap.phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog == null) {
                    View view = View.inflate(RecordFundActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + getResourceString(R.string.customer_service_phone_number) + "?");
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button confirm = (Button) view.findViewById(R.id.confirm);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResourceString(R.string.customer_service_phone_number)));
                            if (ActivityCompat.checkSelfPermission(RecordFundActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(RecordFundActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordFundActivity.this);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else {
                    alertDialog.show();
                }
            }
        });
        page = getIntent().getIntExtra("page", 0);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_record_fund;
    }


    private void initView() {
//        fragBaseFragments.add(new FundRecordRechargeF());
        fragBaseFragments.add(new FundRecordWithdrawCashF());
        fragBaseFragments.add(new FundRecordEarningF());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, 0xffff9999);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_fund_record, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(page);
            }
        }, 200);

    }
}

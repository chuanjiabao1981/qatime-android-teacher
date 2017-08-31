package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.fragment.FragmentMyFilesAll;
import cn.qatime.player.fragment.FragmentMyFilesDoc;
import cn.qatime.player.fragment.FragmentMyFilesOther;
import cn.qatime.player.fragment.FragmentMyFilesPicture;
import cn.qatime.player.fragment.FragmentMyFilesVideo;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * Created by lenovo on 2017/8/28.
 */

public class PersonalMyFilesActivity extends BaseActivity implements View.OnClickListener {

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4, R.id.tab_text5};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Set<MyFilesBean.DataBean> selectSet = new HashSet<>();
    private FragmentMyFilesAll fragmentMyFilesAll;
    private boolean isShowCheckbox;
    private TextView rightText;
    private ImageView rightImage;
    private FragmentMyFilesVideo fragmentMyFilesVideo;
    private FragmentMyFilesPicture fragmentMyFilesPicture;
    private FragmentMyFilesDoc fragmentMyFilesDoc;
    private FragmentMyFilesOther fragmentMyFilesOther;
    private Button bottom;
    public final boolean singleMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的文件");
        rightText = (TextView) findViewById(R.id.right_text);
        rightImage = (ImageView) findViewById(R.id.right);
        rightText.setText("取消");
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckbox();
            }
        });
        rightImage.setImageResource(R.mipmap.calendar);
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckbox();
            }
        });
        bottom = (Button) findViewById(R.id.bottom_button);
        bottom.setOnClickListener(this);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_my_files;
    }


    private void initView() {
        fragmentMyFilesAll = new FragmentMyFilesAll();
        fragmentMyFilesVideo = new FragmentMyFilesVideo();
        fragmentMyFilesPicture = new FragmentMyFilesPicture();
        fragmentMyFilesDoc = new FragmentMyFilesDoc();
        fragmentMyFilesOther = new FragmentMyFilesOther();
        fragBaseFragments.add(fragmentMyFilesAll);
        fragBaseFragments.add(fragmentMyFilesVideo);
        fragBaseFragments.add(fragmentMyFilesPicture);
        fragBaseFragments.add(fragmentMyFilesDoc);
        fragBaseFragments.add(fragmentMyFilesOther);

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
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_personal_my_files, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(5);
    }

    @Override
    public void onBackPressed() {
        if (isShowCheckbox) {
            updateCheckbox();
            return;
        }
        super.onBackPressed();
    }


    public void update(MyFilesBean.DataBean item, boolean isChecked) {
        if (isChecked) {
            if (singleMode) {
                selectSet.clear();
            }
            selectSet.add(item);
        } else {
            selectSet.remove(item);
        }

        fragmentMyFilesAll.adapter.updateItem(item, isChecked);
        fragmentMyFilesVideo.adapter.updateItem(item, isChecked);
        fragmentMyFilesPicture.adapter.updateItem(item, isChecked);
        fragmentMyFilesDoc.adapter.updateItem(item, isChecked);
        fragmentMyFilesOther.adapter.updateItem(item, isChecked);

        if (selectSet.size() <= 0) {
            bottom.setText("删除");
        } else {
            bottom.setText("删除(" + selectSet.size() + ")");
        }

    }

    public void updateCheckbox() {
        isShowCheckbox = !isShowCheckbox;
        fragmentMyFilesAll.adapter.showCheckbox(isShowCheckbox);
        fragmentMyFilesVideo.adapter.showCheckbox(isShowCheckbox);
        fragmentMyFilesPicture.adapter.showCheckbox(isShowCheckbox);
        fragmentMyFilesDoc.adapter.showCheckbox(isShowCheckbox);
        fragmentMyFilesOther.adapter.showCheckbox(isShowCheckbox);

        if (isShowCheckbox) {
            rightText.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.GONE);
            if (selectSet.size() <= 0) {
                bottom.setText("删除");
            } else {
                bottom.setText("删除(" + selectSet.size() + ")");
            }
        } else {
            rightImage.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.GONE);
            bottom.setText("添加新文件");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_button:
                if (isShowCheckbox) {
                    if (selectSet.size() > 0) {
                        deleteSelect();
                    }
                } else {
                    Intent intent = new Intent(this, FilesUploadActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void deleteSelect() {
        // TODO: 2017/8/29 批量删除
        Object[] items = selectSet.toArray();
        selectSet.clear();
        final MyFilesBean.DataBean item = (MyFilesBean.DataBean) items[0];
        if (item != null) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.DELETE,
                    UrlUtils.urlFiles + "files/" + item.getId(), null, new VolleyListener(PersonalMyFilesActivity.this) {
                @Override
                protected void onTokenOut() {
                    tokenOut();
                }

                @Override
                protected void onSuccess(JSONObject response) {
                    Logger.e("删除成功");
                    update(item, false);
                    fragmentMyFilesAll.onShow();
                    fragmentMyFilesVideo.onShow();
                    fragmentMyFilesPicture.onShow();
                    fragmentMyFilesDoc.onShow();
                    fragmentMyFilesOther.onShow();
                }

                @Override
                protected void onError(JSONObject response) {

                }
            }, new VolleyErrorListener());
            addToRequestQueue(request);

        }
    }
}

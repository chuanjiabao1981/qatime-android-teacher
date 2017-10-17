package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.fragment.FragmentUploadFilesDoc;
import cn.qatime.player.fragment.FragmentUploadFilesOther;
import cn.qatime.player.fragment.FragmentUploadFilesPicture;
import cn.qatime.player.fragment.FragmentUploadFilesVideo;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.rx.HttpManager;
import libraryextra.rx.body.ProgressResponseCallBack;
import libraryextra.rx.callback.SimpleCallBack;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * Created by lenovo on 2017/8/28.
 */

public class LocalFilesUploadActivity extends BaseActivity implements View.OnClickListener {

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Set<File> selectSet = new HashSet<>();
    private final boolean isShowCheckbox = true;
    public final boolean singleMode = true;
    private Button upload;
    private TextView size;
    private FragmentUploadFilesVideo fragmentUploadFilesVideo;
    private FragmentUploadFilesPicture fragmentUploadFilesPicture;
    private FragmentUploadFilesDoc fragmentUploadFilesDoc;
    private FragmentUploadFilesOther fragmentUploadFilesOther;
    private int courseId;
    private String selectAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("本地文件");
        setRightText("说明", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalFilesUploadActivity.this, UploadExplainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.right_text).setVisibility(View.VISIBLE);
        courseId = getIntent().getIntExtra("id", 0);
        if (courseId == 0) {
            selectAction = "上传";
        } else {
            selectAction = "上传";
        }
        initView();
        upload = (Button) findViewById(R.id.upload);
        upload.setText(selectAction+"(" + 0 + "/1)");
        size = (TextView) findViewById(R.id.size);
        upload.setOnClickListener(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_upload_files;
    }

    private void initView() {
        findViewById(R.id.right).setVisibility(View.GONE);

        fragmentUploadFilesVideo = new FragmentUploadFilesVideo();
        fragmentUploadFilesPicture = new FragmentUploadFilesPicture();
        fragmentUploadFilesDoc = new FragmentUploadFilesDoc();
        fragmentUploadFilesOther = new FragmentUploadFilesOther();
        fragBaseFragments.add(fragmentUploadFilesVideo);
        fragBaseFragments.add(fragmentUploadFilesPicture);
        fragBaseFragments.add(fragmentUploadFilesDoc);
        fragBaseFragments.add(fragmentUploadFilesOther);

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
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_upload_files, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(5);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(0);
            }
        }, 200);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                Object[] items = selectSet.toArray();
                selectSet.clear();
                File item = (File) items[0];
                Intent intent = new Intent(this,FileUploadActivity.class);
                intent.putExtra("file",item);
                intent.putExtra("id",courseId);
                startActivityForResult(intent,0);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(0);
    }

    public void update(File item, boolean isChecked) {
        if (isChecked) {
            if (singleMode) {
                selectSet.clear();
                fragmentUploadFilesVideo.adapter.selectAll(false);
                fragmentUploadFilesPicture.adapter.selectAll(false);
                fragmentUploadFilesDoc.expandAdapter.selectAll(false);
                fragmentUploadFilesOther.adapter.selectAll(false);
            }
            selectSet.add(item);
        } else {
            selectSet.remove(item);
        }

        fragmentUploadFilesVideo.adapter.updateItem(item, isChecked);
        fragmentUploadFilesPicture.adapter.updateItem(item, isChecked);
        fragmentUploadFilesDoc.expandAdapter.updateItem(item, isChecked);
        fragmentUploadFilesOther.adapter.updateItem(item, isChecked);

        if (selectSet.size() <= 0) {
            upload.setText(selectAction+"(" + 0 + "/1)");
        } else {
            upload.setText(selectAction+"(" + selectSet.size() + "/1)");
        }

    }

//    public void updateCheckbox() {
//        isShowCheckbox = !isShowCheckbox;
//        fragmentUploadFilesVideo.expandAdapter.showCheckbox(isShowCheckbox);
//        fragmentUploadFilesPicture.expandAdapter.showCheckbox(isShowCheckbox);
//        fragmentUploadFilesDoc.expandAdapter.showCheckbox(isShowCheckbox);
//        fragmentUploadFilesOther.expandAdapter.showCheckbox(isShowCheckbox);
//        if (isShowCheckbox) {
//            if (selectSet.size() <= 0) {
//                upload.setText("上传(" + 0 + "/1)");
//            } else {
//                upload.setText("上传(" + selectSet.size() + "/1)");
//            }
//        }
//    }


}

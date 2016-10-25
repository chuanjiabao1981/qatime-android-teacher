package cn.qatime.teacher.player.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.orhanobut.logger.Logger;


import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import libraryextra.utils.StringUtils;

/**
 * 起始页
 */
public class StartActivity extends BaseActivity implements View.OnClickListener {
//    private AlertDialog alertDialog;
//    private String downLoadLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        GetGradeslist();//加载年纪列表
        checkUpdate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_start;
    }

    private void checkUpdate() {
//        //TODO 检查版本，进行更新
//        Map<String, String> map = new HashMap<>();
//        map.put("category", "student_client");
//        map.put("platform", "android");
//        map.put("version", AppUtils.getVersionName(this));
////        map.put("version", "0.0.1");
//        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlcheckUpdate, map), null, new VolleyListener(this) {
//            @Override
//            protected void onTokenOut() {
//
//            }
//
//            @Override
//            protected void onSuccess(JSONObject response) {
//                if (response.isNull("data")) {
//                    startApp();
//                    BaseApplication.newVersion = false;
//                } else {
//                    BaseApplication.newVersion = true;
//                    Logger.e(response.toString());
//                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
//                    final View view = View.inflate(StartActivity.this, R.layout.dialog_check_update, null);
//                    Button down = (Button) view.findViewById(R.id.download);
//                    View x = view.findViewById(R.id.image_x);
//                    TextView newVersion = (TextView) view.findViewById(R.id.new_version);
//
//                    TextView desc = (TextView) view.findViewById(R.id.desc);
//                    alertDialog = builder.create();
//                    try {
//                        x.setOnClickListener(StartActivity.this);
//                        final boolean enforce = response.getJSONObject("data").getBoolean("enforce");
//                        if (enforce) {
//                            TextView pleaseUpdate = (TextView) view.findViewById(R.id.please_update);
//                            pleaseUpdate.setVisibility(View.VISIBLE);
////                            Toast.makeText(StartActivity.this, "重大更新，请先进行升级", Toast.LENGTH_SHORT).show();
////                            alertDialog.setCancelable(false);
//                        }
//                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                                if (enforce) {
//                                    finish();
//                                } else {
//                                    startApp();
//                                }
//                            }
//                        });
//                        String descStr = response.getJSONObject("data").getString("description");
//                        desc.setText(StringUtils.isNullOrBlanK(descStr) ? getResourceString(R.string.performance_optimization) : descStr);
//                        downLoadLinks = response.getJSONObject("data").getString("download_links");
//                        newVersion.setText("V" + response.getJSONObject("data").getString("version"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    down.setOnClickListener(StartActivity.this);
//                    alertDialog.show();
//                    alertDialog.setContentView(view);
//                    alertDialog.setCanceledOnTouchOutside(false);
//                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
//                }
//            }
//
//            @Override
//            protected void onError(JSONObject response) {
//                Toast.makeText(StartActivity.this, getResourceString(R.string.check_for_update_failed), Toast.LENGTH_SHORT).show();
//                startApp();
//            }
//        }, new VolleyErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                super.onErrorResponse(volleyError);
//                Toast.makeText(StartActivity.this, getResourceString(R.string.check_for_update_failed_check_net), Toast.LENGTH_SHORT).show();
                startApp();
//            }
//        }));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.download:
//                alertDialog.dismiss();
//                Toast.makeText(StartActivity.this, getResourceString(R.string.start_download), Toast.LENGTH_SHORT).show();
//                DownFileUtil downFileUtil = new DownFileUtil(this, downLoadLinks, "qatime.apk", "", "qatime.apk") {
//                    @Override
//                    public void downOK() {
//                        DownFileUtil.insertAPK("", getApplicationContext());
//                    }
//
//                    @Override
//                    public void downChange(long current, long max) {
//
//                    }
//                };
//                downFileUtil.downFile();
////                startApp();
//                break;
//            case R.id.image_x:
//                alertDialog.dismiss();
////                startApp();
//                break;
        }
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getSharedPreferences("first", MODE_PRIVATE).getBoolean("firstlogin", true)) {
                    Logger.e("第一次登陆");
                    StartActivity.this.startActivity(new Intent(StartActivity.this, GuideActivity.class));
                    StartActivity.this.finish();
                } else {
                    Logger.e("no第一次登陆");
                    if (!StringUtils.isNullOrBlanK(BaseApplication.getProfile().getToken())) {//token不空  直接自动登录到mianactivity
                        Logger.e("token----" + BaseApplication.getProfile().getToken());
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        StartActivity.this.startActivity(intent);
                        StartActivity.this.finish();
                    }
                }
            }
        }, 2000);
    }


    //年级列表
//    public void GetGradeslist() {
//
//        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/grades", null,
//                new VolleyListener(this) {
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getFilesDir().getAbsolutePath() + "/grade.txt", true);
//                        SPUtils.put(StartActivity.this, "grade", value);
//                    }
//
//                    @Override
//                    protected void onError(JSONObject response) {
//
//                    }
//
//                    @Override
//                    protected void onTokenOut() {
//                        tokenOut();
//                    }
//                }, new VolleyErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                super.onErrorResponse(volleyError);
//            }
//        });
//        addToRequestQueue(request);
//    }
}

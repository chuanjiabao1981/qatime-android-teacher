package cn.qatime.teacher.player.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UpLoadUtil;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.bean.CityBean;
import libraryextra.bean.GradeBean;
import libraryextra.bean.ImageItem;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.ProvincesBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.WheelView;


public class RegisterPerfectActivity extends BaseActivity implements View.OnClickListener {

    private String imageUrl = "";
    private CustomProgressDialog progress;
    private AlertDialog alertDialog;

    private LinearLayout information;
    private ImageView headSculpture;
    private EditText name;
    private TextView editGrade;
    private TextView editRegion;
    private TextView editMore;
    private TextView complete;
    private List<String> grades;
    private CityBean.Data city;
    private ProvincesBean.DataBean province;

    private void assignViews() {
        information = (LinearLayout) findViewById(R.id.information);
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        name = (EditText) findViewById(R.id.name);
        editGrade = (TextView) findViewById(R.id.grade);
        editRegion = (TextView) findViewById(R.id.region);
        editMore = (TextView) findViewById(R.id.edit_more);
        complete = (TextView) findViewById(R.id.complete);

        name = (EditText) findViewById(R.id.name);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_perfect);
        setTitle(getResources().getString(R.string.information_perfect));
        assignViews();
        grades = new ArrayList<>();
        String gradeString = FileUtil.readFile(getFilesDir() + "/grade.txt");
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            GradeBean gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
            grades = gradeBean.getData().getGrades();
        }
        information.setOnClickListener(this);
        complete.setOnClickListener(this);
        editMore.setOnClickListener(this);
        editGrade.setOnClickListener(this);
        editRegion.setOnClickListener(this);
    }

    @Override
    public int getContentView() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.edit_more:
//                intent = new Intent(RegisterPerfectActivity.this, PersonalInformationChangeActivity.class);
//                int register_action = getIntent().getIntExtra("register_action", Constant.REGIST_1);
//                intent.putExtra("register_action", register_action);
//                startActivityForResult(intent, register_action);
                break;
            case R.id.grade:
                showGradePickerDialog();
                break;
            case R.id.region:
                Intent regionIntent = new Intent(this, RegionSelectActivity1.class);
                startActivityForResult(regionIntent, Constant.REQUEST_REGION_SELECT);
                break;
            case R.id.information://去选择图片
                intent = new Intent(RegisterPerfectActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
                break;
            case R.id.complete://完成
                int userId = BaseApplication.getUserId();
                String url = UrlUtils.urlPersonalInformation + userId + "/profile";
                UpLoadUtil util = new UpLoadUtil(url) {
                    @Override
                    public void httpStart() {
                        progress = DialogUtils.startProgressDialog(progress, RegisterPerfectActivity.this);
                        progress.setCanceledOnTouchOutside(false);
                        progress.setCancelable(false);
                    }

                    @Override
                    protected void httpSuccess(final String result) {
                        //由于已经登陆，所以为profile赋值
                        PersonalInformationBean sData = JsonUtils.objectFromJson(result, PersonalInformationBean.class);
                        if (sData != null && sData.getData() != null) {
                            BaseApplication.getProfile().getData().getUser().setAvatar_url(sData.getData().getAvatar_url());
                            Profile profile = BaseApplication.getProfile();
                            Profile.User user = profile.getData().getUser();
                            user.setId(sData.getData().getId());
                            user.setName(sData.getData().getName());
                            user.setNick_name(sData.getData().getNick_name());
                            user.setAvatar_url(sData.getData().getAvatar_url());
                            user.setEx_big_avatar_url(sData.getData().getEx_big_avatar_url());
                            user.setEmail(sData.getData().getEmail());
                            user.setLogin_mobile(sData.getData().getLogin_mobile());
                            user.setChat_account(sData.getData().getChat_account());
                            profile.getData().setUser(user);
                            BaseApplication.setProfile(profile);
                        }
                        DialogUtils.dismissDialog(progress);
//                        if (getIntent().getIntExtra("register_action", Constant.REGIST_1) == Constant.REGIST_1) {
//                            Intent data = new Intent(RegisterPerfectActivity.this, MainActivity.class);
//                            startActivity(data);
//                        }
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void httpFailed(String result) {
                        Toast.makeText(RegisterPerfectActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        DialogUtils.dismissDialog(progress);
                    }
                };

                if (StringUtils.isNullOrBlanK(imageUrl) || (!StringUtils.isNullOrBlanK(imageUrl) && !new File(imageUrl).exists())) {
                    Toast.makeText(this, getResourceString(R.string.please_set_head), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(BaseApplication.getUserId())) {
                    Toast.makeText(RegisterPerfectActivity.this, getResources().getString(R.string.id_is_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String sName = name.getText().toString().trim();
                if (StringUtils.isNullOrBlanK(sName)) {
                    Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String grade = editGrade.getText().toString();
                if (StringUtils.isNullOrBlanK(grade)) {
                    Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (province == null || city == null) {
//                    Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Map<String, String> map = new HashMap<>();
                map.put("name", sName);
                map.put("grade", grade);
                map.put("avatar", imageUrl);
//                map.put("province_id", province.getId());
                map.put("city_id", city.getId());
                util.execute(map);
                break;
        }
    }

    private void showGradePickerDialog() {
        KeyBoardUtils.closeKeybord(this);
        if (alertDialog == null) {
            final View view = View.inflate(RegisterPerfectActivity.this, R.layout.dialog_grade_picker, null);
            final WheelView grade = (WheelView) view.findViewById(R.id.grade);
            grade.setOffset(1);

            grade.setItems(grades);
            grade.setSeletion(grades.indexOf(editGrade.getText()));
            grade.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPerfectActivity.this);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setContentView(view);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    editGrade.setText(grade.getSeletedItem());
                }
            });
        } else {
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_PICTURE_SELECT) {
            if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
                if (data != null) {
                    String url = data.getStringExtra("url");

                    if (url != null && !StringUtils.isNullOrBlanK(url)) {
                        Bitmap bitmap = BitmapFactory.decodeFile(url);
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        Intent intent = new Intent(RegisterPerfectActivity.this, CropImageActivity.class);
                        intent.putExtra("id", uri.toString());
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }
            } else if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
                if (data != null) {
                    ImageItem image = (ImageItem) data.getSerializableExtra("data");
                    if (image != null && !StringUtils.isNullOrBlanK(image.imageId)) {
                        Intent intent = new Intent(this, CropImageActivity.class);
                        intent.putExtra("id", "content://media/external/images/media/" + image.imageId);
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }
            }
        } else if (resultCode == Constant.PHOTO_CROP) {
            Logger.e("裁剪", "回来");
            if (data != null) {
                imageUrl = data.getStringExtra("bitmap");
                Logger.e(imageUrl);
                if (new File(imageUrl).exists()) {
                    Logger.e("回来成功");
                }
                if (!StringUtils.isNullOrBlanK(imageUrl)) {
                    Glide.with(this).load(Uri.fromFile(new File(imageUrl))).transform(new GlideCircleTransform(this)).crossFade().into(headSculpture);
                }
            }
        } else if (requestCode == Constant.REQUEST_REGION_SELECT && resultCode == Constant.RESPONSE_REGION_SELECT) {
            city = (CityBean.Data) data.getSerializableExtra("region_city");
            province = (ProvincesBean.DataBean) data.getSerializableExtra("region_province");
            if (city != null && province != null) {
                editRegion.setText(province.getName() + city.getName());
            }
        }
//        else if (requestCode == Constant.REGIST_1 && resultCode == Constant.RESPONSE) {//只有信息修改成功以后才会走到这
//            Intent intent = new Intent(RegisterPerfectActivity.this, MainActivity.class);
//            startActivity(intent);
//            setResult(resultCode);
//            finish();
//        } else if (requestCode == Constant.REGIST_2 && resultCode == Constant.RESPONSE) {//只有信息修改成功以后才会走到这
//            setResult(resultCode);
//            finish();
//        }
    }

    @Override
    public void onBackPressed() {//此页面返回清理token（未修改信息，清理登陆状态)
        BaseApplication.clearToken();
        finish();
    }

}

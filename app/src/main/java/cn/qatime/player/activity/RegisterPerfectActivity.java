package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
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

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UpLoadUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.CityBean;
import libraryextra.bean.ImageItem;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.ProvincesBean;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.WheelView;


public class RegisterPerfectActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private String imageUrl = "";
    private CustomProgressDialog progress;
    private AlertDialog gradeDialog;
    private AlertDialog subjectDialog;
    private AlertDialog yearsDialog;

    private LinearLayout information;
    private ImageView headSculpture;
    private EditText name;
    private TextView textGrade;
    private TextView region;
    private TextView desc;
    private TextView editMore;
    private TextView complete;
    private List<String> gradesList;
    private CityBean.Data city;
    private ProvincesBean.DataBean province;
    private SchoolBean.Data schoolData;
    private TextView subject;
    private TextView teachingYears;
    private TextView school;
    private ArrayList<String> yearsList;
    private ArrayList<String> subjectList;
    private WheelView yearsWheel;
    private WheelView gradeWheel;
    /**
     * 用来监听imageUrl变化
     */
    private TextView image;

    private void assignViews() {
        information = (LinearLayout) findViewById(R.id.information);
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        name = (EditText) findViewById(R.id.name);
        textGrade = (TextView) findViewById(R.id.grade);
        subject = (TextView) findViewById(R.id.subject);
        teachingYears = (TextView) findViewById(R.id.teaching_years);
        school = (TextView) findViewById(R.id.school);
        region = (TextView) findViewById(R.id.region);
        desc = (TextView) findViewById(R.id.desc);

        editMore = (TextView) findViewById(R.id.edit_more);
        complete = (TextView) findViewById(R.id.complete);
        image = new TextView(this);
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
        initData();
        information.setOnClickListener(this);
        complete.setOnClickListener(this);
        editMore.setOnClickListener(this);
        subject.setOnClickListener(this);
        school.setOnClickListener(this);
        teachingYears.setOnClickListener(this);
        textGrade.setOnClickListener(this);
        region.setOnClickListener(this);


        image.addTextChangedListener(this);
        name.addTextChangedListener(this);
        textGrade.addTextChangedListener(this);
        subject.addTextChangedListener(this);
        teachingYears.addTextChangedListener(this);
        region.addTextChangedListener(this);
        school.addTextChangedListener(this);
        desc.addTextChangedListener(this);
    }

    private void initData() {
        //年级
        gradesList = new ArrayList<>();
        gradesList.add("高中");
        gradesList.add("初中");
        gradesList.add("小学");
//        gradesList.add("高三");
//        gradesList.add("高二");
//        gradesList.add("高一");
//        gradesList.add("初三");
//        gradesList.add("初二");
//        gradesList.add("初一");
//        gradesList.add("六年级");
//        gradesList.add("五年级");
//        gradesList.add("四年级");
//        gradesList.add("三年级");
//        gradesList.add("二年级");
//        gradesList.add("一年级");
        //年级
        subjectList = new ArrayList<>();
        //执教年龄
        yearsList = new ArrayList<>();
        yearsList.add("三年以内");
        yearsList.add("十年以内");
        yearsList.add("二十年以内");
        yearsList.add("二十年以上");
    }

    private void notifySubject(int position) {
        subjectList.clear();
        subjectList.add("语文");
        subjectList.add("数学");
        subjectList.add("英语");
        switch (position) {
            case 0:
            case 1:
                subjectList.add("政治");
                subjectList.add("历史");
                subjectList.add("地理");
                subjectList.add("物理");
                subjectList.add("化学");
                subjectList.add("生物");
                break;
            case 3:
                subjectList.add("科学");
                break;
        }

//        switch (position) {
//            case 0:
//            case 1:
//            case 2:
//                subjectList.add("政治");
//                subjectList.add("历史");
//                subjectList.add("地理");
//                subjectList.add("物理");
//                subjectList.add("化学");
//                subjectList.add("生物");
//                break;
//
//            case 3:
//                subjectList.add("政治");
//                subjectList.add("历史");
//                subjectList.add("物理");
//                subjectList.add("化学");
//                break;
//            case 4:
//                subjectList.add("政治");
//                subjectList.add("历史");
//                subjectList.add("地理");
//                subjectList.add("物理");
//                subjectList.add("生物");
//                break;
//            case 5:
//                subjectList.add("政治");
//                subjectList.add("历史");
//                subjectList.add("地理");
//                subjectList.add("生物");
//                break;
//
//
//            case 6:
//            case 7:
//            case 8:
//            case 9:
//                subjectList.add("科学");
//                break;
//        }
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
                intent = new Intent(RegisterPerfectActivity.this, PersonalInformationActivity.class);
//                int register_action = getIntent().getIntExtra("register_action", Constant.REGIST_1);
//                startActivityForResult(intent, register_action);
                intent.putExtra("register_action", Constant.REGIST);
                startActivityForResult(intent, Constant.REGIST);
                break;
            case R.id.grade:
                showGradePickerDialog();
                break;
            case R.id.subject:
                if (gradeWheel == null) {
                    Toast.makeText(this, "请先选择年级", Toast.LENGTH_SHORT).show();
                } else {
                    notifySubject(gradeWheel.getSeletedIndex());
                    showSubjectPickerDialog();
                }
                break;
            case R.id.teaching_years:
                showTeachingYearsPickerDialog();
                break;
            case R.id.region:
                Intent regionIntent = new Intent(this, RegionSelectActivity1.class);
                startActivityForResult(regionIntent, Constant.REQUEST_REGION_SELECT);
                break;
            case R.id.school:
                Intent schoolIntent = new Intent(this, SchoolSelectActivity.class);
                startActivityForResult(schoolIntent, Constant.REQUEST_SCHOOL_SELECT);
                break;
            case R.id.information://去选择图片
                intent = new Intent(RegisterPerfectActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
                break;
            case R.id.complete://完成
                int userId = BaseApplication.getUserId();
                String url = UrlUtils.urlPersonalInformation + userId;
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
                        Intent data = new Intent(RegisterPerfectActivity.this, MainActivity.class);
                        startActivity(data);
//                        }
                        setResult(Constant.REGIST);
                        finish();
                    }

                    @Override
                    protected void httpFailed(String result) {
                        Toast.makeText(RegisterPerfectActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        DialogUtils.dismissDialog(progress);
                    }
                };

                String sName = name.getText().toString().trim();
                String grade = textGrade.getText().toString();
                Map<String, String> map = new HashMap<>();
                map.put("name", sName);
                map.put("category", grade);
                map.put("subject", subject.getText().toString());
                map.put("teaching_years", getTeachingYears());
                map.put("province_id", province.getId());
                map.put("city_id", city.getId());
                map.put("school_id", schoolData.getId() + "");
                map.put("desc", desc.getText().toString().trim());
                map.put("avatar", imageUrl);
                util.execute(map);
                break;
        }
    }

    private String getTeachingYears() {
        switch (yearsWheel.getSeletedIndex()) {
            case 0:
                return "within_three_years";
            case 1:
                return "within_ten_years";
            case 2:
                return "within_twenty_years";
            case 3:
                return "more_than_twenty_years";
            default:
                return "";
        }
    }

    private void showGradePickerDialog() {
        KeyBoardUtils.closeKeybord(this);
        if (gradeDialog == null) {
            final View view = View.inflate(RegisterPerfectActivity.this, R.layout.dialog_single_picker, null);
            gradeWheel = (WheelView) view.findViewById(R.id.wheel_view);
            gradeWheel.setOffset(1);

            gradeWheel.setItems(gradesList);
            gradeWheel.setSeletion(gradesList.indexOf(textGrade.getText().toString().trim()));
            gradeWheel.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    gradeDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPerfectActivity.this);
            gradeDialog = builder.create();
            gradeDialog.show();
            gradeDialog.setContentView(view);
            gradeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    textGrade.setText(gradeWheel.getSeletedItem());
                    //年级变更后  科目重新选择
                    subject.setText("");
                }
            });
        } else {
            gradeDialog.show();
        }
    }

    private void showSubjectPickerDialog() {
        KeyBoardUtils.closeKeybord(this);
        final View view = View.inflate(RegisterPerfectActivity.this, R.layout.dialog_single_picker, null);
        final WheelView subjectWheel = (WheelView) view.findViewById(R.id.wheel_view);
        subjectWheel.setOffset(1);

        subjectWheel.setItems(subjectList);
        subjectWheel.setSeletion(subjectList.indexOf(subject.getText().toString().trim()));
        subjectWheel.setonItemClickListener(new WheelView.OnItemClickListener() {
            @Override
            public void onItemClick() {
                subjectDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPerfectActivity.this);
        subjectDialog = builder.create();
        subjectDialog.show();
        subjectDialog.setContentView(view);
        subjectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                subject.setText(subjectWheel.getSeletedItem());
            }
        });

    }

    private void showTeachingYearsPickerDialog() {
        KeyBoardUtils.closeKeybord(this);
        if (yearsDialog == null) {
            final View view = View.inflate(RegisterPerfectActivity.this, R.layout.dialog_single_picker, null);
            yearsWheel = (WheelView) view.findViewById(R.id.wheel_view);
            yearsWheel.setOffset(1);

            yearsWheel.setItems(yearsList);
            yearsWheel.setSeletion(yearsList.indexOf(teachingYears.getText().toString().trim()));
            yearsWheel.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    yearsDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPerfectActivity.this);
            yearsDialog = builder.create();
            yearsDialog.show();
            yearsDialog.setContentView(view);
            yearsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    teachingYears.setText(yearsWheel.getSeletedItem());
                }
            });
        } else {
            yearsDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_PICTURE_SELECT) {
            if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
                if (data != null) {
                        String url = data.getStringExtra("url");
                        if (url != null && !StringUtils.isNullOrBlanK(url)) {
                            Uri uri = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                uri = FileProvider.getUriForFile(this, "com.qatime.player.fileprovider", new File(url));
//                            Bitmap bitmap = BitmapFactory.decodeFile(url);
//                            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
//                            bitmap.recycle();
                            } else {
                                uri = Uri.fromFile(new File(url));
                            }
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
                region.setText(province.getName() + city.getName());
            }
        } else if (requestCode == Constant.REQUEST_SCHOOL_SELECT && resultCode == Constant.RESPONSE_SCHOOL_SELECT) {
            schoolData = (SchoolBean.Data) data.getSerializableExtra("school");
            if (schoolData != null) {
                school.setText(schoolData.getName());
            }
        }else if(requestCode == Constant.REGIST&&resultCode==Constant.RESPONSE){
            Intent intent = new Intent(RegisterPerfectActivity.this, MainActivity.class);
            startActivity(intent);
            setResult(resultCode);
            finish();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (StringUtils.isNullOrBlanK(imageUrl) || (!StringUtils.isNullOrBlanK(imageUrl) && !new File(imageUrl).exists())) {
            complete.setEnabled(false);
//            Toast.makeText(this, getResourceString(R.string.please_set_head), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(name.getText().toString().trim())) {
            complete.setEnabled(false);
//            Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(textGrade.getText().toString())) {
            complete.setEnabled(false);
//            Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(subject.getText().toString())) {
            complete.setEnabled(false);
//            Toast.makeText(this, "科目不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(teachingYears.getText().toString())) {
            complete.setEnabled(false);
//            Toast.makeText(this, "教龄不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (province == null || city == null) {
            complete.setEnabled(false);
//            Toast.makeText(this, "城市不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(school.getText().toString())) {
            complete.setEnabled(false);
//            Toast.makeText(this, "学校不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(desc.getText().toString())) {
            complete.setEnabled(false);
//            Toast.makeText(this, "简介不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        complete.setEnabled(true);
    }
}

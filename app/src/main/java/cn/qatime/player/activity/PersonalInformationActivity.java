package cn.qatime.player.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
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
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CustomAlertDialog;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.MDatePickerDialog;
import libraryextra.view.WheelView;

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private ImageView headSculpture;
    private EditText nickName;
    private TextView category;
    private TextView subject;
    private EditText name;
    private TextView sex;
    private TextView birthday;
    private TextView teachingYears;
    private TextView school;
    private EditText describe;

    private String imageUrl;
    private CustomProgressDialog progress;
    private String gender = "male";
    private String select = "";//生日所选日期
    private AlertDialog yearsDialog;
    private WheelView yearsWheel;
    private AlertDialog gradeDialog;
    private AlertDialog subjectDialog;
    private WheelView gradeWheel;
    private List<String> yearsList;
    private ArrayList<String> gradesList;
    private ArrayList<String> subjectList;
    private CityBean.Data city;
    private ProvincesBean.DataBean province;
    private SchoolBean.Data schoolData;
    private TextView region;
    private boolean isChanged = false;

    private void assignViews() {
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        nickName = (EditText) findViewById(R.id.nick_name);
        name = (EditText) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        birthday = (TextView) findViewById(R.id.birthday);
        teachingYears = (TextView) findViewById(R.id.teaching_years);
        category = (TextView) findViewById(R.id.category);
        subject = (TextView) findViewById(R.id.subject);
        school = (TextView) findViewById(R.id.school);
        region = (TextView) findViewById(R.id.region);
        describe = (EditText) findViewById(R.id.describe);
        headSculpture.setOnClickListener(this);


        findViewById(R.id.sex_layout).setOnClickListener(this);
        findViewById(R.id.birthday_layout).setOnClickListener(this);
        findViewById(R.id.teaching_years_layout).setOnClickListener(this);
        findViewById(R.id.category_layout).setOnClickListener(this);
        findViewById(R.id.subject_layout).setOnClickListener(this);
        findViewById(R.id.school_layout).setOnClickListener(this);
        findViewById(R.id.region_layout).setOnClickListener(this);
    }

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private PersonalInformationBean bean;
//2015-10-08 15:04:25.0 ---> 10月08日 15:04
//    format.format(parse.parse("2015-10-08 15:04:25.0");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.personal_information));
        assignViews();
        initListData();
        initData();

        nickName.addTextChangedListener(this);
        name.addTextChangedListener(this);
        sex.addTextChangedListener(this);
        birthday.addTextChangedListener(this);
        teachingYears.addTextChangedListener(this);
        category.addTextChangedListener(this);
        subject.addTextChangedListener(this);
        school.addTextChangedListener(this);
        region.addTextChangedListener(this);
        describe.addTextChangedListener(this);

    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_information;
    }


    private void initData() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null,
                new VolleyListener(PersonalInformationActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                        if (bean != null && bean.getData() != null) {
                            setValue(bean);
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void setValue(PersonalInformationBean bean) {
        Glide.with(PersonalInformationActivity.this).load(bean.getData().getAvatar_url()).placeholder(R.mipmap.personal_information_head).transform(new GlideCircleTransform(PersonalInformationActivity.this)).crossFade().into(headSculpture);
        nickName.setText(bean.getData().getNick_name());
        name.setText(bean.getData().getName());
        imageUrl = bean.getData().getAvatar_url();
        if (!StringUtils.isNullOrBlanK(bean.getData().getGender())) {
            if (bean.getData().getGender().equals("male")) {
                sex.setText(getResources().getString(R.string.male));
            } else {
                gender = "female";
                sex.setText(getResources().getString(R.string.female));
            }
        } else {
            sex.setText("");
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getBirthday())) {
            try {
                birthday.setText(format.format(parse.parse(bean.getData().getBirthday())));
                select = bean.getData().getBirthday();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            birthday.setText(format.format(new Date()));
            select = parse.format(new Date());
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getCategory())) {
            category.setText(bean.getData().getCategory());
        } else {
            category.setText("");
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getSubject())) {
            subject.setText(bean.getData().getSubject());
        } else {
            subject.setText("");
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getTeaching_years())) {
            teachingYears.setText(getTeachingYear(bean.getData().getTeaching_years()));
        } else {
            teachingYears.setText("");
        }

        String regionStr = "";
        String json = FileUtil.readFile(getFilesDir() + "/provinces.txt").toString();
        ProvincesBean provincesBean = JsonUtils.objectFromJson(json, ProvincesBean.class);
        String json1 = FileUtil.readFile(getFilesDir() + "/cities.txt").toString();
        CityBean cityBean = JsonUtils.objectFromJson(json1, CityBean.class);
        String json2 = FileUtil.readFile(getFilesDir() + "/school.txt").toString();
        SchoolBean schoolBean = JsonUtils.objectFromJson(json2, SchoolBean.class);

        if (provincesBean != null && provincesBean.getData() != null) {
            for (int i = 0; i < provincesBean.getData().size(); i++) {
                if (provincesBean.getData().get(i).getId().equals(bean.getData().getProvince())) {
                    province = provincesBean.getData().get(i);
                    regionStr += province.getName();
                    break;
                }
            }
        }
        if (cityBean != null && cityBean.getData() != null) {
            for (int i = 0; i < cityBean.getData().size(); i++) {
                if (cityBean.getData().get(i).getId().equals(bean.getData().getCity())) {
                    city = cityBean.getData().get(i);
                    regionStr += city.getName();
                    break;
                }
            }
        }
        region.setText(regionStr);
        if (schoolBean != null && schoolBean.getData() != null) {
            for (int i = 0; i < schoolBean.getData().size(); i++) {
                if (schoolBean.getData().get(i).getId() == bean.getData().getSchool()) {
                    schoolData = schoolBean.getData().get(i);
                    school.setText(schoolData.getName());
                    break;
                }
            }
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getDesc())) {
            describe.setText(bean.getData().getDesc());
        } else {
            describe.setText("");
        }

    }


    @Override
    public void onBackPressed() {
        if (isChanged) {
            if (StringUtils.isNullOrBlanK(name.getText().toString().trim())) {
                Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrBlanK(category.getText().toString())) {
                Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrBlanK(subject.getText().toString())) {
                Toast.makeText(this, "科目不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrBlanK(teachingYears.getText().toString())) {
                Toast.makeText(this, "教龄不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (province == null || city == null) {
                Toast.makeText(this, "城市不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrBlanK(school.getText().toString())) {
                Toast.makeText(this, "学校不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrBlanK(describe.getText().toString())) {
                Toast.makeText(this, "简介不能为空", Toast.LENGTH_SHORT).show();
                return;
            }


            int userId = BaseApplication.getUserId();
            String url = UrlUtils.urlPersonalInformation + userId;
            UpLoadUtil util = new UpLoadUtil(url) {
                @Override
                public void httpStart() {
                    progress = DialogUtils.startProgressDialog(progress, PersonalInformationActivity.this);
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
                    setResult(Constant.RESPONSE);
                    finish();
                }

                @Override
                protected void httpFailed(String result) {
                    Toast.makeText(PersonalInformationActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    DialogUtils.dismissDialog(progress);
                }
            };

            String birthday = select.equals(parse.format(new Date())) ? "" : select;
            String sName = name.getText().toString().trim();
            String grade = category.getText().toString();
            Map<String, String> map = new HashMap<>();
            map.put("avatar", imageUrl);
            map.put("nick_name", nickName.getText().toString());
            map.put("name", sName);
            map.put("gender", gender);
            map.put("birthday", birthday);
            map.put("teaching_years", getTeachingYears());
            map.put("category", grade);
            map.put("subject", subject.getText().toString());
            map.put("school_id", schoolData == null ? "" : schoolData.getId() + "");
            map.put("province_id", province == null ? "" : province.getId());
            map.put("city_id", city == null ? "" : city.getId());
            map.put("desc", describe.getText().toString().trim());
            util.execute(map);
        } else super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_sculpture:
                Intent intent = new Intent(PersonalInformationActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
                break;
            case R.id.sex_layout:
                CustomAlertDialog dialog = new CustomAlertDialog(PersonalInformationActivity.this);
                dialog.addItem("男", new CustomAlertDialog.onSeparateItemClickListener() {
                    @Override
                    public void onClick() {
                        gender = "male";
                        sex.setText("男");
                    }
                });
                dialog.addItem("女", new CustomAlertDialog.onSeparateItemClickListener() {
                    @Override
                    public void onClick() {
                        gender = "female";
                        sex.setText("女");
                    }
                });
                dialog.show();
                break;
            case R.id.birthday_layout:
                try {
                    MDatePickerDialog dataDialog = new MDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            select = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                            try {
                                birthday.setText(format.format(parse.parse(select)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, parse.parse(select).getYear() + 1900, parse.parse(select).getMonth(), parse.parse(select).getDate());
                    dataDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dataDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.teaching_years_layout:
                showTeachingYearsPickerDialog();
                break;
            case R.id.category_layout:
                showGradePickerDialog();
                break;
            case R.id.subject_layout:
                if (StringUtils.isNullOrBlanK(category.getText().toString().trim())) {
                    Toast.makeText(this, "请先选择年级", Toast.LENGTH_SHORT).show();
                } else {
                    notifySubject(gradesList.indexOf(category.getText().toString().trim()));
                    showSubjectPickerDialog();
                }
                break;
            case R.id.region_layout:
                Intent regionIntent = new Intent(this, RegionSelectActivity1.class);
                startActivityForResult(regionIntent, Constant.REQUEST_REGION_SELECT);
                break;
            case R.id.school_layout:
                if (city == null) {
                    Toast.makeText(this, "请先选择城市", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent schoolIntent = new Intent(this, SchoolSelectActivity.class);
                schoolIntent.putExtra("city_id", city.getId());
                startActivityForResult(schoolIntent, Constant.REQUEST_SCHOOL_SELECT);
                break;
        }
    }


    private String getTeachingYears() {
        if (yearsWheel != null) {
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
        } else {
            return "";
        }
    }

    private String getTeachingYear(String teaching_years) {
        switch (teaching_years) {
            case "within_three_years":
                return "三年以内";
            case "within_ten_years":
                return "十年以内";
            case "within_twenty_years":
                return "二十年以内";
            case "more_than_twenty_years":
                return "二十年以上";
        }
        return "";
    }


    private void showGradePickerDialog() {
        KeyBoardUtils.closeKeybord(this);
        if (gradeDialog == null) {
            final View view = View.inflate(PersonalInformationActivity.this, R.layout.dialog_single_picker, null);
            gradeWheel = (WheelView) view.findViewById(R.id.wheel_view);
            gradeWheel.setOffset(1);

            gradeWheel.setItems(gradesList);
            gradeWheel.setSeletion(gradesList.indexOf(category.getText().toString().trim()));
            gradeWheel.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    gradeDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
            gradeDialog = builder.create();
            gradeDialog.show();
            gradeDialog.setContentView(view);
            gradeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    category.setText(gradeWheel.getSeletedItem());
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
        final View view = View.inflate(PersonalInformationActivity.this, R.layout.dialog_single_picker, null);
        final WheelView subjectWheel = (WheelView) view.findViewById(R.id.wheel_view);
        subjectWheel.setOffset(1);

        subjectWheel.setItems(subjectList);
        int position = subjectList.indexOf(subject.getText().toString().trim());
        subjectWheel.setSeletion(position);
        subjectWheel.setonItemClickListener(new WheelView.OnItemClickListener() {
            @Override
            public void onItemClick() {
                subjectDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
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
            final View view = View.inflate(PersonalInformationActivity.this, R.layout.dialog_single_picker, null);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
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

    private void initListData() {
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
                        Intent intent = new Intent(PersonalInformationActivity.this, CropImageActivity.class);
                        intent.putExtra("id", uri.toString());
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }
            } else if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
                if (data != null) {
                    ImageItem image = (ImageItem) data.getSerializableExtra("data");
                    if (image != null && !StringUtils.isNullOrBlanK(image.imageId)) {
                        Intent intent = new Intent(PersonalInformationActivity.this, CropImageActivity.class);
                        intent.putExtra("id", "content://media/external/images/media/" + image.imageId);
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }

            }
        } else if (resultCode == Constant.PHOTO_CROP) {
            Logger.e("裁剪", "回来");
            if (data != null) {
                imageUrl = data.getStringExtra("bitmap");
                isChanged = true;
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
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        isChanged = true;
    }
}

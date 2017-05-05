package cn.qatime.teacher.player.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.base.BaseActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.utils.Constant;
import cn.qatime.teacher.player.utils.UpLoadUtil;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.bean.ImageItem;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CustomAlertDialog;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.MDatePickerDialog;
import libraryextra.view.WheelView;

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener {
    private boolean isChanged = false;
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

    private String imageUrl = "";
    private CustomProgressDialog progress;
    private String gender = "male";
    private String select = "";//生日所选日期
    private AlertDialog alertDialogTeachingYears;
    private AlertDialog alertDialogSubject;
    private AlertDialog alertDialogCategory;

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
        describe = (EditText) findViewById(R.id.describe);
        nickName.addTextChangedListener(new TextWatcher() {
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
        });
        name.addTextChangedListener(new TextWatcher() {
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
        });
        describe.addTextChangedListener(new TextWatcher() {
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
        });
        headSculpture.setOnClickListener(this);
        findViewById(R.id.sex_layout).setOnClickListener(this);
        findViewById(R.id.birthday_layout).setOnClickListener(this);
        findViewById(R.id.teaching_years_layout).setOnClickListener(this);
        findViewById(R.id.category_layout).setOnClickListener(this);
        findViewById(R.id.subject_layout).setOnClickListener(this);
        findViewById(R.id.school_layout).setOnClickListener(this);
    }

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private PersonalInformationBean bean;
//2015-10-08 15:04:25.0 ---> 10月08日 15:04
//    format.format(parse.parse("2015-10-08 15:04:25.0");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.personal_information));
//        setRightImage(R.mipmap.personal_change_information, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PersonalInformationActivity.this, PersonalInformationChangeActivity.class);
//                intent.putExtra("data", bean);
//                startActivityForResult(intent, Constant.REQUEST);
//            }
//        });
        assignViews();
        initData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_information;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
//            if (!StringUtils.isNullOrBlanK(data.getStringExtra("data"))) {
//                PersonalInformationBean sData = JsonUtils.objectFromJson(data.getStringExtra("data"), PersonalInformationBean.class);
//                if (sData != null && sData.getData() != null) {
//                    bean = sData;
//                    setValue(sData);
//                    BaseApplication.getProfile().getData().getUser().setAvatar_url(sData.getData().getAvatar_url());
//                    Profile profile = BaseApplication.getProfile();
//                    Profile.User user = profile.getData().getUser();
//                    user.setId(sData.getData().getId());
//                    user.setName(sData.getData().getName());
//                    user.setNick_name(sData.getData().getNick_name());
//                    user.setAvatar_url(sData.getData().getAvatar_url());
//                    user.setEx_big_avatar_url(sData.getData().getEx_big_avatar_url());
//                    user.setEmail(sData.getData().getEmail());
//                    user.setLogin_mobile(sData.getData().getLogin_mobile());
//                    user.setChat_account(sData.getData().getChat_account());
//
//                    profile.getData().setUser(user);
//                    BaseApplication.setProfile(profile);
//                    setResult(Constant.RESPONSE);
//                }
//            }
//        }
//    }

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
//        nickName.setSelection(nickName.getText().toString().length());
//        name.setSelection(name.getText().toString().length());

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

//        if (!StringUtils.isNullOrBlanK(bean.getData().getProvince()) && !StringUtils.isNullOrBlanK(bean.getData().getCity())) {
//            region.setText(bean.getData().getProvince() + " " + bean.getData().getCity());
//        }else {
//            region.setText("");}


        SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getCacheDir() + "/school.txt"), SchoolBean.class);

        if (schoolBean != null && schoolBean.getData() != null) {
            for (int i = 0; i < schoolBean.getData().size(); i++) {
                if (bean.getData().getSchool() == schoolBean.getData().get(i).getId()) {
                    school.setText(schoolBean.getData().get(i).getName());
                    break;
                }
            }
        } else {
            school.setText("");
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getDesc())) {
            describe.setText(bean.getData().getDesc());
        } else {
            describe.setText("");
        }

    }

    private String getTeachingYear(String teaching_years) {
        switch (teaching_years) {
            case "within_three_years":
                return getResourceString(R.string.within_three_years);
            case "within_ten_years":
                return getResourceString(R.string.within_ten_years);
            case "within_twenty_years":
                return getResourceString(R.string.within_twenty_years);
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            String url = UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/profile";
            UpLoadUtil util = new UpLoadUtil(url) {
                @Override
                public void httpStart() {
                    progress = DialogUtils.startProgressDialog(progress, PersonalInformationActivity.this);
                    progress.setCanceledOnTouchOutside(false);
                    progress.setCancelable(false);
                }

                @Override
                protected void httpSuccess(String result) {
                    Intent data = new Intent();
                    data.putExtra("data", result);
                    setResult(Constant.RESPONSE, data);
                    DialogUtils.dismissDialog(progress);
                    Toast.makeText(PersonalInformationActivity.this, getResources().getString(R.string.change_information_successful), Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                protected void httpFailed(String result) {
                    Toast.makeText(PersonalInformationActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    DialogUtils.dismissDialog(progress);
                }
            };

            if (StringUtils.isNullOrBlanK(BaseApplication.getUserId())) {
                Toast.makeText(PersonalInformationActivity.this, getResources().getString(R.string.id_is_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            String sName = name.getText().toString();
            if (StringUtils.isNullOrBlanK(sName)) {
                Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrBlanK(category.getText().toString())) {
                Toast.makeText(this, "类型不能空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrBlanK(subject.getText().toString())) {
                Toast.makeText(this, "科目不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
//            String grade = category.getText().toString();
//            if (StringUtils.isNullOrBlanK(grade)) {
//                Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
//                return;
//            }
            String birthday = select.equals(parse.format(new Date())) ? "" : select;
            String desc = describe.getText().toString();
            Map<String, String> map = new HashMap<>();

            try {
                map.put("name", URLEncoder.encode(sName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                map.put("nick_name", URLEncoder.encode(nickName.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//                map.put("grade", grade);
            map.put("avatar", imageUrl);
            map.put("gender", gender);
            map.put("birthday", birthday);
            try {
                map.put("desc", URLEncoder.encode(desc, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                map.put("subject", URLEncoder.encode(subject.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                map.put("category", URLEncoder.encode(category.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//                Logger.e("--" + sName + "--" + grade + "--" + imageUrl + "--" + gender + "--" + birthday + "--" + desc + "--");
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
                        isChanged = true;
                    }
                });
                dialog.addItem("女", new CustomAlertDialog.onSeparateItemClickListener() {
                    @Override
                    public void onClick() {
                        gender = "female";
                        sex.setText("女");
                        isChanged = true;
                    }
                });
                dialog.show();
                break;
            case R.id.birthday_layout:
                try {
                    MDatePickerDialog dataDialog = new MDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            isChanged = true;
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
                showCategoryPickerDialog();
                break;
            case R.id.subject_layout:
                showSubjectPickerDialog();
                break;
            case R.id.school_layout:
                break;
        }
    }

    private void showSubjectPickerDialog() {
        if (alertDialogSubject == null) {
            final View view = View.inflate(PersonalInformationActivity.this, R.layout.dialog_single_picker, null);
            final WheelView wheelView = (WheelView) view.findViewById(R.id.wheel_view);
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
            alertDialogSubject = builder.create();
            wheelView.setOffset(1);
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.subject));
            wheelView.setItems(list);
            wheelView.setSeletion(list.indexOf(subject.getText().toString()));
            wheelView.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialogSubject.dismiss();
                }
            });
            alertDialogSubject.show();
            alertDialogSubject.setContentView(view);
            alertDialogSubject.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    subject.setText(wheelView.getSeletedItem());
                    isChanged = true;
                }
            });
        } else {
            alertDialogSubject.show();
        }
    }

    private void showCategoryPickerDialog() {
        if (alertDialogCategory == null) {
            final View view = View.inflate(PersonalInformationActivity.this, R.layout.dialog_single_picker, null);
            final WheelView wheelView = (WheelView) view.findViewById(R.id.wheel_view);
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
            alertDialogCategory = builder.create();
            wheelView.setOffset(1);
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.category));
            wheelView.setItems(list);
            wheelView.setSeletion(list.indexOf(category.getText().toString()));
            wheelView.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialogCategory.dismiss();
                }
            });
            alertDialogCategory.show();
            alertDialogCategory.setContentView(view);
            alertDialogCategory.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    category.setText(wheelView.getSeletedItem());
                    isChanged = true;
                }
            });
        } else {
            alertDialogCategory.show();
        }
    }

    private void showTeachingYearsPickerDialog() {
        if (alertDialogTeachingYears == null) {
            final View view = View.inflate(PersonalInformationActivity.this, R.layout.dialog_single_picker, null);
            final WheelView wheelView = (WheelView) view.findViewById(R.id.wheel_view);
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
            alertDialogTeachingYears = builder.create();
            wheelView.setOffset(1);
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.teaching_years));
            wheelView.setItems(list);
            wheelView.setSeletion(list.indexOf(teachingYears.getText().toString()));
            wheelView.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialogTeachingYears.dismiss();
                }
            });
            alertDialogTeachingYears.show();
            alertDialogTeachingYears.setContentView(view);
            alertDialogTeachingYears.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    teachingYears.setText(wheelView.getSeletedItem());
                    isChanged = true;
                }
            });
        } else {
            alertDialogTeachingYears.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_PICTURE_SELECT) {
            if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    Uri captureUri;
                    if (data.getData() != null) {
                        captureUri = data.getData();
                    } else {
                        captureUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    }
                    if (captureUri != null && !StringUtils.isNullOrBlanK(captureUri.toString())) {
                        Intent intent = new Intent(PersonalInformationActivity.this, CropImageActivity.class);
                        intent.putExtra("id", captureUri.toString());
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
                Logger.e(imageUrl);
                if (new File(imageUrl).exists()) {
                    Logger.e("回来成功");
                }
                if (!StringUtils.isNullOrBlanK(imageUrl)) {
                    isChanged = true;
                    Glide.with(this).load(Uri.fromFile(new File(imageUrl))).transform(new GlideCircleTransform(this)).crossFade().into(headSculpture);
                }
            }
        }
    }
}

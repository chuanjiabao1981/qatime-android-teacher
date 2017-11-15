package cn.qatime.player.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UpLoadUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.GradeBean;
import libraryextra.bean.ImageItem;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.MDatePickerDialog;
import libraryextra.view.WheelView;

public class PersonalInformationChangeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView headSculpture;
    private TextView replace;
    private EditText nickName;
    private EditText name;
    private RadioGroup radiogroup;
    private RadioButton men;
    private RadioButton women;
    private LinearLayout birthdayView;
    private TextView birthday;
    private LinearLayout teachingYearsView;
    private TextView teachingYears;
    private LinearLayout subjectView;
    private TextView subject;
    private LinearLayout categoryView;
    private TextView category;
    private LinearLayout areaView;
    private TextView provinces;
    private TextView city;
    private EditText school;
    private EditText describe;
    private TextView complete;

    private Uri captureUri;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private String imageUrl = "";
    private String select = "";//生日所选日期
    private GradeBean gradeBean;
    private CustomProgressDialog progress;
    private AlertDialog alertDialogTeachingYears;
    private AlertDialog alertDialogSubject;
    private AlertDialog alertDialogCategory;
    private AlertDialog alertDialogArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.change_information));
        assignViews();
        //获取基本数据信息,对应id与学校 年级
//        String school = FileUtil.readFile(getCacheDir() + "/school.txt");
//        if (!StringUtils.isNullOrBlanK(school)) {
//            schoolBean = JsonUtils.objectFromJson(school, SchoolBean.class);
//        }
        String gradeString = FileUtil.readFile(getFilesDir() + "/grade.txt");
//        LogUtils.e("班级基础信息" + gradeString);
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
        }


        replace.setOnClickListener(this);
        birthdayView.setOnClickListener(this);
        complete.setOnClickListener(this);
        teachingYearsView.setOnClickListener(this);
        categoryView.setOnClickListener(this);
        subjectView.setOnClickListener(this);
        areaView.setOnClickListener(this);
//        gradeView.setOnClickListener(this);
        PersonalInformationBean data = (PersonalInformationBean) getIntent().getSerializableExtra("data");
        if (data != null && data.getData() != null) {
            initData(data);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_information_change;
    }

    private void initData(PersonalInformationBean data) {
        Glide.with(PersonalInformationChangeActivity.this).load(data.getData().getAvatar_url()).placeholder(R.mipmap.personal_information_head).transform(new GlideCircleTransform(PersonalInformationChangeActivity.this)).crossFade().into(headSculpture);
        name.setText(data.getData().getName());
        Editable etext = name.getText();
        Selection.setSelection(etext, etext.length());
        if (!StringUtils.isNullOrBlanK(data.getData().getGender())) {
            if (data.getData().getGender().equals("male")) {
                men.setChecked(true);
                women.setChecked(false);
            } else {
                men.setChecked(false);
                women.setChecked(true);
            }
        }
        if (!StringUtils.isNullOrBlanK(data.getData().getBirthday())) {
            try {
                birthday.setText(format.format(parse.parse(data.getData().getBirthday())));
                select = data.getData().getBirthday();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            birthday.setText(format.format(new Date()));
            select = parse.format(new Date());
        }
        if (!StringUtils.isNullOrBlanK(data.getData().getGrade())) {
            for (int i = 0; i < gradeBean.getData().getGrades().size(); i++) {
                if (data.getData().getGrade().equals(gradeBean.getData().getGrades().get(i))) {
//                    textGrade.setText(data.getData().getGrade());
                    break;
                }
            }
        }
        describe.setText(data.getData().getDesc());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teaching_years_view:
                showTeachingYearsPickerDialog();
                break;
            case R.id.subject_view:
                showSubjectPickerDialog();
                break;
            case R.id.category_view:
                showCategoryPickerDialog();
                break;
            case R.id.area_view:
                showAreaPickerDialog();
                break;
            case R.id.replace://去选择图片
                final Intent intent = new Intent(PersonalInformationChangeActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
                break;
            case R.id.birthday_view://生日
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
                    }, parse.parse(select).getYear() + 1900, parse.parse(select).getMonth() + 1, parse.parse(select).getDay());
                    dataDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dataDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.complete://完成
                String url = UrlUtils.urlPersonalInformation + BaseApplication.getInstance().getUserId();
                UpLoadUtil util = new UpLoadUtil(url) {
                    @Override
                    public void httpStart() {
                        progress = DialogUtils.startProgressDialog(progress, PersonalInformationChangeActivity.this);
                        progress.setCanceledOnTouchOutside(false);
                        progress.setCancelable(false);
                    }

                    @Override
                    protected void httpSuccess(String result) {
                        Intent data = new Intent();
                        data.putExtra("data", result);
                        setResult(Constant.RESPONSE, data);
                        DialogUtils.dismissDialog(progress);
                        Toast.makeText(PersonalInformationChangeActivity.this, getResources().getString(R.string.change_information_successful), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void httpFailed(String result) {
                        // TODO: 2016/8/26 ERROR 处理
                        Toast.makeText(PersonalInformationChangeActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        DialogUtils.dismissDialog(progress);
                    }
                };

                if (StringUtils.isNullOrBlanK(BaseApplication.getInstance().getUserId())) {
                    Toast.makeText(PersonalInformationChangeActivity.this, getResources().getString(R.string.id_is_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String sName = name.getText().toString();
                if (StringUtils.isNullOrBlanK(sName)) {
                    Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
//                String grade = textGrade.getText().toString();
//                if (StringUtils.isNullOrBlanK(grade)) {
//                    Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
//                    return;
//                }
                String gender = radiogroup.getCheckedRadioButtonId() == men.getId() ? "male" : "female";
                String birthday = select.equals(parse.format(new Date())) ? "" : select;
                String desc = describe.getText().toString();
                Map<String, String> map = new HashMap<>();

                map.put("name", sName);
//                map.put("grade", grade);
                map.put("avatar", imageUrl);
                map.put("gender", gender);
                map.put("birthday", birthday);
                map.put("desc", desc);
//                Logger.e("--" + sName + "--" + grade + "--" + imageUrl + "--" + gender + "--" + birthday + "--" + desc + "--");
                util.execute(map);
                break;
        }
    }

    private void showTeachingYearsPickerDialog() {
        if (alertDialogTeachingYears == null) {
            final View view = View.inflate(PersonalInformationChangeActivity.this, R.layout.dialog_single_picker, null);
            final WheelView wheelView = (WheelView) view.findViewById(R.id.wheel_view);
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationChangeActivity.this);
            alertDialogTeachingYears = builder.create();
            wheelView.setOffset(1);
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.teaching_years));
            wheelView.setItems(list);
            wheelView.setSeletion(list.indexOf(teachingYears.getText()));
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
                }
            });
        } else {
            alertDialogTeachingYears.show();
        }

    }

    private void showSubjectPickerDialog() {
        if (alertDialogSubject == null) {
            final View view = View.inflate(PersonalInformationChangeActivity.this, R.layout.dialog_single_picker, null);
            final WheelView wheelView = (WheelView) view.findViewById(R.id.wheel_view);
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationChangeActivity.this);
            alertDialogSubject = builder.create();
            wheelView.setOffset(1);
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.subject));
            wheelView.setItems(list);
            wheelView.setSeletion(list.indexOf(subject.getText()));
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
                }
            });
        } else {
            alertDialogSubject.show();
        }
    }

    private void showCategoryPickerDialog() {
        if (alertDialogCategory == null) {
            final View view = View.inflate(PersonalInformationChangeActivity.this, R.layout.dialog_single_picker, null);
            final WheelView wheelView = (WheelView) view.findViewById(R.id.wheel_view);
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationChangeActivity.this);
            alertDialogCategory = builder.create();
            wheelView.setOffset(1);
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.category));
            wheelView.setItems(list);
            wheelView.setSeletion(list.indexOf(category.getText()));
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
                }
            });
        } else {
            alertDialogCategory.show();
        }
    }

    private void showAreaPickerDialog() {
        if (alertDialogArea == null) {
            final View view = View.inflate(PersonalInformationChangeActivity.this, R.layout.dialog_two_wheel_picker, null);
            final WheelView wheelView1 = (WheelView) view.findViewById(R.id.wheel_view1);
            final WheelView wheelView2 = (WheelView) view.findViewById(R.id.wheel_view2);
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationChangeActivity.this);
            alertDialogArea = builder.create();
            wheelView1.setOffset(1);
            List<String> list1 = Arrays.asList(getResources().getStringArray(R.array.provinces));
            wheelView1.setItems(list1);
            wheelView1.setSeletion(list1.indexOf(provinces.getText()));
            wheelView1.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialogArea.dismiss();
                }
            });
            wheelView2.setOffset(1);
            List<String> list2 = Arrays.asList(getResources().getStringArray(R.array.city));
            wheelView2.setItems(list2);
            wheelView2.setSeletion(list2.indexOf(city.getText()));
            wheelView2.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialogArea.dismiss();
                }
            });
            alertDialogArea.show();
            alertDialogArea.setContentView(view);
            alertDialogArea.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    provinces.setText(wheelView1.getSeletedItem());
                    city.setText(wheelView2.getSeletedItem());
                }
            });
        } else {
            alertDialogArea.show();
        }
    }

    private void assignViews() {
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        replace = (TextView) findViewById(R.id.replace);
        nickName = (EditText) findViewById(R.id.nick_name);
        name = (EditText) findViewById(R.id.name);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        men = (RadioButton) findViewById(R.id.men);
        women = (RadioButton) findViewById(R.id.women);
        birthdayView = (LinearLayout) findViewById(R.id.birthday_view);
        birthday = (TextView) findViewById(R.id.birthday);
        teachingYearsView = (LinearLayout) findViewById(R.id.teaching_years_view);
        teachingYears = (TextView) findViewById(R.id.teaching_years);
        subjectView = (LinearLayout) findViewById(R.id.subject_view);
        subject = (TextView) findViewById(R.id.subject);
        categoryView = (LinearLayout) findViewById(R.id.category_view);
        category = (TextView) findViewById(R.id.category);
        areaView = (LinearLayout) findViewById(R.id.area_view);
        provinces = (TextView) findViewById(R.id.provinces);
        city = (TextView) findViewById(R.id.city);
        school = (EditText) findViewById(R.id.school);
        describe = (EditText) findViewById(R.id.describe);
        complete = (TextView) findViewById(R.id.complete);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_PICTURE_SELECT) {
            if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    if (data.getData() != null) {
                        captureUri = data.getData();
                    } else {
                        captureUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    }
                    if (captureUri != null && !StringUtils.isNullOrBlanK(captureUri.toString())) {
                        Intent intent = new Intent(PersonalInformationChangeActivity.this, CropImageActivity.class);
                        intent.putExtra("id", captureUri.toString());
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }
            } else if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
                if (data != null) {
                    ImageItem image = (ImageItem) data.getSerializableExtra("data");
                    if (image != null && !StringUtils.isNullOrBlanK(image.imageId)) {
                        Intent intent = new Intent(PersonalInformationChangeActivity.this, CropImageActivity.class);
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
        }
    }
}

package cn.qatime.teacher.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.activity.ClassTableActivity;
import cn.qatime.teacher.player.activity.PersonalInformationActivity;
import cn.qatime.teacher.player.activity.PersonalMyTutorshipActivity;
import cn.qatime.teacher.player.activity.PersonalMyWalletActivity;
import cn.qatime.teacher.player.activity.SettingActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.base.BaseFragment;
import cn.qatime.teacher.player.utils.Constant;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2016/10/25 14:47
 * @Describe
 */
public class FragmentPersonalCenter extends BaseFragment implements View.OnClickListener {

    private View classTable;
    private View myTutorship;
    private View setting;
    private View manage;
    private View information;
    private TextView name;
    private TextView balance;
    private ImageView headSculpture;
    //    DecimalFormat df = new DecimalFormat("#.00");
    private TextView nickName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        classTable = view.findViewById(R.id.class_table);
        myTutorship = view.findViewById(R.id.my_tutorship);
        setting = view.findViewById(R.id.setting);
        manage = view.findViewById(R.id.manage);
        headSculpture = (ImageView) view.findViewById(R.id.head_sculpture);
        name = (TextView) view.findViewById(R.id.name);
        nickName = (TextView) view.findViewById(R.id.nick_name);
        balance = (TextView) view.findViewById(R.id.balance);
        information = view.findViewById(R.id.information);
        if (BaseApplication.getProfile().getData() != null && BaseApplication.getProfile().getData().getUser() != null) {
            Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.error_header).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
        }
        name.setText(BaseApplication.getProfile().getData().getUser().getName());
        nickName.setText("昵称:" + BaseApplication.getProfile().getData().getUser().getNick_name());

        classTable.setOnClickListener(this);
        myTutorship.setOnClickListener(this);
        setting.setOnClickListener(this);
        manage.setOnClickListener(this);
        information.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.class_table:
                getActivity().startActivity(new Intent(getActivity(), ClassTableActivity.class));
                break;
            case R.id.my_tutorship:
                getActivity().startActivity(new Intent(getActivity(), PersonalMyTutorshipActivity.class));
                break;
            case R.id.setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.manage:
                intent = new Intent(getActivity(), PersonalMyWalletActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
                break;
            case R.id.information:
                intent = new Intent(getActivity(), PersonalInformationActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
            name.setText(StringUtils.isNullOrBlanK(BaseApplication.getProfile().getData().getUser().getName()) ? "无" : BaseApplication.getProfile().getData().getUser().getName());
            nickName.setText("昵称：" + (StringUtils.isNullOrBlanK(BaseApplication.getProfile().getData().getUser().getNick_name()) ? "无" : BaseApplication.getProfile().getData().getUser().getNick_name()));
        }
    }

}

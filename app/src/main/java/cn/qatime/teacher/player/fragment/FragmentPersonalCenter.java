package cn.qatime.teacher.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.activity.PersonalMyWalletActivity;
import cn.qatime.teacher.player.activity.ClassTableActivity;
import cn.qatime.teacher.player.activity.SettingActivity;
import cn.qatime.teacher.player.base.BaseFragment;
import cn.qatime.teacher.player.utils.Constant;

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
        information = view.findViewById(R.id.information);


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
                getActivity().startActivity(new Intent(getActivity(),ClassTableActivity.class));
                break;
            case R.id.my_tutorship:
                break;
            case R.id.setting:
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.manage:
                intent = new Intent(getActivity(), PersonalMyWalletActivity.class);
                startActivityForResult(intent,Constant.REQUEST);
                break;
            case R.id.information:
//                intent = new Intent(getActivity(), PersonalInformationActivity.class);
//                startActivityForResult(intent, Constant.REQUEST);
                break;
        }
    }
}

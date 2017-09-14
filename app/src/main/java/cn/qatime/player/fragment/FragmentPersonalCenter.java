package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.qatime.player.R;
import cn.qatime.player.activity.ClassTableActivity;
import cn.qatime.player.activity.PayPSWForgetActivity;
import cn.qatime.player.activity.PersonalInformationActivity;
import cn.qatime.player.activity.PersonalMyExclusiveActivity;
import cn.qatime.player.activity.PersonalMyFilesActivity;
import cn.qatime.player.activity.PersonalMyHomeworkActivity;
import cn.qatime.player.activity.PersonalMyInteractActivity;
import cn.qatime.player.activity.PersonalMyQuestionActivity;
import cn.qatime.player.activity.PersonalMyTutorshipActivity;
import cn.qatime.player.activity.PersonalMyVideoActivity;
import cn.qatime.player.activity.PersonalMyWalletActivity;
import cn.qatime.player.activity.SettingActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.holder.BaseViewHolder;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.view.GridDivider;
import libraryextra.bean.CashAccountBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2016/10/25 14:47
 * @Describe
 */
public class FragmentPersonalCenter extends BaseFragment implements View.OnClickListener {


    private TextView name;
    private TextView balance;
    private ImageView headSculpture;
    private TextView nickName;
    private View cashAccountSafe;
    private View close;
    private boolean closed = false;//是否提示过未设置支付密码
    private RecyclerView recyclerView;
    private String[] menuString = {"课程表", "直播课", "一对一", "视频课", "专属课", "提问管理", "作业管理", "我的文件"};
    private int[] menuRes = {R.mipmap.center_class_table, R.mipmap.center_my_live, R.mipmap.center_my_interact, R.mipmap.center_my_video, R.mipmap.center_my_exclusive, R.mipmap.center_my_question, R.mipmap.center_my_homework, R.mipmap.center_my_files};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        initCashAccountSafe();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initCashAccountSafe() {
        CashAccountBean cashAccount = BaseApplication.getCashAccount();
        if (cashAccount != null && cashAccount.getData() != null) {
            if (!cashAccount.getData().isHas_password()) {
                cashAccountSafe.setVisibility(View.VISIBLE);
                cashAccountSafe.setOnClickListener(this);
                close.setOnClickListener(this);
            } else {
                cashAccountSafe.setVisibility(View.GONE);
            }
        }
    }

    private void initView(View view) {
        View setting = view.findViewById(R.id.setting);
        View manage = view.findViewById(R.id.manage);
        headSculpture = (ImageView) view.findViewById(R.id.head_sculpture);
        name = (TextView) view.findViewById(R.id.name);
        nickName = (TextView) view.findViewById(R.id.nick_name);
        balance = (TextView) view.findViewById(R.id.balance);
        cashAccountSafe = view.findViewById(R.id.cash_account_safe);
        close = view.findViewById(R.id.close);
        View information = view.findViewById(R.id.information);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();

        if (BaseApplication.getProfile().getData() != null && BaseApplication.getProfile().getData().getUser() != null) {
            Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.error_header).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
        }
        name.setText(BaseApplication.getProfile().getData().getUser().getName());
        nickName.setText("昵称:" + BaseApplication.getProfile().getData().getUser().getNick_name());
        if (BaseApplication.getCashAccount() != null) {
            balance.setText("￥" + BaseApplication.getCashAccount().getData().getBalance());
        }

        setting.setOnClickListener(this);
        manage.setOnClickListener(this);
        information.setOnClickListener(this);
    }

    @Deprecated
    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new GridDivider(getActivity(), 3, 0xffeeeeee));
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter<BaseViewHolder> recyclerAdapter = new RecyclerView.Adapter<BaseViewHolder>() {
            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View item = View.inflate(getActivity(), R.layout.item_personal_center, null);
                return new BaseViewHolder(item);
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, final int position) {
                holder.setText(R.id.text, menuString[position]);
                holder.setImageResource(R.id.image, menuRes[position]);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(getActivity(), ClassTableActivity.class));
                                break;
                            case 1:
                                startActivity(new Intent(getActivity(), PersonalMyTutorshipActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(getActivity(), PersonalMyInteractActivity.class));
                                break;
                            case 3:
                                startActivity(new Intent(getActivity(), PersonalMyVideoActivity.class));
                                break;
                            case 4:
                                startActivity(new Intent(getActivity(), PersonalMyExclusiveActivity.class));
                                break;
                            case 5:
                                startActivity(new Intent(getActivity(), PersonalMyQuestionActivity.class));
                                break;
                            case 6:
                                startActivity(new Intent(getActivity(), PersonalMyHomeworkActivity.class));
                                break;
                            case 7:
                                startActivity(new Intent(getActivity(), PersonalMyFilesActivity.class));
                                break;
                        }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return menuString.length;
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Subscribe
    public void onEvent(BusEvent event) {
        if (event == BusEvent.ON_REFRESH_CASH_ACCOUNT) {
            if (BaseApplication.getCashAccount() != null) {
                balance.setText("￥" + BaseApplication.getCashAccount().getData().getBalance());
            }
            if (!closed) {
                initCashAccountSafe();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.cash_account_safe:
                intent = new Intent(getActivity(), PayPSWForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.close:
                closed = true;
                cashAccountSafe.setVisibility(View.GONE);
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

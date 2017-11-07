package cn.qatime.player.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.MessageActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.MessageListBean;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.observer.UserInfoObservable;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DateUtils;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2016/10/25 14:57
 * @Describe
 */
public class FragmentNews extends BaseFragment {
    private ArrayList<MessageListBean> items;
    private CommonAdapter<MessageListBean> adapter;
    private PullToRefreshListView listView;
    private boolean msgLoaded = false;
    private List<RecentContact> loadedRecents;
    private UserInfoObservable.UserInfoObserver userInfoObserver;
    private UserInfoObservable userInfoObservable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_news, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getRefreshableView().setDividerHeight(1);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMessageList();
        requestMessages(true);
        registerObservers(true);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();

        adapter = new CommonAdapter<MessageListBean>(getActivity(), items, R.layout.item_fragment_news) {
            @Override
            public void convert(ViewHolder holder, MessageListBean item, int position) {
//                Glide.with(getActivity()).load(item.getIcon()).placeholder(R.mipmap.photo).crossFade().into((ImageView) holder.getView(R.id.image));
                holder.setText(R.id.name, item.getName());
                ((ImageView) holder.getView(R.id.notify)).setVisibility(item.isMute() ? View.VISIBLE : View.GONE);
                holder.getView(R.id.count).setVisibility(item.getUnreadCount() == 0 ? View.GONE : View.VISIBLE);
                holder.setText(R.id.count, String.valueOf(item.getUnreadCount()));
                String timeString = DateUtils.getTimeShowString(item.getTime(), false);
                holder.setText(R.id.time, timeString);

            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("sessionId", items.get(position - 1).getContactId());
                intent.putExtra("sessionType", items.get(position - 1).getSessionType());
                intent.putExtra("name", items.get(position - 1).getName());
                startActivity(intent);
            }
        });

        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (NIMClient.getStatus() == StatusCode.LOGINED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final AlertDialog alertDialog = builder.create();
                    View v = View.inflate(getActivity(), R.layout.dialog_team_notify_alert, null);
                    v.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    ((TextView) v.findViewById(R.id.text)).setText(items.get(position - 1).isMute() ? getResourceString(R.string.resume_alert) : getResourceString(R.string.nolongger_alert));
                    v.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();

                            NIMClient.getService(TeamService.class).muteTeam(items.get(position - 1).getContactId(), !items.get(position - 1).isMute()).setCallback(new RequestCallback<Void>() {
                                @Override
                                public void onSuccess(Void param) {
                                    Team team = TeamDataCache.getInstance().getTeamById(items.get(position - 1).getContactId());
                                    items.get(position - 1).setMute(team.mute());
//                                notificationConfigText.setText(team.mute() ? getString(R.string.close) : getString(R.string.open));
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailed(int code) {
                                    Logger.e("muteTeam failed code:" + code);
                                }

                                @Override
                                public void onException(Throwable exception) {

                                }
                            });
                        }
                    });
                    v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                    alertDialog.setContentView(v);

                    return true;
                }
                return false;
            }
        });
    }


    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        loadedRecents = recents;

                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        //
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 1000 : 0);
    }

    private void onRecentContactsLoaded() {
        if (loadedRecents == null) {
            //当有任意一个为空都不加载...
            return;
        }
        items.clear();
        Iterator<RecentContact> iterator = loadedRecents.iterator();
        while (iterator.hasNext()) {
            RecentContact item = iterator.next();
            Team team = TeamDataCache.getInstance().getTeamById(item.getContactId());
            if (team != null && !team.isMyTeam()) {
                NIMClient.getService(MsgService.class).deleteRecentContact(item);
                NIMClient.getService(MsgService.class).clearChattingHistory(item.getContactId(), item.getSessionType());
                iterator.remove();
                Logger.e("删除已退出的群组");
                continue;
            }
            MessageListBean bean = new MessageListBean();
            bean.setContactId(item.getContactId());
            if (team != null) {
                bean.setMute(team.mute());
            }
            bean.setSessionType(item.getSessionType());
            bean.setUnreadCount(item.getUnreadCount());
            bean.setTime(item.getTime());
            bean.setRecentMessageId(item.getRecentMessageId());
            if (StringUtils.isNullOrBlanK(bean.getName())) {
                bean.setName(TeamDataCache.getInstance().getTeamName(item.getContactId()).replace("讨论组", ""));
            }
            if (!items.contains(bean)) {//依据contactId判断
                items.add(bean);
            }
            if (item.getUnreadCount() != 0 && StringUtils.isNullOrBlanK(bean.getContactId())) {//如果未读数不为0并且不再items中显示的话(contactId未赋值)，则将未读消息数清空
                NIMClient.getService(MsgService.class).clearUnreadCount(item.getContactId(), item.getSessionType());
            }
        }
        if (loadedRecents != null) {
            loadedRecents = null;
        }
        refreshMessages();
    }

    private void refreshMessages() {
        sortRecentContacts(items);
        adapter.notifyDataSetChanged();
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<MessageListBean> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<MessageListBean> comp = new Comparator<MessageListBean>() {
        @Override
        public int compare(MessageListBean lhs, MessageListBean rhs) {
            // 先比较置顶tag
//            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
//            if (sticky != 0) {
//                return sticky > 0 ? -1 : 1;
//            } else {
            long time = lhs.getTime() - rhs.getTime();
            return time == 0 ? 0 : (time > 0 ? -1 : 1);
//            }
        }
    };


    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);
        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages();
                }
            };
        }

        if (userInfoObservable == null) {
            userInfoObservable = new UserInfoObservable(getContext());
        }
        userInfoObservable.registerObserver(userInfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            if (userInfoObservable != null) {
                userInfoObservable.unregisterObserver(userInfoObserver);
            }
        }
    }

    /**
     * 监听用户在线状态
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
//                kickOut(code);
                Logger.e("未登录成功");
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    Logger.e("当前网络不可用");
                } else if (code == StatusCode.UNLOGIN) {
                    Logger.e("未登录");
                } else if (code == StatusCode.CONNECTING) {
                    Logger.e("连接中...");
                } else if (code == StatusCode.LOGINING) {
                    Logger.e("登录中...");
                } else {
//                    onRecentContactsLoaded();
                    Logger.e("其他" + code);
                }
            }
        }
    };

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        }
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {
            // 若列表中已存在的群组状态（是否已解散）发生变化，只会在初次加载时删除
            Iterator<RecentContact> iterator = messages.iterator();
            while (iterator.hasNext()) {
                RecentContact msg = iterator.next();
                Team team = TeamDataCache.getInstance().getTeamById(msg.getContactId());
                if (team != null && !team.isMyTeam()) {
                    NIMClient.getService(MsgService.class).deleteRecentContact(msg);
                    NIMClient.getService(MsgService.class).clearChattingHistory(msg.getContactId(), msg.getSessionType());
                    iterator.remove();
                    Logger.e("删除已退出的群组");
                    continue;
                }
                int index = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (msg.getContactId().equals(items.get(i).getContactId())
                            && msg.getSessionType() == (items.get(i).getSessionType())) {
                        index = i;
                        break;
                    }
                }

                MessageListBean bean = new MessageListBean();
                if (index >= 0) {
                    bean = items.get(index);
                    items.remove(index);
                }
                bean.setContactId(msg.getContactId());
                if (team != null) {
                    bean.setMute(team.mute());
                }
                bean.setSessionType(msg.getSessionType());
                if (StringUtils.isNullOrBlanK(bean.getName())) {
                    bean.setName(TeamDataCache.getInstance().getTeamName(msg.getContactId()).replace("讨论组", ""));
                }
                bean.setUnreadCount(msg.getUnreadCount());
                bean.setTime(msg.getTime());
                bean.setRecentMessageId(msg.getRecentMessageId());
                if (!items.contains(bean)) {
                    items.add(bean);
                }
            }

            refreshMessages();
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                MessageListBean item = items.get(index);
                item.setMsgStatus(message.getStatus());
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (MessageListBean item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages();
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages();
            }
        }
    };
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {

        }
    };


    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            MessageListBean item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    public void setMessage(IMMessage message) {
        int position = -1;
        Logger.e("item.size" + items.size());
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getContactId().equals(message.getSessionId())) {
                    position = i;
                    break;
                }
            }
        }
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("sessionId", items.get(position).getContactId());
        intent.putExtra("sessionType", items.get(position).getSessionType());
        intent.putExtra("name", items.get(position).getName());
        startActivity(intent);
    }
}

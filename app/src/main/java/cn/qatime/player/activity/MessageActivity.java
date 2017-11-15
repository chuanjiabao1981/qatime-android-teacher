package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.Container;
import cn.qatime.player.bean.InputPanel;
import cn.qatime.player.bean.MessageListPanel;
import cn.qatime.player.bean.ModuleProxy;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import libraryextra.utils.NetUtils;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/30 12:25
 * @Description
 */
public class MessageActivity extends BaseActivity implements ModuleProxy, InputPanel.InputPanelListener {
    private String sessionId;//聊天对象id
    private SessionTypeEnum sessionType;
    private Team team;
    private TextView tipText;

    private boolean isMute = false;//当前用户 是否被禁言
    private View rootView;
    private InputPanel inputpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_message, null);
        setContentView(rootView);
        String name = getIntent().getStringExtra("name");
        if (!StringUtils.isNullOrBlanK(name)) {
            setTitle(name);
        } else {
            setTitle(getResources().getString(R.string.team));
        }
        sessionId = getIntent().getStringExtra("sessionId");
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra("sessionType");
//        courseId = getIntent().getIntExtra("courseId", 0);
//        pull_address = getIntent().getStringExtra("pull_address");
        initView();
        registerTeamUpdateObserver(true);
    }

    @Override
    public int getContentView() {
        return 0;
    }

    private void initView() {
        TeamMember teamMember = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getInstance().getAccount());
        if (teamMember != null) {
            isMute = teamMember.isMute();
        }
        tipText = (TextView) findViewById(R.id.tip);
        Container container = new Container(this, sessionId, this);
        messageListPanel = new MessageListPanel(container, rootView);

        inputpanel = new InputPanel(this, this, rootView, true, sessionId);
        inputpanel.setMute(isMute);
        inputpanel.setOnInputShowListener(new InputPanel.OnInputShowListener() {
            @Override
            public void OnInputShow() {
                messageListPanel.scrollToBottom();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (NetUtils.checkRecordAudioPermission(this)) {
                } else {
                    Toast.makeText(this, "未取得录音权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(receiveMessageObserver, register);
    }

    Observer<List<IMMessage>> receiveMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            messageListPanel.onIncomingMessage(messages);
            TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getInstance().getAccount());
            if (team != null) {
                isMute = team.isMute();
                inputpanel.setMute(isMute);
            }
        }
    };

    /**
     * 请求群基本信息
     */

    private void requestTeamInfo() {
        Team team = TeamDataCache.getInstance().getTeamById(sessionId);
        if (team != null) {
            updateTeamInfo(team);
        } else {
            TeamDataCache.getInstance().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        Toast.makeText(MessageActivity.this, getResourceString(R.string.get_group_failed), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    /**
     * 更新群信息
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }
        team = d;
        inputpanel.setTeam(team);
        tipText.setText(team.getType() == TeamTypeEnum.Normal ? getResourceString(R.string.you_have_quit_the_group) : getResourceString(R.string.you_have_quit_the_group));
        tipText.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
    }


    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
//        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(MessageActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    private MessageListPanel messageListPanel;
    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            messageListPanel.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        messageListPanel.onResume();
        registerObservers(true);
        requestTeamInfo();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.viewPager) != null && findViewById(R.id.viewPager).getVisibility() == View.VISIBLE) {
            findViewById(R.id.viewPager).setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerObservers(false);
        inputpanel.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerTeamUpdateObserver(false);
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputpanel.closeEmojiAndInput();
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputpanel.isRecording();
    }

    @Override
    public void ChatMessage(IMMessage message) {

        sendMessage(message);
    }

    private void sendMessage(IMMessage message) {
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, true);

        messageListPanel.onMsgSend(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputpanel.onActivityResult(requestCode, resultCode, data);
    }
}

package cn.qatime.teacher.player.holder;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.adapter.BaseMultiItemFetchLoadAdapter;


public class MsgViewHolderUnknown extends MsgViewHolderBase {

    public MsgViewHolderUnknown(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_unknown;
    }

    @Override
    protected boolean isShowHeadImage() {
        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
            return false;
        }
        return true;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
    }
}

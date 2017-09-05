package cn.qatime.player.bean;

import java.util.List;

import libraryextra.bean.ChatTeamBean;

/**
 * Created by lenovo on 2017/9/5.
 */

public class RemedialClassPlayInfoBean {

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private ChatTeamBean chat_team;

        public ChatTeamBean getChat_team() {
            return chat_team;
        }

        public void setChat_team(ChatTeamBean chat_team) {
            this.chat_team = chat_team;
        }
    }
}

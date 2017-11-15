package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.FragmentNEVideoPlayerAdapter;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.ChatTeamBean;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;

/**
 * Created by lenovo on 2017/9/5.
 */

public class MembersActivity extends BaseActivity {
    private List<ChatTeamBean.Accounts> list = new ArrayList<>();
    private FragmentNEVideoPlayerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("成员列表");
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new FragmentNEVideoPlayerAdapter(this, list, R.layout.item_fragment_nevideo_player);
        listView.setAdapter(adapter);

        ChatTeamBean chatTeam = (ChatTeamBean) getIntent().getSerializableExtra("members");
        if (chatTeam != null) {
            setData(chatTeam.getAccounts());
        }

    }

    @Override
    public int getContentView() {
        return R.layout.activity_members;
    }

    /**
     * @param accounts
     */
    public void setData(List<ChatTeamBean.Accounts> accounts) {
        if (accounts != null) {
            list.clear();
            list.addAll(accounts);
            for (ChatTeamBean.Accounts item : list) {
                if (item == null) continue;
//                if (!StringUtils.isNullOrBlanK(accounts.getOwner())) {
//                    if (accounts.getOwner().equals(item.getAccid())) {
//                        item.setOwner(true);
//                    } else {
//                        item.setOwner(false);
//                    }
//                }
                if (StringUtils.isNullOrBlanK(item.getName())) {
                    item.setFirstLetters("");
                } else {
                    item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getName()));
                }
            }
            Collections.sort(list, new Comparator<ChatTeamBean.Accounts>() {
                @Override
                public int compare(ChatTeamBean.Accounts lhs, ChatTeamBean.Accounts rhs) {
//                    int x = 0;
//                    if (lhs.isOwner() && !rhs.isOwner()) {
//                        x = -3;
//                    } else if (!lhs.isOwner() && rhs.isOwner()) {
//                        x = 3;
//                    } else if (lhs.isOwner() && rhs.isOwner()) {
//                        x = -3;
//                    }

//                    int y = lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
//                    if (x == 0) {
//                        return y;
//                    }
//                    return x;
                    return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                }
            });
            adapter.notifyDataSetChanged();
        }
    }
}

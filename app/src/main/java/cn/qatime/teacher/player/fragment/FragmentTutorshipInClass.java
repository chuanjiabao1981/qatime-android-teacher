package cn.qatime.teacher.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.teacher.player.R;
import cn.qatime.teacher.player.activity.RemedialClassDetailActivity;
import cn.qatime.teacher.player.base.BaseApplication;
import cn.qatime.teacher.player.base.BaseFragment;
import cn.qatime.teacher.player.bean.DaYiJsonObjectRequest;
import cn.qatime.teacher.player.bean.MyTutorialClassBean;
import cn.qatime.teacher.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/5/16 14:46
 * @Describe
 */

public class FragmentTutorshipInClass extends BaseFragment {
    private PullToRefreshListView listView;
    private CommonAdapter<MyTutorialClassBean.DataBean> adapter;
    private List<MyTutorialClassBean.DataBean> list = new ArrayList<>();
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_tutorship, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initOver = true;
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));
        adapter = new CommonAdapter<MyTutorialClassBean.DataBean>(getActivity(), list, R.layout.item_fragment_personal_my_tutorship) {
            @Override
            public void convert(ViewHolder helper, final MyTutorialClassBean.DataBean item, int position) {
                Glide.with(getActivity()).load(item.getPublicize()).placeholder(R.mipmap.photo).crossFade().into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.name, item.getName())
                        .setText(R.id.grade, item.getGrade())
                        .setText(R.id.price, "￥" + item.getCurrent_price())
                        .setText(R.id.progress, "(" + item.getTeacher_percentage() + "%)")
                        .setText(R.id.number, String.valueOf(item.getBuy_tickets_count()))
                        .setText(R.id.teaching_time, item.getCompleted_lesson_count() + "/" + item.getPreset_lesson_count());
            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData(2);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", list.get(position - 1).getId());
                startActivity(intent);
            }
        });
    }

    public void onShow() {
        if (!isLoad) {
            if (initOver) {
                initData(1);
            } else {
                super.onShow();
            }
        }
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        map.put("status", "teaching");

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlTeachers + BaseApplication.getUserId() + "/courses", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        isLoad = true;
                        if (type == 1) {
                            list.clear();
                        }
                        String label = null;
                        if (getActivity() != null) {
                            label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        }
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();

                        try {
                            MyTutorialClassBean data = JsonUtils.objectFromJson(response.toString(), MyTutorialClassBean.class);
                            if (data != null) {
                                list.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        String label = DateUtils.formatDateTime(
                                getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        listView.getLoadingLayoutProxy(false, true)
                                .setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                listView.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}

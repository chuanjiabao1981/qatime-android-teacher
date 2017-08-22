package cn.qatime.player.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ExclusiveCourseDetailBean;
import libraryextra.utils.StringUtils;

public class FragmentExclusiveCourseClassInfo extends BaseFragment {

    WebView describe;
    TextView classStartTime;
    TextView classEndTime;
    TextView subject;
    TextView grade;
    TextView totalclass;
    ExclusiveCourseDetailBean data;
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private TextView suitable;
    private TextView target;
    private WebView learningTips;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exclusive_course_class_info, container, false);
        initview(view);

        return view;
    }


    private void initview(View view) {
        subject = (TextView) view.findViewById(R.id.subject);
        grade = (TextView) view.findViewById(R.id.grade);
        target = (TextView) view.findViewById(R.id.target);
        suitable = (TextView) view.findViewById(R.id.suitable);
        classStartTime = (TextView) view.findViewById(R.id.class_start_time);
        classEndTime = (TextView) view.findViewById(R.id.class_end_time);
        totalclass = (TextView) view.findViewById(R.id.total_class);
        describe = (WebView) view.findViewById(R.id.describe);
        learningTips = (WebView) view.findViewById(R.id.learning_tips);
//        String[] value = new String[]{"高考", "小學考試", "期中考試", "期末", "模擬考", "高高考", "小學考試", "期中考試", "期末", "模擬考"};

        initWebView(describe);
        initWebView(learningTips);

//        ((TextView) view.findViewById(R.id.notice)).setText(Html.fromHtml());
    }

    private void initWebView(WebView webView) {
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setBackgroundColor(0); // 设置背景色
        webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        webView.setFocusable(false);//防止加载之后webview滚动
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        settings.setDefaultFontSize(14);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(settings.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
    }

    public void setData(ExclusiveCourseDetailBean bean) {
        if (bean != null && bean.getData() != null) {
            subject.setText((StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getSubject()) ? "" : bean.getData().getCustomized_group().getSubject()));
            classStartTime.setText(parse2.format(new Date(Long.valueOf(bean.getData().getCustomized_group().getStart_at())*1000)));
            classEndTime.setText(parse2.format(new Date(Long.valueOf(bean.getData().getCustomized_group().getEnd_at())*1000)));
            grade.setText((bean.getData().getCustomized_group().getGrade() == null ? "" : bean.getData().getCustomized_group().getGrade()));
            totalclass.setText(getString(R.string.lesson_count, bean.getData().getCustomized_group().getEvents_count()));
            if (!StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getObjective())) {
                target.setText(bean.getData().getCustomized_group().getObjective());
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getSuit_crowd())) {
                suitable.setText(bean.getData().getCustomized_group().getSuit_crowd());
            }
            String header = "<style>* {color:#666666;margin:0;padding:0;}.one {float: left;width:50%;height:auto;position: relative;text-align: center;}.two {width:100%;height:100%;top: 0;left:0;position: absolute;text-align: center;}</style>";//默认color段落间距
            String body = StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getDescription()) ? getString(R.string.no_desc) : bean.getData().getCustomized_group().getDescription();
            body = body.replace("\r\n", "<br>");
            //......
            String footer =
                    "<p style='margin-top:20'><font style='font-size:15;color:#333333'}>学习须知</font></p>" +
                            "<p style='margin-top:5;'><font style='font-size:15;color:#333333'}>上课前</font></p>" +
                            "<p><font>1.做好课程预习，预先了解本课所讲内容，更好的吸收课程精华；<br>" +
                            "2.准备好相关的学习工具（如：纸、笔等）并在上课前调试好电脑，使用手机请保持电量充足。<br>" +
                            "3.选择安静的学习环境，并将与学习无关的事物置于远处；选择安静的环境避免影响听课。 <br>" +
                            "4.三年级以下的同学请在家长帮助下学习。<br>" +
                            "5.遇到网页不能打开或者不能登录等情况请及时联系客服。</font></p>" +
                            "<p style='margin-top:5;'><font style='font-size:15;color:#333333'>上课中</font></p>" +
                            "<p><font >1.时刻保持注意力集中，认真听讲才能更好的提升学习；<br>" +
                            "2.课程中遇到听不懂的问题及时通过聊天或互动申请向老师提问，老师收到后会给予解答；<br>" +
                            "3.积极响应老师的授课，完成老师布置的课上任务；<br>" +
                            "4.禁止在上课中闲聊或发送一切与本课无关的内容，如有发现，一律禁言；<br>" +
                            "5.上课途中如突遇屏幕卡顿，直播中断等特殊情况，请刷新后等待直播恢复；超过15分钟未恢复去请致电客服；<br>" +
                            "</font></p>" +
                            "<p style='margin-top:5;'><font style='font-size:15;color:#333333'>上课后</font></p>" +
                            "<p><font>1.直播结束后请大家仍可以在直播教室内进行聊天和讨论，老师也会适时解答；<br>" +
                            "2.请同学按时完成老师布置的作业任务。</font></p>" +
                            "<p style='margin-top:5;'><font style='font-size:15;color:#333333'>回放说明</font></p>" +
                            "<p><font>1.购买课程后方可观看回放；<br>" +
                            "2.专属课课程回放暂无任何观看限制，学生可任意观看；<br>" +
                            "3.直播结束后最晚于24小时内上传回放；<br>" +
                            "4.回放内容不完全等于直播内容，请尽量观看直播进行学习；<br>" +
                            "5.回放内容仅供学生学习使用，未经允许不得进行录制。\n</font></p>";
            describe.loadDataWithBaseURL(null, header + body, "text/html", "UTF-8", null);
            learningTips.loadDataWithBaseURL(null, header + footer, "text/html", "UTF-8", null);
        }
    }
}

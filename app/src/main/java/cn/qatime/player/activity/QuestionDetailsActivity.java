package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.AttachmentsBean;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.QuestionsBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.ExpandView;
import libraryextra.bean.ImageItem;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/18.
 */

public class QuestionDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ExpandView expandView;
    private TextView author;
    private TextView createTime;
    private TextView questionName;
    private ExpandView replyView;
    private QuestionsBean.DataBean question;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextView resolveQuestion;
    private QuestionsBean.DataBean.AnswerBean answerBean;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        initView();
        initData();
//        Intent intent = new Intent(ExclusiveQuestionsActivity.this, QuestionResolveActivity.class);
//        intent.putExtra("courseId", courseId);
//        startActivityForResult(intent, Constant.REQUEST);
    }

    @Override
    public int getContentView() {
        return 0;
    }

    private void initView() {
        expandView = (ExpandView) findViewById(R.id.question_view);
        replyView = (ExpandView) findViewById(R.id.reply_view);
        questionName = (TextView) findViewById(R.id.question_name);
        resolveQuestion = (TextView) findViewById(R.id.resolve_question);
        createTime = (TextView) findViewById(R.id.create_time);
        author = (TextView) findViewById(R.id.author);
        findViewById(R.id.expand).setOnClickListener(this);
    }


    private void initData() {
        String id = getIntent().getStringExtra("id");
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlQuestions + id, null, new VolleyListener(QuestionDetailsActivity.this) {

            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    question = JsonUtils.objectFromJson(response.getString("data"), QuestionsBean.DataBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (question == null) {
                    Toast.makeText(QuestionDetailsActivity.this, "问题获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTitle(question.getTitle());
                setRightText("提交", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (question.getStatus().equals("resolved")) {
                            dialog();
                        } else {
                            resolveQuestion();
                        }
                    }
                });
                questionName.setText(question.getTitle());
                if ("resolved".equals(question.getStatus())) {
                    long replyT = question.getAnswer().getCreated_at() * 1000L;
                    createTime.setText("回复时间 " + parse.format(new Date(replyT)));
                } else {
                    long time = question.getCreated_at() * 1000L;
                    createTime.setText("创建时间 " + parse.format(new Date(time)));
                }
                author.setText(question.getUser_name());
                List<AttachmentsBean> questionAttachments = question.getAttachments();
                AttachmentsBean audioAttachments = new AttachmentsBean();
                List<ImageItem> imageAttachments = new ArrayList<>();
                if (questionAttachments != null && questionAttachments.size() > 0) {
                    for (AttachmentsBean answerAttachment : questionAttachments) {
                        if ("mp3".equals(answerAttachment.file_type)) {
                            audioAttachments = answerAttachment;
                        } else {
                            ImageItem imageItem = new ImageItem();
                            imageItem.imagePath = answerAttachment.file_url;
                            imageAttachments.add(imageItem);
                        }
                    }
                }
                expandView.initExpandView(question.getBody(), audioAttachments.file_url, imageAttachments, true);
                if ("resolved".equals(question.getStatus())) {//已回复
                    resolveQuestion.setText("修改回答");
                    findViewById(R.id.reply_layout).setVisibility(View.VISIBLE);
                    if (question.getAnswer() != null) {
                        List<AttachmentsBean> answerAttachments = question.getAnswer().getAttachments();
                        AttachmentsBean answerAudioAttachments = new AttachmentsBean();
                        List<ImageItem> answerImageAttachments = new ArrayList<>();
                        if (answerAttachments != null && answerAttachments.size() > 0) {
                            for (AttachmentsBean answerAttachment : answerAttachments) {
                                if ("mp3".equals(answerAttachment.file_type)) {
                                    answerAudioAttachments = answerAttachment;
                                } else {
                                    ImageItem imageItem = new ImageItem();
                                    imageItem.imagePath = answerAttachment.file_url;
                                    answerImageAttachments.add(imageItem);
                                }
                            }
                        }
                        replyView.initExpandView(question.getAnswer().getBody(), answerAudioAttachments.file_url, answerImageAttachments, true);
                    } else {
                        replyView.initExpandView("无", null, null, true);
                    }
                }

                resolveQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QuestionDetailsActivity.this, QuestionResolveActivity.class);
                        startActivityForResult(intent, Constant.REQUEST);
                    }
                });
            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new VolleyErrorListener()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            answerBean = (QuestionsBean.DataBean.AnswerBean) data.getSerializableExtra("answer");
//            if(question.getAnswer()!=null){
//                answerBean.setId(question.getAnswer().getId());
//            }
            question.setAnswer(answerBean);
            resolveQuestion.setText("修改回答");
            findViewById(R.id.reply_layout).setVisibility(View.VISIBLE);
            List<AttachmentsBean> answerAttachments = question.getAnswer().getAttachments();
            AttachmentsBean answerAudioAttachments = new AttachmentsBean();
            List<ImageItem> answerImageAttachments = new ArrayList<>();
            if (answerAttachments != null && answerAttachments.size() > 0) {
                for (AttachmentsBean answerAttachment : answerAttachments) {
                    if ("mp3".equals(answerAttachment.file_type)) {
                        answerAudioAttachments = answerAttachment;
                    } else {
                        ImageItem imageItem = new ImageItem();
                        imageItem.imagePath = answerAttachment.file_url;
                        answerImageAttachments.add(imageItem);
                    }
                }
            }
            replyView.initExpandView(question.getAnswer().getBody(), answerAudioAttachments.file_url, answerImageAttachments, true);
        }
    }

    private void resolveQuestion() {
        if (StringUtils.isNullOrBlanK(answerBean)) {
            Toast.makeText(this, "请输入回答内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isNullOrBlanK(question.getAnswer().getBody())) {
            map.put("body", question.getAnswer().getBody());
        }
        if (answerBean.getAttachments() != null && answerBean.getAttachments().size() > 0) {
            map.put("quotes_attributes", getContentString());
        }
        JSONObject obj = new JSONObject(map);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlLiveStudio + "questions/" + question.getId() + "/answers", obj,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        try {
                            JSONObject error = response.getJSONObject("error");
                            if(error.getInt("code")==3002){
                                Toast.makeText(QuestionDetailsActivity.this, error.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private String getContentString() {
        StringBuilder sb = new StringBuilder("[");
        for (AttachmentsBean attachment : answerBean.getAttachments()) {
            sb.append("{\"attachment_id\":\"")
                    .append(attachment.id)
                    .append("\"},");
        }
        if (sb.length() > 1) {
            sb.setCharAt(sb.length() - 1, ']');
            Logger.e(sb.toString());
            return sb.toString();
        } else {
            return "";
        }
    }

    private void dialog() {
        if (alertDialog == null) {
            View view = View.inflate(QuestionDetailsActivity.this, R.layout.dialog_cancel_or_confirm, null);
            Button cancel = (Button) view.findViewById(R.id.cancel);
            Button confirm = (Button) view.findViewById(R.id.confirm);
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText("重新提交将覆盖原有内容且不可恢复，是否提交？");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resolveQuestion();
                    alertDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(QuestionDetailsActivity.this);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setContentView(view);
        } else {
            alertDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand:
                if (expandView.isExpand()) {
                    expandView.collapse();
                } else {
                    expandView.expand();
                }
                break;
        }
    }

}

package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.QuestionsBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.ExpandView;
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
    private String answerBody;


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
        resolveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void initData() {
        String id = getIntent().getStringExtra("id");
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlQuestions+id,null,new VolleyListener(QuestionDetailsActivity.this){

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
                            reResolveQuestion();
                        }else {
                            resolveQuestion();
                        }
                    }
                });
                questionName.setText(question.getTitle());
                long time = question.getCreated_at() * 1000L;
                createTime.setText("创建时间 " + parse.format(new Date(time)));
                author.setText(question.getUser_name());
                expandView.initExpandView(question.getBody(), null, null, true);
                if ("resolved".equals(question.getStatus())) {//已回复
                    resolveQuestion.setText("修改回答");
                    findViewById(R.id.reply_layout).setVisibility(View.VISIBLE);
                    replyView.initExpandView(question.getAnswer().getBody(), null, null, true);
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
        },new VolleyErrorListener()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            answerBody = data.getStringExtra("body");
            if (question.getAnswer() == null) {
                QuestionsBean.DataBean.AnswerBean answer = new QuestionsBean.DataBean.AnswerBean();
                answer.setBody(answerBody);
                question.setAnswer(answer);
            } else {
                question.getAnswer().setBody(answerBody);
            }
            resolveQuestion.setText("修改回答");
            findViewById(R.id.reply_layout).setVisibility(View.VISIBLE);
            replyView.initExpandView(question.getAnswer().getBody(), null, null, true);
        }

    }

    private void resolveQuestion() {
        if(StringUtils.isNullOrBlanK(answerBody)){
            Toast.makeText(this, "请输入回答内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("body", question.getAnswer().getBody());
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

    private void reResolveQuestion() {
        if(StringUtils.isNullOrBlanK(answerBody)){
            Toast.makeText(this, "请输入回答内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("body", question.getAnswer().getBody());
        JSONObject obj = new JSONObject(map);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PATCH, UrlUtils.urlLiveStudio + "answers/" + question.getAnswer().getId(), obj,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (expandView != null) {
            expandView.onDestroy();
        }
    }
}

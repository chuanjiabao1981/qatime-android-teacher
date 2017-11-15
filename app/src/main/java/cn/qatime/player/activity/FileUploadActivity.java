package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.DaYiJsonObjectRequest;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.utils.ImageUtil;
import cn.qatime.player.utils.MyVideoThumbLoader;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.rx.HttpManager;
import libraryextra.rx.body.ProgressResponseCallBack;
import libraryextra.rx.callback.SimpleCallBack;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/31.
 */

public class FileUploadActivity extends BaseActivity {

    private TextView name;
    private TextView size;
    private ProgressBar progress;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            progress.setProgress(msg.what);
            boolean done = (boolean) msg.obj;
            if (done) {
                Toast.makeText(FileUploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };
    private int courseId;
    private MyFilesBean.DataBean item;
    private ImageView image;

    @Override
    public int getContentView() {
        return R.layout.activity_file_upload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("文件上传");
        initView();
        File file = (File) getIntent().getSerializableExtra("file");
        name.setText(file.getName());
        size.setText(DataCleanUtils.getFormatSize(file.length()));
        String extName =file.getName().substring(file.getName().lastIndexOf(".")+1,file.getName().length());
        if (extName.equals("doc") || extName.equals("docx")) {
            image.setImageResource(R.mipmap.word);
        } else if (extName.equals("xls") || extName.equals("xlsx")) {
            image.setImageResource(R.mipmap.excel);
        } else if (extName.equals("pdf")) {
            image.setImageResource(R.mipmap.pdf);
        } else if (extName.equals("mp4")) {
            MyVideoThumbLoader mVideoThumbLoader = new MyVideoThumbLoader();
            mVideoThumbLoader.showThumbByAsyncTask(file,image);
        } else if (extName.equals("jpg") || extName.equals("png")) {
            Glide.with(this).load(file.getAbsolutePath()).placeholder(R.mipmap.unknown).centerCrop().crossFade().dontAnimate().into(image);
        } else {
            image.setImageResource( R.mipmap.unknown);
        }
        courseId = getIntent().getIntExtra("id", 0);
        if (file.exists()) {
            HttpManager.post(UrlUtils.urlFiles + "files")
                    .headers("Remember-Token", BaseApplication.getInstance().getProfile().getToken())
                    .params("file", file, new ProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
                            int pro = (int) ((float)bytesWritten / contentLength * 100);
                            Message msg = Message.obtain();
                            msg.what = pro;
                            msg.obj = done;
                            handler.sendMessage(msg);
                        }
                    })
//                        .addFileParams("file", files, new ProgressResponseCallBack() {
//                            @Override
//                            public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
//                                Logger.e("bytesWritten:" + bytesWritten + "  contentLength:" + contentLength + "   done:" + done);
//                            }
//                        })
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onSuccess(String o) {
                            if (courseId != 0) {
                                try {
                                    JSONObject jsonObject = new JSONObject(o);
                                    item = JsonUtils.objectFromJson(jsonObject.getJSONObject("data").toString(), MyFilesBean.DataBean.class);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                addToCourse();
                            }
                        }

                        @Override
                        public void onTokenOut() {

                        }
                    });
        }
    }

    private void addToCourse() {
        if (item != null) {
            Map<String, String> map = new HashMap<>();
            map.put("file_id", item.getId() + "");
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGroups + courseId + "/files", map)
                    , null, new VolleyListener(FileUploadActivity.this) {
                @Override
                protected void onTokenOut() {
                    tokenOut();
                }

                @Override
                protected void onSuccess(JSONObject response) {
                    Logger.e("上传课件成功");
                    setResult(0);
                    finish();
                }

                @Override
                protected void onError(JSONObject response) {

                }
            }, new VolleyErrorListener());
            addToRequestQueue(request);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        size = (TextView) findViewById(R.id.size);
        image = (ImageView) findViewById(R.id.image);
        progress = (ProgressBar) findViewById(R.id.progress_horizontal);
    }
}

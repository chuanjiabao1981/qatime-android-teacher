package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.rx.HttpManager;
import libraryextra.rx.body.ProgressResponseCallBack;
import libraryextra.rx.callback.SimpleCallBack;
import libraryextra.utils.DataCleanUtils;

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
        if (file != null) {
            HttpManager.post(UrlUtils.urlFiles + "files")
                    .headers("Remember-Token", BaseApplication.getProfile().getToken())
                    .params("file", file, new ProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
                            int pro = (int) (bytesWritten / contentLength * 100);
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
                    .execute(new SimpleCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onTokenOut() {

                        }
                    });
        }
    }

    @Override
    public void finish() {
        setResult(0);
        super.finish();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        size = (TextView) findViewById(R.id.size);
        progress = (ProgressBar) findViewById(R.id.progress_horizontal);
    }
}

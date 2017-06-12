package cn.qatime.player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.qatime.player.R;
import libraryextra.bean.ImageItem;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/10 21:03
 * @Description
 */
public class PictureSelectAdapter extends BaseAdapter {
    private final List<ImageItem> list;
    private final Context context;
    private boolean cameraGone;

    public boolean isCameraGone() {
        return cameraGone;
    }

    public void setCameraGone(boolean cameraGone) {
        this.cameraGone = cameraGone;
    }

    public PictureSelectAdapter(Context context, List<ImageItem> list, boolean cameraGone) {
        this.context = context;
        this.list = list;
        this.cameraGone = cameraGone;
    }

    @Override
    public int getCount() {
        return cameraGone ? list.size() : list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_picture_select, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(context) / 3, ScreenUtils.getScreenWidth(context) / 3 - 10);
        holder.image.setLayoutParams(param);

        if (cameraGone) {
            ImageItem item = list.get(position);
            if (StringUtils.isNullOrBlanK(item.thumbnailPath)) {
                item.thumbnailPath = item.imagePath;
            } else {
                File file = new File(item.thumbnailPath);
                if (!file.exists()) {
                    item.thumbnailPath = item.imagePath;
                }
            }
            Glide.with(context).load("file://" + item.thumbnailPath).placeholder(R.mipmap.default_image).crossFade().centerCrop().into(holder.image);
        } else {
            if (position == 0) {
                holder.image.setImageResource(R.mipmap.camera);
            } else {
                ImageItem item = list.get(position - 1);
                if (StringUtils.isNullOrBlanK(item.thumbnailPath)) {
                    item.thumbnailPath = item.imagePath;
                } else {
                    File file = new File(item.thumbnailPath);
                    if (!file.exists()) {
                        item.thumbnailPath = item.imagePath;
                    }
                }
                Glide.with(context).load("file://" + item.thumbnailPath).placeholder(R.mipmap.default_image).crossFade().centerCrop().into(holder.image);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public final ImageView image;
        public final View root;

        public ViewHolder(View root) {
            image = (ImageView) root.findViewById(R.id.image);
            this.root = root;
        }
    }
}

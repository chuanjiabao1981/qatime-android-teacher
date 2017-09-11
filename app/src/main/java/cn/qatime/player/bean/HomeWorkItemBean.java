package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.ImageItem;

/**
 * Created by lenovo on 2017/9/8.
 */

public class HomeWorkItemBean implements Serializable{
   public String content;
   public List<ImageItem> imageItems;
   public String videoPath;
}

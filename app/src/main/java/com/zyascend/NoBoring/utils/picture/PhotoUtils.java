package com.zyascend.NoBoring.utils.picture;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.dao.Photo;
import com.zyascend.NoBoring.dao.PhotoFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：扫描所有含图片的文件夹
 * 作者：zyascend on 2017/7/22 15:13
 * 邮箱：zyascend@qq.com
 */

public class PhotoUtils {

    public static Map<String, PhotoFolder> getPhotoFolder() {

        Map<String,PhotoFolder> res = new HashMap<>();

        //初始化 全部图片 项
        String allKey = "所有图片";
        PhotoFolder allFolder = new PhotoFolder();
        allFolder.setName(allKey);
        allFolder.setDirPath(allKey);
        allFolder.setPhotoList(new ArrayList<Photo>());

        res.put(allKey, allFolder);

        //构建查询索引，只查询jpg和png
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = BaseApplication.context.getContentResolver()
                .query(uri
                        ,null
                        ,MediaStore.Images.Media.MIME_TYPE + " in(?, ?)"
                        ,new String[] { "image/jpeg", "image/png" }
                        ,MediaStore.Images.Media.DATE_MODIFIED + " desc");
        int pathIndex = cursor
                .getColumnIndex(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()){
            do {
                // 获取图片路径
                String path = cursor.getString(pathIndex);
                // 获取图片父路径
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                String dirPath = parentFile.getAbsolutePath();

                if (res.containsKey(dirPath)) {
                    Photo photo = new Photo(path);
                    PhotoFolder photoFolder = res.get(dirPath);
                    photoFolder.getPhotoList().add(photo);
                    res.get(allKey).getPhotoList().add(photo);
                } else {
                    PhotoFolder photoFolder = new PhotoFolder();
                    List<Photo> photoList = new ArrayList<>();
                    Photo photo = new Photo(path);
                    photoList.add(photo);
                    photoFolder.setPhotoList(photoList);
                    photoFolder.setDirPath(dirPath);
                    photoFolder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                    res.put(dirPath, photoFolder);
                    res.get(allKey).getPhotoList().add(photo);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }
}

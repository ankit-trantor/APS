package es.esy.chhg.chatapp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class MediaUtil {
    private static final String FOLDER_MEDIA_ROOT = "chatApp";
    private static final String FOLDER_IMAGE = "image";
    private static final String FOLDER_AUDIO = "audio";
    private static final String FOLDER_VIDEO = "video";

    public static String getBaseName(String url) {
        String basename = null;
        if (url != null) {
            int index = url.lastIndexOf("/");
            if (index > -1) {
                basename = url.substring(index + 1);
            }
        }
        return basename;
    }

    public static File getMediaRootFolder(Context context, String path) {
        String folder = null;
        File file = new File(path);
        if (MimeTypeUtil.getMimeType(file.getAbsolutePath()).startsWith("image")) {
            folder = FOLDER_MEDIA_ROOT + "/" + FOLDER_IMAGE;
        } else if (MimeTypeUtil.getMimeType(file.getAbsolutePath()).startsWith("video")) {
            folder = FOLDER_MEDIA_ROOT + "/" + FOLDER_VIDEO;
        } else if (MimeTypeUtil.getMimeType(file.getAbsolutePath()).startsWith("audio")) {
            folder = FOLDER_MEDIA_ROOT + "/" + FOLDER_AUDIO;
        }
        return new File(context.getExternalFilesDir(null), folder + "/");
    }

    public static File getMediaOnDisk(Context context, String path) {
        //return path != null ? new File(getMediaRootFolder(context, path), getBaseName(path)) : null;
        String pathDisk = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path;
        return new File(pathDisk);
    }
}
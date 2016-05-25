package es.esy.chhg.chatapp.utils;

import android.webkit.MimeTypeMap;

public class MimeTypeUtil {
    public static String getMimeType(String url) {
        url = url.toLowerCase();
        String type = null;
        String extension = null;
        int index = url.lastIndexOf(".");
        if (index > 0) {
            extension = url.substring(index + 1);
        }
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
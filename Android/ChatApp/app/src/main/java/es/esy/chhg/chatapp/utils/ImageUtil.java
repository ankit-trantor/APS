package es.esy.chhg.chatapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.IOException;

public class ImageUtil {
    public static String getPath(Context context, Uri uri) {
        String path = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(column_index);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }

        return path;
    }

    /*
    - método criada para verificar se a imagem está em modo paisagem ou retrado
     */

    public static int getPhotoOrientation(Uri pathImage) {
        try {
            ExifInterface exifInterface = new ExifInterface(pathImage.getPath());

            // Lê a orientação que foi salva a foto
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            return orientation;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
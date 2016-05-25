package es.esy.chhg.chatapp.picasso;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

public class CropTopTransformation implements Transformation {
    private int mWidth;
    private int mHeight;

    public CropTopTransformation() {
    }

    public CropTopTransformation(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        mWidth = mWidth == 0 ? source.getWidth() : mWidth;
        mHeight = mHeight == 0 ? source.getHeight() : mHeight;

        float scaleX = (float) mWidth / source.getWidth();
        float scaleY = (float) mHeight / source.getHeight();
        float scale = Math.max(scaleX, scaleY);

        float scaledWidth = scale * source.getWidth();
        float scaledHeight = scale * source.getHeight();
        float left = (mWidth - scaledWidth) / 2;
        float top = 0; // TOP
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(source, null, targetRect, null);

        if (!source.isRecycled()) {
            source.recycle();
        }
        return bitmap;
    }

    @Override
    public String key() {
        return "CropTopTransformation(width=" + mWidth + ", height=" + mHeight + ", cropType= TOP"
                + ")";
    }
}

package es.esy.chhg.chatapp.picasso;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import es.esy.chhg.chatapp.R;

public class PicassoUtil {
    public static void picassoCropTopAndRadius(Context context, final ImageView imageView, final String path, int width, int height, int error) {
        Picasso.
                with(context).
                load(path).
                transform(new CropTopTransformation(width, height)).
                transform(new RoundedTransformation((int) context.getResources().getDimension(R.dimen.radius_image))).
                error(error).
                placeholder(error).
                into(imageView);
    }

    public static void picassoCropTopAndRadius(Picasso picasso, Context context, final ImageView imageView, final String path, int width, int height, int error) {
        picasso.
                load(path).
                transform(new CropTopTransformation(width, height)).
                transform(new RoundedTransformation((int) context.getResources().getDimension(R.dimen.radius_image))).
                error(error).
                placeholder(error).
                into(imageView);
    }

    public static void picassoCropTopAndRadiusResId(Context context, final ImageView imageView, int resId, int width, int height, int error) {
        Picasso.
                with(context).
                load(resId).
                transform(new RoundedTransformation((int) context.getResources().getDimension(R.dimen.radius_image))).
                resize(width, height).
                centerInside().
                error(error).
                placeholder(error).
                into(imageView);
    }
}

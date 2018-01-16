package classact.com.xprize.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.googlecode.leptonica.android.Scale;

/**
 * Created by hcdjeong on 2017/06/19.
 */

public class TextShrinker {
    public static void shrink(
            ImageView imageView,
            int width,
            float percentage,
            int image,
            Resources resources) {

        Drawable newDrawable = shrinkDrawable(image, width, percentage, resources);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // imageView.setImageResource(0);
        imageView.setImageDrawable(newDrawable);
    }

    public static ImageView shrink(
            ImageView imageView,
            int width,
            float percentage,
            Resources resources) {

        Drawable newDrawable = shrinkDrawable(imageView.getDrawable(), width, percentage, resources);
        // imageView.setImageResource(0);
        imageView.setImageDrawable(newDrawable);
        return imageView;
    }

    private static Drawable shrinkDrawable(
            int image,
            int width,
            float percentage,
            Resources resources) {

        Drawable initialDrawable = resources.getDrawable(image, null);
        Drawable newDrawable = null;

        if (initialDrawable != null) {
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            float density = displayMetrics.density;
            float containerWidth = density * width; // container width

            float drawableWidth = initialDrawable.getIntrinsicWidth(); // Inner drawable width
            float drawableHeight = initialDrawable.getIntrinsicHeight(); // Inner drawable height

            Log.d("TEST", "" + drawableWidth + ", " + drawableHeight);

            Bitmap bitmap = ((BitmapDrawable) initialDrawable).getBitmap();
            newDrawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(
                    bitmap,
                    (int) (drawableWidth * percentage),
                    (int) (drawableHeight * percentage),
                    true));

            Log.d("New drawable", "" + newDrawable.getIntrinsicWidth() + ", " + newDrawable.getIntrinsicHeight());

//            float drawableWidth = initialDrawable.getIntrinsicWidth(); // Inner drawable width
//            float drawableHeight = initialDrawable.getIntrinsicHeight(); // Inner drawable height
//            Log.d("TEST", "Drawable with: " + drawableWidth);
//            float containerDrawableWidthRatio = containerWidth / drawableWidth; // drawable to container ratio
//
//            if (containerDrawableWidthRatio < 1.f) {
//                Bitmap bitmap = ((BitmapDrawable) initialDrawable).getBitmap();
//                newDrawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(
//                        bitmap,
//                        (int) (containerDrawableWidthRatio * drawableWidth * percentage),
//                        (int) (containerDrawableWidthRatio * drawableHeight * percentage),
//                        true));
//                System.out.println("< 1.f");
//            } else {
//                float widthPercentageSize = drawableWidth / containerWidth;
//                if (widthPercentageSize > percentage) {
//                    Bitmap bitmap = ((BitmapDrawable) initialDrawable).getBitmap();
//                    float widthPercentageDiff = widthPercentageSize - percentage;
//                    float widthToSubtract = containerWidth * widthPercentageDiff;
//                    float multiplyRatio = (drawableWidth - widthToSubtract) / drawableWidth;
//                    newDrawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(
//                            bitmap,
//                            (int) (multiplyRatio * drawableWidth),
//                            (int) (multiplyRatio * drawableHeight),
//                            true));
//                } else {
//                    newDrawable = initialDrawable;
//                }
//                System.out.println("> 1.f");
//            }
        }
        return newDrawable;
    }

    private static Drawable shrinkDrawable(
            Drawable initialDrawable,
            int width,
            float percentage,
            Resources resources) {
        Drawable newDrawable = null;

        if (initialDrawable != null) {
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            float density = displayMetrics.density;
            float containerWidth = density * width; // container width

            float drawableWidth = initialDrawable.getIntrinsicWidth(); // Inner drawable width
            Log.d("TEST", "Drawable with: " + drawableWidth);
            float containerDrawableWidthRatio = containerWidth / drawableWidth; // drawable to container ratio

            if (containerDrawableWidthRatio < 1.f) {
                Rect rect = initialDrawable.getBounds();
                Bitmap bitmap = ((BitmapDrawable) initialDrawable).getBitmap();
                newDrawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(
                        bitmap,
                        (int) (containerDrawableWidthRatio * rect.right * percentage),
                        (int) (containerDrawableWidthRatio * rect.bottom * percentage),
                        true));
                System.out.println("< 1.f");
            } else {
                float widthPercentageSize = drawableWidth / containerWidth;
                if (widthPercentageSize > percentage) {
                    Rect rect = initialDrawable.getBounds();
                    Bitmap bitmap = ((BitmapDrawable) initialDrawable).getBitmap();
                    float widthPercentageDiff = widthPercentageSize - percentage;
                    float widthToSubtract = containerWidth * widthPercentageDiff;
                    float multiplyRatio = (rect.right - widthToSubtract) / rect.right;
                    newDrawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(
                            bitmap,
                            (int) (multiplyRatio * rect.right),
                            (int) (multiplyRatio * rect.bottom),
                            true));
                } else {
                    newDrawable = initialDrawable;
                }
                System.out.println("> 1.f");
            }
        }
        return newDrawable;
    }
}

package classact.com.xprize.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by hcdjeong on 2017/06/19.
 */

public class TextShrinker {
    public static ImageView shrink(
            ImageView imageView,
            int width,
            float percentage,
            Resources resources) {

        Drawable newDrawawble = shrinkDrawable(imageView.getDrawable(), width, percentage, resources);
        imageView.setImageResource(0);
        imageView.setImageDrawable(newDrawawble);
        return imageView;
    }

    public static ImageButton shrink(
            ImageButton imageButton,
            int width,
            float percentage,
            Resources resources) {

        Drawable newDrawawble = shrinkDrawable(imageButton.getDrawable(), width, percentage, resources);
        imageButton.setImageResource(0);
        imageButton.setImageDrawable(newDrawawble);
        return imageButton;
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

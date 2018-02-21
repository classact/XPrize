package classact.com.xprize.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

/**
 * Created by hcdjeong on 2017/10/27.
 * Utilities for constraint layout
 */

public class EZ {

    private final Context context;
    public final Image image = new Image();
    public final Guide guide = new Guide();
    public final Text text = new Text();

    @Inject
    public EZ(Context context) {
        this.context = context;
    }

    public void addView(ViewGroup parent, View... children) {
        for (View child : children) {
            parent.addView(child);
        }
    }

    public ImageButton controlButton() {
        ImageButton controlButton = new ImageButton(context);
        controlButton.setBackgroundColor(Color.TRANSPARENT);
        ViewGroup.MarginLayoutParams controlButtonLayoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
        );
        controlButton.setLayoutParams(controlButtonLayoutParams);
        controlButton.setScaleX(0.65f);
        controlButton.setScaleY(0.65f);
        return controlButton;
    }

    public FrameLayout frameFull() {
        FrameLayout frame = new FrameLayout(context);
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        frame.setLayoutParams(frameLayoutParams);
        return frame;
    }

    public void layoutWrapContent(View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
            );
            view.setLayoutParams(layoutParams);
        }
    }

    public FrameLayout frame() {
        FrameLayout frame = new FrameLayout(context);
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        frame.setLayoutParams(frameLayoutParams);
        return frame;
    }

    public void size(View view, int width, int height) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public void scale(View view, float percentageScale) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        float x = view.getX();
        float y = view.getY();

        float currentWidth = layoutParams.width;
        float currentHeight = layoutParams.height;

        float newWidth = currentWidth * percentageScale;
        float newHeight = currentHeight * percentageScale;

        // 5, 10; 5 - 10 = -5; x -= -5/2; 10, 5; 10 - 5 = 5; x += 5/2

        float xOffset = (currentWidth - newWidth) / 2;
        float yOffset = (currentHeight - newHeight) / 2;

        layoutParams.width = (int) newWidth;
        layoutParams.height = (int) newHeight;

        view.setLayoutParams(layoutParams);

        view.setX(x + xOffset);
        view.setY(y + yOffset);
    }

    public void sizeDpi(View view, int width, int height) {
        float dpi = context.getResources().getDisplayMetrics().density;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.width = (int) (dpi * width);
        layoutParams.height = (int) (dpi * height);
        view.setLayoutParams(layoutParams);
    }

    public int getWidth(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return layoutParams.width;
    }

    public int getHeight(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return layoutParams.height;
    }

    public void height(View view, int height) {
        float dpi = context.getResources().getDisplayMetrics().density;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.height = (int) (dpi * height);
        view.setLayoutParams(layoutParams);
    }

    public ImageView rect(int width, int height, int color) {
        ImageView rect = new ImageView(context);
        ViewGroup.MarginLayoutParams rectLayoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
        );
        rectLayoutParams.width = width;
        rectLayoutParams.height = height;
        rect.setLayoutParams(rectLayoutParams);
        rect.setBackgroundColor(color);
        return rect;
    }

    public void topMargin(int topMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.topMargin = topMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void leftMargin(int leftMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.leftMargin = leftMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void startMargin(int startMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.setMarginStart(startMargin);
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void rightMargin(int rightMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.rightMargin = rightMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void endMargin(int endMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.setMarginEnd(endMargin);
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void topLeftMargin(int topMargin, int leftMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.topMargin = topMargin;
            viewLayoutParams.leftMargin = leftMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void topStartMargin(int topMargin, int startMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.topMargin = topMargin;
            viewLayoutParams.setMarginStart(startMargin);
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void topRightMargin(int topMargin, int rightMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.topMargin = topMargin;
            viewLayoutParams.rightMargin = rightMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void topEndMargin(int topMargin, int endMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.topMargin = topMargin;
            viewLayoutParams.setMarginEnd(endMargin);
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void botMargin(int bottomMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.bottomMargin = bottomMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void botLeftMargin(int bottomMargin, int leftMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.bottomMargin = bottomMargin;
            viewLayoutParams.leftMargin = leftMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void botStartMargin(int bottomMargin, int startMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.bottomMargin = bottomMargin;
            viewLayoutParams.setMarginStart(startMargin);
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void botRightMargin(int bottomMargin, int rightMargin, int leftMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.bottomMargin = bottomMargin;
            viewLayoutParams.rightMargin = rightMargin;
            view.setLayoutParams(viewLayoutParams);
        }
    }

    public void botEndMargin(int bottomMargin, int endMargin, int leftMargin, View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams viewLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewLayoutParams.bottomMargin = bottomMargin;
            viewLayoutParams.setMarginEnd(endMargin);
            view.setLayoutParams(viewLayoutParams);
        }
    }

    /**
     * Function to highlight using argb
     * @param view View to highlight
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public void highlight(View view, int r, int g, int b) {
        view.setBackgroundColor(Color.argb(100, r, g, b));
    }

    public void highlight(int color, View... views) {
        for (View view : views) {
            view.setBackgroundColor(color);
        }
    }

    /**
     * Visibility function
     * @param views views to hide
     */
    public void hide(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public LiveObjectAnimator fadeShow(Lifecycle lifecycle, long millis, View view) {
        if (view.getAlpha() != 1f) {
            show(view);
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f);
            animator.setAutoCancel(true);
            return new LiveObjectAnimator(lifecycle, animator)
                    .setDuration((long) (millis * (1f - view.getAlpha())));
        }
        return null;
    }

    public LiveObjectAnimator fadeHide(Lifecycle lifecycle, long millis, View view) {
        if (view.getAlpha() != 0f) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f);
            animator.setAutoCancel(true);
            return new LiveObjectAnimator(lifecycle, animator)
                    .setDuration((long) (millis * view.getAlpha()));
        }
        return null;
    }

    public LiveObjectAnimator fadeShow(LiveObjectAnimator animator, Lifecycle lifecycle, long millis, View view) {
        if (view.getAlpha() != 1f) {
            show(view);
            if (animator != null) {
                animator.get().cancel();
            }
            animator = new LiveObjectAnimator(lifecycle, ObjectAnimator.ofFloat(view, "alpha", 1f))
                    .setDuration((long) (millis * (1f - view.getAlpha())))
                    .addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Log.d("TEST", "Fade Show ended");
                        }
                    })
                    .start();
        }
        return animator;
    }

    public LiveObjectAnimator fadeHide(LiveObjectAnimator animator, Lifecycle lifecycle, long millis, View view) {
        if (animator != null) {
            animator.get().cancel();
        }
        if (view.getAlpha() != 0f) {
            animator = new LiveObjectAnimator(lifecycle, ObjectAnimator.ofFloat(view, "alpha", 0f))
                    .setDuration((long) (millis * view.getAlpha()))
                    .addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            hide(view);
                            Log.d("TEST", "Fade Hide ended");
                        }
                    })
                    .start();
        }
        return animator;
    }

    /**
     * Visibility function
     * @param view to show
     */

    public void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void show(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void clickable(View... views) {
        for (View view : views) {
            view.setClickable(true);
            view.setFocusable(true);
            view.setEnabled(true);
        }
    }

    public void unclickable(View... views) {
        for (View view : views) {
            view.setEnabled(false);
            view.setFocusable(false);
            view.setClickable(false);
        }
    }

    public void gray(View... views) {
        Paint grayscalePaint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        grayscalePaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        for (View view : views) {
            view.setLayerType(View.LAYER_TYPE_HARDWARE, grayscalePaint);
        }
    }

    public void ungray(View... views) {
        for (View view : views) {
            view.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    public EZCircle circle(float cx, float cy, float radius) {
        return new EZCircle(context, cx, cy, radius);
    }

    public EZCircle circle(float cx, float cy, float radius, int color) {
        return new EZCircle(context, cx, cy, radius, color);
    }

    public EZRectangle rectangle(float left, float top, float right, float bottom) {
        return new EZRectangle(context, left, top, right, bottom);
    }

    public void doSomething(CBack x) {

    }

    public interface CBack {
        void doSomething();
    }

    public class Image {

        /**
         * Courtesy: jackgris @ https://stackoverflow.com/questions/23294489/android-convert-current-screen-to-bitmap
         * @param view to take snap shot of
         * @return view's bitmap snapshot
         */
        public Bitmap takeSnapShot(View view) {
            Bitmap snapShot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(snapShot);
            view.draw(canvas);
            return snapShot;
        }

        /* Courtesy
         * http://blog.bradcampbell.nz/greyscale-views-on-android/
         * http://chiuki.github.io/android-shaders-filters/#/14
         */
        public void setGrayscale(View view, boolean grayscale) {
            if (grayscale) {
                Paint grayscalePaint = new Paint();
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                grayscalePaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
                view.setLayerType(View.LAYER_TYPE_HARDWARE, grayscalePaint);
            } else {
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        }
    }

    /**
     * {@link Text} provides easy help functions for {@link TextView}s
     */
    public class Text {

        public void centerByColor(TextView textView, int color) {

            // Get color coords
            int[] coords = colorCoords(textView, color);

            // Extract coords
            int startX = coords[0];
            int endX = coords[1];
            int startY = coords[2];
            int endY = coords[3];
            int marginX = coords[4];
            int marginY = coords[5];

            // Center text view by coords
            if (marginY > startY) {
                textView.setY(textView.getY() + (marginY-startY));
            } else if (marginY < startY) {
                textView.setY(textView.getY() - (startY-marginY));
            }

            if (marginX > startX) {
                textView.setX(textView.getX() + (marginX-startX));
            } else if (marginX < startX) {
                textView.setX(textView.getX() - (startX-marginX));
            }

            Log.d("POS", "x: " + textView.getX() + ", y: " + textView.getY());
        }

        /**
         * Pixel pos returns the first and last y-coordinates of a colored in {@link TextView}
         * Note that {@link TextView} must have had drawing cache enabled
         *
         * @param textView to check pixel position of
         * @return int array of various x-y positions and margins
         */
        public int[] colorCoords(TextView textView, int color) {

            // Build drawing cache
            textView.buildDrawingCache();

            // Get bitmap from text view's drawing cache
            Bitmap b = textView.getDrawingCache();

            // Init positions to hold x-y positions and margins
            int[] colorCoords = new int[6];

            // Init variables used in calculation
            boolean firstXFound = false;
            int startX = 0, endX = 0;

            boolean firstYFound = false;
            int startY = 0, endY = 0;

            int bWidth = b.getWidth();
            int bHeight = b.getHeight();

            int iterations = 0;
            long startTime = SystemClock.uptimeMillis();

            int ix = 1;

            // Top-down
            for (int y = 0; y < bHeight; y++) {
                for (int x = (ix == 1) ? 0 : 1; x < bWidth; x += 2) {
                    iterations++;
                    if (b.getPixel(x, y) == color) {
                        if (y > 0 && ((x > 0 && b.getPixel(x-1, y-1) == color) || (b.getPixel(x, y-1) == color))) {
                            startY = y-1;
                        } else {
                            startY = y;
                        }
                        y = bHeight;
                        break;
                    }
                }
            }

            ix = 1;

            // Bottom-up
            for (int y = bHeight-1; y > startY; y--) {
                for (int x = (ix == 1) ? 0 : 1; x < bWidth; x += 2) {
                    iterations++;
                    if (b.getPixel(x, y) == color) {
                        if (y < bHeight-1 && ((x > 0 && b.getPixel(x-1, y+1) == color) || (b.getPixel(x, y+1) == color))) {
                            endY = y+1;
                        } else {
                            endY = y;
                        }
                        y = startY;
                        break;
                    }
                }
            }

            int iy = startY+1;

            // Left-right
            for (int x = 0; x < bWidth; x++) {
                for (int y = (iy == startY+1) ? startY : startY+1; y < endY+1; y += 2) {
                    iterations++;
                    if (b.getPixel(x, y) == color) {
                        if (x > 0 && ((y < endY && b.getPixel(x-1, y+1) == color) || (b.getPixel(x-1, y) == color))) {
                            startX = x-1;
                        } else {
                            startX = x;
                        }
                        x = bWidth;
                        break;
                    }
                }
            }

            iy = startY+1;

            // Right-left
            for (int x = bWidth-1; x > startX; x--) {
                for (int y = (iy == startY+1) ? startY : startY+1; y < endY+1; y += 2) {
                    iterations++;
                    if (b.getPixel(x, y) == color) {
                        if (x < bWidth-1 && ((y < endY && b.getPixel(x+1, y+1) == color) || (b.getPixel(x+1, y) == color))) {
                            endX = x+1;
                        } else {
                            endX = x;
                        }
                        x = startX;
                        break;
                    }
                }
            }

//            // Loop throw pixels of bitmap
//            for (int y = 0; y < bHeight; y++) {
//                for (int x = 0; x < bWidth; x++) {
//                    iterations++;
//                    int pixel = b.getPixel(x, y);
//                    if (pixel == color) {
//                        if (firstXFound) {
//                            if (x < startX) {
//                                startX = x;
//                            } else if (x > endX) {
//                                endX = x;
//                            }
//                        } else {
//                            startX = x;
//                            endX = x;
//                            firstXFound = true;
//                        }
//                        if (firstYFound) {
//                            endY = y;
//                        } else {
//                            startY = y;
//                            endY = y;
//                            firstYFound = true;
//                        }
//                    }
//                }
//            }

            Log.d("TEST", "" +
                    iterations + " iterations, " +
                    (SystemClock.uptimeMillis() - startTime) + " millis");

            // Calculate x margin
            int whitespaceX = startX + (bWidth - endX);
            int marginX = whitespaceX/2;

            // Calculate y margin
            int whitespaceY = startY + (bHeight - endY);
            int marginY = whitespaceY/2;

            // Store positions and margins
            colorCoords[0] = startX;
            colorCoords[1] = endX;
            colorCoords[2] = startY;
            colorCoords[3] = endY;
            colorCoords[4] = marginX;
            colorCoords[5] = marginY;

            return colorCoords;
        }
    }

    /**
     * {@link Guide} provides easy help functions for {@link ConstraintLayout} {@link Guideline}
     */
    public class Guide {

        public Guideline create(boolean horizontal, float percentage) {
            Guideline guideline = new Guideline(context);
            ConstraintLayout.LayoutParams guidelineLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            guidelineLayoutParams.guidePercent = percentage;
            guidelineLayoutParams.orientation = (horizontal) ?
                    ConstraintLayout.LayoutParams.HORIZONTAL :
                    ConstraintLayout.LayoutParams.VERTICAL;
            guideline.setLayoutParams(guidelineLayoutParams);
            guideline.setId(View.generateViewId());
            return guideline;
        }

        public void setPercentage(Guideline guideline, float percentage) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            layoutParams.guidePercent = percentage;
            guideline.setLayoutParams(layoutParams);
        }

        public void setBegin(Guideline guideline, int begin) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            layoutParams.guideBegin = begin;
            guideline.setLayoutParams(layoutParams);
        }

        public void setEnd(Guideline guideline, int end) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            layoutParams.guideEnd = end;
            guideline.setLayoutParams(layoutParams);
        }

        public float getPercentage(Guideline guideline) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            return layoutParams.guidePercent;
        }

        public void center(Guideline horizontal, Guideline vertical, View view) {
            ConstraintLayout.LayoutParams horizontalLayoutParams = (ConstraintLayout.LayoutParams) horizontal.getLayoutParams();
            int horizontalOrientation = horizontalLayoutParams.orientation;
            ConstraintLayout.LayoutParams verticalLayoutParams = (ConstraintLayout.LayoutParams) vertical.getLayoutParams();
            int verticalOrientation = verticalLayoutParams.orientation;

            if (!(horizontalOrientation == ConstraintLayout.LayoutParams.HORIZONTAL ||
                    verticalOrientation == ConstraintLayout.LayoutParams.VERTICAL)) {
                Log.e("ez.guide.center(gh, gv, view)", "Invalid orientation");
                return;
            }

            ConstraintLayout.LayoutParams viewLayoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            viewLayoutParams.topToTop = horizontal.getId();
            viewLayoutParams.leftToLeft = vertical.getId();
            viewLayoutParams.rightToRight = vertical.getId();
            viewLayoutParams.bottomToBottom = horizontal.getId();
            view.setLayoutParams(viewLayoutParams);
        }

        public void center(Guideline guideline, View view) {

            ConstraintLayout.LayoutParams guidelineLayoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            int orientation = guidelineLayoutParams.orientation;

            ConstraintLayout.LayoutParams viewLayoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            if (orientation == ConstraintLayout.LayoutParams.HORIZONTAL) {
                viewLayoutParams.topToTop = guideline.getId();
                viewLayoutParams.bottomToBottom = guideline.getId();
            } else if (orientation == ConstraintLayout.LayoutParams.VERTICAL) {
                viewLayoutParams.leftToLeft = guideline.getId();
                viewLayoutParams.rightToRight = guideline.getId();
            } else {
                Log.w("ez.guide.center(g, view)", "Invalid orientation");
            }
            view.setLayoutParams(viewLayoutParams);
        }

        /**
         * <h4>Get Percentage Pos</h4>
         * <p>Calculates and returns value of either X or Y co-ordinate of a guideline based on its percentage</p>
         * @param guideline Input guideline
         * @return X or Y coordinate
         */
        public float getPos(Guideline guideline) {

            // Get guideline percentage
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            float percentage = layoutParams.guidePercent;

            // Get screen dimensions
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            float screenHeight = (float) dm.heightPixels - 48f; // Minus 48 as there's a 48px discrepancy for some reason...
            float screenWidth = (float) dm.widthPixels;

            // Calculate position based on guideline orientation
            float pos = 0f;
            switch (layoutParams.orientation) {
                case ConstraintLayout.LayoutParams.HORIZONTAL:
                    pos = percentage * screenHeight;
                    break;
                case ConstraintLayout.LayoutParams.VERTICAL:
                    pos = percentage * screenWidth;
                    break;
                default:
                    break;
            }
            return pos;
        }

        public int getBegin(Guideline guideline) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            return layoutParams.guideBegin;
        }

        public int getEnd(Guideline guideline) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
            return layoutParams.guideEnd;
        }
    }

    /**
     * {@link EZCircle} is used to generate a 'circle' to add to a view
     * Useful for testing position of a point in a view
     */
    private class EZCircle extends View {
        Paint paint = new Paint();
        float cx, cy, radius;
        int color;

        public EZCircle(Context context, float cx, float cy, float radius) {
            super(context);
            this.cx = cx;
            this.cy = cy;
            this.radius = radius;
            this.color = Color.RED;
        }

        public EZCircle(Context context, float cx, float cy, float radius, int color) {
            super(context);
            this.cx = cx;
            this.cy = cy;
            this.radius = radius;
            this.color = color;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(color);
            paint.setStrokeWidth(0);
            canvas.drawCircle(cx, cy, radius, paint);
        }
    }

    /**
     * {@link EZRectangle} is used to generate a 'rectangle' to add to a view
     * Useful for testing position of a point in a view
     */
    private class EZRectangle extends View {
        Paint paint = new Paint();
        float left, top, right, bottom;

        public EZRectangle(Context context, float left, float top, float right, float bottom) {
            super(context);
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(0);
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}

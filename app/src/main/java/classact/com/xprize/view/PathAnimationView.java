package classact.com.xprize.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.ArrayList;

import classact.com.xprize.R;

/**
 * Created by Tseliso on 8/27/2016.
 */
public class PathAnimationView extends View {
    private Context context;
    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Handler handler;
    private int currentPath;
    private int currentItem;
    private ArrayList<ArrayList<PathCoordinate>> paths;
    private ArrayList<Path> donePaths;
    private ArrayList<PathCoordinate> letterCoordinates;
    Bitmap marker;
    Bitmap letter;

    public interface AnimationDone{
        public void onAnimationDone();
    }

    public PathAnimationView(Context context) {
        super(context);
        this.context = context;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(40);
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1},
                0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        marker = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.drawing_hand);
        donePaths = new ArrayList<Path>();
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }


    public void animateThisPath(){
        try {
            currentItem = 0;
            currentPath = 0;
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(drawPath, 1);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setPaths(ArrayList<ArrayList<PathCoordinate>> paths){//,ArrayList<PathCoordinate> letterCoordinates){
        this.paths = paths;
        //this.letterCoordinates = letterCoordinates;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(0xFFAAAAAA);
        mPaint.setColor(0xFFFFFFFF);
        //drawLetter(canvas);
        mPaint.setColor(0xFFFF0000);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        canvas.drawPath(mPath, mPaint);
    }

    private void drawLetter(Canvas canvas){
        for(PathCoordinate c: letterCoordinates){
            canvas.drawCircle(c.getX(),c.getX(),2f,mPaint);
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        //mPath.reset();
        mPath =  new Path();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        for(Path path: donePaths)
            mCanvas.drawPath(path,mPaint);
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
        for(Path path: donePaths)
            mCanvas.drawPath(path,mPaint);
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        donePaths.add(mPath);
        //mPath.reset();
    }

    public Runnable drawPath =  new Runnable() {
        @Override
        public void run() {
            float x = paths.get(currentPath).get(currentItem).getX();
            float y = paths.get(currentPath).get(currentItem).getY();
            if (currentItem == 0) {
                touch_start(x, y);
                //invalidate();
                currentItem ++;
                handler.postDelayed(drawPath,150);
            }
            else if (currentItem < paths.get(currentPath).size() - 1){
                if (currentItem < paths.get(currentPath).size() - 3  && currentItem > 0) {
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mCanvas.drawBitmap(marker, x, y, mPaint);
                }
                touch_move(x, y);
                invalidate();
                currentItem++;
                handler.postDelayed(drawPath,150);
            }
            else{
                currentItem = 0;
                currentPath++;
                touch_up();
                //invalidate();
                if (currentPath < paths.size())
                    handler.postDelayed(drawPath,150);
                else{
                    ((AnimationDone)context).onAnimationDone();
                }
            }
        }
    };
}
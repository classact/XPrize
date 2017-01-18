package classact.com.xprize.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by Tseliso on 8/31/2016.
 */
public class ShowObjectNameView extends View{
    private Context context;
    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint       mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private boolean drew = false;
    private String mText;

    public ShowObjectNameView(Context context){
        super(context);
        this.context = context;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFFFF0000);

        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }

    public void setText(String text){
        mText = text;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFAAAAAA);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        Typeface type = Typeface.defaultFromStyle(Typeface.BOLD);
        mPaint.setTypeface(type);
        mPaint.setTextSize(250f);
        mPaint.setColor(0xFF000000);
        canvas.drawText(mText,900,1400,mPaint);
        mPaint.setColor(0xFFFF0000);
        canvas.drawText(mText.substring(0,1),900,1400,mPaint);


    }
}

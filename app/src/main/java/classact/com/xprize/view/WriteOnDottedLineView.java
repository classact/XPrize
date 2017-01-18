package classact.com.xprize.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by Tseliso on 9/19/2016.
 */
public class WriteOnDottedLineView extends View{
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WriteOnDottedLineView(Context context) {
        super(context);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFAAAAAA);

        Typeface type = Typeface.defaultFromStyle(Typeface.BOLD);
        mPaint.setTypeface(type);
        mPaint.setTextSize(250f);
    }
}

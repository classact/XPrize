package classact.com.xprize.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import java.util.ArrayList;

import classact.com.xprize.R;

/**
 * Created by Tseliso on 9/18/2016.
 */
public class SegmetedWritingView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ArrayList<String> word;
    private int highlight;
    private int background;
    private int bitmapResourceId;


    public SegmetedWritingView(Context context,int background) {
        super(context);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        //mPaint.setStyle(Paint.Style.STROKE);
       // mPaint.setStrokeJoin(Paint.Join.ROUND);
        //mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        highlight = -1;
        this.background = background;
    }

    public void  setWord (ArrayList word,int resourceId){
        this.word = word;
        this.bitmapResourceId = resourceId;
    }

    public void setHighlight(int highlight){
        this.highlight = highlight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFAAAAAA);
        Bitmap bitmap= BitmapFactory.decodeResource(this.getContext().getResources(),
                background);
        canvas.drawBitmap(bitmap,0,0,mPaint);
        Typeface type = Typeface.defaultFromStyle(Typeface.BOLD);
        mPaint.setTypeface(type);
        mPaint.setTextSize(250f);
        int i = 0;
        int x = 0;
        for(String letter: word) {
            if (i == highlight)
                mPaint.setColor(0xFFFF0000);
            else
                mPaint.setColor(0xFF000000);
            canvas.drawText(letter, x,300, mPaint);
            if (letter.equalsIgnoreCase("m") || letter.equalsIgnoreCase("w"))
                x += 80;
            if (letter.equalsIgnoreCase("l") || letter.equalsIgnoreCase("i"))
                x -= 50;
            x += 150;
            mPaint.setColor(0xFF000000);
            if (i < word.size() - 1) {
                canvas.drawText("-", x, 300, mPaint);
                x += 150;
            }
            i++;
        }
        if (highlight == word.size() - 1){
            Bitmap object = BitmapFactory.decodeResource(this.getContext().getResources(),
                    bitmapResourceId);
            canvas.drawBitmap(bitmap,x+250,20,mPaint);
        }

    }
}

package classact.com.xprize.activity.drill.math;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.Square;
import classact.com.xprize.utils.SquarePacker;
import classact.com.xprize.utils.UnionFind;

public class MathsDrillTwoActivity extends AppCompatActivity {
    private JSONObject allData;
    private MediaPlayer mp;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private JSONArray numbers;
    private RelativeLayout rootLayout;
    private RelativeLayout objectsContainer;
    private int[] positions;
    private Handler handler;
    private boolean touchEnabled;
    private boolean endDrill;

    private final int PICTURES_FRAME_WIDTH = 745;
    private final int PICTURES_FRAME_HEIGHT = 955;
    private final int NUMBERS_FRAME_WIDTH = 745;
    private final int NUMBERS_FRAME_HEIGHT = 955;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_two);
        rootLayout = (RelativeLayout) findViewById(R.id.activity_math_drill_two);
        objectsContainer = (RelativeLayout) findViewById(R.id.objects_container);

        numberOne = (ImageView) findViewById(R.id.cakedemo_obect);
        numberTwo = (ImageView) findViewById(R.id.numeral_2);
        numberThree = (ImageView) findViewById(R.id.numeral_3);

        numberOne.setImageResource(0);
        numberTwo.setImageResource(0);
        numberThree.setImageResource(0);

        for (int i = 0; i < objectsContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) objectsContainer.getChildAt(i);
            iv.setImageResource(0);
        }

        // Init data blah blah
        handler = new Handler();
        touchEnabled = false;
        endDrill = false;
        initialiseData();

        setupObjects();
        setupNumbers();
    }

    private void setupObjects(){
        try{
            if (allData != null) {

                // Relative layout for 'images'
                RelativeLayout rli = new RelativeLayout(getApplicationContext());
                // rli.setBackgroundColor(Color.argb(150, 0, 255, 255));
                rootLayout.addView(rli);
                RelativeLayout.LayoutParams rliParams = (RelativeLayout.LayoutParams) rli.getLayoutParams();
                rliParams.topMargin = 330; // 310 min
                rliParams.leftMargin = 255; // 230 min
                rliParams.width = PICTURES_FRAME_WIDTH; // 795 max
                rliParams.height = PICTURES_FRAME_HEIGHT; // 995 max
                rli.setLayoutParams(rliParams);

                int n = allData.getInt("number_of_objects");
                int w = NUMBERS_FRAME_WIDTH;
                int h = NUMBERS_FRAME_HEIGHT;
                int imageId = FetchResource.imageId(getApplicationContext(), allData.getString("object"));

                SquarePacker squarePacker = new SquarePacker(w, h);
                Square[] squares = squarePacker.get(n);

                for (int i = 0; i < squares.length; i++) {
                    // Get square
                    Square square = squares[i];
                    // Get drawable
                    Drawable d = getResources().getDrawable(imageId, null);
                    // Create image view
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageDrawable(d);
                    iv.setScaleX(0.8f);
                    iv.setScaleY(0.8f);
                    // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
                    // Add image view to numbers layout
                    rli.addView(iv);
                    // Edit image view layout params
                    RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    ivParams.leftMargin = 0;
                    ivParams.topMargin = 0;
                    ivParams.width = square.w;
                    ivParams.height = square.w;
                    iv.setLayoutParams(ivParams);
                    // Set coordinates
                    iv.setX((float) square.x);
                    iv.setY((float) square.y);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setupNumbers() {
        try {
            if (numbers != null) {

                // Relative layout for 'numbers'
                RelativeLayout rln = new RelativeLayout(getApplicationContext());
                // rln.setBackgroundColor(Color.argb(150, 255, 0, 0));
                rootLayout.addView(rln);
                RelativeLayout.LayoutParams rlnParams = (RelativeLayout.LayoutParams) rln.getLayoutParams();
                rlnParams.topMargin = 330; // 310 min, 370 max
                rlnParams.leftMargin = 1465; // 1440 min, 1500 max
                rlnParams.width = NUMBERS_FRAME_WIDTH; // 795 max, 675 min
                rlnParams.height = NUMBERS_FRAME_HEIGHT; // 995 max, 875 min
                rln.setLayoutParams(rlnParams);

                int n = numbers.length();
                int w = NUMBERS_FRAME_WIDTH;
                int h = NUMBERS_FRAME_HEIGHT;

                SquarePacker squarePacker = new SquarePacker(w, h);
                Square[] squares = squarePacker.get(n);
                int[] scrambles = FisherYates.shuffle(n);

                for (int i = 0; i < squares.length; i++) {
                    int si = scrambles[i];
                    // Get square
                    Square square = squares[si];
                    // Get drawable
                    Drawable d = getResources()
                            .getDrawable(getImageIdFromJSONArray(numbers, i, "image"), null);
                    // Create image view
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageDrawable(d);
                    iv.setScaleX(0.8f);
                    iv.setScaleY(0.8f);
                    // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
                    // Add image view to numbers layout
                    rln.addView(iv);
                    // Edit image view layout params
                    RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    ivParams.leftMargin = 0;
                    ivParams.topMargin = 0;
                    ivParams.width = square.w;
                    ivParams.height = square.w;
                    iv.setLayoutParams(ivParams);
                    // Set coordinates
                    iv.setX((float) square.x);
                    iv.setY((float) square.y);

                    // Setup listener
                    final int numberIndex = i;

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            numberClicked(numberIndex);
                        }
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void factorial(int n) {
        System.out.println("FACTORIAL!!!!!");
        int log2n = (int) Math.floor((Math.log10(n))/(Math.log10(2)));
        System.out.println("log2n: " + log2n);
        for (int i = log2n; i > 0; i--) {
            int h = n >> i;
            System.out.println(n + " >> " + i + " = " + h);
        }
    }

    private void playSound(String sound, final Runnable action) {
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (action != null) {
                        action.run();
                    }
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            if (action != null) {
                action.run();
            }
        }
    }

    private int getImageIdFromJSONArray(JSONArray jsonArray, int pos, String name) {
        int imageId = 0;
        try {
            imageId = FetchResource.imageId(getApplicationContext(), jsonArray.getJSONObject(pos).getString(name));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imageId;
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            numbers = allData.getJSONArray("numerals");
            String sound = allData.getString("monkey_has");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumberOfObjects();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void sayNumberOfObjects(){
        try{
            String sound = allData.getString("number_of_objects_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayCanYouTouch();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void numberClicked(int position){
        if (touchEnabled) {
            try {
                int correct = numbers.getJSONObject(position).getInt("right");
                String sound = numbers.getJSONObject(position).getString("sound");
                if (correct == 0) {
                    playSound(sound, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int affirmationSound = ResourceSelector.getNegativeAffirmationSound(getApplicationContext());
                                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + affirmationSound);
                                if (mp == null) {
                                    mp = new MediaPlayer();
                                }
                                mp.reset();
                                mp.setDataSource(getApplicationContext(), myUri);
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                    }
                                });
                                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mp.reset();
                                    }
                                });
                                mp.prepare();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                } else {
                    touchEnabled = false;
                    playSound(sound, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int affirmationSound = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
                                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + affirmationSound);
                                if (mp == null) {
                                    mp = new MediaPlayer();
                                }
                                mp.reset();
                                mp.setDataSource(getApplicationContext(), myUri);
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                    }
                                });
                                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mp.release();
                                        finish();
                                    }
                                });
                                mp.prepare();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                finish();
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    }

    public void sayCanYouTouch(){
        try{
            String sound = allData.getString("can_you_find_and_touch");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumber();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void sayNumber(){
        try{
            String sound = allData.getString("numeral_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            touchEnabled = true;
                        }
                    }, 500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }

    private class Coord {
        public double x;
        public double y;

        private Coord(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getX() {
            return x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getY() {
            return y;
        }
    }

    private class Circle {
        private double radius;
        private Coord position;

        private Circle(double radius) {
            this.radius = radius;
        }

        private void setPosition(Coord position) {
            this.position = position;
        }

        public Coord getPosition() {
            return position;
        }

        public double getRadius() {
            return radius;
        }
    }

    private class CirclePacker {

        private double maxRadius;
        private double width;
        private double height;
        private Coord center;
        private List<Circle> circles;

        private CirclePacker(double width, double height) {
            this.width = width;
            this.height = height;
            center = new Coord(width/2, height/2);
            circles = new ArrayList<>();
        }

        public List<Circle> getCircles() {
            return circles;
        }

        public void add(Circle circle) {
            circles.add(circle);
            if (circles.size() == 1) {
                double radiusOfNewCircle = circle.getRadius();
                circle.setPosition(new Coord(radiusOfNewCircle, radiusOfNewCircle));
                maxRadius = circle.getRadius();
            } else {
                Circle lastCircle = circles.get(circles.size() - 2);
                double radiusOfNewCircle = circle.getRadius();
                double x = lastCircle.getPosition().x + lastCircle.getRadius() + radiusOfNewCircle;
                double y = lastCircle.getPosition().y;

                if (validX(radiusOfNewCircle, x)) {
                    circle.setPosition(new Coord(x, y));
                    if (radiusOfNewCircle > maxRadius) {
                        maxRadius = radiusOfNewCircle;
                    }
                } else {
                    x = radiusOfNewCircle;
                    y += maxRadius + radiusOfNewCircle;
                    circle.setPosition(new Coord(x, y));
                    maxRadius = radiusOfNewCircle;
                }
            }
        }

        public Coord calcCenter(List<Circle> circles) {
            int n = circles.size();
            int nConn = n * (n - 1)/2;

            int cConn = 0;
            double cumX = 0.0;
            double cumY = 0.0;

            UnionFind uf = new UnionFind(n);
            for (int i = 0; i < n-1; i++) {
                for (int j = i+1; j < n; j++) {
                    if (!uf.connected(i, j)) {
                        uf.union(i, j);
                        Coord midPoint = (midpoint(circles.get(i).getPosition(),
                                circles.get(j).getPosition()));
                        cumX += midPoint.x;
                        cumY += midPoint.y;
                        cConn++;
                    }
                }
            }
            // Reposition all objects
            double newX = cumX/cConn;
            double newY = cumY/cConn;

            System.out.println("nConn: " + nConn + ", cConn: " + cConn);
            return new Coord(newX, newY);
        }

        public float calcMultiplier(int n) {
            return (float) (5.1 * Math.pow((Math.E), -(n/(Math.pow(Math.E, Math.E)))));
        }

        public void repositionAll(Coord offset, List<Circle> circles) {
            for (Circle c : circles) {
                Coord cCoord = c.getPosition();
                c.setPosition(new Coord(cCoord.x - offset.x, cCoord.y - offset.y));
            }
        }

        private double calcOffset(double r, double s) {
            return Math.sqrt((r * r) - (s * s));
        }

        private Coord midpoint(Coord a, Coord b) {
            return new Coord((a.x + b.x)/2, (a.y + b.y)/2);
        }

        private void addCircle(Circle c) {
        }

        private boolean validX(double radius, double x) {
            return (x + radius) < width;
        }

        private boolean validY(double radius, double y) {
            return (y + radius) < height;
        }

        private double distance(double r1, double r2) {
            return 0.0;
        }
    }
}

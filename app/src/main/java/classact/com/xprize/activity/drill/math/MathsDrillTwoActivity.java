package classact.com.xprize.activity.drill.math;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_two);
        rootLayout = (RelativeLayout) findViewById(R.id.activity_math_drill_two);
        objectsContainer = (RelativeLayout)findViewById(R.id.objects_container);

        // Relative layout for 'images'
        RelativeLayout rli = new RelativeLayout(getApplicationContext());
        rli.setBackgroundColor(Color.argb(150, 0, 255, 255));
        RelativeLayout.LayoutParams rliParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rliParams.topMargin = 370; // 310 min
        rliParams.leftMargin = 287; // 230 min
        rliParams.width = 675; // 795 max
        rliParams.height = 875; // 995 max
        rli.setLayoutParams(rliParams);

        // Relative layout for 'numbers'
        RelativeLayout rln = new RelativeLayout(getApplicationContext());
        rln.setBackgroundColor(Color.argb(150, 255, 0, 0));
        RelativeLayout.LayoutParams rlnParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rlnParams.topMargin = 370; // 310 min
        rlnParams.leftMargin = 1500; // 1440 min
        rlnParams.width = 675; // 795 max
        rlnParams.height = 875; // 995 max
        rln.setLayoutParams(rlnParams);

        rootLayout.addView(rli);
        rootLayout.addView(rln);

        numberOne = (ImageView)findViewById(R.id.cakedemo_obect);
        numberTwo = (ImageView)findViewById(R.id.numeral_2);
        numberThree = (ImageView)findViewById(R.id.numeral_3);

        numberOne.setImageResource(0);
        numberTwo.setImageResource(0);
        numberThree.setImageResource(0);

        // Init data blah blah
        handler = new Handler();
        touchEnabled = false;
        endDrill = false;
        initialiseData();

        // Packaging logic
        try {
            if (numbers != null) {
                List<float[]> imageDims = new ArrayList<>();
                int numberCount = numbers.length();
                int area = 675 * 875;
                double maxArea = area/numberCount;
                CirclePacker circlePacker = new CirclePacker(675, 875);

                float screenDensity = getResources().getDisplayMetrics().density;

                for (int i = 0; i < numberCount; i++) {
                    Drawable d = getResources()
                            .getDrawable(getImageIdFromJSONArray(numbers, i, "image"), null);
                    int radius = Math.max(d.getIntrinsicWidth(), d.getIntrinsicHeight())/2;
                    float scaledRadius = (float) radius/screenDensity;
                    double circleArea = Math.PI * scaledRadius * scaledRadius;
                    float scale = (float) (maxArea/circleArea);
                    if (scale > 0.5f) {
                        scale = 0.5f;
                    }
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageDrawable(d);
                    iv.setBackgroundColor(Color.argb(100, 0, 0, 255));
                    iv.setScaleX(scale);
                    iv.setScaleY(scale);
                    float[] dims = new float[2];
                    dims[0] = d.getIntrinsicWidth()/screenDensity * scale;
                    dims[1] = d.getIntrinsicHeight()/screenDensity * scale;
                    imageDims.add(dims);
                    rln.addView(iv);

                    circlePacker.add(new Circle((double) scaledRadius * scale));
                }
                List<Circle> circles = circlePacker.getCircles();
                for (int i = 0; i < rln.getChildCount(); i++) {
                    if (i < circles.size()) {
                        ImageView iv = (ImageView) rln.getChildAt(i);
                        Circle c = circles.get(i);
                        Coord cCoords = c.getPosition();
                        float[] dims = imageDims.get(i);
                        int dWidth = (int) dims[0];
                        int dHeight = (int) dims[1];
                        iv.setX((float) cCoords.x - dWidth/2);
                        iv.setY((float) cCoords.y - dHeight/2);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*
        numberOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(1);
            }
        });
        numberTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(2);
            }
        });
        numberThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(3);
            }
        });*/
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
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            // setupNumbers();
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

    private void setupObjects(){
        try{
            int number = allData.getInt("number_of_objects");
            int resId = FetchResource.imageId(getApplicationContext(), allData.getString("object"));
            for (int i = 1; i <= 20; i++){
                ImageView object = (ImageView)objectsContainer.getChildAt(i-1);
                if (i <= number){
                    object.setVisibility(View.VISIBLE);
                    object.setImageResource(resId);
                }
                else{
                    object.setVisibility(View.INVISIBLE);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setupNumbers(){
        try {
            positions = new int[3];
            Arrays.fill(positions, -1);
            Random rand = new Random();
            for (int i = 0; i < 3; i++) {
                int pos = rand.nextInt(3);
                if (positions[pos] == -1) {
                    positions[pos] = i;
                } else {
                    boolean done = false;
                    for (int j = 2; j > -1; j--) {
                        if (positions[j] == -1 && !done) {
                            positions[j] = i;
                            done = true;
                            pos = j;
                        }
                    }
                }
                switch (pos) {
                    case 0:
                        numberOne.setImageResource(getImageIdFromJSONArray(numbers, i, "image"));
                        break;
                    case 1:
                        numberTwo.setImageResource(getImageIdFromJSONArray(numbers, i, "image"));
                        break;
                    case 2:
                        numberThree.setImageResource(getImageIdFromJSONArray(numbers, i, "image"));
                        break;
                }
            }
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
                int correct = numbers.getJSONObject(positions[position - 1]).getInt("right");
                Uri myUri;
                int sound;
                if (correct == 0) {
                    sound = ResourceSelector.getNegativeAffirmationSound(getApplicationContext());
                    myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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
                } else {
                    touchEnabled = false;
                    sound = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
                    myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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
                System.out.println(circles.size());
                Circle lastCircle = circles.get(circles.size() - 2);
                double radiusOfNewCircle = circle.getRadius();
                System.out.println(lastCircle.getPosition());
                double x = lastCircle.getPosition().x + lastCircle.getRadius() + radiusOfNewCircle;
                double y = lastCircle.getPosition().y;

                if (validX(radiusOfNewCircle, x)) {
                    circle.setPosition(new Coord(x, lastCircle.getPosition().y));
                    if (radiusOfNewCircle > maxRadius) {
                        maxRadius = radiusOfNewCircle;
                    }
                } else {
                    x = radiusOfNewCircle;
                    y += maxRadius + radiusOfNewCircle;
                    if (validY(radiusOfNewCircle, y)) {
                        circle.setPosition(new Coord(x, y));
                        maxRadius = radiusOfNewCircle;
                    } else {
                        System.out.println("FML....");
                    }
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

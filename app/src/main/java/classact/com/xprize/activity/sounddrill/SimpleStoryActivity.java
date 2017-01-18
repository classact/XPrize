package classact.com.xprize.activity.sounddrill;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import classact.com.xprize.R;
import classact.com.xprize.utils.ImageHelper;
import classact.com.xprize.utils.ResourceSelector;

public class SimpleStoryActivity extends AppCompatActivity {
    private JSONArray sentences;
    private MediaPlayer mp;
    private Handler handler;
    private int currentSentence;
    ArrayList<Integer> sounds;
    ArrayList<ImageView> sentenceViews;
    private int currentSound;
    private JSONObject allData;
    private LinearLayout container;
    private ImageView nextButton;
    private int currentQuestion;
    private JSONArray questions;
    private ImageView singleImage;
    private ImageView trippleImageOne;
    private ImageView trippleImageTwo;
    private ImageView tripleImageThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_story);
        container = (LinearLayout)findViewById(R.id.container_simple_story);
        singleImage = (ImageView)findViewById(R.id.single_image);
        trippleImageOne = (ImageView)findViewById (R.id.tripple_image_1);
        trippleImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(1);
            }
        });
        trippleImageTwo = (ImageView)findViewById(R.id.tripple_image_2);
        trippleImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(2);
            }
        });
        tripleImageThree = (ImageView)findViewById(R.id.tripple_image_3);
        tripleImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(3);
            }
        });
        nextButton = (ImageView)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSentence();
            }
        });
        //storyLink = (ImageView)findViewById(R.id.story_link);
        getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.storylink);
        handler = new Handler();
        currentSentence = 1;
        initialiseData();
    }


    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            sentences = allData.getJSONArray("sentences");
            //ImageView link = new ImageView(this);
            //link.setImageResource(allData.getInt("story_image"));
            int sound = allData.getInt("story_link_sound");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    currentSound = 0;
                    populateAndShowSentence();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private LinearLayout getLine(){
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        return line;
    }

    private void populateSentence(){
        try{
            JSONArray sentence = sentences.getJSONArray(currentSentence - 1);
            container.removeAllViews();
            container.setVisibility(View.VISIBLE);
            LinearLayout line = getLine();
            ImageView item;
            sounds = new ArrayList<>();
            sentenceViews = new ArrayList<>();
            int width = 0;
            for (int i = 0; i < sentence.length();i++) {
                JSONObject word = sentence.getJSONObject(i);
                item = new ImageView(this);
                item.setTag(word.getInt("sound"));
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playSound(view);
                    }
                });
                if (i > 0)
                    item.setPadding(50,10,0,0);
                item.setMaxHeight(143);
                item.setImageResource(word.getInt("black_word"));
                width += ImageHelper.getLength(word.getInt("black_word"),this);
                if (width > 1150){
                    container.addView(line);
                    line = getLine();
                    width = 0;
                }
                line.addView(item);
                sentenceViews.add(item);
                sounds.add(new Integer(word.getInt("sound")));
            }
            container.addView(line);
            currentSound = 0;
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void populateAndShowSentence(){
        try{
            getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);
            populateSentence();
            currentSound = 0;
            sayListenFirst();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void playSound(View v){
        try{
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + v.getTag());
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayListenFirst() {
        try{
            int sound = allData.getInt("listen_first_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    saySentenceWord();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void turnWord(String turnString){
        try{
            JSONArray sentence = sentences.getJSONArray(currentSentence - 1);
            JSONObject word = sentence.getJSONObject(currentSound);
            ImageView image = sentenceViews.get(currentSound);
            int picture = word.getInt(turnString);
            if (picture > 0)
                image.setImageResource(picture);
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void saySentenceWord() {
        try{
            if (currentSound < sounds.size()) {
                turnWord("red_word");
                int sound = sounds.get(currentSound);
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        turnWord("black_word");
                        currentSound++;
                        saySentenceWord();
                    }
                });
            }
            else{
                sayItsYourTurn();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    Runnable showSentenceWithoutSound = new Runnable()
    {
        @Override
        public void run() {
            try {
                if (currentSound > -1 && currentSound < sounds.size())
                    turnWord("black_word");
                showSentenceNoSound();
            }
            catch(Exception ex){
                ex.printStackTrace();
                finish();
            }
        }
    };

    private void showSentenceNoSound() {
        try{
            if (currentSound < sounds.size()) {
                currentSound++;
                if (currentSound < sounds.size())
                    turnWord("red_word");
                handler.postDelayed(showSentenceWithoutSound,1500);
            }
            else{
                handler.postDelayed(finishListening,100);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void sayItsYourTurn() {
        try{
            int sound = allData.getInt("now_you_read_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    currentSound = -1;
                    //currentSentence = 1;
                    handler.postDelayed(showSentenceWithoutSound,100);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    Runnable finishListening = new Runnable()
    {
        @Override
        public void run() {
            currentSentence ++;
            if (currentSentence - 1 <  sentences.length() ){
                populateAndShowSentence();
            }
            else{
                listenToTheWholeStory();
            }
        }
    };

    public void listenToTheWholeStory(){
        try {
            int sound = allData.getInt("listen_to_whole_story");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    readWholeStory();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void readWholeStory(){
        try{
            int image = allData.getInt("story_image");
            getWindow().getDecorView().getRootView().setBackgroundResource(image);
            container.setVisibility(View.INVISIBLE);
            int sound = allData.getInt("full_story_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    readWholeStoryInstructions();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    //

    public void readWholeStoryInstructions(){
        try {
            container.setVisibility(View.VISIBLE);
            container.removeAllViews();
            int sound = allData.getInt("read_whole_story_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    touchArrowInstructions();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void touchArrowInstructions(){
        try {
            container.setVisibility(View.VISIBLE);
            container.removeAllViews();
            int sound = allData.getInt("touch_arrow");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    startStory();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    public void startStory(){
        currentSentence = 0;
        getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);
        nextButton.setVisibility(View.VISIBLE);
        nextSentence();
    }

    public void nextSentence(){
        currentSentence++;
        if (currentSentence - 1 <  sentences.length())
            populateSentence();
        else
            wellDone();
    }

    //
    public void populateFullStory(){
        try {
            int lines = 0;
            boolean done = false;
            container.removeAllViews();
            container.setVisibility(View.VISIBLE);
            while (!done) {
                JSONArray sentence = sentences.getJSONArray(currentSentence - 1);
                LinearLayout line = getLine();
                ImageView item;
                int width = 0;
                for (int i = 0; i < sentence.length(); i++) {
                    JSONObject word = sentence.getJSONObject(i);
                    item = new ImageView(this);
                    item.setTag(word.getInt("sound"));
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            playSound(view);
                        }
                    });
                    if (i > 0)
                        item.setPadding(50, 10, 0, 0);
                    item.setMaxHeight(143);
                    item.setImageResource(word.getInt("black_word"));
                    width += ImageHelper.getLength(word.getInt("black_word"), this);
                    if (width > 1150) {
                        lines ++;
                        container.addView(line);
                        line = getLine();
                        width = 0;
                    }
                    line.addView(item);
                }
                lines++;
                currentSentence++;
                if ((lines > 5) || (currentSentence > sentences.length()))
                    done = true;
                container.addView(line);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void wellDone(){
        try {
            container.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
            int image = allData.getInt("story_image");
            getWindow().getDecorView().getRootView().setBackgroundResource(image);
            int sound = allData.getInt("well_done_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    nowAnswerQuestions();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void nowAnswerQuestions(){
        try {
            int sound = allData.getInt("now_answer_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    doComprehension();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void doComprehension(){
        try {
            getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.backgound_comprehension);
            currentQuestion = 1;
            questions = allData.getJSONArray("questions");
            nextQuestion();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private  void imageClicked(int image){
        try {
            JSONObject question = questions.getJSONObject(currentQuestion - 1);
            if (question.getJSONArray("images").getJSONObject(image-1).getInt("is_right") == 1){
                playSound(ResourceSelector.getPositiveAffirmationSound(this));
                currentQuestion++;
                nextQuestion();
            }
            else{
                playSound(ResourceSelector.getNegativeAffirmationSound(this));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void nextQuestion(){
        try{

            if (currentQuestion -1 < questions.length()){
                JSONObject question = questions.getJSONObject(currentQuestion - 1);
                if (question.getInt("is_touch") == 1){ //Three objects
                    singleImage.setVisibility(View.INVISIBLE);
                    tripleImageThree.setVisibility(View.VISIBLE);
                    trippleImageOne.setVisibility(View.VISIBLE);
                    trippleImageTwo.setVisibility(View.VISIBLE);
                    trippleImageOne.setImageResource(question.getJSONArray("images").getJSONObject(0).getInt("image"));
                    trippleImageOne.setImageResource(question.getJSONArray("images").getJSONObject(1).getInt("image"));
                    trippleImageOne.setImageResource(question.getJSONArray("images").getJSONObject(2).getInt("image"));
                }
                else{
                    singleImage.setVisibility(View.VISIBLE);
                    tripleImageThree.setVisibility(View.INVISIBLE);
                    trippleImageOne.setVisibility(View.INVISIBLE);
                    trippleImageTwo.setVisibility(View.INVISIBLE);
                    singleImage.setImageResource(question.getJSONArray("images").getJSONObject(0).getInt("image"));

                }
                int sound = question.getInt("question_sound");
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            mp.reset();
                            JSONObject question = questions.getJSONObject(currentQuestion - 1);
                            if (question.getInt("is_touch") == 0) { //Three objects
                                handler.postDelayed(plaSingleImageAnswerRunnable, 3000);
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                            finish();
                        }
                    }
                });
            }
            else{
                finish();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable plaSingleImageAnswerRunnable = new Runnable(){
        public void run(){
            playSingleImageAnswer();
        }
    };

    private void playSingleImageAnswer() {
        try{
            JSONObject question = questions.getJSONObject(currentQuestion - 1);
            singleImage.setImageResource(question.getJSONArray("images").getJSONObject(1).getInt("image"));
            int sound = question.getInt("answer_sound");
            mp.reset();
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    currentQuestion++;
                    nextQuestion();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    public void playSound(int sound){
        try{
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }

    //@Override
    //public void onBackPressed() {
    //}
}

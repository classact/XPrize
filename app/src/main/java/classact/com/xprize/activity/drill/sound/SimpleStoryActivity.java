package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import classact.com.xprize.R;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ImageHelper;
import classact.com.xprize.utils.ResourceSelector;

public class SimpleStoryActivity extends AppCompatActivity {
    private JSONArray sentences;
    private MediaPlayer mp;
    private Handler handler;
    private int currentSentence;
    ArrayList<String> sounds;
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

        // Set background to background-simple-story (until I change it in the layout file)
        getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);

        container = (LinearLayout)findViewById(R.id.container_simple_story);
        singleImage = (ImageView)findViewById(R.id.single_image);
        trippleImageOne = (ImageView)findViewById (R.id.tripple_image_1);
        trippleImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(0);
            }
        });
        trippleImageTwo = (ImageView)findViewById(R.id.tripple_image_2);
        trippleImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(1);
            }
        });
        tripleImageThree = (ImageView)findViewById(R.id.tripple_image_3);
        tripleImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(2);
            }
        });
        nextButton = (ImageView)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSentence();
            }
        });
        handler = new Handler();
        currentSentence = 0;
        initialiseData();
    }

    private void initialiseData(){

        // Debug
        System.out.println(":: SimpleStoryActivity.initialiseData > Debug: METHOD CALLED");

        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            sentences = allData.getJSONArray("sentences");
            currentSound = 0;
            populateAndShowSentence();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void populateAndShowSentence(){

        // Debug
        System.out.println(":: SimpleStoryActivity.populateAndShowSentence > Debug: METHOD CALLED");

        try{
            getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);
            populateSentence();
            currentSound = 0;
            sayListenFirst();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void populateSentence(){

        // Debug
        System.out.println(":: SimpleStoryActivity.populateSentence > Debug: METHOD CALLED");

        try{
            JSONArray sentence = sentences.getJSONArray(currentSentence);
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
                item.setTag(word.getString("sound"));
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
                sounds.add(new String(word.getString("sound")));
            }
            container.addView(line);
            currentSound = 0;
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void sayListenFirst() {

        // Debug
        System.out.println(":: SimpleStoryActivity.sayListenFirst > Debug: METHOD CALLED");

        try{
            String sound = allData.getString("listen_first_sound");
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
                    saySentenceWord();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void saySentenceWord() {

        // Debug
        System.out.println(":: SimpleStoryActivity.saySentenceWord > Debug: METHOD CALLED");

        try{
            if (currentSound < sounds.size()) {
                turnWord("red_word");
                String sound = sounds.get(currentSound);
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
                        turnWord("black_word");
                        currentSound++;
                        saySentenceWord();
                    }
                });
                mp.prepare();
            }
            else{
                sayItsYourTurn();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void sayItsYourTurn() {

        // Debug
        System.out.println(":: SimpleStoryActivity.sayItsYourTurn > Debug: METHOD CALLED");

        try{
            String sound = allData.getString("now_you_read_sound");
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
                    currentSound = -1;
                    //currentSentence = 1;
                    handler.postDelayed(showSentenceWithoutSound,100);
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void showSentenceNoSound() {

        // Debug
        System.out.println(":: SimpleStoryActivity.showSentenceNoSound > Debug: METHOD CALLED");

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
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    Runnable showSentenceWithoutSound = new Runnable()
    {
        @Override
        public void run() {

            // Debug
            System.out.println(":: SimpleStoryActivity.showSentenceWithoutSound(Runnable) > Debug: METHOD CALLED");

            try {
                if (currentSound > -1 && currentSound < sounds.size())
                    turnWord("black_word");
                showSentenceNoSound();
            }
            catch(Exception ex){
                ex.printStackTrace();
                if (mp != null){
                    mp.release();
                }
                finish();
            }
        }
    };

    Runnable finishListening = new Runnable()
    {
        @Override
        public void run() {

            // Debug
            System.out.println(":: SimpleStoryActivity.finishListening(Runnable) > Debug: METHOD CALLED");

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

        // Debug
        System.out.println(":: SimpleStoryActivity.listenToTheWholeStory > Debug: METHOD CALLED");

        try {
            String sound = allData.getString("listen_to_whole_story");
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
                    readWholeStory();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void readWholeStory(){

        // Debug
        System.out.println(":: SimpleStoryActivity.readWholeStory > Debug: METHOD CALLED");

        try{
            int image = allData.getInt("story_image");
            getWindow().getDecorView().getRootView().setBackgroundResource(image);
            container.setVisibility(View.INVISIBLE);
            String sound = allData.getString("full_story_sound");
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
                    readWholeStoryInstructions();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void readWholeStoryInstructions(){

        // Debug
        System.out.println(":: SimpleStoryActivity.readWholeStoryInstructions > Debug: METHOD CALLED");

        try {
            container.setVisibility(View.VISIBLE);
            container.removeAllViews();
            String sound = allData.getString("read_whole_story_sound");
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
                    touchArrowInstructions();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void touchArrowInstructions(){

        // Debug
        System.out.println(":: SimpleStoryActivity.touchArrowInstructions > Debug: METHOD CALLED");

        try {
            container.setVisibility(View.VISIBLE);
            container.removeAllViews();
            String sound = allData.getString("touch_arrow");
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
                    startStory();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void startStory(){

        // Debug
        System.out.println(":: SimpleStoryActivity.startStory > Debug: METHOD CALLED");

        currentSentence = 0;
        getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);
        nextButton.setVisibility(View.VISIBLE);
        nextSentence();
    }

    public void nextSentence(){

        // Debug
        System.out.println(":: SimpleStoryActivity.nextSentence > Debug: METHOD CALLED");

        currentSentence++;
        if (currentSentence - 1 <  sentences.length())
            populateSentence();
        else
            wellDone();
    }

    public void wellDone(){

        // Debug
        System.out.println(":: SimpleStoryActivity.wellDone > Debug: METHOD CALLED");

        try {
            container.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
            int image = allData.getInt("story_image");
            getWindow().getDecorView().getRootView().setBackgroundResource(image);
            String sound = allData.getString("well_done_sound");
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
                    nowAnswerQuestions();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void nowAnswerQuestions(){

        // Debug
        System.out.println(":: SimpleStoryActivity.nowAnswerQuestions > Debug: METHOD CALLED");

        try {
            String sound = allData.getString("now_answer_sound");
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
                    doComprehension();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void doComprehension(){

        // Debug
        System.out.println(":: SimpleStoryActivity.doComprehension > Debug: METHOD CALLED");

        try {
            getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.backgound_comprehension);
            currentQuestion = 1;
            questions = allData.getJSONArray("questions");
            nextQuestion();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void nextQuestion(){

        // Debug
        System.out.println(":: SimpleStoryActivity.nextQuestion > Debug: METHOD CALLED");

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
                String sound = question.getString("question_sound");
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
                mp.prepare();
            }
            else{
                if (mp != null){
                    mp.release();
                }
                finish();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public Runnable plaSingleImageAnswerRunnable = new Runnable(){
        public void run(){

            // Debug
            System.out.println(":: SimpleStoryActivity.plaSingleImageAnswerRunnable(Runnable).run > Debug: METHOD CALLED");

            playSingleImageAnswer();
        }
    };

    private void playSingleImageAnswer() {

        // Debug
        System.out.println(":: SimpleStoryActivity.playSingleImageAnswer > Debug: METHOD CALLED");

        try{
            JSONObject question = questions.getJSONObject(currentQuestion - 1);
            singleImage.setImageResource(question.getJSONArray("images").getJSONObject(1).getInt("image"));
            String sound = question.getString("answer_sound");
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
                    currentQuestion++;
                    nextQuestion();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private LinearLayout getLine(){

        // Debug
        System.out.println(":: SimpleStoryActivity.getLine > Debug: METHOD CALLED");

        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        return line;
    }

    public void playSound(View v){

        // Debug
        System.out.println(":: SimpleStoryActivity.playSound > Debug: METHOD CALLED");

        try{
            String sound = (String) v.getTag();
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
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void turnWord(String turnString){

        // Debug
        System.out.println(":: SimpleStoryActivity.turnWord > Debug: METHOD CALLED");

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
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    //
    public void populateFullStory(){

        // Debug
        System.out.println(":: SimpleStoryActivity.populateFullStory > Debug: METHOD CALLED");

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
                    item.setTag(word.getString("sound"));
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
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private  void imageClicked(int image){

        // Debug
        System.out.println(":: SimpleStoryActivity.imageClicked > Debug: METHOD CALLED");

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
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void playSound(int sound){

        // Debug
        System.out.println(":: SimpleStoryActivity.playSound > Debug: METHOD CALLED");

        try{
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        // Debug
        System.out.println(":: SimpleStoryActivity.onPause > Debug: METHOD CALLED");

        if (mp != null){
            mp.release();
        }
    }
}

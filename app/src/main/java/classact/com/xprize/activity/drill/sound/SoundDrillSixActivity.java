package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillSixActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_six) ConstraintLayout rootView;
    @BindView(R.id.background) ImageView background;
    @BindView(R.id.left_letter) ImageView leftLetter;
    @BindView(R.id.right_letter) ImageView rightLetter;
    @BindView(R.id.isolated_letter) ImageView isolatedLetter;
    @BindView(R.id.left_box_letter) ImageView leftBoxLetter;
    @BindView(R.id.right_box_letter) ImageView rightBoxLetter;
    @BindView(R.id.left_box_drop_zone) ImageView leftBoxDropZone;
    @BindView(R.id.right_box_drop_zone) ImageView rightBoxDropZone;

    @BindView(R.id.letter_01) ImageView item1;
    @BindView(R.id.letter_02) ImageView item2;
    @BindView(R.id.letter_03) ImageView item3;
    @BindView(R.id.letter_04) ImageView item4;
    @BindView(R.id.letter_05) ImageView item5;
    @BindView(R.id.letter_06) ImageView item6;
    @BindView(R.id.letter_07) ImageView item7;
    @BindView(R.id.letter_08) ImageView item8;

    private int currentItem;
    public float x;
    public float y;
    public int image1;
    public int image2;
    public int [] positions;
    public boolean isInReceptacle1;
    public boolean isInReceptacle2;
    private int correctItems = 0;
    private boolean itemsEnabled;
    private Runnable mRunnable;

    private SoundDrill06ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_six);
        ButterKnife.bind(this);

        preloadImage(
                R.drawable.backgroundcapitalletterbox,
                R.drawable.background_boxesdrill
        );

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill06ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        itemsEnabled = false;

        item1.setOnTouchListener((v, event) -> {
            currentItem = 0;
            return dragItem(v,event);
        });
        item2.setOnTouchListener((v, event) -> {
            currentItem = 1;
            return dragItem(v,event);
        });
        item3.setOnTouchListener((v, event) -> {
            currentItem = 2;
            return dragItem(v,event);
        });
        item4.setOnTouchListener((v, event) -> {
            currentItem = 3;
            return dragItem(v,event);
        });
        item5.setOnTouchListener((v, event) -> {
            currentItem = 4;
            return dragItem(v,event);
        });
        item6.setOnTouchListener((v, event) -> {
            currentItem = 5;
            return dragItem(v,event);
        });
        item7.setOnTouchListener((v, event) -> {
            currentItem = 6;
            return dragItem(v,event);
        });
        item8.setOnTouchListener((v, event) -> {
            currentItem = 7;
            return dragItem(v,event);
        });

        leftBoxDropZone.setOnDragListener((v, event) -> {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED) {
                        isInReceptacle1 = true;
                    }else if (action == DragEvent.ACTION_DRAG_EXITED) {
                        isInReceptacle1 = false;
                    } else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle1) {
                        if ( positions[currentItem] == image1) {
                            starWorks.play(this, leftBoxDropZone);
                            reward();
                        }
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle1) {
                        if ( positions[currentItem] != image1) {
                            playSound(ResourceSelector.getNegativeAffirmationSound(context), null);
                        } else {
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        );

        rightBoxDropZone.setOnDragListener((v, event) -> {
            try {
                int action = event.getAction();
                if (action == DragEvent.ACTION_DRAG_ENTERED) {
                    isInReceptacle2 = true;
                } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                    isInReceptacle2 = false;
                } else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle2 ) {
                    if ( positions[currentItem] == image2) {
                        starWorks.play(this, rightBoxDropZone);
                        reward();
                    }
                } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle2) {
                    if ( positions[currentItem] != image2) {
                        playSound(ResourceSelector.getNegativeAffirmationSound(context), null);
                    } else {
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        });

        initialiseData();
        playWeCanWriteTheLetter();
    }

    private void initialiseData(){
        try{

            ez.hide(item1, item2, item3, item4, item5, item6, item7, item8);

            Random rnd = new Random();
            List<Integer> indexes = new ArrayList<>();
            indexes.add(0);
            indexes.add(1);
            indexes.add(2);
            indexes.add(3);
            indexes.add(4);
            indexes.add(5);
            indexes.add(6);
            indexes.add(7);

            Letter letter = vm.getLetter();

            int lowerCaseImageId = FetchResource.imageId(context, letter.getLetterPictureLowerCaseBlackURI());
            int upperCaseImageId = FetchResource.imageId(context, letter.getLetterPictureUpperCaseBlackURI());

            loadImage(leftLetter, lowerCaseImageId);
            loadImage(rightLetter, upperCaseImageId);

            // Shuffle image1 and image2
            if (rnd.nextInt(2) == 0) {
                image1 = lowerCaseImageId;
                image2 = upperCaseImageId;
            } else {
                image1 = upperCaseImageId;
                image2 = lowerCaseImageId;
            }

            positions = new int[8];

            for (int i = 8; i > 0; i--) {
                int index = indexes.get(rnd.nextInt(i));
                positions[index] = (i > 4) ? lowerCaseImageId : upperCaseImageId;
                indexes.remove(indexes.indexOf(index));
            }

            loadImage(item1, positions[0]);
            loadImage(item2, positions[1]);
            loadImage(item3, positions[2]);
            loadImage(item4, positions[3]);
            loadImage(item5, positions[4]);
            loadImage(item6, positions[5]);
            loadImage(item7, positions[6]);
            loadImage(item8, positions[7]);

            correctItems = 0;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playWeCanWriteTheLetter() {
        try {
            String sound = "drill6drillsound1";
            playSound(sound, () -> handler.delayed(this::playDrillLetter, 350));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDrillLetter(){
        try {
            String sound = vm.getLetter().getLetterSoundURI();
            playSound(sound, this::playInTwoWays);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playInTwoWays(){
        try {
            String sound = "drill6drillsound2";
            playSound(sound, () -> handler.delayed(this::playStartLowerCase, 650));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playStartLowerCase(){
        try {
            int imageId = FetchResource.imageId(context, vm.getLetter().getLetterPictureLowerCaseBlackURI());
            String sound = "drill6drillsound3";

            loadImage(background, R.drawable.backgroundcapitalletterbox,
                    new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    ez.hide(leftLetter, rightLetter);
                    loadImage(isolatedLetter, imageId);
                    rootView.setBackgroundColor(Color.WHITE);
                    playSound(sound, () -> playDrillLetterSecondTime());
                    return false;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDrillLetterSecondTime(){
        try {
            String sound = vm.getLetter().getLetterSoundURI();
            playSound(sound, () -> handler.delayed(this::playUpperCase, 650));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playUpperCase(){
        try {
            loadImage(isolatedLetter,
                    FetchResource.imageId(context, vm.getLetter().getLetterPictureLowerCaseBlackURI()),
                    FetchResource.imageId(context, vm.getLetter().getLetterPictureUpperCaseBlackURI()));
            String sound = "drill6drillsound4";
            playSound(sound, this::playDrillLetterThirdTime);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDrillLetterThirdTime(){
        try {
            String sound = vm.getLetter().getLetterSoundURI();
            playSound(sound, () -> handler.delayed(this::playDragTheLetters, 610));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDragTheLetters(){
        try {

            loadImage(background, R.drawable.backgroundcapitalletterbox, R.drawable.background_boxesdrill, new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    ez.hide(isolatedLetter);
                    loadImage(leftBoxLetter, image1);
                    loadImage(rightBoxLetter, image2);
                    return false;
                }
            });

            String sound = "drill6drillsound5";
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp) -> {
                    mp.start();
                    handler.delayed(() -> {
                        itemsEnabled = true;
                        ez.show(item1, item2, item3, item4, item5, item6, item7, item8);
                    },mp.getDuration() - 100);
                });
            mediaPlayer.setOnCompletionListener((mp) -> mediaPlayer.stop());
            mediaPlayer.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void reward() {
        correctItems++;
        if (correctItems == 8){
            mRunnable = null;
            mRunnable = () -> handler.delayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 450);
            playSoundAndRunnableAfterCompletion(ResourceSelector.getPositiveAffirmationSound(this));
        } else {
            playSound(ResourceSelector.getPositiveAffirmationSound(this), null);
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (itemsEnabled) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                isInReceptacle1 = false;
                isInReceptacle2 = false;
                return true;
            }
        }
        return false;
    }

    private void playSoundAndRunnableAfterCompletion(int soundId) {
        try {
            playSound(soundId, mRunnable);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.ResourceSelector;

public class SoundDrillTwoActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_two) ConstraintLayout rootView;
    @BindView(R.id.background) ImageView background;

    @BindView(R.id.left_image) ImageView leftImage;
    @BindView(R.id.right_image) ImageView rightImage;

    private int currentPair;

    private int play_mode = 1;
    private boolean itemsEnabled;

    private SoundDrill02ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_two);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill02ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        itemsEnabled = false;

        initialize();
        showPair();
    }

    private void initialize (){
        try {
            leftImage.setOnClickListener((v) -> clickedItem(0));
            rightImage.setOnClickListener((v) -> clickedItem(1));

            currentPair = 0;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showPair(){
        try {
            play_mode = 1;

            leftImage.setVisibility(View.INVISIBLE);
            rightImage.setVisibility(View.INVISIBLE);

            leftImage.setImageResource(FetchResource.imageId(context, vm.getLeftWord(currentPair).getImagePictureURI()));
            rightImage.setImageResource(FetchResource.imageId(context, vm.getRightWord(currentPair).getImagePictureURI()));

            showFirstItem();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showFirstItem(){
        try {
            leftImage.setVisibility(View.VISIBLE);
            String sound = "drill2drillsound1";
            playSound(sound, this::playFirstSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playFirstSound(){
        String sound;
        try {
            sound = vm.getLeftWord(currentPair).getWordSoundURI();
            playSound(sound, this::startSecondItem);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void startSecondItem(){
        try {
            rightImage.setVisibility(View.VISIBLE);
            String sound = "drill2drillsound1";
            playSound(sound, this::playSecondSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSecondSound(){
        String sound;
        try {
            sound = vm.getRightWord(currentPair).getWordSoundURI();
            playSound(sound, this::playIntro);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playIntro(){
        try {
            play_mode = 2;
            String sound = "drill2drillsound2";
            playSound(sound, this::playPhonicSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playPhonicSound() {
        String sound;
        try {
            sound = vm.getLetter().getPhonicSoundURI();
            playSound(sound, () -> itemsEnabled = true);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void clickedItem(int item){
        if (itemsEnabled) {
            try {
                if (play_mode == 2) {
                    if (vm.isCorrect(currentPair, item)) {

                        itemsEnabled = false;

                        ImageView iv = null;
                        if (item == 0) {
                            iv = leftImage;
                        } else if (item == 1) {
                            iv = rightImage;
                        }
                        Globals.playStarWorks(this, iv, 15, 12, 9);

                        playSound(ResourceSelector.getPositiveAffirmationSound(this), () -> {
                            if (currentPair++ < vm.getWordCount() - 1) {
                                handler.delayed(this::showPair, 350);
                            } else {
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });
                    } else {
                        playSound(ResourceSelector.getNegativeAffirmationSound(this), null);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
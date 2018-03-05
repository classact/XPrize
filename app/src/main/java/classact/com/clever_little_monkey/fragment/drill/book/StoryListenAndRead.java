package classact.com.clever_little_monkey.fragment.drill.book;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.database.model.StoryWord;
import classact.com.clever_little_monkey.event.PauseEvent;
import classact.com.clever_little_monkey.event.ResumeEvent;
import classact.com.clever_little_monkey.fragment.DrillFragment;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.LiveObjectAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryListenAndRead extends DrillFragment {

    private StoryBuilder storyBuilder;
    private List<StoryBook> storyBooks;
    private LinearLayout sentenceLines;
    private ImageButton leftArrow, rightArrow;
    private ImageButton pauseButton;

    private ImageView continueMonkey;
    private boolean continueMonkeyAnimationCancelled;

    final float WORD_HEIGHT = 85f;
    final float LEFT_SCREEN_MARGIN = 100f;
    final float TOP_SCREEN_MARGIN = 80f;
    final float RIGHT_SCREEN_MARGIN = 100f;
    final float BOTTOM_SCREEN_MARGIN = 80f;

    final int MINIMUM_WORD_DURATION = 1100;

    private StoryListenAndReadViewModel vm;

    private List<View> circles;

    @BindView(R.id.fragment_container) ConstraintLayout container;
    private FrameLayout touchScreen;

    @Inject
    public StoryListenAndRead() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_listen_and_read, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get view model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(StoryListenAndReadViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        // Handler
        handler = vm.getHandler();

        // Media player
        mediaPlayer = vm.getMediaPlayer();

        // Get story builder
        storyBuilder = vm.getStoryBuilder();

        // Get story books
        storyBooks = vm.getStoryBooks();

        // Prepare circles
        circles = new ArrayList<>();

//        // Background
//        ImageView background = new ImageView(context);
//        MarginLayoutParams backgroundLayoutParams = new MarginLayoutParams(
//                MarginLayoutParams.MATCH_PARENT,
//                MarginLayoutParams.MATCH_PARENT
//        );
//        background.setLayoutParams(backgroundLayoutParams);
//        background.setScaleType(ImageView.ScaleType.FIT_XY);
//        container.addView(background);
//        Glide.with(this).load(R.drawable.background_simple_story).into(background);

        // Get display metrics
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        // Get screen density
        float screenDensity = displayMetrics.density;

        int leftPadding = (int) (LEFT_SCREEN_MARGIN * screenDensity);
        int topPadding = (int) (TOP_SCREEN_MARGIN * screenDensity);
        int rightPadding = (int) (RIGHT_SCREEN_MARGIN * screenDensity);
        int bottomPadding = (int) (BOTTOM_SCREEN_MARGIN * screenDensity);

        // GUIDELINES
        // Vertical Start
        Guideline verticalStart = ez.guide.create(false, 0.0f);

        // Vertical End
        Guideline verticalEnd = ez.guide.create(false, 1.0f);

        // Horizontal Start
        Guideline horizontalStart = ez.guide.create(true, 0.0f);

        // Horizontal End
        Guideline horizontalEnd = ez.guide.create(true, 1.0f);

        // ADD GUIDELINES TO CONTAINER
        container.addView(verticalStart);
        container.addView(verticalEnd);
        container.addView(horizontalStart);
        container.addView(horizontalEnd);

        /* Continue Monkey */

        continueMonkey = new ImageView(context);
        Drawable monkeyDrawable = getResources().getDrawable(R.drawable.monkey_jumpsout12pic, null);
        ConstraintLayout.LayoutParams monkeyLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        monkeyLayoutParams.width = (int) (0.75f * monkeyDrawable.getIntrinsicWidth());
        monkeyLayoutParams.height = (int) (0.75f * monkeyDrawable.getIntrinsicHeight());
        monkeyLayoutParams.rightToLeft = verticalEnd.getId();
        monkeyLayoutParams.bottomToTop = horizontalEnd.getId();
        monkeyLayoutParams.rightMargin = (int) (90f * screenDensity);
        monkeyLayoutParams.bottomMargin = (int) (55f * screenDensity);
        continueMonkey.setLayoutParams(monkeyLayoutParams);
        loadImage("monkey_jumpsout12pic", continueMonkey);
        setTouchListener(continueMonkey, Color.argb(50, 255, 255, 225), PorterDuff.Mode.SRC_ATOP);

        continueMonkey.setAlpha(0f);
        continueMonkey.setEnabled(false);

        container.addView(continueMonkey);

        // Add Continue monkey listener
        continueMonkey.setOnClickListener((v) -> {
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // ARROW
        Drawable arrowDrawable = context.getDrawable(R.drawable.simple_story_next);

        // RIGHT ARROW
        rightArrow = new ImageButton(context);
        rightArrow.setScaleType(ImageView.ScaleType.FIT_XY);

        // UPDATE NEXT ARROW LAYOUT PARAMS
        ConstraintLayout.LayoutParams rightArrowLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        rightArrowLayoutParams.width = arrowDrawable.getIntrinsicWidth()/2;
        rightArrowLayoutParams.height = arrowDrawable.getIntrinsicHeight()/2;
        rightArrowLayoutParams.rightToLeft = verticalEnd.getId();
        rightArrowLayoutParams.bottomToTop = horizontalEnd.getId();
        rightArrowLayoutParams.rightMargin = (int) (80f * screenDensity);
        rightArrowLayoutParams.bottomMargin = (int) (33f * screenDensity);
        rightArrow.setLayoutParams(rightArrowLayoutParams);
        addOnArrowTouchListener(rightArrow);

        rightArrow.setImageResource(R.drawable.simple_story_next);
        rightArrow.setBackgroundColor(Color.TRANSPARENT);

        // ADD NEXT ARROW TO CONTAINER
        container.addView(rightArrow);

        // PREVIOUS ARROW
        leftArrow = new ImageButton(context);
        leftArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        leftArrow.setScaleX(-1.0f);

        // UPDATE PREVIOUS ARROW LAYOUT PARAMS
        ConstraintLayout.LayoutParams leftArrowLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        leftArrowLayoutParams.width = arrowDrawable.getIntrinsicWidth()/2;
        leftArrowLayoutParams.height = arrowDrawable.getIntrinsicHeight()/2;
        leftArrowLayoutParams.leftToRight = verticalStart.getId();
        leftArrowLayoutParams.bottomToTop = horizontalEnd.getId();
        leftArrowLayoutParams.leftMargin = (int) (80f * screenDensity);
        leftArrowLayoutParams.bottomMargin = (int) (33f * screenDensity);
        leftArrow.setLayoutParams(leftArrowLayoutParams);
        addOnArrowTouchListener(leftArrow);

        leftArrow.setImageResource(R.drawable.simple_story_next);
        leftArrow.setBackgroundColor(Color.TRANSPARENT);

        // ADD PREVIOUS ARROW TO CONTAINER
        container.addView(leftArrow);

        // ADD ON CLICK LISTENERS TO ARROWS
        rightArrow.setOnClickListener((v) -> readNext());
        leftArrow.setOnClickListener((v) -> readPrevious());

        /* SENTENCE LINES */

        sentenceLines = new LinearLayout(context);
        sentenceLines.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        sentenceLines.setOrientation(LinearLayout.VERTICAL);
        container.addView(sentenceLines);

        /* PAUSE SCREEN */

        touchScreen = new FrameLayout(context);
        FrameLayout.LayoutParams touchScreenLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        touchScreen.setLayoutParams(touchScreenLayoutParams);
        touchScreen.setBackgroundColor(Color.TRANSPARENT);
        touchScreen.setFocusable(true);
        touchScreen.setClickable(true);
        container.addView(touchScreen);

                /* ******* PAUSE BUTTON START ********* */

        Drawable pauseButtonDrawable = context.getDrawable(R.drawable.pause_button);
        pauseButton = new ImageButton(context);
        pauseButton.setImageResource((vm.isPaused()) ? R.drawable.play_button : R.drawable.pause_button);
        pauseButton.setBackgroundColor(Color.TRANSPARENT);
        ConstraintLayout.LayoutParams pauseButtonLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        pauseButton.setScaleX(0.65f);
        pauseButton.setScaleY(0.65f);
//        pauseButtonLayoutParams.width = (int) (0.75f * pauseButtonDrawable.getIntrinsicWidth());
//        pauseButtonLayoutParams.height = (int) (0.75f * pauseButtonDrawable.getIntrinsicHeight());
        pauseButtonLayoutParams.rightToLeft = verticalEnd.getId();
        pauseButtonLayoutParams.topToBottom = horizontalStart.getId();
        pauseButtonLayoutParams.topMargin = (int) (5f * screenDensity);
        pauseButtonLayoutParams.rightMargin = (int) (5f * screenDensity);
        pauseButton.setLayoutParams(pauseButtonLayoutParams);
        pauseButton.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    bus.send((vm.isPaused()) ? new ResumeEvent() : new PauseEvent());
                    break;
                case MotionEvent.ACTION_UP:
                    pauseButton.setImageResource((vm.isPaused()) ? R.drawable.play_button : R.drawable.pause_button);
                    v.performClick();
                    break;
                default:
                    break;
            }
            return true;
        });
//        container.addView(pauseButton);

        /* ******** PAUSE BUTTON END ********** */

        /* READ STORY */

        read(vm.iStory, vm.iParagraph);
        if (vm.iColoredSentence != -1) {
            colorSentence(vm.iColoredSentence, Color.RED);
        }
        if (vm.iColoredWord != -1) {
            colorView(getWord(vm.iColoredWord), Color.RED);
        }

        /* STATES */

        states.add(this::readEachSentenceAfterMother);
        states.add(this::listenFirst);
        states.add(this::narrateFirstSentence);
        states.add(this::nowRead);
        states.add(this::narrateFirstWord);
        states.add(this::autoReadStart);
        states.add(this::autoReadComplete);
        states.add(this::nowReadTheWholeStory);
        states.add(this::touchTheArrowToMoveOn);
        states.add(this::selfReadStart);
        states.add(this::selfReadComplete);
        states.add(this::wellDoneNowYouCanRead);
        states.add(this::comprehensionStart);

        handler.delayed(() -> states.get(vm.currentState).execute(), 400);
    }

    private void disableTouchScreen() {
        ez.hide(touchScreen);
    }

    private void enableTouchScreen() {
        ez.show(touchScreen);
    }

    // INSTRUCTION 1
    public void readEachSentenceAfterMother() {
        vm.currentState = 0;
        state01();
        String sound = "ssi_1"; // Read each sentence after mother
        playSound(sound, () -> handler.delayed(this::listenFirst, 400));
    }

    // INSTRUCTION 2
    public void listenFirst() {
        vm.currentState = 1;
        state01();
        String sound = "ssi_5"; // Listen first
        playSound(sound, () -> handler.delayed(this::narrateFirstSentence, 1000));
    }

    // SENTENCE NARRATION
    public void narrateFirstSentence() {
        vm.currentState = 2;
        state01();
        readSentence(vm.iSentence, this::nowRead);
    }

    // INSTRUCTION 3
    public void nowRead() {
        vm.currentState = 3;
        state01();
        String sound = "ssi_6"; // Now read
        playSound(sound, () -> handler.delayed(this::narrateFirstWord, 600));
    }

    // WORD NARRATION
    public void narrateFirstWord() {
        vm.currentState = 4;
        state01();
        readWord(vm.iWord, this::autoReadStart);
    }

    // WORD NARRATION
    public void autoReadStart() {
        vm.currentState = 5;
        state01();
        readNextWord();
    }

    public void autoReadComplete() {
        vm.currentState = 6;
        state01();
        nowReadTheWholeStory();
    }

    public void nowReadTheWholeStory() {
        vm.currentState = 7;
        state01();
        String sound = "ssi_10"; // Now read the whole story. Touch a word if you need help
        playSound(sound, () -> {
                handler.delayed(() -> {
                    View view = getWord(0, 0);
                    handler.delayed(() -> {
                        colorView(view, Color.RED);
                        handler.delayed(() -> {
                            colorView(view, Color.DKGRAY);
                            handler.delayed(() -> {
                                colorView(view, Color.RED);
                                handler.delayed(() -> {
                                    colorView(view, Color.DKGRAY);
                                }, 400);
                            }, 400);
                        }, 400);
                    }, 0);
                }, 3000);
            }, 0,
            this::touchTheArrowToMoveOn, 0);
    }

    public void touchTheArrowToMoveOn() {
        vm.currentState = 8;
        state02();
        ez.show(rightArrow);
        String sound = "ssi_17"; // Touch the arrow to move on
        playSound(sound, this::selfReadStart);
    }

    public void selfReadStart() {
        state03();
        vm.currentState = 9;
    }

    public void selfReadComplete() {
        state03();
        vm.currentState = 10;
        wellDoneNowYouCanRead();
    }

    public void wellDoneNowYouCanRead() {
        vm.currentState = 11;
        state02();
        String sound = "ssi_15"; // Well done! You can read!
        playSound(sound, this::comprehensionStart);
    }

    public void comprehensionStart() {
        state02();
        vm.currentState = 12;
    }

    public void state01() {
        vm.setNarrationPhase();
        ez.gray(leftArrow, rightArrow);
        ez.unclickable(leftArrow, rightArrow);
        enableTouchScreen();
    }

    public void state02() {
        vm.setSelfReadPhase();
        ez.gray(leftArrow, rightArrow);
        ez.unclickable(leftArrow, rightArrow);
        enableTouchScreen();
    }

    public void state03() {
        vm.setSelfReadPhase();
        ez.ungray(leftArrow, rightArrow);
        ez.clickable(leftArrow, rightArrow);
        disableTouchScreen();
    }

    /**
     * READ SENTENCE
     * @param iSentence index of sentence to be read
     * @param action action to execute after sentence has been read
     */
    public void readSentence(int iSentence, final Runnable action) {
        try {
            // Set index of sentence in view model
            vm.iSentence = iSentence;

            // Get sound file for sentence to play
            String sound = storyBooks
                    .get(vm.iStory)
                    .getParagraphs()
                    .get(vm.iParagraph)
                    .getSentences()
                    .get(iSentence)
                    .getSentence()
                    .soundFile;

            // Play sound
            playSound(sound, () -> {

                // Color sentence on prepare
                colorSentence(iSentence, Color.RED);
                vm.iColoredSentence = iSentence;

            }, 0, () -> {

                // Un-color sentence
                colorSentence(iSentence, Color.DKGRAY);
                vm.iColoredSentence = -1;
                if (action != null) {
                    handler.delayed(() -> action.run(), 0);
                }
            }, 0);
        } catch (Exception ex) {
            Log.e("READ NEXT SENTENCE ERROR", "ROW (" +
                    vm.iStory + ", " +
                    vm.iParagraph + ", " +
                    iSentence + ")");
            ex.printStackTrace();
        }
    }

    /**
     * Read word
     * @param iWord index of word to read
     * @param action action to execute after word has been read
     */
    public void readWord(int iWord, Runnable action) {

        vm.iWord = iWord;

        View view = getWord(iWord);

        if (view == null) {
            Log.e("READ WORD ERROR", "NO VIEW (" +
                    vm.iStory + ", " +
                    vm.iParagraph + ", " +
                    vm.iSentence + ", " + iWord + ")");
            return;
        }

        String sound = null;

        if (view instanceof StoryBuilder.PlayableImageView) {
            sound = ((StoryBuilder.PlayableImageView) view).getSound();
        } else if (view instanceof StoryBuilder.PlayableTextView) {
            sound = ((StoryBuilder.PlayableTextView) view).getSound();
        } else if (view instanceof StoryBuilder.PlayableLinearLayout) {
            sound = ((StoryBuilder.PlayableLinearLayout) view).getSound();
        }

        if (sound == null) {
            Log.e("READ WORD ERROR", "NO SOUND FOR (" +
                    vm.iStory + ", " +
                    vm.iParagraph + ", " +
                    vm.iSentence + ", " + iWord + ")");
            return;
        }

        try {
            final View targetView = view;
            String soundPath = FetchResource.sound(context, sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnResetListener((mp) -> {
                // In case of interrupt
                colorView(targetView, Color.DKGRAY);
                vm.iColoredWord = -1;
            });
            mediaPlayer.setOnPreparedListener((mp) -> {
                colorView(targetView, Color.RED);
                vm.iColoredWord = iWord;
                mp.start();
            });
            mediaPlayer.setOnCompletionListener((mp) -> {
                int timeDifference = MINIMUM_WORD_DURATION - mediaPlayer.getDuration();
                if (timeDifference < 0) {
                    handler.delayed(() -> {
                        colorView(targetView, Color.DKGRAY);
                        vm.iColoredWord = -1;
                        if (action != null) {
                            action.run();
                        }
                    }, 0);
                } else {
                    handler.delayed(() -> {
                        colorView(targetView, Color.DKGRAY);
                        vm.iColoredWord = -1;
                        if (action != null) {
                            action.run();
                        }
                    }, timeDifference);
                }
                mp.stop();
            });
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Log.e("READ WORD ERROR", "ERROR PLAYING '" + sound + "'");
            ex.printStackTrace();
        }
    }

    /**
     * Read word
     * @param view view of word to read
     * @param action action to execute after word has been read
     */
    public void readWord(View view, Runnable action) {

        if (view == null) {
            Log.e("READ WORD ERROR", "VIEW IS NULL");
            return;
        }

        String sound = null;

        if (view instanceof StoryBuilder.PlayableImageView) {
            sound = ((StoryBuilder.PlayableImageView) view).getSound();
        } else if (view instanceof StoryBuilder.PlayableTextView) {
            sound = ((StoryBuilder.PlayableTextView) view).getSound();
        } else if (view instanceof StoryBuilder.PlayableLinearLayout) {
            sound = ((StoryBuilder.PlayableLinearLayout) view).getSound();
        }

        if (sound == null) {
            Log.e("READ WORD ERROR", "NO SOUND FOR VIEW");
            return;
        }

        try {
            final View targetView = view;
            String soundPath = FetchResource.sound(context, sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnResetListener((mp) -> {
                // In case of interrupt
                colorView(targetView, Color.DKGRAY);
            });
            mediaPlayer.setOnPreparedListener((mp) -> {
                colorView(targetView, Color.RED);
                mp.start();
            });
            mediaPlayer.setOnCompletionListener((mp) -> {
                int timeDifference = MINIMUM_WORD_DURATION - mediaPlayer.getDuration();
                if (timeDifference < 0) {
                    handler.delayed(() -> {
                        colorView(targetView, Color.DKGRAY);
                        if (action != null) {
                            action.run();
                        }
                    }, 0);
                } else {
                    handler.delayed(() -> {
                        colorView(targetView, Color.DKGRAY);
                        if (action != null) {
                            action.run();
                        }
                    }, timeDifference);
                }
                mp.stop();
            });
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Log.e("READ WORD ERROR", "ERROR PLAYING '" + sound + "'");
            ex.printStackTrace();
        }
    }

    /**
     * Read next word
     */
    public void readNextWord() {

        // Increment word index
        vm.iWord++;

        // Get story book sentence
        StoryBookSentence storyBookSentence =
                storyBooks
                        .get(vm.iStory)
                        .getParagraphs()
                        .get(vm.iParagraph)
                        .getSentences()
                        .get(vm.iSentence);

        // Initialize variable to hold number of words
        int numberOfWords = storyBookSentence.getNumberOfWords();

        // Check if word is legit. If so, read it
        if (vm.iWord < numberOfWords) {
            readWord(vm.iWord, this::readNextWord);
            return;
        }

        // Reset word index
        vm.iWord = 0;

        // Increment sentence
        vm.iSentence++;

        // If legit sentence, read next word
        if (vm.iSentence < storyBooks.get(vm.iStory).getParagraphs().get(vm.iParagraph).getSentences().size()) {
            handler.delayed(
                    () -> readSentence(vm.iSentence,
                            () -> readWord(vm.iWord, this::readNextWord)),
                    600);
            return;
        }

        // Reset sentence
        vm.iSentence = 0;

        // Check if it's end of last paragraph to determine page-flip delay
        long delayMillis = 200;
        if (vm.iParagraph == storyBooks.get(vm.iStory).getParagraphs().size() - 1) {
            delayMillis = 600;
        }

        // Read next page after delay
        handler.delayed(() -> {

            // Read next page
            readNext();

            // Read word
            // Check if it's back to first paragraph
            if (vm.iParagraph == 0) {
                // We've finished reading
                // Auto read finished
                autoReadComplete();
            } else {

                handler.delayed(
                        () -> readSentence(vm.iSentence,
                                () -> readWord(vm.iWord, this::readNextWord)),
                        600);
            }

        }, delayMillis);
    }

    /**
     * Color view (or word)
     * @param view view to highlight
     * @param color color of highlight
     */
    public void colorView(View view, int color) {
        if (view instanceof StoryBuilder.PlayableTextView) {
            ((StoryBuilder.PlayableTextView) view).setTextColor(color);
        } else if (view instanceof StoryBuilder.PlayableLinearLayout) {
            ((StoryBuilder.PlayableLinearLayout) view).setColor(color);
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else if (view instanceof LinearLayout) {
            for (int i = 0; i < ((LinearLayout) view).getChildCount(); i++) {
                View iView = ((LinearLayout) view).getChildAt(i);
                if (iView instanceof TextView) {
                    ((TextView) iView).setTextColor(color);
                }
            }
        }
    }

    /**
     * Color the sentence including its lines
     * @param iSentence index of sentence
     * @param color color of sentence
     */
    public void colorSentence(int iSentence, int color) {
        StoryBookSentence storyBookSentence =
                storyBooks
                        .get(vm.iStory)
                        .getParagraphs()
                        .get(vm.iParagraph)
                        .getSentences()
                        .get(iSentence);
        for (int i = 0; i < storyBookSentence.getLineIndexes().size(); i++) {
            LinearLayout line = (LinearLayout) sentenceLines
                    .getChildAt(storyBookSentence
                            .getLineIndexes()
                            .get(i));

            if (line == null) {
                Log.e("COLOUR SENTENCE ERROR", "LINE (" +
                        vm.iStory + ", " +
                        vm.iParagraph + ", " +
                        iSentence + ")");
                return;
            }

            for (int j = 0; j < line.getChildCount(); j++) {
                View view = line.getChildAt(j);
                colorView(view, color);
            }
        }
    }

    public void read(int iStory, int iParagraph) {

        // Remove all children from sentence lines
        // as the children should be removed from parent view
        sentenceLines.removeAllViews();

        // Get story, paragraph and sentences of paragraph
        StoryBook storyBook = storyBooks.get(iStory);
        StoryBookParagraph storyBookParagraph = storyBook.getParagraphs().get(iParagraph);
        List<StoryBookSentence> storyBookSentences = storyBookParagraph.getSentences();

        // Determine if it's the first, last or 'in-between' page
        if (iParagraph == 0) {
            vm.setFirstPage();
        } else if (iParagraph == storyBook.getParagraphs().size() - 1) {
            vm.setLastPage();
        } else {
            vm.setInBetweenPage();
        }

        // Show arrows depending on type of page
        if (vm.isNarrationPhase()) {
            // If it's narration phase, show no arrow
            ez.hide(leftArrow, rightArrow);
        } else if (vm.isSelfReadPhase()) {
            // If it's self read phase, check if it's first, last or 'in-between' page
            ez.show(rightArrow);

            if (vm.isFirstPage()) {
                // Can go forward only
                ez.hide(leftArrow);
                ez.clickable(rightArrow);

                // Hide continue monkey
                ez.fadeHide(getLifecycle(), 1000, continueMonkey);
            } else if (vm.isLastPage()) {
                // Can go backward only
                ez.show(leftArrow);
                ez.unclickable(rightArrow);
                ez.hide(rightArrow);

                // Show continue monkey
                continueMonkeyAnimationCancelled = false;
                LiveObjectAnimator animator = ez.fadeShow(getLifecycle(), 1000, continueMonkey);
                if (animator != null) {
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ez.clickable(continueMonkey);
                        }
                    }).start();
                }

            } else {
                // Can go both ways
                ez.show(leftArrow);
                ez.show(rightArrow);
                ez.clickable(rightArrow);

                // Hide continue monkey
                ez.unclickable(continueMonkey);
                continueMonkeyAnimationCancelled = false;
                LiveObjectAnimator animator = ez.fadeHide(getLifecycle(), 500, continueMonkey);
                if (animator != null) {
                    animator.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            continueMonkeyAnimationCancelled = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (!continueMonkeyAnimationCancelled) {
                                ez.hide(continueMonkey);
                            }
                        }
                    }).start();
                }
            }
        }

        // For each sentence, get the words and construct view based on words
        for (StoryBookSentence storyBookSentence : storyBookSentences) {
            List<StoryWord> storyWords = storyBookSentence.getWords();

            List<LinearLayout> views = storyBuilder.sentence(
                    mediaPlayer,
                    storyWords,
                    WORD_HEIGHT,
                    LEFT_SCREEN_MARGIN,
                    RIGHT_SCREEN_MARGIN);

            List<Integer> lineIndexes = new ArrayList<>();
            int numberOfWords = 0;

            for (LinearLayout view : views) {
                sentenceLines.addView(view);
                lineIndexes.add(sentenceLines.indexOfChild(view));
                numberOfWords += view.getChildCount();

                // Add click listener for words
                for (int i = 0; i < view.getChildCount(); i++) {
                    View pView = view.getChildAt(i);
                    if (pView instanceof StoryBuilder.PlayableImageView ||
                        pView instanceof StoryBuilder.PlayableLinearLayout ||
                        pView instanceof StoryBuilder.PlayableTextView) {
                        pView.setOnClickListener((v) -> readWord(pView, null));
                    }
                }
            }

            storyBookSentence.setLineIndexes(lineIndexes);
            storyBookSentence.setNumberOfWords(numberOfWords);
        }

        // Reset circles
        for (View view : circles) {
            container.removeView(view);
        }
        circles.clear();

        // Number of circles
        int n = storyBook.getParagraphs().size();

        // Get screen density and width
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        float screenWidth = displayMetrics.widthPixels;
        float screenHeight = displayMetrics.heightPixels;

        // distance circles cover
        float margin = 400 * density;
        float dn = screenWidth - (2 * margin);
        float xGap = dn / n;

        for (int i = 0; i < n; i++) {
            int color = (i == iParagraph) ?
                    Color.argb(100, 0, 255, 255) :
                    Color.argb(100, 200, 200, 200);
            View circle = ez.circle(
                    margin + (xGap/2) + (i * xGap),
                    screenHeight - (133 * density),
                    10,
                    color);
            circles.add(circle);
        }

//        View rectangle = ez.rectangle(
//                margin,
//                screenHeight - (135 * density),
//                screenWidth - margin,
//                screenHeight - (125 * density));
//        circles.add(rectangle);

        for (View view : circles) {
            container.addView(view);
        }

//        float fraction = dn * (1 / n-1);

//        // Create circles
//        for (int i = 0; i < n; i++) {
//            View view = ez.circle(i * fraction, screenHeight - (35f * density), 10f, Color.GREEN);
//            container.addView(view);
//        }

        vm.iStory = iStory;
        vm.iParagraph = iParagraph;
    }

    /**
     * This method reads the 'next' page or paragraph of the story
     */
    public void readNext() {

        // Increment paragraph and see if the next paragraph exists
        vm.iParagraph++;
        if (vm.iParagraph < storyBooks.get(vm.iStory).getParagraphs().size()) {
            read(vm.iStory, vm.iParagraph);
            return;
        }

        // Increment story and see if it's the last one
        vm.iStory++;
        if (!(vm.iStory < storyBooks.size())) {
            vm.iStory = 0;
        }

        // Reset paragraph and see if next story (with first paragraph) is legit
        vm.iParagraph = 0;
        if (vm.iParagraph >= storyBooks.get(vm.iStory).getParagraphs().size()) {
            // Fail fast!
            Log.d("READ NEXT ERROR", "@ " +
                    vm.iStory + ", " +
                    vm.iParagraph);
            return;
        }

        // Everything's legit, let's read
        read(vm.iStory, vm.iParagraph);
    }

    /**
     * This method reads the 'previous' page or paragraph of the story
     */
    public void readPrevious() {

        // Decrement paragraph
        vm.iParagraph--;

        // See if it's the previous story's paragraph.
        // If so decrement story
        if (vm.iParagraph < 0) {
            vm.iStory--;
        }

        // See if we're reaching 'below' the first story
        // If so, set it to the highest story
        if (vm.iStory < 0) {
            vm.iStory = storyBooks.size() - 1;
        }

        // If it's the previous story's paragraph
        // If so, set it to the last paragraph of previous story
        if (vm.iParagraph < 0) {
            vm.iParagraph = storyBooks.get(vm.iStory).getParagraphs().size() - 1;
        }

        // If paragraph size is -1, we know we're in trouble
        if (vm.iParagraph < 0) {
            // Fail fast!
            Log.d("READ PREVIOUS ERROR", "@ " +
                    vm.iStory + ", " +
                    vm.iParagraph);
            return;
        }

        // Everything's legit, let's read
        read(vm.iStory, vm.iParagraph);
    }

    /**
     * This method is used to get the view of a word based on its index
     * @param iWord index of word to return
     * @return View of word
     */
    public View getWord(int iWord) {
        return getWord(iWord, vm.iSentence);
    }

    /**
     * This method is used to get the view of a word based on its index
     * @param iWord index of word to return
     * @return View of word
     */
    public View getWord(int iWord, int iSentence) {
        View view = null;

        // Get sentence
        StoryBookSentence storyBookSentence =
                storyBooks
                        .get(vm.iStory)
                        .getParagraphs()
                        .get(vm.iParagraph)
                        .getSentences()
                        .get(iSentence);

        // Variables to hold number of words in a line of a sentence
        int viewCount;

        // Get adjusted word index
        // For example, if I get the 5th word of a sentence,
        // and that sentence covers two lines,
        // the fifth word may only be the 2nd word of the second line.
        // Then the index is '1' instead of '4' for the 5th word
        int iWordAdjusted = iWord;

        // Loop through each line of the sentence
        for (int i = 0; i < storyBookSentence.getLineIndexes().size(); i++) {

            // Get line of words of sentence
            LinearLayout line = (LinearLayout) sentenceLines
                    .getChildAt(storyBookSentence
                            .getLineIndexes()
                            .get(i));

            // Ensure there are no 'null' lines
            // That would be a mistake
            if (line == null) {
                Log.e("GET WORD ERROR", "LINE MISSING (" +
                        vm.iStory + ", " +
                        vm.iParagraph + ", " +
                        iSentence + ")");
                return null;
            }

            // Get number of views (words) in a line
            viewCount = line.getChildCount();

            // If the adjusted word index is within the number of words in the line
            // We know the adjusted word is within the line
            // Ie. If index is 3, and I have 6 words in the line,
            // then I know that the word is within the line.
            // So breakout the loop, as I've found the word
            if (iWordAdjusted < viewCount) {
                view = line.getChildAt(iWordAdjusted);
                break;
            }

            // Remove the number of words from the previous sentence
            // from the adjusted index
            iWordAdjusted -= viewCount;

            // Check if the adjusted index does not make sense
            if (iWordAdjusted < 0) {
                Log.e("GET WORD ERROR", "INVALID WORD INDEX (" +
                        vm.iStory + ", " +
                        vm.iParagraph + ", " +
                        iSentence + ", " +
                        iWord + ")");
                return null;
            }
        }

        return view;
    }


    public void addOnArrowTouchListener(final ImageButton arrow) {
        arrow.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    arrow.setColorFilter(Color.argb(125, 0, 255, 0));
                    break;
                case MotionEvent.ACTION_UP:
                    arrow.setColorFilter(null);
                    arrow.performClick();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Override
    public void onPauseEvent() {
        enableTouchScreen();
    }

    @Override
    public void onResumeEvent() {
        disableTouchScreen();
    }

    @Override
    public void onStopEvent() {

    }
}
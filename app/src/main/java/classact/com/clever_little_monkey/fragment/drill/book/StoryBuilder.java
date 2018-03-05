package classact.com.clever_little_monkey.fragment.drill.book;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.database.model.StoryWord;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.LiveMediaPlayer;

/**
 * Created by hcdjeong on 2017/12/04.
 * Story builder YEAH!!
 */

public class StoryBuilder {

    private final Context context;

    public StoryBuilder(Context context) {
        this.context = context;
    }

    public List<LinearLayout> sentence(LiveMediaPlayer mediaPlayer,
                                       List<StoryWord> words,
                                       float wordHeight,
                                       float leftScreenMargin,
                                       float rightScreenMargin) {

        final float WORD_GAP = wordHeight/3.55f;
        float wordGap = WORD_GAP;

        // Setup linear layout
        List<LinearLayout> sentenceLines = new ArrayList<>();

        PlayableLinearLayout sentenceLine = createLinearLayout();

        int lineWidth = 0;

        // Get display metrics
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // Get screen density (will be used below)
        float screenDensity = displayMetrics.density;
        // Get screen width (will be used below)
        float screenWidth = displayMetrics.widthPixels
                - (leftScreenMargin * screenDensity)
                - (rightScreenMargin * screenDensity);

        // Hold composite
        PlayableLinearLayout composite = null;

        // Hold composite width
        int compositeWidth = 0;

        // Hold composite left margin
        int compositeLeftMargin = 0;

        // Hold composite sound
        String compositeSound = null;

        for (int i = 0; i < words.size(); i++) {

            StoryWord word = words.get(i);

            // Is it used
            if (!word.use) {
                continue;
            }

            // Determine word gap
            // If it's first word, remove gap. Otherwise, keep word gap value
            wordGap = (i == 0) ? 0 : wordGap;

            // Prepare variables to hold word text or word image
            String wordText = word.word;
            int wordImage = 0;
            boolean isImage = false;
            View view;
            int viewWidth;

            // Show a word image?
            if (word.isImage && word.imageFile != null) {
                wordImage = FetchResource.imageId(context, word.imageFile);
                isImage = (wordImage > 0);
            }

            // If it's an image
            if (isImage) {
                // Create new image view
                PlayableImageView imageView = new PlayableImageView(context);
                // Set image resource
                imageView.setImageResource(wordImage);
                // Get drawable of image
                Drawable imageDrawable = context.getResources().getDrawable(wordImage, null);
                // Get width-height ratio of image
                float imageWidthHeightRatio =
                        (float) imageDrawable.getIntrinsicWidth() /
                        (float) imageDrawable.getIntrinsicHeight();
                // Create new layout params
                MarginLayoutParams imageViewLayoutParams = createLayoutParams();
                // Calculate image length (a side) - multiply by screen density
                float imageHeight = wordHeight * screenDensity;
                // Calculate width of image
                float imageWidth = (int) (imageHeight * imageWidthHeightRatio);
                // Set image view width (based on previously calculated width-height ratio)
                imageViewLayoutParams.width = (int) imageWidth;
                // Set image view height
                imageViewLayoutParams.height = (int) imageHeight;
                // Set new layout params for image view
                imageView.setLayoutParams(imageViewLayoutParams);
                // Assign image view to view
                view = imageView;
                // Add image width to line width
                viewWidth = (int) imageWidth;
                // Set sound if exists
                if (word.soundFile != null) {
                    imageView.setSound(word.soundFile);
                }

            } else {
                // Create new text view
                PlayableTextView textView = new PlayableTextView(context);
                // Set text of text view
                textView.setText(wordText);
                // Set typeface (to edu_aid font)
                textView.setTypeface(ResourcesCompat.getFont(context, R.font.edu_aid));
                // Set text color
                textView.setTextColor(Color.DKGRAY);
                // Set text size
                textView.setTextSize(wordHeight);
                // Create new layout params
                MarginLayoutParams textViewLayoutParams = createLayoutParams();
                // Set new layout params for text view
                textView.setLayoutParams(textViewLayoutParams);
                // Assign text view to view
                view = textView;
                // Measure text view width
                textView.measure(0, 0);
                // Add text view width to line width
                viewWidth = textView.getMeasuredWidth();
                // Set sound if exists
                if (word.soundFile != null) {
                    textView.setSound(word.soundFile);
                }
            }

            // Calculate left margin (for word gap)
            int leftMargin = (wordGap == 0) ? 0 : (int) (wordGap * screenDensity);

            // Is the word part of a composite?
            // First check if a composite has been created
            if (composite == null) {
                // No composite has been created
                // Now check if a componsite needs to be created
                if (word.combineRight) {
                    // Word is part of a composite
                    // Create the composite
                    composite = createLinearLayout();
                    // Add word gap to composite
                    addWordGap(composite, leftMargin);
                    // Add view to composite
                    composite.addView(view);
                    // Set composite width to view width
                    compositeWidth = viewWidth;
                    // Set composite left margin to left margin
                    compositeLeftMargin = leftMargin;
                    // Set composite sound (if exists and has not been set)
                    if (compositeSound == null && word.soundFile != null) {
                        compositeSound = word.soundFile;
                    }
                } else {
                    // Is a non-composite
                    // No composite is required
                    // Add word gap to view
                    addWordGap(view, leftMargin);
                    // Add word highlight on touch
//                    addOnTouchListener(view, mediaPlayer, word.soundFile);

                    // Increase line width by view width
                    lineWidth += viewWidth;
                    // Increase line width by left margin
                    lineWidth += leftMargin;

                    // Add a new line?
                    if (lineWidth > screenWidth) {
                        // Reset line width
                        lineWidth = viewWidth;
                        // Add sentence line to sentence lines
                        // But first...
                        // Swap all sentence line children
                        LinearLayout sentenceLineCopy = swapFrom(sentenceLine);
                        // Add sentence line copy to sentence lines
                        sentenceLines.add(sentenceLineCopy);
                        // Reset sentence line
                        sentenceLine = createLinearLayout();
                        // Remove word gap from view
                        addWordGap(view, 0);
                    }

                    // Add view to linear layout
                    sentenceLine.addView(view);
                }
            } else {
                // A composite has already been created
                // Add the current view to the composite
                composite.addView(view);
                // Increase composite width by view width
                // Note: NO NEED to increase composite left margin
                compositeWidth += viewWidth;
                // Set composite sound (if exists and has not been set)
                if (compositeSound == null && word.soundFile != null) {
                    compositeSound = word.soundFile;
                }

                // Is this the last word of the composite?
                if (!word.combineRight) {
                    // No more views to combine
                    // It is the last word of the composite
                    // Swap children to a copy
                    PlayableLinearLayout compositeCopy = swapFrom(composite);
                    // Nullify composite
                    composite = null;
                    // Set sound for composite copy
                    compositeCopy.setSound(compositeSound);
                    // Add composite highlight on touch
//                    addOnTouchListener(compositeCopy, mediaPlayer, compositeSound);

                    // Increase line width by composite width
                    lineWidth += compositeWidth;
                    // Increase line width by composite left margin
                    lineWidth += compositeLeftMargin;

                    // Add a new line?
                    if (lineWidth > screenWidth) {
                        // Reset line width
                        lineWidth = compositeWidth;
                        // Add sentence line to sentence lines
                        // But first...
                        // Swap all sentence line children
                        LinearLayout sentenceLineCopy = swapFrom(sentenceLine);
                        // Add sentence line copy to sentence lines
                        sentenceLines.add(sentenceLineCopy);
                        // Reset sentence line
                        sentenceLine = createLinearLayout();
                        // Remove word gap from composite copy
                        addWordGap(compositeCopy, 0);
                    }

                    // Add composite copy to linear layout
                    sentenceLine.addView(compositeCopy);

                    // Set composite sound to null
                    compositeSound = null;
                }
            }

//            // Log line width
//            Log.d("Line width (" + (sentenceLines.size() + 1) + ") " + wordText, "" + lineWidth + ", " + viewWidth);

            // Combine with next word?
            wordGap = (word.combineRight) ? 0 : WORD_GAP;
        }

        // Add final sentence line to sentence lines
        sentenceLines.add(sentenceLine);

        // Return
        return sentenceLines;
    }

    private PlayableLinearLayout createLinearLayout() {
        PlayableLinearLayout linearLayout = new PlayableLinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(0, 0, 0, 0);
        return linearLayout;
    }

    private MarginLayoutParams createLayoutParams() {
        return new MarginLayoutParams(
                MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT
        );
    }

    private PlayableLinearLayout swapFrom(PlayableLinearLayout linearLayout) {
        // Create replacer linear layout - will hold all children of original
        PlayableLinearLayout replacerLinearLayout = createLinearLayout();
        // Loop through original
        while (linearLayout.getChildCount() > 0) {
            // Get child
            View childView = linearLayout.getChildAt(0);
            // Swap child
            linearLayout.removeView(childView);
            replacerLinearLayout.addView(childView);
        }
        // Swap padding
        replacerLinearLayout.setPadding(
                linearLayout.getPaddingLeft(),
                linearLayout.getPaddingTop(),
                linearLayout.getPaddingRight(),
                linearLayout.getPaddingBottom());
        return replacerLinearLayout;
    }

    private void addWordGap(View view, int wordGap) {
        if (view instanceof ImageView || view instanceof TextView) {
            // Update margin
            // Get view layout params
            MarginLayoutParams viewLayoutParams = (MarginLayoutParams) view.getLayoutParams();
            // Add word gap
            viewLayoutParams.leftMargin = wordGap;
            // Set updated layout params for view
            view.setLayoutParams(viewLayoutParams);
        } else if (view instanceof LinearLayout) {
            // Update padding
            view.setPadding(wordGap, 0, 0, 0);
        }
    }

//    private void addOnTouchListener(View view, LiveMediaPlayer mediaPlayer, String sound) {
//        // Log.d("Sound", "Sound is: " + sound);
//        view.setOnTouchListener((v, event) -> {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    if (view instanceof TextView) {
//                        ((TextView) view).setTextColor(Color.RED);
//                    } else if (view instanceof LinearLayout) {
//                        for (int i = 0; i < ((LinearLayout) view).getChildCount(); i++) {
//                            View childView = ((LinearLayout) view).getChildAt(i);
//                            if (childView instanceof TextView) {
//                                ((TextView) childView).setTextColor(Color.RED);
//                            }
//                        }
//                    }
//                    // Play sound
//                    if (mediaPlayer != null) {
//                        try {
//                            String soundPath = FetchResource.sound(context, sound);
//                            mediaPlayer.reset();
//                            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
//                            mediaPlayer.setOnPreparedListener((mp) -> {
//                                mediaPlayer.start();
//                            });
//                            mediaPlayer.prepare();
//                        } catch(Exception ex){
//                            ex.printStackTrace();
//                        }
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    if (view instanceof TextView) {
//                        ((TextView) view).setTextColor(Color.DKGRAY);
//                    } else if (view instanceof LinearLayout) {
//                        for (int i = 0; i < ((LinearLayout) view).getChildCount(); i++) {
//                            View childView = ((LinearLayout) view).getChildAt(i);
//                            if (childView instanceof TextView) {
//                                ((TextView) childView).setTextColor(Color.DKGRAY);
//                            }
//                        }
//                    }
//                    view.performClick();
//                    break;
//                default:
//                    break;
//            }
//            return true;
//        });
//    }

    public class PlayableImageView extends AppCompatImageView {

        private Context context;
        private String sound;

        public PlayableImageView(Context context) {
            super(context);
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getSound() {
            return sound;
        }
    }

    public class PlayableTextView extends AppCompatTextView {

        private Context context;
        private String sound;

        public PlayableTextView(Context context) {
            super(context);
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getSound() {
            return sound;
        }
    }

    public class PlayableLinearLayout extends LinearLayout {

        private Context context;
        private String sound;

        public PlayableLinearLayout(Context context) {
            super(context);
        }

        public void setColor(int color) {
            for (int i = 0; i < this.getChildCount(); i++) {
                View view = this.getChildAt(i);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(color);
                }
            }
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getSound() {
            return sound;
        }
    }
}
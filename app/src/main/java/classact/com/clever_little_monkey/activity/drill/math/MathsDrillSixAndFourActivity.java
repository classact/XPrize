package classact.com.clever_little_monkey.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.utils.FetchResource;

public class MathsDrillSixAndFourActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {

    @BindView(R.id.activity_maths_drill_six_and_four) ConstraintLayout parentView;
    @BindView(R.id.monkey) ImageView monkey;
    @BindView(R.id.monkeys_items) RelativeLayout itemsReceptacle;
    @BindView(R.id.damas_items) RelativeLayout objectsContainer;
    @BindView(R.id.equation) LinearLayout equation;
    @BindView(R.id.equation_one) ImageView equationNumberOne;
    @BindView(R.id.equation_two) ImageView equationNumberTwo;
    @BindView(R.id.equation_answer) ImageView equationAnswer;
    @BindView(R.id.equation_sign) ImageView equationSign;
    @BindView(R.id.equation_equals) ImageView equationEqualsSign;
    @BindView(R.id.number_01) ImageView numberOne;
    @BindView(R.id.number_02) ImageView numberTwo;
    @BindView(R.id.number_03) ImageView numberThree;

    @BindView(R.id.g_h_number_01) Guideline gh01;
    @BindView(R.id.g_h_number_02) Guideline gh02;
    @BindView(R.id.g_h_number_03) Guideline gh03;
    @BindView(R.id.g_v_number_01) Guideline gv01;
    @BindView(R.id.g_v_number_02) Guideline gv02;
    @BindView(R.id.g_v_number_03) Guideline gv03;
    @BindView(R.id.g_h_equation) Guideline ghEq;
    @BindView(R.id.g_v_equation) Guideline gvEq;

    private TextView eq;
    private TextView eqFull;
    private TextView one;
    private TextView two;
    private TextView three;

    private int draggedItems = 0;
    private int targetItems = 0;
    private int segment = 0;

    private boolean dragEnabled;
    private boolean touchEnabled;

    private View lastWrongView;

    private MathDrill06EViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_four);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06EViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();
        init();

        playSound(vm.getInstructions().get(0), this::sayNumber);
    }

    private void init(){
        try {
            // Hide equation components
            ez.hide(equationNumberOne, equationSign, equationNumberTwo, equationEqualsSign, equationAnswer);

            // Hide numbers
            ez.hide(numberOne, numberTwo, numberThree);

            // Add on drag listener to Monkey
            monkey.setOnDragListener(this);

            segment = 1;
            setupObjects();
            setupNumbers();
            setupEquation();

            touchEnabled = false;
            dragEnabled = false;
            draggedItems = 0;
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupObjects(){
        try {
            int itemResId = fetch.imageId(vm.getLargerNumber().getImageName());
            int count = vm.getLargerNumber().getNumberOfImages();
            targetItems = vm.getCorrectNumber().getNumber();

            for(int i = 0; i < count; i++){
                ImageView iv = (ImageView) objectsContainer.getChildAt(i);
                loadImage(iv, itemResId);
                iv.setOnTouchListener(this);
            }
        }
        catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupNumbers(){
        try {
            one = new TextView(this);
            two = new TextView(this);
            three = new TextView(this);

            // Set font
            one.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
            two.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
            three.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));

            // One Txt
            one.setTextSize(120f);
            one.setTextColor(Color.BLACK);

            // Two Txt
            two.setTextSize(120f);
            two.setTextColor(Color.BLACK);

            // Three Txt
            three.setTextSize(120f);
            three.setTextColor(Color.BLACK);

            // Add to layout
            parentView.addView(one);
            parentView.addView(two);
            parentView.addView(three);

            // One LP
            ConstraintLayout.LayoutParams oneLP = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            oneLP.topToTop = gh01.getId();
            oneLP.leftToLeft = gv01.getId();
            oneLP.rightToRight = gv01.getId();
            oneLP.bottomToBottom = gh01.getId();
            one.setLayoutParams(oneLP);

            // Two LP
            ConstraintLayout.LayoutParams twoLP = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            twoLP.topToTop = gh02.getId();
            twoLP.leftToLeft = gv02.getId();
            twoLP.rightToRight = gv02.getId();
            twoLP.bottomToBottom = gh02.getId();
            two.setLayoutParams(twoLP);

            // Three LP
            ConstraintLayout.LayoutParams threeLP = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            threeLP.topToTop = gh03.getId();
            threeLP.leftToLeft = gv03.getId();
            threeLP.rightToRight = gv03.getId();
            threeLP.bottomToBottom = gh03.getId();
            three.setLayoutParams(threeLP);

            // Set values
            TextView[] numbers = { one, two, three };
            int n = vm.getNumbers().size();

            // Guard
            if (n < 3) {
                Toast.makeText(this, "Error retrieving numbers", Toast.LENGTH_LONG).show();
                throw new Exception("Math Drill 6E Numbers Error");
            }

            // Loop through numbers
            for (int i = 0; i < n; i++) {
                TextView t = numbers[i];
                final int index = i;
                if (i == vm.getCorrectIndex()) {
                    t.setOnClickListener((v) -> onCorrectNumberClicked(t, index));
                } else {
                    t.setOnClickListener((v) -> onWrongNumberClicked(t, index));
                }
                t.setText(String.valueOf(vm.getNumber(i).getNumber()));
            }

            // 'Gray' and hide em
            ez.alpha(0.2f, one, two, three);
            ez.hide(one, two, three);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void onCorrectNumberClicked(View v, int index) {
        try {
            if (touchEnabled) {
                // Uncolor last wrong view
                if (lastWrongView != null && v != lastWrongView) {
                    unHighlight(lastWrongView);
                }

                // Disable touch
                touchEnabled = false;

                // Highlight as correct
                highlightCorrect(v);

                // Play sound number
                playSound(vm.getNumber(index).getSound(),
                        () -> {
                            starWorks.play(this, v); // Play starworks
                            playSound(FetchResource.positiveAffirmation(this), // Positive affirmation
                            () -> {
                                ez.fadeHide(getLifecycle(), 400, one).start();
                                ez.fadeHide(getLifecycle(), 400, two).start();
                                ez.fadeHide(getLifecycle(), 400, three).start();

                                // Update equation
                                ez.fadeShow(getLifecycle(), 400, eqFull).start();

                                // Play equation
                                handler.delayed(
                                    () -> playSound(vm.getEquationSound(),
                                        () -> {
                                            // Play correct number
                                            handler.delayed(
                                                () -> playSound(vm.getCorrectNumber().getSound(),
                                                    () -> {
                                                        // End
                                                        finish();
                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    }),500);
                                        }), 500);
                            });
                        });

            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void onWrongNumberClicked(View v, int index) {
        try {
            if (touchEnabled) {
                // Uncolor last wrong view
                if (lastWrongView != null && v != lastWrongView) {
                    unHighlight(lastWrongView);
                }

                // Highlight as wrong, play number sound and play negative affirmation
                lastWrongView = v;
                highlightWrong(v);
                playSound(vm.getNumber(index).getSound(),
                    () -> playSound(FetchResource.negativeAffirmation(this),
                    () -> unHighlight(v)));
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void setupEquation() {
        eq = new TextView(this);
        eq.setTextSize(120f);
        eq.setTextColor(Color.BLACK);

        eqFull = new TextView(this);
        eqFull.setTextSize(120f);
        eqFull.setTextColor(Color.BLACK);

        // Set typeface
        eq.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        eqFull.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));

        // Add to layout
        parentView.addView(eq);
        parentView.addView(eqFull);

        // Adjust eq guideline
        ez.guide.setPercentage(ghEq, .91f);
        ez.guide.setPercentage(gvEq, .2f);

        // EQ LP
        ConstraintLayout.LayoutParams eqLP = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        eqLP.topToTop = ghEq.getId();
        eqLP.leftToRight = gvEq.getId();
        eqLP.bottomToBottom = ghEq.getId();
        eq.setLayoutParams(eqLP);

        // EQ Full LP
        ConstraintLayout.LayoutParams eqFullLP = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        eqFullLP.topToTop = ghEq.getId();
        eqFullLP.leftToRight = gvEq.getId();
        eqFullLP.bottomToBottom = ghEq.getId();
        eqFull.setLayoutParams(eqFullLP);

        // Set Equation sum
        String sum = "" + vm.getLargerNumber().getNumberOfImages() +
                " ~ " + vm.getSmallerNumber().getNumberOfImages() +
                " = ";
        eq.setText(sum);

        sum = sum + vm.getCorrectNumber().getNumber();
        eqFull.setText(sum);

        // Hide equation
        ez.alpha(0f, eqFull);
        ez.hide(eq, eqFull);
    }

    private void sayNumber(){
        try{
            playSound(vm.getLargerNumber().getNumberOfImagesSound(), this::sayHeGives);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private void sayHeGives(){
        try{
            playSound(vm.getInstructions().get(1), () -> {
                segment = 2;
                sayNumberToGive();
            });
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayNumberToGive(){
        try{
            playSound(vm.getSmallerNumber().getNumberOfImagesSound(), () -> {
                if (segment == 2) {
                    sayToMonkey();
                } else {
                    sayToMonkeySpace();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkey(){
        try{
            playSound(vm.getInstructions().get(2), this::sayDrag);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayDrag(){
        try{
            playSound(vm.getInstructions().get(3), () -> {
                segment = 3;
                sayNumberToGive();
            });
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkeySpace(){
        try{
            playSound(vm.getInstructions().get(4), () -> dragEnabled = true);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void placeOnTable(){
        ImageView destination = (ImageView) itemsReceptacle.getChildAt(draggedItems - 1);
        loadImage(destination, fetch.imageId(vm.getLargerNumber().getImageName()));
        destination.setVisibility(View.VISIBLE);
    }

    private void showEquation (){
        try{
            ez.show(eq, eqFull);
            playSound(vm.getEquationSound(), this::playTouch);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playTouch(){
        try{
            ez.show(one, two, three);
            playSound(vm.getInstructions().get(5), () -> {
                ez.alpha(1.0f, one, two, three);
                touchEnabled = true;
            });
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(null, dragShadow, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    return true;
                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
//                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//                        return true;
//                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    if (dragEnabled) {
                        if (draggedItems < targetItems) {
                            draggedItems++;
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {

                        if ( draggedItems <= targetItems) {
                            placeOnTable();
                        }

                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        String numberSound = vm.getNumberPool().get(draggedItems).getSound();

                        if (draggedItems >= targetItems) {
                            dragEnabled = false;
                            playSound(numberSound, () -> {
                                starWorks.play(this, monkey);
                                playSound(FetchResource.positiveAffirmation(context), this::showEquation);
                            });
                        } else {
                            playSound(numberSound, null);
                        }
                    } else {
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                    }
                    return true;
                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
    }
}
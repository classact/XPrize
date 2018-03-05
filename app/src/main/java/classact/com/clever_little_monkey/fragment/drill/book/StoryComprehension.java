package classact.com.clever_little_monkey.fragment.drill.book;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import classact.com.clever_little_monkey.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryComprehension extends Fragment {


    public StoryComprehension() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_comprehension, container, false);
    }

}

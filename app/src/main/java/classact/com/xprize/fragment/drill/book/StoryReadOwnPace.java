package classact.com.xprize.fragment.drill.book;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import classact.com.xprize.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryReadOwnPace extends Fragment {


    public StoryReadOwnPace() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_read_own_pace, container, false);
    }

}

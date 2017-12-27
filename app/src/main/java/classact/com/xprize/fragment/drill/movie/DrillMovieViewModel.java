package classact.com.xprize.fragment.drill.movie;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.model.Movie;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/09/12.
 * Yes!!
 */

public class DrillMovieViewModel extends DrillViewModel {

    private String background;
    private Movie movie;

    @Inject
    public DrillMovieViewModel(Bus bus) {
        super(bus);
        background = "";
        movie = null;
    }

    @Override
    public DrillMovieViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public DrillMovieViewModel prepare(Context context) {

        if (movie == null) {
            // Initialize DB Helper
            DbHelper dbHelper = DbHelper.getDbHelper(context);

            // Open database
            dbHelper.openDatabase();

            // Get db background
            background = "bg_movie";

            // Get unit section drill
            UnitSectionDrill unitSectionDrill =
                    UnitSectionDrillHelper.getUnitSectionDrillInProgress(
                            dbHelper.getReadableDatabase(), 1);

            // Get Movie Id
            int movieId = MovieDao.getMovieId(dbHelper.getReadableDatabase(), unitSectionDrill.getUnitSectionDrillId());

            // Get Movie
            movie = MovieDao.getMovie(dbHelper.getReadableDatabase(), movieId);

            // Close database
            dbHelper.close();
        }
        return this;
    }

    public String getBackground() {
        return background;
    }

    public Movie getMovie() {
        return movie;
    }
}
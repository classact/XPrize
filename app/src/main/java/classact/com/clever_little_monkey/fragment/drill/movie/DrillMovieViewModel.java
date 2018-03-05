package classact.com.clever_little_monkey.fragment.drill.movie;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import javax.inject.Inject;

import classact.com.clever_little_monkey.database.DbHelper;
import classact.com.clever_little_monkey.database.helper.UnitSectionDrillHelper;
import classact.com.clever_little_monkey.database.model.Movie;
import classact.com.clever_little_monkey.database.model.UnitSectionDrill;
import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/09/12.
 * Yes!!
 */

public class DrillMovieViewModel extends DrillViewModel {

    private String background;
    private Movie movie;
    private UnitSectionDrillHelper unitSectionDrillHelper;

    @Inject
    public DrillMovieViewModel(Bus bus, UnitSectionDrillHelper unitSectionDrillHelper) {
        super(bus);
        background = "";
        movie = null;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
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
                    unitSectionDrillHelper.getUnitSectionDrillInProgress(
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
package classact.com.xprize.fragment.drill.movie;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import classact.com.xprize.database.model.Movie;
import classact.com.xprize.database.model.Story;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public class MovieDao {

    public static Movie getMovie(SQLiteDatabase db,
                                 int movieId) {

        Movie movie = null;

        Cursor cursor = db.rawQuery(
                "SELECT m.* " +
                        "FROM tbl_Movie m " +
                        "WHERE m.id = " + movieId + " " +
                        "ORDER BY m.id ASC", null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                movie = new Movie(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("video_file")),
                        cursor.getString(cursor.getColumnIndex("subtitle_file")));
            }
            cursor.close();
        }
        return movie;
    }

    public static int getMovieId(SQLiteDatabase db,
                                 int unitSectionDrillId) {

        int movieId = -1;

        Cursor cursor = db.rawQuery(
                "SELECT movie_id " +
                        "FROM tbl_UnitSectionDrillMovie usdm " +
                        "WHERE usdm.unit_section_drill_id = " + unitSectionDrillId + " " +
                        "ORDER BY usdm.id ASC", null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                movieId = cursor.getInt(cursor.getColumnIndex("movie_id"));
            }
            cursor.close();
        }
        return movieId;
    }
}

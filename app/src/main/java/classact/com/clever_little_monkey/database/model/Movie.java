package classact.com.clever_little_monkey.database.model;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public class Movie {
    public final int id;
    public final String name;
    public final String videoFile;
    public final String subtitleFile;

    public Movie(int id, String name, String videoFile, String subtitleFile) {
        this.id = id;
        this.name = name;
        this.videoFile = videoFile;
        this.subtitleFile = subtitleFile;
    }
}
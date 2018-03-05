package classact.com.clever_little_monkey.database;

/**
 * Created by hcdjeong on 2017/12/04.
 */

public interface DataStore {

    void create();
    void drop();
    void open();
    void close();
    void read();
    void write();
    void update();
    void delete();
}
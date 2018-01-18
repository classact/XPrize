package classact.com.xprize.database;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * Created by hcdjeong on 2018/01/05.
 * Db access class
 */

public abstract class DbAccessor {

    protected DbHelper dbHelper;

    protected DbAccessor(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Establish db connection
     * @return Returns true/false if db connection has been established
     */
    protected boolean dbOpen() {
        checkNotNull(dbHelper);
        try {
            // Test opening database
            dbHelper.openDatabase();

            // All good
            return true;

            // Otherwise
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    protected void dbClose() {
        checkNotNull(dbHelper);
        dbHelper.close();
    }
}

package classact.com.xprize.activity.menu.controller;

import classact.com.xprize.database.DbHelper;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public interface DatabaseQuery {
    public Object execute(DbHelper dbHelper);
}
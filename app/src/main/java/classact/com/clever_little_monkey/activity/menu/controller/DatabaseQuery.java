package classact.com.clever_little_monkey.activity.menu.controller;

import classact.com.clever_little_monkey.database.DbHelper;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public interface DatabaseQuery {
    public Object execute(DbHelper dbHelper);
}
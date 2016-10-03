package jessie_stam.jessiestam_pset4_jaar2_desktop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * To Do List App - DBHelper
 *
 * Jessie Stam
 * 10560599
 *
 * Creates a SQLite database in which to store to do items. It stores an item's id, title and
 * status.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "firstdb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "todo_table";

    // construct the class
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    // define status
    private String current_status;
    private String new_status;

    @Override
    public void onCreate(SQLiteDatabase dataBase) {

        // create the table, add id, todo_item and current_status
        String query = "CREATE TABLE " + TABLE + "(_id " + "INTEGER PRIMARY KEY, " + "todo_text TEXT, " + "current_status TEXT);";
        dataBase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {

        // drop table and create it again when application is updated
        dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(dataBase);
    }

    /**
     * Create new row from TodoItem
     */
    public void create(TodoItem todo_item) {

        // initialize database for writing
        SQLiteDatabase dataBase = getWritableDatabase();
        ContentValues values = new ContentValues();

        // add to-do item to the list and insert into table
        values.put("_id", todo_item.getId());
        values.put("todo_text", todo_item.getTitle());
        values.put("current_status", todo_item.getCurrentStatus());
        dataBase.insert(TABLE, null, values);
        dataBase.close();
    }

    /*
     * Read through the database and return database list
     */
    public ArrayList<HashMap<String, String>> read_item() {

        // initialize database for reading
        SQLiteDatabase dataBase = getReadableDatabase();

        // select values from the table
        String query = "SELECT _id, " + "todo_text, " + "current_status " + "FROM " + TABLE;

        ArrayList<HashMap<String, String>> todo_list = new ArrayList<>();
        Cursor cursor = dataBase.rawQuery(query, null);

        // for every item in the list, read values
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> todo_list_item = new HashMap<>();
                todo_list_item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
                todo_list_item.put("todo_text", cursor.getString
                        (cursor.getColumnIndex("todo_text")));
                todo_list_item.put("current_status", cursor.getString
                        (cursor.getColumnIndex("current_status")));
                todo_list.add(todo_list_item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        dataBase.close();

        // return database list
        return todo_list;
    }

    /*
     * Update database: change the status and check text and id
     */
    public void update(TodoItem todo_item) {

        // initialize database for writing
        SQLiteDatabase dataBase = getWritableDatabase();
        ContentValues values = new ContentValues();

        current_status = todo_item.getCurrentStatus();

        // change status
        switch(current_status) {
            case ("unfinished"):
                new_status = "finished";
                break;
            case ("finished"):
                new_status = "unfinished";
                break;
        }
        todo_item.setCurrentStatus(new_status);

        // add to-do item to list and add to the table
        values.put("todo_text", todo_item.getTitle());
        values.put("current_status", new_status);

        // change data in the database for specific id
        dataBase.update(TABLE, values, "_id = ? ", new String[] {String.valueOf(todo_item.getId())});
        dataBase.close();
    }

    /*
     * Delete item from the table
     */
    public void delete(int id) {

        // initialize database for writing
        SQLiteDatabase dataBase = getWritableDatabase();

        // delete to-do item from the table
        dataBase.delete(TABLE, " _id = ?", new String[] {String.valueOf(id)});
        dataBase.close();
    }
}

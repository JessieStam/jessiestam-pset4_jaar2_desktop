package jessie_stam.jessiestam_pset4_jaar2_desktop;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String todo_item;
    ArrayList<String> todo_list;
    EditText user_input;
    ListView screen_list;
    ArrayAdapter<String> todoAdapter;
    TodoManager todo_manager;
    DBHelper db_helper;
    ArrayList<HashMap<String, String>> db_list;
    String clicked_item;
    String current_status;
    String update_todo;
    ArrayList<TodoItem> item_list;
    String clicked_remove_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_input = (EditText) findViewById(R.id.user_todo_input);
        screen_list = new ListView(this);
        screen_list = (ListView) findViewById(R.id.todo_list_id);

        todo_list = new ArrayList<>();
        item_list = new ArrayList<>();

        todoAdapter = new ArrayAdapter<>
                (this, R.layout.listview_layout,R.id.listview_text, todo_list);

        // nieuwe DBHelper maken
        db_helper = new DBHelper(this);

        // constructor manager class aanroepen
        todo_manager = TodoManager.getOurInstance();



        /*
         * Check status of todo_item, change background color accordingly
         */
        screen_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // read SQLite database to get status of clicked item
                db_list = db_helper.read_item();

                // get name from clicked item
                clicked_item = (String) parent.getItemAtPosition(position);

//                Log.d("test", "item is clicked: " + clicked_item);

                // iterate over hashmaps in database list
                for (HashMap<String, String> hashmap : db_list) {
                    // iterate over entries in hashmap
                    for (Map.Entry<String, String> hashmap_entry : hashmap.entrySet()) {
                        // if clicked item name is in hashmap, save status of that hashmap

                        Log.d("onclick test", hashmap_entry.toString());

                        if (hashmap_entry.toString().equals("todo_text=" + clicked_item)) {
                            update_todo = hashmap.get("todo_text");
                            current_status = hashmap.get("current_status");

//                            Log.d("onclick test 2", "gets in loop 2");
//                            Log.d("status", current_status);
                        }
                    }
                }

//                Log.d("onclick test", "gets out of loop");
                if (current_status != null) {
                    changeItemColor(view, current_status);
                }

                db_helper.update(getTodoItem(clicked_item));
            }
        });

        /**
         * set long click listener for removing items
         */
        screen_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View string, int position, long id) {

                clicked_remove_item = (String) parent.getItemAtPosition(position);

                // remove the item at the touched position and update data
                todo_list.remove(position);
                todoAdapter.notifyDataSetChanged();

                int remove_id = getTodoItem(clicked_remove_item).getId();

                //remove title from the SQLite
                db_helper.delete(remove_id);

                return true;
            }
        });

    }

    /*
    * Adds an item to the list
    */
    public void addToListItem(View view) {

        // use adapter to put todo_list information to screen_list
        screen_list.setAdapter(todoAdapter);

        // get item for the list
        todo_item = user_input.getText().toString();

        Log.d("test", todo_item);

        // add user input to ListView
        todo_list.add(todo_item);

        // create new item
        TodoItem new_item = todo_manager.create_item(todo_item);

        // add TodoItem to list
        item_list.add(new_item);

        // refresh ListView
        todoAdapter.notifyDataSetChanged();

        // add item to the SQLite
        db_helper.create(new_item);

        // clear the input line after text is added
        user_input.getText().clear();
    }

    public void changeItemColor(View view, String status) {

        switch(status) {
            case ("unfinished"):
                view.setBackgroundColor(Color.GRAY);
                break;
            case ("finished"):
                view.setBackgroundColor(Color.WHITE);
                break;
        }

    }

//    public int getTodoItemId(String item_name) {
//
//        int id = 0;
//
//        for (TodoItem item : item_list) {
//            if (item.getTitle().equals(item_name)) {
//                int id = item.getId();
//            }
//        }
//        return id;
//    }

    public TodoItem getTodoItem(String item_name) {

        for (TodoItem item : item_list) {
            if (item.getTitle().equals(item_name)) {
                return item;
            }
        }
        return null;
    }
}

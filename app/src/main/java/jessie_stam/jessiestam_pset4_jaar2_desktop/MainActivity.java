package jessie_stam.jessiestam_pset4_jaar2_desktop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_input = (EditText) findViewById(R.id.user_todo_input);
        screen_list = new ListView(this);
        screen_list = (ListView) findViewById(R.id.todo_list_id);
        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todo_list);

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

                // iterate over hashmaps in database list
                for (HashMap<String, String> hashmap : db_list) {
                    // iterate over entries in hashmap
                    for (Map.Entry<String, String> hashmap_entry : hashmap.entrySet()) {
                        // if clicked item name is in hashmap, save status of that hashmap
                        if (hashmap_entry.toString().equals(clicked_item)) {
                            update_todo = hashmap.get("todo_text");
                            current_status = hashmap.get("current_status");
                        }
                    }
                }

                TodoItem todo_item = todo_manager.create_item(update_todo);
                db_helper.update(todo_item);
            }
        });

        /**
         * set long click listener for removing items
         */
        screen_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View string, int position, long id) {

                // remove the item at the touched position and update data
                todo_list.remove(position);
                todoAdapter.notifyDataSetChanged();

                //remove title from the SQLite
                db_helper.delete((int) id);

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

        // create new item
        TodoItem new_item = todo_manager.create_item(todo_item);

        // add user input to ListView
        todo_list.add(todo_item);

        // refresh ListView
        todoAdapter.notifyDataSetChanged();

        // clear the input line after text is added
        user_input.getText().clear();

        // add item to the SQLite
        db_helper.create(new_item);
    }
}

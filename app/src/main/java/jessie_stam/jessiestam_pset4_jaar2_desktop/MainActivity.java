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
    ArrayList<String> restore_todo_list;
    EditText user_input;
    ListView screen_list;
    ArrayAdapter<String> todoAdapter;
    TodoManager todo_manager;
    DBHelper db_helper;
    ArrayList<HashMap<String, String>> db_list;
    int clicked_item;
    String clicked_item_string;
    String current_status;
    String update_todo;
    ArrayList<TodoItem> item_list;
    String clicked_remove_item;
    ArrayList<Integer> clicked_item_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_input = (EditText) findViewById(R.id.user_todo_input);
        screen_list = new ListView(this);
        screen_list = (ListView) findViewById(R.id.todo_list_id);

        todo_list = new ArrayList<>();
        item_list = new ArrayList<>();
        clicked_item_list = new ArrayList<>();

        // nieuwe DBHelper maken
        db_helper = new DBHelper(this);

        todoAdapter = new ArrayAdapter<>
                (this, R.layout.listview_layout,R.id.listview_text, todo_list);

        screen_list.setAdapter(todoAdapter);

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

                // clicked item list contains all finished items
                if (clicked_item_list != null) {
                    Log.d("test", "we itereate over clicked items");
                    for (int pos : clicked_item_list) {

                        if (pos == id) {
                            Log.d("test", "item equals clicked item");
                            clicked_item_list.remove(pos);
                            break;
                        }
                    }
                    Log.d("test", "it is still in the loop");
                    clicked_item_list.add((int) id);
                }

                // iterate over hashmaps in database list
                for (HashMap<String, String> hashmap : db_list) {
                    // iterate over entries in hashmap
                    for (Map.Entry<String, String> hashmap_entry : hashmap.entrySet()) {
                        // if clicked item name is in hashmap, save status of that hashmap

                        Log.d("onclick test", hashmap_entry.toString());

                        if (hashmap_entry.toString().equals("todo_text=" + clicked_item)) {
                            update_todo = hashmap.get("todo_text");
                            current_status = hashmap.get("current_status");
                        }
                    }
                }

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
//        screen_list.setAdapter(todoAdapter);

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

    public TodoItem getTodoItem(String item_name) {

        for (TodoItem item : item_list) {
            if (item.getTitle().equals(item_name)) {
                return item;
            }
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save list objects
        outState.putStringArrayList("restore_todo_list", todo_list);
        outState.putParcelableArrayList("item_list", item_list);
        outState.putStringArrayList("clicked_item_list", clicked_item_list);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // restore list objects
        restore_todo_list = savedInstanceState.getStringArrayList("restore_todo_list");
        item_list = savedInstanceState.getParcelableArrayList("item_list");
        clicked_item_list = savedInstanceState.getStringArrayList("clicked_item_list");
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

        item_list.clear();
        todo_list.clear();

        db_list = db_helper.read_item();

        for (HashMap<String, String> hashmap : db_list) {
            // iterate over entries in hashmap
            String retrieved_id = hashmap.get("_id");
            String retrieved_title = hashmap.get("todo_text");
            String retrieved_status = hashmap.get("current_status");

            TodoItem new_item = todo_manager.create_item(retrieved_title);
            new_item.setId(Integer.parseInt(retrieved_id));
            new_item.setCurrentStatus(retrieved_status);


            item_list.add(new_item);

            todo_list.add(retrieved_title);
            todoAdapter.notifyDataSetChanged();
        }

    }


}

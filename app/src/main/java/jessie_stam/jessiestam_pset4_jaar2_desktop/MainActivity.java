package jessie_stam.jessiestam_pset4_jaar2_desktop;

import android.graphics.Color;
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

/**
 * To Do List App - MainActivity
 *
 * Jessie Stam
 * 10560599
 *
 * Lets users create a to do list. By pushing the button, a to do is added to the list. By clicking
 * on items in the list, the item can be checked of by changing its color. By long-cliking the
 * item it will be removed completely.
 */
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
    ArrayList<Integer> clicked_item_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // define objects
        user_input = (EditText) findViewById(R.id.user_todo_input);
        screen_list = new ListView(this);
        screen_list = (ListView) findViewById(R.id.todo_list_id);

        todo_list = new ArrayList<>();
        item_list = new ArrayList<>();
        clicked_item_list = new ArrayList<>();

        // initialize new DBHelper object
        db_helper = new DBHelper(this);

        // create new adapter and set to listview
        todoAdapter = new ArrayAdapter<>
                (this, R.layout.listview_layout,R.id.listview_text, todo_list);
        screen_list.setAdapter(todoAdapter);

        // construct new manager
        todo_manager = TodoManager.getOurInstance();

        // first time app is created, print instructions into listview
        if (todo_list.size() == 0) {
            todoAdapter.add("Use the box below to add items");
            todoAdapter.add("Tap the items in the list to check them off");
            todoAdapter.add("Long-click items to remove them");
        }

        /**
         * When item is clicked, its status is checked. Background color is changed accordingly
         */
        screen_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // read SQLite database to get status of clicked item
                db_list = db_helper.read_item();
                clicked_item = (String) parent.getItemAtPosition(position);

                // iterate over TodoItems and its entries in database list
                for (HashMap<String, String> hashmap : db_list) {
                    for (Map.Entry<String, String> hashmap_entry : hashmap.entrySet()) {

                        //when matching TodoItem text for clicked item is found, get status
                        if (hashmap_entry.toString().equals("todo_text=" + clicked_item)) {
                            update_todo = hashmap.get("todo_text");
                            current_status = hashmap.get("current_status");
                        }
                    }
                }

                // change the background color of clicked item
                if (current_status != null) {
                    changeItemColor(view, current_status);
                }

                // update status of clicked item in SQLite database
                db_helper.update(getTodoItem(clicked_item));
            }
        });

        /**
         * On long-click, remove item from list and SQLite database
         */
        screen_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View string, int position, long id) {

                clicked_remove_item = (String) parent.getItemAtPosition(position);

                // remove the item at the touched position and update data
                todo_list.remove(position);
                todoAdapter.notifyDataSetChanged();

                //remove title from the SQLite
                int remove_id = getTodoItem(clicked_remove_item).getId();
                db_helper.delete(remove_id);

                return true;
            }
        });

    }

    /*
    * When button is clicked, add item to the listview
    */
    public void addToListItem(View view) {

        // get item for the list and add to listview
        todo_item = user_input.getText().toString();
        todo_list.add(todo_item);
        todoAdapter.notifyDataSetChanged();

        // create new TodoItem object and add to list
        TodoItem new_item = todo_manager.create_item(todo_item);
        item_list.add(new_item);

        // add item to the SQLite
        db_helper.create(new_item);

        // clear the input line after text is added
        user_input.getText().clear();
    }

    /**
     * Changes the color of listview items
     */
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

    /**
     * Gets the matching TodoItem from the list for title String
     */
    public TodoItem getTodoItem(String item_name) {

        for (TodoItem item : item_list) {
            if (item.getTitle().equals(item_name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * When app is resumed, read SQLite table and put its contents back into the listview
     */
    @Override
    protected void onResume() {
        super.onResume();

        item_list.clear();
        todo_list.clear();

        // read SQLite database
        db_list = db_helper.read_item();

        // iterate over TodoItems in databases
        for (HashMap<String, String> hashmap : db_list) {

            //ave id, title and status
            String retrieved_id = hashmap.get("_id");
            String retrieved_title = hashmap.get("todo_text");
            String retrieved_status = hashmap.get("current_status");

            // recreate TodoItem and put in list
            TodoItem new_item = todo_manager.create_item(retrieved_title);
            new_item.setId(Integer.parseInt(retrieved_id));
            new_item.setCurrentStatus(retrieved_status);

            // put items back into the listview
            item_list.add(new_item);
            todo_list.add(retrieved_title);
            todoAdapter.notifyDataSetChanged();
        }
    }
}

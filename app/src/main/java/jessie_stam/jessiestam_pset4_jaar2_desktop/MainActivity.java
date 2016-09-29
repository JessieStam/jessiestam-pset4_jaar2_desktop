package jessie_stam.jessiestam_pset4_jaar2_desktop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String todo_item;
    ArrayList<String> todo_list;
    EditText user_input;
    ListView screen_list;
    ArrayAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_input = (EditText) findViewById(R.id.user_todo_input);
        screen_list = new ListView(this);
        screen_list = (ListView) findViewById(R.id.todo_list_id);
        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todo_list);

        // nieuew DBHelper maken
        // db_helper = new DBHelper(this);

        // constructer manager class aanroepen
        // todo_manager = TodoManager.getOurInstance();

    }
}

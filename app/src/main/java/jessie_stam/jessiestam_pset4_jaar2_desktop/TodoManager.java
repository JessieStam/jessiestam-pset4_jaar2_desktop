package jessie_stam.jessiestam_pset4_jaar2_desktop;

/**
 * Created by Jessie on 29-9-2016.
 */

public class TodoManager {

    // define counter for item id
    private Integer item_id = 0;

//    // define status
//    private String current_status = "unfinished";

    private static TodoManager ourInstance = null;

    // constructor
    public static TodoManager getOurInstance(){

        if (ourInstance == null) {
            ourInstance = new TodoManager();
        }
        return ourInstance;
    }

    /**
     * Create item for list
     */
    public TodoItem create_item (String todo_item_string) {

        // make new item
        TodoItem todo_item = new TodoItem(todo_item_string);

        // set title for the new item
        todo_item.setTitle(todo_item_string);

        // set id for the new item
        todo_item.setId(item_id);

        // set status for the new item
        todo_item.setCurrentStatus("unfinished");

        // update id for next item
        item_id += 1;

        return todo_item;
    }
}

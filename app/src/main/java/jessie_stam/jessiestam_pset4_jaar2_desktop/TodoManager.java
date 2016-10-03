package jessie_stam.jessiestam_pset4_jaar2_desktop;

/**
 * To Do List App - TodoManager
 *
 * Jessie Stam
 * 10560599
 *
 * Helps create new TodoItem objects.
 */

public class TodoManager {

    // define id and instance
    private Integer item_id = 0;
    private static TodoManager ourInstance = null;

    // construct the instance
    public static TodoManager getOurInstance(){

        if (ourInstance == null) {
            ourInstance = new TodoManager();
        }
        return ourInstance;
    }

    /**
     * Create TodoItem for list
     */
    public TodoItem create_item (String todo_item_string) {

        // create new item and set id, title and status
        TodoItem todo_item = new TodoItem(todo_item_string);
        todo_item.setTitle(todo_item_string);
        todo_item.setId(item_id);
        todo_item.setCurrentStatus("unfinished");

        // update id for next item
        item_id += 1;

        return todo_item;
    }
}

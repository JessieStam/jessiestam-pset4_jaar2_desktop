package jessie_stam.jessiestam_pset4_jaar2_desktop;

/**
 * Created by Jessie on 29-9-2016.
 */

public class TodoManager {

    // define counter for list id
    //private Integer list_id = 0;

    // define counter for item id
    private Integer item_id = 0;

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

        // update id for next item
        item_id += 1;

        return todo_item;
    }

//    /**
//     * Iterates over todo_list_list and checks if it's the list to be deleted
//     */
//    public void delete_item(String delete_item) {
//
//        for (int i = 0; i < todo_list.size(); i++) {
//
//            if (todo_list_list.get(i).toString().equals(delete_list)) {
//
//                // remove item from list list
//                todo_list_list.remove(todo_list_list.get(i));
//
//            }
//        }
//    }


}

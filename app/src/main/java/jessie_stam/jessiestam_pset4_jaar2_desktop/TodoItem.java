package jessie_stam.jessiestam_pset4_jaar2_desktop;

/**
 * Created by Jessie on 29-9-2016.
 */

public class TodoItem {

    // field for id
    private int id;

    //field for item
    private String todo_item;

    // constructor for todo_item
    public TodoItem(String new_string) {todo_item = new_string;}

    // constructor for id
    public TodoItem(Integer new_id) {id = new_id;}

    //methods for todo_item
    public String getTitle() {return todo_item;}

    public void setTitle(String new_title) {todo_item = new_title;}

    // methods for id
    public Integer getId() {return id;}

    public void setId(Integer new_id) {id = new_id;}
}

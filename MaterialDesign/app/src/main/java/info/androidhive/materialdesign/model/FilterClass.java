package info.androidhive.materialdesign.model;

/**
 * Created by Timbarnard on 25/10/2015.
 */
public class FilterClass {

    private String ID;
    private String name;

    public FilterClass(String ID, String name) {
        setID(ID);
        setName(name);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

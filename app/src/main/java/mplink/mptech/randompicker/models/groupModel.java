package mplink.mptech.randompicker.models;

/**
 * Created by Monkey Park on 3/24/2018.
 */

public class groupModel {

    public groupModel()
    {

    }

    public groupModel(String id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    private String id;

    private String groupName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}

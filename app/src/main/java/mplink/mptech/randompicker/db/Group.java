package mplink.mptech.randompicker.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Monkey Park on 3/6/2018.
 */

@Entity
public class Group {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String groupName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

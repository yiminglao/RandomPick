package mplink.mptech.randompicker.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Monkey Park on 3/6/2018.
 */
@Entity
public class Member {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String memberName;

    @ForeignKey(entity = Group.class,parentColumns = "id",childColumns = "gid",onDelete = CASCADE)
    private int gid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }
}

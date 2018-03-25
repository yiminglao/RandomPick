package mplink.mptech.randompicker.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Monkey Park on 3/6/2018.
 */

@Dao
public interface GroupDao {

    @Query("SELECT * FROM `group`")
    LiveData<List<Group>> getAll();

    @Query("SELECT * FROM `group` WHERE id = :id")
    Group getById(int id);

    @Insert
    void insert(Group group);

    @Insert
    void insert(Group... group);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);


}

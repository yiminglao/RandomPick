package mplink.mptech.randompicker.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mplink.mptech.randompicker.db.Member;

/**
 * Created by Monkey Park on 3/6/2018.
 */
@Dao
public interface MemberDao {

    @Query("SELECT * FROM member")
    LiveData<List<Member>> getAll();

    @Query("SELECT * FROM member WHERE id = :id")
    Member getByMemberId(int id);

    @Query("SELECT * FROM member WHERE gid = :gid")
    List<Member> getMemberByGroupId(int gid);

    @Insert
    void insert(Member member);

    @Insert
    void insert(Member... members);

    @Update
    void update(Member member);

    @Delete
    void delete(Member member);

    @Query("DELETE FROM member WHERE gid = :gid")
    void deleteAll(String gid);


}

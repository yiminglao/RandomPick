package mplink.mptech.randompicker.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Monkey Park on 3/6/2018.
 */
@Database(entities = {Group.class,Member.class},version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase Instance;

    public static AppDatabase getInstance(Context context)
    {
        if(Instance == null)
        {
            Instance = Room.databaseBuilder(context,AppDatabase.class,"randompick-database").build();
        }

        return Instance;
    }

    public abstract GroupDao groupDao();

    public abstract MemberDao memberDao();

}

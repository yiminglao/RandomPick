package mplink.mptech.randompicker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import mplink.mptech.randompicker.db.AppDatabase;
import mplink.mptech.randompicker.db.Group;

/**
 * Created by Monkey Park on 3/6/2018.
 */

public class AllGroupViewModel extends ViewModel {

    private LiveData<List<Group>> groupList;

    public LiveData<List<Group>> getAllGroup(Context context)
    {
        if(groupList == null)
        {
            groupList = AppDatabase.getInstance(context).groupDao().getAll();
        }

        return groupList;
    }

}

package mplink.mptech.randompicker.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import mplink.mptech.randompicker.db.AppDatabase;
import mplink.mptech.randompicker.db.Member;

/**
 * Created by peipei on 3/25/18.
 */

public class AllMemberViewModel extends ViewModel {

    private LiveData<List<Member>> memberList;

    public LiveData<List<Member>> getAllMember(Context context)
    {
        if(memberList == null)
        {
            memberList = AppDatabase.getInstance(context).memberDao().getAll();
        }

        return memberList;
    }

}

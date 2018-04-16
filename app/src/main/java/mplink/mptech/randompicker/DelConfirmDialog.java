package mplink.mptech.randompicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mplink.mptech.randompicker.models.GroupModel;
import mplink.mptech.randompicker.models.MemberModel;

/**
 * Created by Monkey Park on 3/8/2018.
 */

public class DelConfirmDialog extends DialogFragment {

    private GroupModel group;

    private MemberModel member;


    public DelConfirmDialog() {
        // Required empty public constructor
    }

    public static DialogFragment Instance(GroupModel group)
    {
        DelConfirmDialog dialogFragment = new DelConfirmDialog();
        dialogFragment.group = group;
        return dialogFragment;
    }

    public static DialogFragment Instance(MemberModel member)
    {
        DelConfirmDialog dialogFragment = new DelConfirmDialog();
        dialogFragment.member = member;
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_del_confirm_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.stat_sys_warning)
                .setTitle("Delete Confirm")
                .setMessage("Are you sure want to delete it?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(group != null)
                        {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                            String uid = sharedPreferences.getString(getString(R.string.userId),"");

                            mDatabase.child(uid).child(getString(R.string.group)).child(group.getId()).removeValue();
                            mDatabase.child(uid).child(getString(R.string.member)).child(group.getId()).removeValue();

                        }
                        else if(member != null)
                        {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                            String uid = sharedPreferences.getString(getString(R.string.userId),"");

                            mDatabase.child(uid).child(getString(R.string.member)).child(member.getGid()).child(member.getId()).removeValue();

                        }



                        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }
}

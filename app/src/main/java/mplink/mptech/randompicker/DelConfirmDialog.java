package mplink.mptech.randompicker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mplink.mptech.randompicker.db.AppDatabase;
import mplink.mptech.randompicker.db.Group;

/**
 * Created by Monkey Park on 3/8/2018.
 */

public class DelConfirmDialog extends DialogFragment {

    public Group group;


    public DelConfirmDialog() {
        // Required empty public constructor
    }

    public static DialogFragment Instance(Group group)
    {
        DelConfirmDialog dialogFragment = new DelConfirmDialog();
        dialogFragment.group = group;
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
                .setMessage("Are you sure want to delete it")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(getContext()).groupDao().delete(group);
                            }
                        }).start();
                        Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
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

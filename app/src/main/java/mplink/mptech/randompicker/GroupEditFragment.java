package mplink.mptech.randompicker;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mplink.mptech.randompicker.db.AppDatabase;
import mplink.mptech.randompicker.db.Group;
import mplink.mptech.randompicker.models.GroupModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupEditFragment extends Fragment {

    public Group group;

    private View root;

    private Toolbar toolbar;

    private Button btnCancel, btnSave;

    private TextInputEditText edtGroupName;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;



    private static final int RC_SIGN_IN = 123;

    public GroupEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_group_edit, container, false);

        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) root.findViewById(R.id.editToolBar);

        toolbar.setTitle("Edit Group");

        btnCancel = (Button) root.findViewById(R.id.btnCancel);

        btnSave = (Button) root.findViewById(R.id.btnSave);

        edtGroupName = (TextInputEditText) root.findViewById(R.id.edtGroupName);

        if(group != null)
        {
            edtGroupName.setText(group.getGroupName());
            toolbar.setTitle("Edit Group");
        }else
        {
            toolbar.setTitle("Add New Group");
        }

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = edtGroupName.getText().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference();

                if(groupName.trim().length() > 0)
                {

                    if(group != null)
                    {
                        //group.setGroupName(groupName);
                        GroupModel groups = new GroupModel(group.getGid(),groupName);
                        mDatabase.child(mAuth.getUid()).child(getString(R.string.group)).child(group.getGid()).setValue(groups);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(getContext()).groupDao().update(group);
                            }
                        }).start();
                        Toast.makeText(getActivity(), "Group is update", Toast.LENGTH_SHORT).show();
                    }else
                    {

                        String id = mDatabase.push().getKey();
                        GroupModel groups = new GroupModel(id,groupName);
                        mDatabase.child(mAuth.getUid()).child(getString(R.string.group)).child(id).setValue(groups);

                        final Group newGroup = new Group();

                        newGroup.setGroupName(groupName);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(getContext()).groupDao().insert(newGroup);
                            }
                        }).start();
                        Toast.makeText(getActivity(), "Group is Added", Toast.LENGTH_SHORT).show();
                        edtGroupName.setText("");
                    }




                }else
                {
                    Toast.makeText(getActivity(), "Please enter the group name!", Toast.LENGTH_SHORT).show();
                }






            }
        });


        return root;
    }



}

package mplink.mptech.randompicker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.acl.Group;
import java.util.List;
import java.util.Map;

import mplink.mptech.randompicker.models.GroupModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupEditFragment extends Fragment {

    public GroupModel group;

    private View root;

    private Toolbar toolbar;

    private Button btnCancel, btnSave;

    private TextInputEditText edtGroupName;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private List<GroupModel> groupList;

    private static final int RC_SIGN_IN = 123;

    public GroupEditFragment() {
        // Required empty public constructor
    }

    public void setGroupList(List<GroupModel> groupList)
    {
        this.groupList = groupList;
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

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        final String uid = sharedPreferences.getString(getString(R.string.userId),"");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = edtGroupName.getText().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference();

                if(groupName.trim().length() > 0)
                {
                    if(isNameExist(groupName))
                    {
                        Toast.makeText(getActivity(), "The Group name is already exist", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        if(group != null)
                        {

                            GroupModel groups = new GroupModel(group.getId(),groupName);
                            mDatabase.child(uid).child(getString(R.string.group)).child(group.getId()).setValue(groups);

                            Toast.makeText(getActivity(), "Group is update", Toast.LENGTH_SHORT).show();

                        }else
                        {
                            String id = mDatabase.push().getKey();
                            GroupModel groups = new GroupModel(id,groupName);
                            mDatabase.child(uid).child(getString(R.string.group)).child(id).setValue(groups);

                            Toast.makeText(getActivity(), "Group is Added", Toast.LENGTH_SHORT).show();
                            edtGroupName.setText("");
                        }
                    }

                    }


            }
        });


        return root;
    }

    private boolean isNameExist(String groupName)
    {
        boolean checkName = false;
        for(int i= 0; i<groupList.size(); i++)
        {
            if(groupList.get(i).getGroupName().equals(groupName))
            {
                checkName = true;
            }
        }
       return checkName;
    }



}

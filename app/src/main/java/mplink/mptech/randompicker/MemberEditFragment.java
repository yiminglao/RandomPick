package mplink.mptech.randompicker;


import android.app.Activity;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

import mplink.mptech.randompicker.models.GroupModel;
import mplink.mptech.randompicker.models.MemberModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberEditFragment extends Fragment {

    private View root;
    private Toolbar toolbar;

    private Button btnCancel,btnSave;

    public String groupId;

    public MemberModel member;

    private TextInputEditText edtMemberName;

    private DatabaseReference mDatabase;

    private List<MemberModel> memberModelList;

    public MemberEditFragment() {
        // Required empty public constructor
    }

    public void setMemberModelList(List<MemberModel> list)
    {
        this.memberModelList = list;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_member_edit, container, false);

        toolbar = (Toolbar) root.findViewById(R.id.editToolBar);

        toolbar.setTitle("Edit Group");

        btnCancel = (Button) root.findViewById(R.id.btnCancel);

        btnSave = (Button) root.findViewById(R.id.btnSave);

        edtMemberName = (TextInputEditText) root.findViewById(R.id.edtMemberName);

        if(member != null)
        {
            edtMemberName.setText(member.getMemberName());
            toolbar.setTitle("Edit Member");
        }else
        {
            toolbar.setTitle("Add New Member");
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

                String memberName = edtMemberName.getText().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference();

                if(memberName.trim().length() > 0)
                {
                    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    String uid = sharedPreferences.getString(getString(R.string.userId),"");

                    if(member != null)
                    {
                        MemberModel memberModel = new MemberModel(member.getId(),memberName,member.getGid());

                        mDatabase.child(uid).child(getString(R.string.member)).child(member.getGid()).child(member.getId()).setValue(memberModel);

                        Toast.makeText(getActivity(), "Member is update", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(memberModelList.isEmpty())
                        {
                            String id = mDatabase.push().getKey();

                            MemberModel memberModel = new MemberModel(id,memberName,groupId);
                            mDatabase.child(uid).child(getString(R.string.member)).child(groupId).child(id).setValue(memberModel);

                            final MemberModel newMember = new MemberModel();
                            newMember.setGid(id);
                            newMember.setMemberName(memberName);
                            newMember.setId(id);

                            Toast.makeText(getActivity(), "Member is Added", Toast.LENGTH_SHORT).show();
                            edtMemberName.setText("");
                        }
                        else
                        {
                            boolean checkName = true;
                            for(int i=0; i<memberModelList.size(); i++)
                            {
                                if(memberModelList.get(i).getMemberName().equals(memberName))
                                {
                                    Toast.makeText(getActivity(), "The member name is already exist", Toast.LENGTH_SHORT).show();
                                    checkName = false;
                                }
                            }
                            if(checkName)
                            {
                                String id = mDatabase.push().getKey();

                                MemberModel memberModel = new MemberModel(id,memberName,groupId);
                                mDatabase.child(uid).child(getString(R.string.member)).child(groupId).child(id).setValue(memberModel);

                                final MemberModel newMember = new MemberModel();
                                newMember.setGid(id);
                                newMember.setMemberName(memberName);
                                newMember.setId(id);

                                Toast.makeText(getActivity(), "Member is Added", Toast.LENGTH_SHORT).show();
                                edtMemberName.setText("");
                            }
                        }

                    }

                }else
                {
                    Toast.makeText(getActivity(), "Please enter the Member name!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return root;
    }

}

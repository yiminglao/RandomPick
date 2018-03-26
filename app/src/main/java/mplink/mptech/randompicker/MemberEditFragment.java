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

import mplink.mptech.randompicker.db.AppDatabase;
import mplink.mptech.randompicker.db.Group;
import mplink.mptech.randompicker.db.Member;
import mplink.mptech.randompicker.models.GroupModel;
import mplink.mptech.randompicker.models.MemberModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberEditFragment extends Fragment {

    private View root;
    private Toolbar toolbar;

    private Button btnCancel,btnSave;

    public Group group;

    public Member member;

    private TextInputEditText edtMemberName;

    private DatabaseReference mDatabase;

    public MemberEditFragment() {
        // Required empty public constructor
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

                String memberName = edtMemberName.getText().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference();

                if(memberName.trim().length() > 0)
                {
                    SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    String uid = sharedPreferences.getString(getString(R.string.userId),"");

                    if(member != null)
                    {
                        MemberModel memberModel = new MemberModel(member.getMid(),memberName,member.getGid());

                        mDatabase.child(uid).child(getString(R.string.member)).child(member.getMid()).setValue(memberModel);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(getContext()).memberDao().update(member);
                            }
                        }).start();
                        Toast.makeText(getActivity(), "Group is update", Toast.LENGTH_SHORT).show();
                    }else
                    {

                        String id = mDatabase.push().getKey();

                        MemberModel memberModel = new MemberModel(id,memberName,group.getGid());
                        mDatabase.child(uid).child(getString(R.string.member)).child(id).setValue(memberModel);


                        final Member newMember = new Member();
                        newMember.setGid(id);
                        newMember.setMemberName(memberName);
                        newMember.setMid(id);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(getContext()).memberDao().insert(newMember);
                            }
                        }).start();
                        Toast.makeText(getActivity(), "Member is Added", Toast.LENGTH_SHORT).show();
                        edtMemberName.setText("");
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

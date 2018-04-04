package mplink.mptech.randompicker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mplink.mptech.randompicker.models.GroupModel;
import mplink.mptech.randompicker.models.MemberModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberListFragment extends Fragment {

    private View root;

    private Toolbar toolbar;

    public GroupModel group;

    private FloatingActionButton fabAdd;

    private RecyclerView recyclerView;

    private MemberRecyclerViewAdapter adapter;

    private DatabaseReference mDatabase;

    private List<MemberModel> memberModelList;

    public MemberListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_member_list, container, false);

        toolbar = (Toolbar) root.findViewById(R.id.memberToolbar);

        toolbar.setTitle("Member List");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ActionBar actionBar = ((AppCompatActivity) getActivity()) != null ? ((AppCompatActivity) getActivity()).getSupportActionBar() : null;
        if(actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        memberModelList = new ArrayList<>();

        fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberEditFragment memberEditFragment = new MemberEditFragment();
                memberEditFragment.groupId = group.getId();
                memberEditFragment.setMemberModelList(memberModelList);
                ((MainActivity) getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content,memberEditFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.rvMemberList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new MemberRecyclerViewAdapter(memberModelList,getContext(),(MemberRecyclerViewAdapter.onMemberClickListener) getActivity());

        recyclerView.setAdapter(adapter);



        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(getString(R.string.userId),"");

        mDatabase.child(uid).child(getString(R.string.member)).child(group.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memberModelList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final MemberModel memberModel = postSnapshot.getValue(MemberModel.class);
                    if(memberModel != null && memberModel.getGid().equals(group.getId()))
                    {
                        memberModelList.add(memberModel);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return root;
    }

}

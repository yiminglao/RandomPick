package mplink.mptech.randompicker;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mplink.mptech.randompicker.db.Group;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberListFragment extends Fragment {

    private View root;

    private Toolbar toolbar;

    public Group group;

    private FloatingActionButton fabAdd;

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

        ActionBar actionBar = ((AppCompatActivity) getActivity()) != null ? ((AppCompatActivity) getActivity()).getSupportActionBar() : null;
        if(actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        Log.d("gid",group.getGid());

        fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberEditFragment memberEditFragment = new MemberEditFragment();
                memberEditFragment.group = group;
                ((MainActivity) getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content,memberEditFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }

}

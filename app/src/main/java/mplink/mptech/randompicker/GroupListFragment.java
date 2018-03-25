package mplink.mptech.randompicker;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mplink.mptech.randompicker.db.Group;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends Fragment{

    private View root;

    private RecyclerView recyclerView;

    private GroupRecyclerViewAdapter adapter;

    private Toolbar toolbar;
    
    private FloatingActionButton fabAdd;

    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 123;

    public GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.frag_group_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.mBtnSetting:
                Toast.makeText(getActivity(), "456789", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mBtnLogOut:
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "you are log out", Toast.LENGTH_SHORT).show();
                                mAuth = FirebaseAuth.getInstance();

                                // Choose authentication providers
                                List<AuthUI.IdpConfig> providers = Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

                                // Create and launch sign-in intent
                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setAvailableProviders(providers)
                                                .build(),
                                        RC_SIGN_IN);
                            }
                        });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_group_list, container, false);

        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle("Random Pick");

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);



        fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAdd);
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content,new GroupEditFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });



        recyclerView = (RecyclerView) root.findViewById(R.id.rvGroupList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        adapter = new GroupRecyclerViewAdapter(new ArrayList<Group>(), (GroupRecyclerViewAdapter.OnGroupClickListener) getActivity(),getContext());

        recyclerView.setAdapter(adapter);

        ViewModelProviders.of(this)
                .get(AllGroupViewModel.class)
                .getAllGroup(getContext())
                .observe(this, new Observer<List<Group>>() {
                    @Override
                    public void onChanged(@Nullable List<Group> groups) {
                        adapter.addItem(groups);
                    }
                });


        return root;
    }


}

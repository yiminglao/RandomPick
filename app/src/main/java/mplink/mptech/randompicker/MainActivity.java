package mplink.mptech.randompicker;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import mplink.mptech.randompicker.db.Group;

public class MainActivity extends AppCompatActivity implements GroupRecyclerViewAdapter.OnGroupClickListener{

    FragmentManager fm;

    private FirebaseAuth mAuth;



    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    private void updateUI(FirebaseUser user) {

        if (user != null) {
            Log.d("uid", mAuth.getCurrentUser().getUid());

            fm = this.getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(android.R.id.content,new GroupListFragment(),"groupListFrag")
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }



    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return super.onSupportNavigateUp();
    }

    @Override
    public void groupClick(Group group) {
        fm = getSupportFragmentManager();
        RandomFragment randomFragment = new RandomFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(android.R.id.content,randomFragment).addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void groupEditClick(Group group) {
        GroupEditFragment editFragment = new GroupEditFragment();
        editFragment.group = group;
        fm = this.getSupportFragmentManager();
        fm.beginTransaction()
                .replace(android.R.id.content,editFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void groupDelClick(Group group) {
        DialogFragment dialogFragment = DelConfirmDialog.Instance(group);
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }

}

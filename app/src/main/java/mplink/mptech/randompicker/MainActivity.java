package mplink.mptech.randompicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import mplink.mptech.randompicker.db.AppDatabase;
import mplink.mptech.randompicker.db.Group;
import mplink.mptech.randompicker.db.Member;
import mplink.mptech.randompicker.models.GroupModel;

public class MainActivity extends AppCompatActivity implements GroupRecyclerViewAdapter.OnGroupClickListener ,
                                                                MemberRecyclerViewAdapter.onMemberClickListener{

    FragmentManager fm;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;


    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();



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
            SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(getString(R.string.userId),user.getUid());
            editor.apply();
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
        randomFragment.group = group;
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

    @Override
    public void memberEditClick(Member member) {
        MemberEditFragment memberEditFragment = new MemberEditFragment();
        memberEditFragment.member = member;
        fm = this.getSupportFragmentManager();
        fm.beginTransaction()
                .replace(android.R.id.content,memberEditFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    @Override
    public void memberDelClick(Member member) {
        DialogFragment dialogFragment = DelConfirmDialog.Instance(member);
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }

}

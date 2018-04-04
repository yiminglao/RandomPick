package mplink.mptech.randompicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import mplink.mptech.randompicker.models.GroupModel;
import mplink.mptech.randompicker.models.MemberModel;

public class MainActivity extends AppCompatActivity implements GroupRecyclerViewAdapter.OnGroupClickListener ,
                                                                MemberRecyclerViewAdapter.onMemberClickListener{

    FragmentManager fm;

    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

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

        updateUI(currentUser);

    }



    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return super.onSupportNavigateUp();
    }

    @Override
    public void groupClick(GroupModel group) {
        RandomFragment randomFragment = new RandomFragment();
        randomFragment.group = group;

        fm = this.getSupportFragmentManager();
        fm.beginTransaction()
                .replace(android.R.id.content,randomFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void groupEditClick(GroupModel group,List<GroupModel> list) {
        GroupEditFragment editFragment = new GroupEditFragment();
        editFragment.group = group;
        editFragment.setGroupList(list);
        fm = this.getSupportFragmentManager();
        fm.beginTransaction()
                .replace(android.R.id.content,editFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void groupDelClick(GroupModel group) {
        DialogFragment dialogFragment = DelConfirmDialog.Instance(group);
        dialogFragment.show(getSupportFragmentManager(),"dialog");


    }

    @Override
    public void memberEditClick(MemberModel member) {
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
    public void memberDelClick(MemberModel member) {
        DialogFragment dialogFragment = DelConfirmDialog.Instance(member);
        dialogFragment.show(getSupportFragmentManager(),"dialog");
    }

}

package mplink.mptech.randompicker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private View root;

    FragmentManager fm;

    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 123;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);



        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(getString(R.string.userId),user.getUid());
            editor.apply();
            ((MainActivity) getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content,new GroupListFragment(),"groupListFrag")
                    .commit();

        }
    }

}

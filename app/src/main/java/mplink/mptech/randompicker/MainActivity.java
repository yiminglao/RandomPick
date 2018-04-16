package mplink.mptech.randompicker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import mplink.mptech.randompicker.models.GroupModel;
import mplink.mptech.randompicker.models.MemberModel;

public class MainActivity extends AppCompatActivity implements GroupRecyclerViewAdapter.OnGroupClickListener ,
                                                                MemberRecyclerViewAdapter.onMemberClickListener{

    private InterstitialAd mInterstitialAd;

    private GroupModel groups;

    FragmentManager fm;

    private String uid;

    RandomFragment randomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        groups = new GroupModel();

        MobileAds.initialize(this, "ca-app-pub-3612854431763761~6473835553");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3612854431763761/5917988706");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(getString(R.string.userId),getString(R.string.out));

        if(uid.equals(getString(R.string.out)))
        {

            fm = this.getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(android.R.id.content,new LoginFragment())
                    .commit();
        }
        else
        {
            fm = this.getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(android.R.id.content,new GroupListFragment(),"groupListFrag")
                    .commit();
        }


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                randomFragment = new RandomFragment();
                randomFragment.group = groups;
                fm.beginTransaction()
                .replace(android.R.id.content,randomFragment,"randomFrag")
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
            }
        });

    }




    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return super.onSupportNavigateUp();
    }


    @Override
    public void groupClick(GroupModel group) {

        this.groups = group;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            RandomFragment randomFragment = new RandomFragment();
            randomFragment.group = groups;
            fm.beginTransaction()
                    .replace(android.R.id.content,randomFragment)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

    }

    @Override
    public void groupEditClick(GroupModel group,List<GroupModel> list) {
        GroupEditFragment editFragment = new GroupEditFragment();
        editFragment.group = group;
        editFragment.setGroupList(list);
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

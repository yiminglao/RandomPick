package mplink.mptech.randompicker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import mplink.mptech.randompicker.models.GroupModel;
import mplink.mptech.randompicker.models.MemberModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class RandomFragment extends Fragment {

    int result;

    String selectedName ="";

    private View root;

    private Toolbar toolbar;

    public GroupModel group;

    private DatabaseReference mDatabase;

    TextView txtPickName;

    private List<MemberModel> memberModelList;

    private List<MemberModel> tempMemberModelList;

    Button btnRandom, btnAgain, btnReset;

    private TextToSpeech tts;


    public RandomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_random, container, false);

        toolbar = (Toolbar) root.findViewById(R.id.randToolbar);

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);

        memberModelList = new ArrayList<>();

        tempMemberModelList  = new ArrayList<>();

        btnRandom = (Button) root.findViewById(R.id.btnRandom);

        btnAgain = (Button) root.findViewById(R.id.btnAgain);

        btnReset = (Button) root.findViewById(R.id.btnReset);

        txtPickName = (TextView) root.findViewById(R.id.txtPickName);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                tempMemberModelList.addAll(memberModelList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("TTS", "This Language is not supported");

                    } else {
                        Log.d("TTS", "Initilization Failed!");
                    }
                }
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRandom();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakName(selectedName);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempMemberModelList.clear();
                tempMemberModelList.addAll(memberModelList);
                Toast.makeText(getActivity(), "List been reset! " + tempMemberModelList.size(), Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }



    public void startRandom() {
        Random rand = new Random();
        int randNum = 0;

        if (tempMemberModelList.size()>0 && memberModelList.size() > 0) {
            randNum = rand.nextInt(tempMemberModelList.size());
            result = randNum;
            selectedName = tempMemberModelList.get(result).getMemberName();
            txtPickName.setText(selectedName);
            speakName(selectedName);
            tempMemberModelList.remove(result);
        }
        else if(tempMemberModelList.size() == 0 && memberModelList.size() > 0)
        {
            tempMemberModelList.clear();
            tempMemberModelList.addAll(memberModelList);
            randNum = rand.nextInt(tempMemberModelList.size());
            result = randNum;
            selectedName = tempMemberModelList.get(result).getMemberName();
            txtPickName.setText(selectedName);
            speakName(selectedName);
            tempMemberModelList.remove(result);
        }
        else
        {
            Toast.makeText(getActivity(), "Please add member to you list!", Toast.LENGTH_SHORT).show();
        }
        Log.d("random pick " , selectedName + "size of array "+ tempMemberModelList.size());

    }

    public void speakName(String name)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            tts.speak(name, TextToSpeech.QUEUE_FLUSH,null,null);
        }else
        {
            tts.speak(name, TextToSpeech.QUEUE_FLUSH, null);
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.frag_random_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.mbtnAdd:
                MemberListFragment memberListFragment = new MemberListFragment();
                memberListFragment.group = group;
                ((MainActivity) getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content,memberListFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}

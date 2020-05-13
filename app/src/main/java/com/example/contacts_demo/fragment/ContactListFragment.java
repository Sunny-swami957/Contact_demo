package com.example.contacts_demo.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts_demo.R;
import com.example.contacts_demo.adapter.AlphaListAdapter;
import com.example.contacts_demo.adapter.ContactListAdapter;
import com.example.contacts_demo.database.DatabaseHandler;
import com.example.contacts_demo.entity.Contact;
import com.example.contacts_demo.entity.ContactList;

import java.util.ArrayList;

public class ContactListFragment extends Fragment{

    private TextView addTextView;
    private NavController navController;
    private TextView alphaTV;
    private RecyclerView listRV,alphaRV;
    private ContactListAdapter adapter;
    private AlphaListAdapter adapter1;
    private ContactList cViewModel;
    private ArrayList<String> mAlpha=new ArrayList<String>();
    private DatabaseHandler db;
    SharedPreferences sharedPreferences;
    private static Integer size=0;
    private static Integer scrollPos=0;
    private static boolean pauseflag=false;
    private LinearLayoutManager mlayoutmanager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.contact_list_fragment, container, false);
        return v;
    }

    @SuppressLint({"NewApi", "FragmentLiveDataObserve", "FragmentBackPressedCallback"})
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addTextView=(TextView)view.findViewById(R.id.add_contact);
        navController= NavHostFragment.findNavController(this);
        alphaTV=(TextView)view.findViewById(R.id.alpha_display);
        listRV=(RecyclerView)view.findViewById(R.id.list_recyclerview);
        alphaRV=(RecyclerView)view.findViewById(R.id.alpha_list_recyclerview);
        db = new DatabaseHandler(getContext());
        //cViewModel=ViewModelProvider.Factory.this;
        ContactList.getInstance().setContactListFragment(this);
        if(mAlpha.isEmpty()) {
            char ch;

            for (ch = 'A'; ch <= 'Z'; ch++) {
                mAlpha.add(ch + "");
            }
            mAlpha.add("#");
        }
        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.addFragment);
            }
        });
        adapter = new ContactListAdapter(navController);
        listRV.setAdapter(adapter);
        mlayoutmanager=new LinearLayoutManager(getContext());
        listRV.setLayoutManager(mlayoutmanager);
        listRV.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int firstVisibleItem = mlayoutmanager.findFirstVisibleItemPosition();
                char ch=ContactList.getInstance().getContacts().getValue().get(firstVisibleItem).getFirst_name().charAt(0);
                if(Character.isDigit(ch))
                    ch='#';
                else
                    ch=Character.toUpperCase(ch);
                Log.d("Alpha select", String.valueOf(ch));
                alphaTV.setText(String.valueOf(ch));
            }
        });
        adapter1 = new AlphaListAdapter(mAlpha,alphaTV,listRV);
        alphaRV.setAdapter(adapter1);
        alphaRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactList.getInstance().getContacts().observe(this,new Observer<ArrayList<Contact>>() {
                    @Override
                    public void onChanged(final ArrayList<Contact> contacts) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(size!=contacts.size()) {
                                    Log.d("List changed", "true"+contacts.size());
                                    adapter.notifyDataSetChanged();
                                    size=contacts.size();
                                }
                            }
                        });
                    }
                }
        );
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void onPause() {

        super.onPause();
        scrollPos=mlayoutmanager.findFirstVisibleItemPosition();
        Log.d("List fragment","On pause called"+scrollPos);
        pauseflag=true;
    }
    public void onResume() {

        super.onResume();
        Log.d("List fragment","On resume called"+scrollPos+","+pauseflag);
        if(pauseflag) {
            listRV.scrollToPosition(scrollPos);
            pauseflag=false;
        }
    }
    public void update()
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("List changed", "true");
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void setScrollPos(int pos)
    {
        int firstVisibleItem = mlayoutmanager.findFirstVisibleItemPosition();
        if(pos-firstVisibleItem<0) {
            if(pos==0)
                listRV.smoothScrollToPosition(pos);
            else
                listRV.smoothScrollToPosition(pos-1);
        }
        else
            listRV.smoothScrollToPosition(pos+8);
    }

}

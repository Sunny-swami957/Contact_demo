package com.example.contacts_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.contacts_demo.R;
import com.example.contacts_demo.database.DatabaseHandler;
import com.example.contacts_demo.entity.ContactList;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactFragment extends Fragment {

    private TextView cancelTV,doneTV;
    private EditText first_nameET,last_nameET,emailET,phoneET;
    private CircleImageView addFavIV;
    private NavController navController;
    private JSONObject postData;
    private Bundle bundle;
    private DatabaseHandler db;
    private InputMethodManager imm;
    private boolean isEdit;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.add_contact_fragment, container, false);

        return v;
    }

    @SuppressLint({"FragmentBackPressedCallback", "NewApi"})
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancelTV=(TextView)view.findViewById(R.id.cancel_button);
        doneTV=(TextView)view.findViewById(R.id.done_button);
        first_nameET=(EditText)view.findViewById(R.id.fn_text);
        last_nameET=(EditText)view.findViewById(R.id.ln_text);
        phoneET=(EditText)view.findViewById(R.id.phone_text);
        emailET=(EditText)view.findViewById(R.id.email_text);
        addFavIV=(CircleImageView)view.findViewById(R.id.add_fav);
        first_nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                {
                    first_nameET.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                }else{
                    first_nameET.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        last_nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                {
                    last_nameET.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                }else{
                    last_nameET.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                {
                    phoneET.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                }else{
                    phoneET.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                {
                    emailET.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                }else{
                    emailET.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        db = new DatabaseHandler(getContext());
        bundle=getArguments();
        if(bundle!=null) {
            first_nameET.setText(bundle.getString("first_name"));
            last_nameET.setText(bundle.getString("last_name"));
            emailET.setText(bundle.getString("email"));
            phoneET.setText(bundle.getString("phone"));
            if(Integer.parseInt(bundle.getString("fav"))==0)
            {
                addFavIV.setImageResource(R.drawable.icon);
            }else{
                addFavIV.setImageResource(R.drawable.select_icon);
            }
            isEdit=true;
        }else
            isEdit=false;

        first_nameET.requestFocus();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(first_nameET, InputMethodManager.SHOW_IMPLICIT);
        addFavIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addFavIV.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.select_icon).getConstantState())
                {
                    addFavIV.setImageResource(R.drawable.icon);
                }else{
                    addFavIV.setImageResource(R.drawable.select_icon);
                }
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                navController.navigateUp();
            }
        });
        doneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                if(phoneET.getText().length()==0)
                {
                    Toast.makeText(getContext(),"Phone number cannot be empty!",Toast.LENGTH_LONG);
                }else{
                     postData= new JSONObject();
                    try {
                        postData.put("firstName", first_nameET.getText().toString());
                        postData.put("lastName", last_nameET.getText().toString());
                        postData.put("email", emailET.getText().toString());
                        postData.put("phone", phoneET.getText().toString());
                        if(addFavIV.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.select_icon).getConstantState())
                            postData.put("favorite", true);
                        else
                            postData.put("favorite", false);
                        if(!isEdit)
                            ContactList.getInstance().sendData(db,postData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                navController.navigateUp();
            }
        });
        navController= NavHostFragment.findNavController(this);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                navController.navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

}

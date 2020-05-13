package com.example.contacts_demo.entity;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.example.contacts_demo.activity.MainActivity;
import com.example.contacts_demo.database.DatabaseHandler;
import com.example.contacts_demo.fragment.ContactListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;

public class ContactList {
    private MutableLiveData<ArrayList<Contact>> sContacts;
    private MutableLiveData<ArrayList<Integer>> mPositions;
    private static ContactList instance;
    private DatabaseHandler db;
    private MainActivity mainActivity;
    private ContactListFragment contactListFragment;
    private JSONObject postData;
    private Integer insertPos=0;
    private boolean update;

    public ContactList() {
    }
    public static ContactList getInstance() {
        if(instance==null) {
            instance = new ContactList();
        }
        return instance;
    }
    public MutableLiveData<ArrayList<Integer>> getPositions() {
        if(mPositions==null) {
            mPositions=new MutableLiveData<ArrayList<Integer>>();

            ArrayList<Integer> positions = new ArrayList<Integer>();
            for(int i=0;i<27;i++)
                positions.add(-1);
            mPositions.setValue(positions);
        }
        return mPositions;
    }
    public void setContactListFragment(ContactListFragment clf)
    {
        this.contactListFragment=clf;
    }
    /*public void setContacts(ArrayList<Contact> contacts)
    {
        this.sContacts=contacts;
    }*/
    public int find(Contact c,int l,int r,LexicographicComparator lc)
    {
        if(l>r)
            return r+1;
        int mid=(l+r)/2;
        if(lc.compare(sContacts.getValue().get(mid),c)<=0)
        {
            return find(c,mid+1,r,lc);
        }
        return find(c,l,mid-1,lc);
    }
    public void addContact(Contact c)
    {
        if(this.sContacts==null)
        {
            this.sContacts=new MutableLiveData<ArrayList<Contact>>();
            this.sContacts.setValue(new ArrayList<Contact>());
            this.sContacts.getValue().add(c);
        }else{
            int pos=find(c,0,sContacts.getValue().size()-1,new LexicographicComparator());
            this.sContacts.getValue().add(pos,c);
        }
    }
    public void addNewContact(Contact c)
    {
        int newPos=0;
        if(this.sContacts==null)
        {
            this.sContacts=new MutableLiveData<ArrayList<Contact>>();
            this.sContacts.setValue(new ArrayList<Contact>());
            this.sContacts.getValue().add(c);
        }else{
            int pos=find(c,0,sContacts.getValue().size()-1,new LexicographicComparator());
            insertPos=pos+1;
            Log.d("Inserted position",String.valueOf(insertPos));
            this.sContacts.getValue().add(pos,c);
        }
        populateTask(db);
        contactListFragment.setScrollPos(insertPos);
    }
    public MutableLiveData<ArrayList<Contact>> getContacts()
    {
        if(this.sContacts==null)
        {
            this.sContacts=new MutableLiveData<ArrayList<Contact>>();
            this.sContacts.setValue(new ArrayList<Contact>());
        }
        return this.sContacts;
    }
    public void populateTask(DatabaseHandler db)
    {
        char ch;
        int i;
        for(i=0;i<getPositions().getValue().size();i++) {
            getPositions().getValue().set(i,-1);
        }
        for(i=0;i<this.sContacts.getValue().size();i++)
        {
            ch=this.sContacts.getValue().get(i).getFirst_name().charAt(0);
            if(ch>=65&&ch<=90)
            {
                if(getPositions().getValue().get(ch-65)==-1)
                    getPositions().getValue().set(ch-65,i);
            }else if(ch>=97&&ch<=122)
            {
                if(getPositions().getValue().get(ch-97)==-1)
                    getPositions().getValue().set(ch-97,i);
            }else{

            }
        }
        ArrayList<Integer> temp=new ArrayList<Integer>();
        for(i=0;i<getPositions().getValue().size();i++) {
            if(getPositions().getValue().get(i)!=-1) {
                temp.add(getPositions().getValue().get(i));
                //Log.d("Populate task", Integer.toString(i)+"-->"+getPositions().getValue().get(i));
            }

        }
        int j=0;
        for(i=0;i<getPositions().getValue().size();i++) {
            if(getPositions().getValue().get(i)==-1)
            {
                if(j!=temp.size())
                    getPositions().getValue().set(i,temp.get(j));
                else if(j!=0)
                    getPositions().getValue().set(i,temp.get(j-1));
                else
                    getPositions().getValue().set(i,0);
            }else{
                j++;
            }
            Log.d("Populate task", Integer.toString(i)+"-->"+getPositions().getValue().get(i));
        }
    }
    class LexicographicComparator implements Comparator<Contact> {
        @Override
        public int compare(Contact a, Contact b) {
            String p1=a.getFirst_name();
            String p2=b.getFirst_name();
            char ch1,ch2;
            int i;
            for(i=0;i<p1.length()&&i<p2.length();i++)
            {
                ch1=Character.toUpperCase(p1.charAt(i));
                ch2=Character.toUpperCase(p2.charAt(i));
                if(Character.isDigit(ch1)&&!Character.isDigit(ch2))
                    return 1;
                else if(!Character.isDigit(ch1)&&Character.isDigit(ch2))
                    return -1;

                if(ch1!=ch2)
                    return ch1-ch2;
            }
            if(i==p2.length())
                return -1;
            return 1;
        }
    }
    public void sync(DatabaseHandler db, MainActivity mainActivity,boolean val)
    {
        this.db=db;
        if(val)
            this.mainActivity=mainActivity;
        this.update=val;
        new JsonTask().execute();
    }
    private class JsonTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("JSON task","Preexecute");
        }
        @Override
        protected JSONArray doInBackground(Void... voids) {

            Log.d("JSON task","onbackground");
            String str="http://167.172.6.138:8080/contacts";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONArray(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e("JSON task", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(JSONArray response) {
            super.onPostExecute(response);
            if(response != null)
            {
                int i,x;
                JSONObject jsonObject;
                //db.deleteContact();
                if(sContacts!=null)
                    sContacts.getValue().clear();
                Log.d("JSON task", "Success: ");
                for(i=0;i<response.length();i++)
                {
                    try {

                        jsonObject=response.getJSONObject(i);
                        x=0;
                        if(jsonObject.getBoolean("favorite"))
                            x=1;
                        Contact c=new Contact(jsonObject.getString("firstname")
                                ,jsonObject.getString("lastname")
                                ,jsonObject.getString("email")
                                ,jsonObject.getString("phone")
                                ,x);
                        addContact(c);
                        //db.addContact(c);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            populateTask(db);
            if(update)
                mainActivity.setup();
            else
                contactListFragment.update();
        }
    }
    public void sendData(DatabaseHandler db,JSONObject jsonObject)
    {
        this.db=db;
        this.postData=jsonObject;
        new SendDeviceDetails().execute();
    }
    private class SendDeviceDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("SendDeviceDetails task","Preexecute");
        }
        @Override
        protected String doInBackground(Void... voids) {

            String str="http://167.172.6.138:8080/contacts";
            try {
                URL url = new URL(str);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                Log.d("SendDeviceDetails task","Onbackground"+postData.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(postData.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return postData.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                int k=0;
                if(postData.getString("favorite")=="true")
                    k=1;
                Contact c=new Contact(postData.getString("firstName"),postData.getString("lastName"),
                        postData.getString("email"),postData.getString("phone"),k);
                //db.addContact(c);
                addNewContact(c);
                sync(db,null,false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("SendDeviceDetails", "posted "+result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
}
    /*public void updateData(JSONObject jsonObject)
    {
        this.postData=jsonObject;
        new UpdateDetails().execute();
    }
    private class UpdateDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("UpdateDetails task","Preexecute");
        }
        @Override
        protected String doInBackground(Void... voids) {

            String str="http://167.172.6.138:8080/contacts";
            try {
                URL url = new URL(str);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                Log.d("UpdateDetails task","Onbackground"+postData.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(postData.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return postData.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("UpdateDetails", "posted "+result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }*/


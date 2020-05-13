package com.example.contacts_demo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.contacts_demo.entity.Contact;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "contactsManager";
        private static final String TABLE_CONTACTS = "contacts";
        private static final String KEY_FIRST_NAME = "first_name";
        private static final String KEY_LAST_NAME = "last_name";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_PH_NO = "phone_number";
        private static final String KEY_FAVOURITE ="favourite";

        public DatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                    + KEY_PH_NO + " TEXT , "+ KEY_FIRST_NAME + " TEXT,"
                    + KEY_LAST_NAME + " TEXT," + KEY_EMAIL + " TEXT ,"
                    + KEY_FAVOURITE + "TEXT )";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            onCreate(db);
        }

        // code to add the new contact
        public void addContact(Contact contact) {
            SQLiteDatabase db = this.getWritableDatabase();
            Log.d("Database Handler","DB Size: "+getContactsCount());

                String CREATE_CONTACTS_TABLE = "INSERT INTO " + TABLE_CONTACTS + " VALUES ('"
                        + contact.getPhone() + "','" + contact.getFirst_name()
                        + "','" + contact.getLast_name() + "','"
                        + contact.getEmail() + "'," + String.valueOf(contact.getFav()) + ")";
                db.execSQL(CREATE_CONTACTS_TABLE);
        }

 /*       // code to get the single contact
        public Contact getContact(String ph) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_PH_NO ,KEY_FIRST_NAME ,KEY_LAST_NAME
                    ,KEY_EMAIL}, KEY_PH_NO + "=?",
                    new String[] { ph }, null, null, null, null);
            if(cursor!=null)
                cursor.moveToFirst();
            else
                return null;

            Contact contact=new Contact(cursor.getString(1),cursor.getString(2)
                    ,cursor.getString(3),cursor.getString(0),Integer.parseInt(cursor.getString(4)));
            return contact;
        }*/

        // code to get all contacts in a list view
        public ArrayList<Contact> getAllContacts() {
            ArrayList<Contact> contactList = new ArrayList<Contact>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS ;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setFirst_name(cursor.getString(1));
                    contact.setLast_name(cursor.getString(2));
                    contact.setEmail(cursor.getString(3));
                    contact.setPhone(cursor.getString(0));
                    contact.setFav(Integer.parseInt(cursor.getString(4)));

                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
            //db.close();
            // return contact list
            return contactList;
        }

        // code to update the single contact
  /*      public int updateContact(Contact contact) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_FIRST_NAME, contact.getFirst_name());
            values.put(KEY_LAST_NAME, contact.getLast_name());
            values.put(KEY_EMAIL, contact.getEmail());
            values.put(KEY_PH_NO, contact.getPhone());
            values.put(KEY_FAVOURITE, String.valueOf(contact.getFav()));

            // updating row
            return db.update(TABLE_CONTACTS, values, KEY_FIRST_NAME + " = ?",
                    new String[] { String.valueOf(contact.getID()) });
        }*/

        // Deleting single contact
        public void deleteContact() {
            SQLiteDatabase db = this.getWritableDatabase();
            String countQuery = "DELETE  FROM " + TABLE_CONTACTS;
            db.execSQL(countQuery);
        }

        // Getting contacts Count
        public int getContactsCount() {
            String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = 0;
            if(cursor != null && !cursor.isClosed()){
                count = cursor.getCount();
                cursor.close();
            }
            return count;
        }

}

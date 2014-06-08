package com.example.managecontacts;
import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class ContactsDatabase extends SQLiteOpenHelper {
 
	/** Class to handle the SQLite database for each contact */
	
    /** Variables */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "contacts";
 
    /** Variables for each column of the database */
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_POSTCODE = "postcode";
    private static final String KEY_DOB_YEAR = "DOBYear";
    private static final String KEY_DOB_MONTH = "DOBMonth";
    private static final String KEY_DOB_DAY = "DOBDay";
    private static final String KEY_LASTTALKED = "lastTalked";

    /** Constructor */
    public ContactsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /** Method which creates the table and appropriate columns */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_MOBILE + " TEXT," + KEY_EMAIL + " TEXT," + KEY_POSTCODE 
                + " TEXT," + KEY_DOB_YEAR + " TEXT," + KEY_DOB_MONTH + " TEXT," + KEY_DOB_DAY + " TEXT," + KEY_LASTTALKED + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
 
        // Create tables again
        onCreate(db);
    }
  
    /** Method to add a new contact to the database */
    public void addContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_MOBILE, contact.getMobile());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_POSTCODE, contact.getPostcode());
        values.put(KEY_DOB_YEAR, contact.getDOBYear());
        values.put(KEY_DOB_MONTH, contact.getDOBMonth());
        values.put(KEY_DOB_DAY, contact.getDOBDay());
        values.put(KEY_LASTTALKED, contact.getTalked());
 
        /** Insert the row and then close the database */
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }
     
    /** Gathers all the data from the database */
    public List<Contacts> getAllContacts() {
        List<Contacts> contactList = new ArrayList<Contacts>();
        /** Query the database */
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_CONTACTS, null);

        /** Loops through every row and adds to List<Contacts> */
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false) {
            Contacts contact = new Contacts();
            contact.setId(Integer.parseInt(cursor.getString(0)));
            contact.setName(cursor.getString(1));
            contact.setMobile(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
            contact.setPostcode(cursor.getString(4));
            contact.setDOBYear(cursor.getInt(5));
            contact.setDOBMonth(cursor.getInt(6));
            contact.setDOBDay(cursor.getInt(7));
            //contact.setTalked(cursor.getString(8));
            contactList.add(contact);
            cursor.moveToNext();
        }
 
        /** Return the List<Contacts> */
        return contactList;
    }
  
    // Deleting single contact
    public boolean deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        id = id + 1;
        Log.v("deleteContact(int id)", "Id: " + id);
        return db.delete(TABLE_CONTACTS, KEY_ID + "='" + id + "'", null) > 0;
    }
  
}
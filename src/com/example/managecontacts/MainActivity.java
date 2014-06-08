package com.example.managecontacts;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


//import com.example.antiquity.MonumentsDatabase;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	/** Global variables */
	
	/** Storing contacts */ 
	private List<Contacts> contactsList;
	private ListView contactsListView;
	private Contacts contact;
	private ContactsDatabase conDatabase;
	private ContactsAdapter arrayAdapter;
	
	/** Input Text Fields */
	private EditText nameText;
	private EditText mobileText;
	private EditText emailText;
	private EditText postCode;
	private DatePicker dateOfBirth;
	
	/** Input variables */
	private String userInputName;
	private String userInputMobile;
	private String userInputEmail;
	private String userInputPostCode;
	private int userInputYear;
	private int userInputMonth;
	private int userInputDay;

	/** Update Text Fields */
	private EditText updateName;
	private EditText updateMobile;
	private EditText updateEmail;
	private EditText updatePostcode;
	private DatePicker updateDob;
	
	/** Updated Contact */
	private String updateNameText;
	private String updateMobileText;
	private String updateEmailText;
	private String updatePostcodeText;
	private int updateDobYear;
	private int updateDobMonth;
	private int updateDobDay;
	
	/** User details */
	private String _name;
	private String _mobile;
	private String _email;
	private String _postcode;
	private int _dobYear;
	private int _dobMonth;
	private int _dobDay;
	
	/** Display Details */
	private TextView displayName;
	private TextView displayNumber;
	private TextView displayEmail;
	private TextView displayPostcode;
	private TextView displayAge;
	private int currentAge;
	private int currentYear;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
	    super.onCreate( savedInstanceState );
	    setContentView(R.layout.activity_main);

	    /** Make a new instance of the database */
	    conDatabase = new ContactsDatabase(MainActivity.this);
	    contactsList = conDatabase.getAllContacts();
        
	    contactsListView = (ListView) findViewById(R.id.contactsListView);

	    /** Instantiate the adapter for the ListView */
        arrayAdapter = new ContactsAdapter(this, contactsList);
        contactsListView.setAdapter(arrayAdapter);
        editContact();
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
	    // Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.add:
	        	addContact();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/** Add a contact */
	public void addContact() {
		AlertDialog.Builder addContactDialog = new AlertDialog.Builder(this);
		/** Inflate the add_contact XML file */
	    LayoutInflater addContactInflater = this.getLayoutInflater();
	    final View parentView  = addContactInflater.inflate(R.layout.add_contact, null);
	    
	    /** Instantiate each EditText widget */
	    nameText = (EditText)parentView.findViewById(R.id.name);
	    mobileText = (EditText)parentView.findViewById(R.id.number);
	    emailText = (EditText)parentView.findViewById(R.id.emailAddress);
	    postCode = (EditText)parentView.findViewById(R.id.postcode);
	    dateOfBirth = (DatePicker)parentView.findViewById(R.id.dob);

	    addContactDialog.setView(parentView);
	    addContactDialog.setPositiveButton(R.string.add_contact, new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		/** Get the text from the EditText widgets */
	        	userInputName = nameText.getText().toString();
	        	userInputMobile = mobileText.getText().toString();
	        	userInputEmail = emailText.getText().toString();
	        	userInputPostCode = postCode.getText().toString();
	        	userInputYear = dateOfBirth.getYear();
	        	userInputMonth = dateOfBirth.getMonth();
	        	userInputDay = dateOfBirth.getDayOfMonth();
	        	
	            if(userInputName != null && userInputName.length() > 0){
	            	if(userInputYear < currentYear) {
	            		Toast.makeText(getApplicationContext(), "You entered a date that is in the future! Please try again.", Toast.LENGTH_LONG).show();
	            	} else {
	            		/** Store the Contact in the db */
		            	contact = new Contacts();
		            	contact.setName(userInputName);
		            	contact.setMobile(userInputMobile);
		            	contact.setEmail(userInputEmail);
		            	contact.setPostcode(userInputPostCode);
		            	contact.setDOBYear(userInputYear);
		            	contact.setDOBMonth(userInputMonth);
		            	contact.setDOBDay(userInputDay);
		            	contactsList.add(contact);
		            	conDatabase.addContact(contact);

		            	arrayAdapter.notifyDataSetChanged();
	            	}
	            }else{
	                 Toast.makeText(getApplicationContext(), "You missed out part of the Contact!", Toast.LENGTH_LONG).show();
	            }      	   	
	        }
	    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	        	dialog.cancel();
	    	}
	    });
	    AlertDialog mainAlert = addContactDialog.create();
	    mainAlert.show();
	}
	
	/** Edit a contact */
	public void editContact() {
		
		/** Implement a Listener for each row of the ListView */
		contactsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) { 
            	final AlertDialog.Builder gpsDisabled = new AlertDialog.Builder(MainActivity.this);
            	gpsDisabled.setNegativeButton(R.string.edit_contact, new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int id) {
        				AlertDialog.Builder editContactDialog = new AlertDialog.Builder(MainActivity.this);
        				/** Inflate the edit_contact XML file */
        				LayoutInflater editContactInflater = MainActivity.this.getLayoutInflater();
        				final View parentView  = editContactInflater.inflate(R.layout.edit_contact, null);
        				
        				/** Instantiate each EditText widget */
        				updateName = (EditText)parentView.findViewById(R.id.name);
        				updateMobile = (EditText)parentView.findViewById(R.id.number);
        				updateEmail = (EditText)parentView.findViewById(R.id.email);
        				updatePostcode = (EditText)parentView.findViewById(R.id.postcode);
        				updateDob = (DatePicker)parentView.findViewById(R.id.dob);
        				    
        				getDetails(position);
        					
        				/** Set the TextView with the data gathered from DB query */
        				updateName.setText(_name);
        				updateMobile.setText(_mobile);
        			    updateEmail.setText(_email);
        			    updatePostcode.setText(_postcode);
        			    updateDob.updateDate(_dobYear, _dobMonth, _dobDay);
        			        
        			    editContactDialog.setView(parentView);
        			    editContactDialog.setPositiveButton(R.string.update_contact, new DialogInterface.OnClickListener() {
        			        public void onClick(DialogInterface dialog, int id) {
        			        	updateNameText = updateName.getText().toString();
        			        	updateMobileText = updateMobile.getText().toString();
        			        	updateEmailText = updateEmail.getText().toString();
        			        	updatePostcodeText = updatePostcode.getText().toString();
        			        	updateDobYear = updateDob.getYear();
        			        	updateDobMonth = updateDob.getMonth();
        			        	updateDobDay = updateDob.getDayOfMonth();
        			        		
        			        	/** Update the Contact in the ListView */
        			        	contact = new Contacts();
        		            	contact.setName(updateNameText);
        		            	contact.setMobile(updateMobileText);
        		            	contact.setEmail(updateEmailText);
        		            	contact.setPostcode(updatePostcodeText);
        		            	contact.setDOBYear(updateDobYear);
        		            	contact.setDOBMonth(updateDobMonth);
        		            	contact.setDOBDay(updateDobDay);
        		            	contactsList.set(position, contact);
        		            		
        		            	/** Update the edit to the SQLite Database */
        		            	editContactDetails(position, updateNameText, updateMobileText, updateEmailText,
        		            					updatePostcodeText, updateDobYear, updateDobMonth, updateDobDay);
        		            		
        		            	arrayAdapter.notifyDataSetChanged();
        			        }
        			    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        			        public void onClick(DialogInterface dialog, int id) {
        			        	dialog.cancel();
        			        }
        			    });
        			    AlertDialog updateAlert = editContactDialog.create();
        			    updateAlert.show();
        			}	
        		}).setPositiveButton(R.string.delete_contact, new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int id) {
        				Log.v("Check the position" , "position: " + position);
        				/** Delete the correct Contact (Row) from ListView/DB */
        				contactsList.remove(position);       				
        				conDatabase.deleteContact(position);
        				arrayAdapter.notifyDataSetChanged();
        			}
        		}).setNeutralButton(R.string.contact_details, new DialogInterface.OnClickListener() {
			      	public void onClick(DialogInterface dialog, int id) {
			      		getDetails(position);
			      		displayContactDetails();
			      	}
        		});
        		AlertDialog mainAlert = gpsDisabled.create();
        		mainAlert.show();
            }  
        });	
	}
	
	/** Display Contacts details on an AlertDialog */
	public void displayContactDetails() {
		AlertDialog.Builder detailsDialog = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater detailsInflater = MainActivity.this.getLayoutInflater();
		final View parentView  = detailsInflater.inflate(R.layout.contact_details, null);
		
		/** Instantiate each TextView widget */
		displayName = (TextView)parentView.findViewById(R.id.displayName);
	    displayNumber = (TextView)parentView.findViewById(R.id.displayNumber);
	    displayEmail = (TextView)parentView.findViewById(R.id.displayEmail);
	    displayPostcode = (TextView)parentView.findViewById(R.id.displayPostcode);
	    displayAge = (TextView)parentView.findViewById(R.id.displayAge);
	    
	    age();
	    
	    /** Set the TextView with the data gathered from DB query */
	    displayName.setText(_name);
		displayNumber.setText(_mobile);
        displayEmail.setText(_email);
        displayPostcode.setText(_postcode);
        displayAge.setText("" + currentAge);
        
        detailsDialog.setView(parentView);
        detailsDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		// DO NOTHING
        	}
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		// DO NOTHING
        	}
        });
        AlertDialog mainAlert = detailsDialog.create();
		mainAlert.show();
	}
	
	/** Update details with new text */
	public void editContactDetails(int position, String name, String mobile, String email, String postcode, int dobYear, int dobMonth,
								int dobDay) {
		position = position + 1;
		/** Query the database */
		SQLiteDatabase db = conDatabase.getWritableDatabase();
		Cursor editCursor = db.rawQuery("UPDATE contacts SET name = '"+ name +"', mobile = '" + mobile +"', email = '" + email + "'," +
				"postcode = '" + postcode + "', DOBYear = '" + dobYear + "', DOBMonth = '" + dobMonth + "', DOBDay = '" + dobDay + "' WHERE id = '" + position + "'", null);
		editCursor.moveToFirst();
		while(editCursor.isAfterLast() == false) {
            _name = editCursor.getString(1);
			Log.e("Check if the name and position is reading through correctly", "Name" + _name);
			editCursor.moveToNext();
		}
		
		/** Close the database after querying has completed */
		db.close();
	}
	
	/** Method to get all the details */
	public void getDetails(int position) {
		
		position = position + 1;
		/** Query the database */
        SQLiteDatabase db = conDatabase.getWritableDatabase();
        Cursor detailsCursor = db.rawQuery("SELECT * FROM contacts where id = '" + position + "'", null);
        
        detailsCursor.moveToFirst();
        while(detailsCursor.isAfterLast() == false) {
        	String id = detailsCursor.getString(0);
            _name = detailsCursor.getString(1);
            _mobile = detailsCursor.getString(2);
            _email = detailsCursor.getString(3);
            _postcode = detailsCursor.getString(4);
            _dobYear = detailsCursor.getInt(5);
            _dobMonth = detailsCursor.getInt(6);
            _dobDay = detailsCursor.getInt(7);
            String talked = detailsCursor.getString(8);
            Log.e("get all the details", "id: " + id + "Name: " + _name + "Mobile: " + _mobile + 
            			"Email: " + _email + "Postcode: " + _postcode + "Year: " + _dobYear + "Month: " + _dobMonth + "Day: " + _dobDay + "Last talked: " + talked);
            detailsCursor.moveToNext();
        }
	}
	
	/** Method to work out the users age */
	public void age() {
		/** Get the current date */
		Calendar ageCalendar = Calendar.getInstance();
        String timezoneID = TimeZone.getDefault().getID();
        ageCalendar.setTimeZone(TimeZone.getTimeZone(timezoneID));
        currentYear = ageCalendar.get(Calendar.YEAR);
        
        /** Work out the age */
        currentAge = currentYear - userInputYear;
	}
}

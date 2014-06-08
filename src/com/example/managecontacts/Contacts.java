package com.example.managecontacts; 
 
public class Contacts {
     
	/** Class used for custom List */
	
    /** Global Vars */
    private int _id;
    private String name = "";
    private String mobileNumber = "";
    private String emailAddress = "";
    private String postCode = "";
    private int dobYear;
    private int dobMonth;
    private int dobDay;
    private String talkedLast = "";
     
    /** Empty constructor */
    public Contacts() {
    	
    }
  
    /** Constructor for all values of the contact */
    public Contacts(int id, String name, String mobileNumber, String emailAddress, String postCode,
    			int dobYear, int dobMonth, int dobDay, String talkedLast){
        this._id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.postCode = postCode;
        this.dobYear = dobYear;
        this.dobMonth = dobMonth;
        this.dobDay = dobDay;
        this.talkedLast = talkedLast;
    }
     
    /** Constructor for  */
    public Contacts(String name, String mobileNumber, String emailAddress, String postCode,
    		int dobYear, int dobMonth, int dobDay){
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.postCode = postCode;
        this.dobYear = dobYear;
        this.dobMonth = dobMonth;
        this.dobDay = dobDay;
    }
    
    /** Get the id of the contact */
    public int getId() {
    	return _id;
    }
    
    public void setId(int id) {
    	this._id = id;
    }
    
    /** Get the name of the contact */
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    /** Get the mobile of the contact */
    public String getMobile() {
    	return mobileNumber;
    }
    
    public void setMobile(String mobileNumber) {
    	this.mobileNumber = mobileNumber;
    }
    
    /** Get the email of the contact */
    public String getEmail() {
    	return emailAddress;
    }
    
    public void setEmail(String emailAddress) {
    	this.emailAddress = emailAddress;
    }
    
    /** Get the postcode of the contact */
    public String getPostcode() {
    	return postCode;
    }
    
    public void setPostcode(String postCode) {
    	this.postCode = postCode;
    }
    
    /** Get the year of the contacts date of birth */
    public int getDOBYear() {
    	return dobYear;
    }
    
    public void setDOBYear(int dobYear) {
    	this.dobYear = dobYear;
    }
    
    /** Get the month of the contacts date of birth */
    public int getDOBMonth() {
    	return dobMonth;
    }
    
    public void setDOBMonth(int dobMonth) {
    	this.dobMonth = dobMonth;
    }
    
    /** Get the day of the contacts date of birth */
    public int getDOBDay() {
    	return dobDay;
    }
    
    public void setDOBDay(int dobDay) {
    	this.dobDay = dobDay;
    }
    
    /** Get the last time the user talked to the contact */
    public String getTalked() {
    	return talkedLast;
    }
    
    public void setTalked(String talkedLast) {
    	this.talkedLast = talkedLast;
    }
    
}
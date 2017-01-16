# StockManagement
A basic stock management app for android


Initial requirements
https://drive.google.com/open?id=0B6C_X7KA-0m2R2VIb2VIVXFJWTA


Overview of the solution
This is an android based mobile app for managing inventory levels for clinics across Africa. The app relies on firebase real time database. Through this, stock level changes are reflected across all devices simultaneously. 

Functional description
The app can carry out the following functions.
View all clinics and the stock levels of each clinic.
View clinics that have stock levels below five items of any drug.
Dispense drug items, i.e. reduce the stock level of a certain drug in a certain clinic.
Add stock, i.e. increase the stock level of a certain drug in a certain clinic.
Send android system notification for a clinic with a low drug stock level.
Display any change made across any number of devices in real time.

Technical description

The real time database. 
All Firebase Realtime Database data is stored as JSON objects so there are no tables or records. 
The json tree structure for the app is as below.

{
  "clinic": {
    "key": {
      "name": "value",
      "city": "value",
      "country": "value"
    }
  },
   "inventory": {
    "key": {
      "nevirapine": "value",
      "stavudine": "value",
      "zidotabine": "value"
    }
  }
}


2.   The local database
The app employs android sqlite database whose structure is as below.

stockmanagement database

clinicdetails table
_id
clinickey
clinicname
cliniccity
cliniccountry


3.  Database models
There are two data models, clinic and inventory.
Clinic.class
String id;
String name;
String country;
String city;
Integer nevirapine;
Integer stavudine;
Integer zidotabine;

Inventory.class
private String id;
private String drugName;
private String drugItems;

4.	Adapters
There are two adapters employed by the app.
 Clinic adapter - displays all the clinics with all the drug stock levels in each clinic.
Inventory adapter - displays only the clinics with drug stock levels below 5 items.

5.  Activities
There is only one activity that displays two tabs, clinic tab and inventory tab

6. Fragments.
There are two fragments for displaying each of the adapter contents.


Download the application

Version 1.0  can be downloaded using the link below.
https://drive.google.com/file/d/0B6C_X7KA-0m2M1FkZENIYjV3M2M/view?usp=sharing


Note the app relies on an active internet connection for it to function.


Developer
Ramogi Ochola
0713567907
o.ramogi@gmail.com


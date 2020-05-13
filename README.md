# Contact_demo

Setup Information :-

1) Clone the project using the following command :

     " git clone https://github.com/Sunny-swami957/Contact_demo.git "
     
2) After step 1, A folder named Contact_demo will be created in the specified directory.
3) Import the project in Android Studio.
4) Build the project and Run it on an android emulator/device.

Project Information :-

Simple contacts app with three screens. 

1. Home screen :   lists contacts
2. Detail screen :   shows contact details
3. Add contact screen :   allows user to add a contact

Front End Code Information :-

This project has a main activity which acts as a container for 3 different fragments :
1) ContactListFragment
2) ContactDetailFragment
3) AddContactFragment

Starting fragment is set to ContactListFragment .
Navigation between these fragments is handled by the Navigation Controller offered by the android dependencies .
  "  def nav_version = "2.3.0-alpha06"
    implementation "androidx.navigation:navigation-fragment:$nav_version"
    implementation "androidx.navigation:navigation-ui:$nav_version"  "
    
Navigation Controller will take care of all the navigations throughout the application.

1) ContactListFragment

This fragment contains two recycler views:
a) For populating all the contacts ( list recycler view )
b) For populating the alphabets for sorting access on the right side of the screen. ( alphabet recycler view )

We have stored the starting position of each alphabet such that it updates after changes in the contact list.
On clicking the alphabet buttons in the second recyclerview , the scroll position of the first will get changed to get the first element starting with the selected alphabet.

2) ContactDetailFragment

It will navigate from the ContactListFragment and all the details about a contact will be passed from list fragment to detail fragment.

It will fill all the details recieved and It offers the call,message and email option for the specified information.

Edit option is currently unavilaible so you might observe no changes after editing the changes.

3) AddContactFragment

It can be navigated from both ContactListFragment and ContactDetailFragment.
When " + " button on the list fragment will be clicked , this fragment will open with empty fields and will prompt the user to fill the information.

After clicking "Done" button , A new contact will be created and uploaded to the server and the fragment will navigate to the list fragment and scroll to the position of newly added contact.

After clicking "Cancel" button , It will simply return to the previously opened fragment.

If this fragment is navigated through "Edit" button , It will retrieve all the previous information and fill all the non empty information fields. After clicking "Done" button in this case ,will not update the detail as this feature is unavailable at this moment.

Back End Code Information :-

There are two Entity classes specifed int the entity directory.
1) Contact
2) ContactList

1)Contact 

  This is a class that will create an object to store all the contact information.
  
2) ContactList

  This is a singleton class which means only one instance of this class will be created throughout the app to maintain consistency in the local data.
  
  It will have an arraylist of contact objects which will be passed to the list fragment to populate the recycler view.
  This ArrayList<Contact> is a LiveData which means any changes in this list will be observed by the recycler view fragment and the recycler view will update its data. 
  
  This class also contains an arrayList of Positions of each contact starting with the respective alphabet which The list fragment will need in order to scroll the list recycler view while clicking alphabet recycler view. It will be populated by calling the function
  "populateTask()".
  
  This class will perform two server tasks :-
  
  1) Get the data from the server
          It will populate the contact arraylist and also call the "populateTask()" to fill the starting positions of alphabet.
  2) Post the data to the server
        It will add the contact to the server data and It will insert the data in arraylist of contact using the optimal data structure approach " Binary Search ". 
        
        
We have used CircularImageView as a third party dependency for the circular images.

I have covered almost all the information regarding the project.
Feel free to reach me regarding any queries.

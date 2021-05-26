    package com.example.catovacka;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.catovacka.databinding.ActivityMainBinding;
import com.example.catovacka.fragments.Contacts;
import com.example.catovacka.fragments.chats;
import com.example.catovacka.fragments.settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding aBinding;
    private Drawer Drawer;
    private AccountHeader Info;
    private Toolbar toolbar;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(aBinding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        constants.link_into_data = FirebaseDatabase.getInstance().getReference();
        constants.link_into_storage = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nacitatDB();
        } else {
                      
                    InitPermision();
                    InitializeVToolbar();
                    InitializeVariable();
        }
    }

    private void initContact() {
        Cursor phones = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        ,null, null, null, null);

        if (phones != null && phones.getCount() > 0) {
            while (phones.moveToNext()) {
                if (Integer.parseInt(phones.getString(phones.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNumber = phoneNumber.replace(" ","").replace("(","").replace(")","").replace("-","");
                        if(phoneNumber.charAt(0)!='+')
                        {
                            phoneNumber = "+"+phoneNumber;
                        }
                        constants.list.add(new constructorOfContacts(name,phoneNumber));
                        Log.d("test","name = "+name+" phone = "+phoneNumber);
                    }
                }
            }
            phones.close();
        }


    private void InitPermision() {
        if (checkOfPermission(Manifest.permission.READ_CONTACTS)) {
            initContact();
            //System.out.println(constants.list);
        }
    }

    public boolean checkOfPermission(String permision) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, permision) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permision}, 200);
            return false;
        } else
            return true;
    }

    private void nacitatDB() {
        constants.link_into_data.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    constants.cislo = String.valueOf(task.getResult().child("NUMBER").getValue());
                    constants.meno = String.valueOf(task.getResult().child("USER").getValue());
                    InitPermision();
                    requestSmsPermission();
                    //initContact();
                    InitializeVToolbar();
                    InitializeVariable();
                }
            }
        });
    }

    private void InitializeVariable() {
        if (user != null) {
            setSupportActionBar(toolbar);
            getSupportFragmentManager().beginTransaction().replace(R.id.maininfo, new Contacts()).commit();
            infoHornaCast();
            VytvorMenu();

        } else {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }

    }


    private void VytvorMenu() {
        Drawer = new DrawerBuilder()
                .withActivity(this).withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .withAccountHeader(Info)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(2)
                                .withIconTintingEnabled(true)
                                .withName("Chaty")
                                .withSelectable(false).withIcon(R.drawable.ic_launcher_foreground),
                        new PrimaryDrawerItem().withIdentifier(4)
                                .withIconTintingEnabled(true)
                                .withName("Nastavenia")
                                .withSelectable(false).withIcon(R.drawable.ic_launcher_settings),
                        new PrimaryDrawerItem().withIdentifier(5)
                                .withIconTintingEnabled(true)
                                .withName("Pozvať priateľov").withIcon(R.drawable.ic_launcher_personadd)
                                .withSelectable(false)
                ).withOnDrawerItemClickListener((view, i, iDrawerItem) -> {
                    switch (i) {
                        case 1:
                            getSupportFragmentManager().beginTransaction().replace(R.id.maininfo, new Contacts()).commit();
                            break;
                        case 2:
                            getSupportFragmentManager().beginTransaction().replace(R.id.maininfo, new settings()).commit();
                            break;
                        case 3:
                            altershow();
                            break;
                    }

                    return false;
                })
                .build();
    }
    private void  altershow()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setTitle("Enter phone number");
        alert.setMessage("");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sendsms(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void sendsms(String phoneNumber) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "Hi i'm start to uset application čatovačka. Download and start use app!", null, null);

    }

    private void infoHornaCast() {
        Resources res = getResources();
        Drawable img = res.getDrawable(R.drawable.ic_verified);
        Info = new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.drawable.hlavicka).
                addProfiles(new ProfileDrawerItem().withName(constants.meno).withEmail(constants.cislo).withIcon(img)).build();
    }

    private void InitializeVToolbar() {
        toolbar = aBinding.HornaCast;
    }

    public void zmena_mena(View view) {
        EditText text = findViewById(R.id.novemeno);
        String p = text.getText().toString();
        if (p.equals(null) || p.equals("")) {

        } else {
            constants.meno = p;
            Map<String, Object> info_to_databse = new HashMap<>();
            info_to_databse.put("ID", user.getUid());
            info_to_databse.put("NUMBER", constants.cislo);
            info_to_databse.put("USER", p);
            constants.link_into_data.child("users").child(user.getUid()).updateChildren(info_to_databse);
            InitializeVariable();
            getSupportFragmentManager().beginTransaction().replace(R.id.maininfo, new settings()).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            InitPermision();
        }


    }

    private void requestSmsPermission() {
        // chec permission is given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},200);
        }
    }
}
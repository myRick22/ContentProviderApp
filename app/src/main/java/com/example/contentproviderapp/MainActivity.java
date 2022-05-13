package com.example.contentproviderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton imgContacts;

    private static final int RCREQUESTCODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgContacts = findViewById(R.id.imgContacts);

        imgContacts.setOnClickListener(view -> {

            askForReadContactsPermission();
        });
    }

    private void askForReadContactsPermission() {
        //se il codice per accedere ai contatti del telefono è diverso dal permesso ottenuto
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //allora devo chiedere il permesso di accedere
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS }, RCREQUESTCODE);
        } else {
            //utente ha già il permesso di accedere
            askForContacts();
        }
    }

    private void askForContacts() {

        //
        ContentResolver contentResolver = getContentResolver();
        //accediamo il singolo contatto del telefono
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri,null,null,null,null);

        Log.i("CONTENT_PROVIDER", "Number of contats is" + cursor.getCount());

        if (cursor.getCount()> 0) {

            while (cursor.moveToNext()) {

                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        }

    }


    //codice che riceve il risultato dalla richiesta del permesso di accedere ai contatti
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RCREQUESTCODE:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    askForContacts();
                } else {

                }
                return;

        }
    }
}
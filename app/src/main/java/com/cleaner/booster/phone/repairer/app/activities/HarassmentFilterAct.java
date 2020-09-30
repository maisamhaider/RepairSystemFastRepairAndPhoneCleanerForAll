package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

public class HarassmentFilterAct extends BaseActivity {
    Utils utils;
    Button copyContact_btn, block_unBlock_btn;
    static final int PICK_CONTACT = 1;
    String cNumber;
    String name;
    TelecomManager telecomManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harassment_filter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);
        telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        adView(findViewById(R.id.harassmentFilter_adView));
        copyContact_btn = findViewById(R.id.copyContact_btn);
        block_unBlock_btn = findViewById(R.id.block_unBlock_btn);

        copyContact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        block_unBlock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startActivity(telecomManager.createManageBlockedNumbersIntent(), null);
                }
            }
        });


    }

    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == RESULT_OK) {

                    try {
                        Uri contactData = data.getData();
                        Cursor c = managedQuery(contactData, null, null, null, null);
                        if (c.moveToFirst()) {
                            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                phones.moveToFirst();
                                cNumber = phones.getString(phones.getColumnIndex("data1"));
                                System.out.println("number is:" + cNumber);
                            }
                            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText(name, cNumber);
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(this, "Number is copied" + cNumber, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        Toast.makeText(HarassmentFilterAct.this, "Please select number again", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
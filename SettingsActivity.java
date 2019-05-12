package com.arvindsudheer.letschat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button UpdateAccountSettings;
    private TextView UploadID;
    private EditText userName;
    private CircleImageView userProfileImage;
    private EditText userAddress;
    private EditText dateOfBirth;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        
        Initializefields();



        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSettings();
            }
        });
        RetrieveUserInfo();

//        userProfileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent galleryIntent = new Intent();
//               // galleryIntent.setAction()
//
//            }
//        });
    }


    private void UpdateSettings() {

        String setUserName = userName.getText().toString();
        String setUserAddress = userAddress.getText().toString();
//        String setUserDOB = dateOfBirth.getText().toString();

        if(TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(SettingsActivity.this, "Please enter username...",Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(setUserAddress))
        {
            Toast.makeText(SettingsActivity.this, "Please enter address..",Toast.LENGTH_SHORT).show();

        }
        else
        {
            HashMap<String,String> profileMap = new HashMap<>();
            profileMap.put("uid",currentUserID);
            profileMap.put("name",setUserName);
            profileMap.put("address",setUserAddress);


            RootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, " Profile Updated Successfully",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, " Error"+message,Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
    private void RetrieveUserInfo() {

        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveAddress = dataSnapshot.child("address").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

                            userName.setText(retrieveUserName);
                            userAddress.setText(retrieveAddress);

                        }
                        else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveAddress = dataSnapshot.child("address").getValue().toString();


                            userName.setText(retrieveUserName);
                            userAddress.setText(retrieveAddress);
                        }
                        else
                        {

                            Toast.makeText(SettingsActivity.this, "Please set and update your profile info",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void Initializefields() {
        UpdateAccountSettings = (Button)findViewById(R.id.update_settings_button);
        UploadID = (TextView) findViewById(R.id.upload_profile_details);
        userName = (EditText) findViewById(R.id.set_user_name);
        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);
        userAddress = (EditText) findViewById(R.id.set_profile_address);
        dateOfBirth = (EditText) findViewById(R.id.set_profile_dob);

    }
    public void SendUserToMainActivity() {
        Intent mainIntent;
        mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainIntent);
        finish();

    }
}

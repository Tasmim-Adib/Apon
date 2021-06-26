package com.example.evelyn;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;



public class SettingsActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private EditText donationdate,fullnameEditText,ageEditText,thanaEdit,contactEdit;
    private Button submitButton;
    private Uri imageUri;
    private Bitmap imageStore;
    private ImageView calenderIcon;
    private AutoCompleteTextView districtEdit,hospitalEdit,bloodGroupEditText;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef;
    private StorageReference storageReference;
    private ProgressDialog loadingBar;

    String currentUserID;
    DatePickerDialog.OnDateSetListener setListener;


    public String[] districts = {"Jashore", "Dhaka", "Gazipur", "Narsingdi", "Manikganj", "Munshiganj", "Narayanganj", "Mymensingh"
            , "Sherpur", "Jamalpur", "Netrokona", "Kishoreganj", "Tangail", "Faridpur", "Maradipur", "Shariatpur", "Rajbari","Gopalganj"
    ,"Khulna","Bagerhat","Chuadangah","Jhenaidah","Satkhira","Kustia","Magura","Meherpur","Narail","Rajshahi","Natore","Pabna"
            ,"Bogura","Chapainawabganj","Joypurhat","Naogaon","Sirajganj","Dinajpur","Kurigram","Gaibandha","Lalmonirhat","Nilphamari",
    "Panchagarh","Rangpur","Thakurgaon","Brahmanbaria","Cumilla","Chandpur","Lakshmipur","Noakhali","Feni","Khagrachhari","Rangamati","Bandarban"
    ,"Chattogram","Cox's Bazar","Barishal","Borguna","Bhola","Jhalokathi","Patuakhali","Pirojpur","Habiganj","Moulvibazar","Sunamganj","Sylhet"
    };

    String[] hospitals = {"Dhaka Medical College, Dhaka","Bangabandhu Shikh Mujib Medical University, Dhaka","Jashore Medical College, Jashore",
    "Sher-e-Bangla Medical College, Barishal","Sir Salimullah Medical College, Dhaka","Shaheed Suhrawardy Medical College, Dhaka",
    "Mymensingh Medical College, Mymensingh","Chattogram Medical College, Chattogram","Rajshahi Medical College, Rajshahi","Sylhet MAG Osmani Medical College, Sylhet",
    "Rangpur Medical College, Rangpur","Cumilla Medical College, Cumilla","Khulna Medical College, Khulna","Shaheed Ziaur Rahman Medical College, Bogura",
    "Bangabandhu Sheikh Mujib Medical College, Faridpur","M Abdur Rahim Medical College, Dinajpur","Pabna Medical College, Pabna","Abdul Malek Ukil Medical College, Noakhali"
    ,"Cox's Bazar Medical College, Cox's Bazar","Satkhira Medical College, Satkhira","Shahid Syed Nazrul Islam Medical College, Kishoreganj","Kushtia Medical College, Kustia",
            "Sheikh Sayera Khatun Medical College, Gopalganj","Shaheed Tajuddin Ahmad Medical College, Gazipur","Sheikh Hasina Medical College, Tangail","Sheikh Hasina Medical College, Jamalpur",
    "Colonel Malek Medical College, Manikganj","Shaheed M. Monsur Ali Medical College, Sirajganj","Patuakhali Medical College, Patuakhali",
    "Rangamati Medical College, Rangamati","Mugda Medical College, Dhaka","Sheikh Hasina Medical College, Habiganj","Netrokona Medical College, Netrokona","Nilphamari Medical College, Nilphamari",
    "Magura Medical College, Magura","Naogaon Medical College, Naogaon","Chandpur Medical College, Chandpur","Bangabandhu Medical College, Sunamganj","Armed Forces Medical College, Dhaka Cantonment",
            "Army Medical College, Bogra","Army Medical College, Chittagong","Army Medical College, Cumilla","Army Medical College, Jessore","Rangpur Army Medical College, Rangpur","National Medical College, Dhaka"
    ,"Ibrahim Medical College, Dhaka","Bangladesh Medical College, Dhaka","Holy Family Red Crescent Medical College, Dhaka","Jahurul Islam Medical College, Kishoreganj","Uttara Adhunik Medical College, Dhaka",
    "Shaheed Monsur Ali Medical College, Dhaka","Enam Medical College and Hospital, Dhaka","Community Based Medical College, Mymensingh","Ibn Sina Medical College, Dhaka","Shahabuddin Medical College, Dhaka",
            "Medical College for Women & Hospital, Dhaka", "Z. H. Sikder Women's Medical College, Dhaka", "Kumudini Women's Medical College, Tangail","Tairunnessa Memorial Medical College, Gazipur","Ad-din Women's Medical College, Dhaka","International Medical College, " +
            "Gazipur","Central Medical College, Cumilla","B.G.C Trust Medical College, Chattogram","Eastern Medical College, Cumilla","Islami Bank Medical College, Rajshahi",
    "Khwaja Yunus Ali Medical College", "Jalalabad Ragib-Rabeya Medical College, Sylhet","East West Medical College, Dhaka","North East Medical College, Sylhet","Institute of Applied Health Sciences, Chattogram","Gonoshasthaya Samaj Vittik Medical College, Dhaka",
    "Chattagram Maa-O-Shishu Hospital Medical College, Chattogram","T.M.S.S. Medical College, Bogra","Prime Medical College, Rangpur","North Bengal Medical College, Sirajganj","Rangpur Community Medical College, Rangpur"
    ,"Delta Medical College, Dhaka","Southern Medical College, Chattogram","Ad-din Sakina Medical College, Jashore","Popular Medical College, Dhaka","Green Life Medical College, Dhaka","Dhaka Community Medical College, Dhaka",
    "Dhaka Community Hospital, Dhaka","Japan East West Medical College Hospital, Dhaka", "Ad-din Akij Medical College Hospital, Khulna","Aichi Hospital, Dhaka", "Arif Memorial Hospital, Barishal",
            "Al Haramain Hospital, Sylhet","Ambia Memorial Hospital, Barisal","Anwer Khan Modern Hospital Ltd, Dhaka","Apollo Hospitals Dhaka","Asgar Ali Hospital Gandaria, Dhaka","Aysha Memorial Specialised Hospital, Dhaka",
  "Bangabandhu Memorial Hospital (BBMH), Chittagong", "Bangladesh College of Nursing","Bangladesh Eye Hospital Ltd., Dhaka","Bangladesh Medical College Hospital, Dhaka","Bangladesh Specialized Hospital","Bangladesh Spine & Orthopaedic General Hospital Ltd, Dhaka",
 "Basundhura Hospital (Pvt.) Ltd","BDR (Bangladesh Rifles) Hospital","BRB Hospital- Panthapath Dhaka","Cardio Care Specialized and General Hospital Ltd, Dhaka", "CARe Hospital, Dhaka","Care Zone Hospital, Dhaka","Catharsis Medical Centre Limited, Gazipur",
   "Central Hospital, Dhaka","Chander Hasi Hospital Limited, Habigonj, Sylhet.","Chittagong Eye Infirmary and Training Hospital","Chittagong Diabetic General Hospital","Continental Hospital Ltd","Cox's Bazar Hospital for Women & Children, Chittagong",
"Dhaka Central International Medical College Hospital, Adabor, Dhaka","Dhaka Dental College and Hospital","Dhaka Hospital, Dhaka","Dhaka Shishu Hospital","Dr. Alauddin Ahmed Clinic, Jhalakati","Duwell Medical","Eastern Hospital & Medical Research Centre","Esperto Health Care & Research Center, Dhaka",
"Farazy Hospital Ltd, Dhaka","Farazy Dental Hospital & Research Center, Dhaka","Gazi Medical College Hospital, Khulna","Genuine Cancer Hospital Limited, Chittagong","Government Homeopathic Medical College Hospital", "Greenland Hospital Limited, Uttara, Dhaka",
"Holy Family Red Crescent Medical College Hospital, Dhaka","Ibn Sina Hospital Sylhet Ltd", "Ibn Sina Hospitals, Dhaka", "Institute of Child and Mother Health, Dhaka","Institute of Laser Surgery & Hospital, Dhaka","Ispahani Islamia Eye Institute and Hospital (IIEI&H)",
"Khwaja Yunus Ali Medical College and Hospital","Labaid Cardiac Hospital, Dhaka","Labaid Specialized Hospital], Dhaka","Maa Nursing Home & Diagnostic Centre, Tangail","Medinova Medical Services Ltd.[14]","MH Samorita Hospital & Medical College, Tejgaon, Dhaka","Mikrani Dental Banasree Dhaka (Dental Hospital) Dhaka",
"Mojibunnessa Eye Hospital, Dhaka","Mount Adora Hospital, Sylhet.","National Heart Foundation, Sylhet.","National Institute of Cardiovascular Diseases","National Institute of Ear, Nose and Throat","National Institute of Kidney Disease & Urology","National Institute of Mental Health",
"National Institute of Neuroscience","National Institute of Preventive and Social Medicine","North East Medical College and Hospital","Nurjahan Hospital Ltd, Sylhet.", "Oasis Hospital (Pvt) Ltd, Sylhet",
"Pongu Hospital Jessore ","Rashmono General Hospital, Dhaka","Royal Hospital And Research Center Ltd., Chittagong","Saint Martin Hospital","Samorita Hospital Ltd.","Saphena Women's Dental College & Hospital","Sheikh Fazilatunnesa Mujib Memorial KPJ Specialized Hospital & Nursing College",
"Siddiqia Eye Foundation, Mymensingh,Square Hospital Ltd, Dhaka", "Sylhet Eye Hospital & Laser Centre","Sylhet Maa O Shishu Hospital","Sylhet Women's Medical College","Union Specialized Hospital Limited, Aftabnagor, Dhaka","United Hospital Ltd., Dhaka"};

    String[] groups = {"A+","A-","B+","B-","AB+","AB-","O+","O-"};


    ArrayAdapter<String> distAdapter,hospitalAdapter,groupAdapter;
    private static final int galleryPick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        circleImageView = (CircleImageView)findViewById(R.id.profile_image);
        loadingBar = new ProgressDialog(this);
        fullnameEditText = (EditText)findViewById(R.id.fullnameEditText);
        ageEditText = (EditText)findViewById(R.id.ageEditText);
        bloodGroupEditText = (AutoCompleteTextView) findViewById(R.id.BloodGroupEditText);
        thanaEdit = (EditText)findViewById(R.id.thanaEditText);
        districtEdit = (AutoCompleteTextView) findViewById(R.id.districtEditText);
        contactEdit = (EditText)findViewById(R.id.ContactEditText);
        hospitalEdit = (AutoCompleteTextView) findViewById(R.id.hospitalEditText);
        donationdate = (EditText)findViewById(R.id.lastdonationEditText);
        submitButton = (Button)findViewById(R.id.submitButtonId);
        calenderIcon = (ImageView)findViewById(R.id.calenderIcon);


        distAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,districts);
        districtEdit.setThreshold(1);
        districtEdit.setAdapter(distAdapter);

        groupAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,groups);
        bloodGroupEditText.setThreshold(1);
        bloodGroupEditText.setAdapter(groupAdapter);

        hospitalAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,hospitals);
        hospitalEdit.setThreshold(1);
        hospitalEdit.setAdapter(hospitalAdapter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid().toString();
        RootRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images");

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

       calenderIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               DatePickerDialog datePickerDialog;
               datePickerDialog = new DatePickerDialog(SettingsActivity.this,  new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       month = month + 1;
                       String date = dayOfMonth + "/" + month + "/" + year;
                       donationdate.setText(date);
                   }
               },year,month,day);



               datePickerDialog.show();



           }
       });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });
    }



    private void chooseImage() {

            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,galleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
                imageUri = data.getData();

                imageStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                circleImageView.setImageBitmap(imageStore);

            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void saveData() {

        final String name = fullnameEditText.getText().toString();
        final String age = ageEditText.getText().toString();
        final String bloodGroup = bloodGroupEditText.getText().toString();
        final String thana = thanaEdit.getText().toString();
        final String district = districtEdit.getText().toString();
        final String contact = contactEdit.getText().toString();
        final String hospital = hospitalEdit.getText().toString();
        final String lastDate = donationdate.getText().toString();

        if(TextUtils.isEmpty(name)){
            fullnameEditText.setError("please give your name");
            fullnameEditText.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(age)){
            ageEditText.setError("please give your age");
            fullnameEditText.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(bloodGroup)){
            bloodGroupEditText.setError("please give your Blood Group");
            bloodGroupEditText.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(thana)){
            thanaEdit.setError("give your thana name");
            thanaEdit.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(district)){
            districtEdit.requestFocus();
            districtEdit.setError("give your district name");
            return;
        }
        if(TextUtils.isEmpty(contact)){
            contactEdit.setError("contact number please");
            contactEdit.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(lastDate)){
            donationdate.requestFocus();
            donationdate.setError("please enter the last donation date");
            return;
        }
        if(TextUtils.isEmpty(hospital)){
            hospitalEdit.setError("nearest hospital name");
            hospitalEdit.requestFocus();
            return;
        }

        else{
            loadingBar.setTitle("Register");
            loadingBar.setMessage("Your information is uploading");

            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();


            StorageReference filePath = storageReference.child(currentUserID+"." +getFileExtension(imageUri));
            filePath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadUrl = uriTask.getResult();

                            Upload upload = new Upload(name,age,bloodGroup,thana,district,contact,hospital,downloadUrl.toString(),lastDate);
                            HashMap hash = new HashMap();
                            hash.put("district",district);
                            hash.put("hospital",hospital);

                            RootRef.child(bloodGroup).child(currentUserID).setValue(hash);
                            RootRef.child("Users").child(currentUserID).setValue(upload);
                            loadingBar.dismiss();
                            Intent mainIntent = new Intent(getApplicationContext(),FirstActivity.class);
                            startActivity(mainIntent);
                            Toast.makeText(SettingsActivity.this, "Successfully stored in firestore", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(SettingsActivity.this, "Error :"+exception, Toast.LENGTH_SHORT).show();
                        }
                    });



        }

    }



}

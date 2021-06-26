package com.example.evelyn;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class UpdateFragment extends Fragment {

    private View myUpdateView;
    private AutoCompleteTextView hospitalEditText;
    private EditText lastDate;
    private ImageView calenderIcon;
    private Button doneButton;
    private Calendar myCalendar;
    private DatabaseReference databaseReference;
    private String currentUser;
    private ArrayAdapter<String> updateHospitalAdapter;
    SettingsActivity settingsActivity = new SettingsActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myUpdateView = inflater.inflate(R.layout.fragment_update,container,false);
        hospitalEditText = myUpdateView.findViewById(R.id.last_hospital_edit);
        lastDate = myUpdateView.findViewById(R.id.last_blood_donation_edit);
        doneButton = myUpdateView.findViewById(R.id.last_blood_donation_button);
        calenderIcon = myUpdateView.findViewById(R.id.update_calender_icon);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        calenderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        updateHospitalAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,settingsActivity.hospitals);
        hospitalEditText.setThreshold(1);
        hospitalEditText.setAdapter(updateHospitalAdapter);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeHistory();
            }
        });


        return myUpdateView;

    }

    private void makeHistory() {
        String lastDonate = lastDate.getText().toString();
        String lastHospital = hospitalEditText.getText().toString();

        if(TextUtils.isEmpty(lastDonate)){
            lastDate.setError("Set a date");
            lastDate.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(lastHospital)){
            hospitalEditText.requestFocus();
            hospitalEditText.setError("Name of Hospital");
            return;
        }

        else{
            String historyKey = databaseReference.push().getKey();
            databaseReference.child("Users").child(currentUser).child("lastDate").setValue(lastDonate);
            HistoryUpload historyUpload = new HistoryUpload(lastDonate,lastHospital);
            databaseReference.child("History").child(currentUser).child(historyKey).setValue(historyUpload);
            Toast.makeText(getContext(),"Added to your History",Toast.LENGTH_SHORT).show();
            lastDate.setText(null);
            hospitalEditText.setText(null);
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        lastDate.setText(sdf.format(myCalendar.getTime()));
    }
}

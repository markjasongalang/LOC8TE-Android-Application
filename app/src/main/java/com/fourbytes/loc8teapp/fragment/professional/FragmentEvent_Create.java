package com.fourbytes.loc8teapp.fragment.professional;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourbytes.loc8teapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FragmentEvent_Create extends Fragment {

    private View view;

    private AppCompatButton btnBack;
    private AppCompatButton btnSave;
    private AppCompatButton btnSetLocation;

    private EditText titleEditView;
    private EditText locationEditView;
    private EditText descriptionEditView;
    private EditText dateEditView;
    private EditText startTimeEditView;
    private EditText endTimeEditView;
    private EditText parkingSlotsEditView;
    private TextView btnCancel;

    private double latitude;
    private double longitude;
    private boolean isLocationSet = false;
    private  boolean isValidTime = false;
    private  boolean isValidDate = false;
    private FragmentManager fragmentManager;
    public FragmentEvent_Create() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_create, container, false);
        //edit views
        titleEditView = view.findViewById(R.id.edit_title);
        locationEditView = view.findViewById(R.id.edit_location);
        descriptionEditView = view.findViewById(R.id.edit_description);
        dateEditView = view.findViewById(R.id.edit_date);
        startTimeEditView = view.findViewById(R.id.edit_time_start);
        endTimeEditView = view.findViewById(R.id.edit_time_end);
        parkingSlotsEditView = view.findViewById(R.id.edit_parkingslots);
        //buttons
        btnBack = view.findViewById(R.id.btn_back);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);

        fragmentManager = getParentFragmentManager();

        endTimeEditView.setEnabled(false);
        locationEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_SetLocation.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: function for Save changes
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_MyEvents_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

            }
        });

        dateEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar newCalendar = Calendar.getInstance();

                final DatePickerDialog StartTime = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date todayDate = new Date();
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        try {
                            if(dateFormatter.parse(dateFormatter.format(newDate.getTime())).after(dateFormatter.parse(dateFormatter.format(todayDate.getTime())))){
                                System.out.println("Valid Date");
                                isValidDate = true;
                            }else{
                                System.out.println("Invalid Date");
                                isValidDate = false;
                            }

                            dateEditView.setText(dateFormatter.format(newDate.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                StartTime.show();
            }
        });

        startTimeEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTimeEditView.setText( selectedHour + ":" + selectedMinute);
                        endTimeEditView.setEnabled(true);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        endTimeEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        System.out.println(startTimeEditView);
                        String endTime = selectedHour + ":" + selectedMinute;
                        String startTime = String.valueOf(startTimeEditView.getText());
                        endTimeEditView.setText(selectedHour + ":" + selectedMinute);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

                        try {
                            if(dateFormat.parse(endTime).after(dateFormat.parse(startTime)))
                            {
                                System.out.println("Valid Time");
                                isValidTime = true;
                            }else{
                                System.out.println("Invalid Time");
                                isValidTime = false;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, FragmentEvent_MyEvents_Professional.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        fragmentManager.setFragmentResultListener("locationData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                setLocationText(result.getDouble("latitude"), result.getDouble("longitude"));

            }
        });

        return view;
    }

    private void createEvent(String title, double longitude, double latitude, String description, Date date, Time start, Time end, int parkingSlots){

    }

    private void setLocationText(double latitude, double longitude){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        final String API_KEY = getString(R.string.google_maps_api_key);

        String location_string = latitude + ", " + longitude;

        System.out.println(location_string);
        String url = Uri.parse("https://maps.googleapis.com/maps/api/geocode/json")
                .buildUpon()
                .appendQueryParameter("latlng", location_string)
                .appendQueryParameter("key", API_KEY)
                .toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");

                            if(status.equals("OK")){
                                JSONArray results = response.getJSONArray("results");
                                String formatted_address = results.getJSONObject(0).getString("formatted_address");
                                locationEditView.setText(formatted_address);
                                isLocationSet = true;
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
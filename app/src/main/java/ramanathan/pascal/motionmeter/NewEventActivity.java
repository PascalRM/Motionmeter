package ramanathan.pascal.motionmeter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ramanathan.pascal.motionmeter.model.Event;
import ramanathan.pascal.motionmeter.model.Events;

public class NewEventActivity extends AppCompatActivity {

    EditText name;
    EditText date;
    EditText startTime;
    DatePickerDialog datePickerDialog;
    EditText endTime;

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        name = findViewById(R.id.editTextName);
        date = findViewById(R.id.editTextDate);
        startTime = findViewById(R.id.editTextStartTime);
        endTime = findViewById(R.id.editTextEndTime);
    }

    public void updateEvent(){
        Event newEvent = Events.getInstance().getNewEvent();

        try {
            newEvent.setStartdate(formatter.parse(startTime.getText().toString()+" "+date.getText().toString()));
            newEvent.setEnddate(formatter.parse(endTime.getText().toString()+" "+date.getText().toString()));
            newEvent.setName(name.getText().toString());
        } catch (Exception e) {
            System.out.println(e);
        }

        Events.getInstance().setNewEvent(newEvent);
    }

    //* --- OnClick Events --- *

    public void onClickDateView(View v) {
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        datePickerDialog = new DatePickerDialog(NewEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        date.setError(null);
    }

    public void onClickTime(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selectedHourString = String.valueOf(selectedHour);
                String selectedMinuteString = String.valueOf(selectedMinute);

                if(selectedHourString.length()==1){
                    selectedHourString = "0" + selectedHourString;
                }

                if(selectedMinuteString.length() == 1){
                    selectedMinuteString = "0" + selectedMinuteString;
                }

                startTime.setText( selectedHourString + ":" + selectedMinuteString);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Wählen Sie die Startzeit aus");
        mTimePicker.show();
        startTime.setError(null);
    }

    public void onClickEndTime(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selectedHourString = String.valueOf(selectedHour);
                String selectedMinuteString = String.valueOf(selectedMinute);

                if(selectedHourString.length()==1){
                    selectedHourString = "0" + selectedHourString;
                }

                if(selectedMinuteString.length() == 1){
                    selectedMinuteString = "0" + selectedMinuteString;
                }

                endTime.setText( selectedHourString + ":" + selectedMinuteString);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Wählen Sie die Endzeit aus");
        mTimePicker.show();
        endTime.setError(null);
    }

    public boolean checkInput(){
        boolean keineFehler = true;

        if(TextUtils.isEmpty(name.getText())){
            name.setError("Bitte Namen eingeben");
            keineFehler = false;
        }

        if(TextUtils.isEmpty(date.getText())){
            date.setError("Bitte Datum Auswählen");
            keineFehler = false;
        }
        if(TextUtils.isEmpty(startTime.getText())){
            startTime.setError("Bitte Startzeit eingeben");
            keineFehler = false;
        }
        if(TextUtils.isEmpty(endTime.getText())){
            endTime.setError("Bitte Endzeit eingeben");
            keineFehler = false;
        }


        // Uhrzeit Fehler check
        try{
            Date startTimeDate = formatter.parse(startTime.getText().toString()+" "+date.getText().toString());
            Date endTimeDate = formatter.parse(endTime.getText().toString()+" "+date.getText().toString());

            if(!endTimeDate.after(startTimeDate)){
                endTime.setError("Gueltige Zeit auswaehlen");
                keineFehler = false;
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

        return keineFehler;
    }

    public void nextView(View view) {
        if(checkInput() == true){
            updateEvent();

            Intent intent = new Intent(this, NewEvent2Activity.class);
            startActivity(intent);
        }
    }
}

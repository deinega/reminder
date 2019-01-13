package com.torontodjango.reminder;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.content.Intent;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private final String TAG = "EditActivity";

    private EditText name;
    private CheckBox enabled;
    private Button dateButton;
    private Button timeButton;

    private Task task;

    private GregorianCalendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Log.d(TAG, "getting view values");

        name = (EditText)findViewById(R.id.name);
        enabled = (CheckBox)findViewById(R.id.enabled);
        dateButton = (Button)findViewById(R.id.date_button);
        timeButton = (Button)findViewById(R.id.time_button);

        Log.d(TAG, "got view values");

        task = new Task();
        task.fromIntent(getIntent());

        name.setText(task.getName());
        name.addTextChangedListener(nameChangedListener);

        enabled.setChecked(task.getEnabled());
        enabled.setOnCheckedChangeListener(enabledChangeListener);

        calendar = new GregorianCalendar();
        calendar.setTimeInMillis(task.getDate());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        updateButtons();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (DATE_DIALOG_ID == id)
            return new DatePickerDialog(this, dateSetListener, year, month, day);
        else if (TIME_DIALOG_ID == id)
            return new TimePickerDialog(this, timeSetListener, hour, minute, true);
        else
            return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        if (DATE_DIALOG_ID == id)
            ((DatePickerDialog)dialog).updateDate(year, month, day);
        else if (TIME_DIALOG_ID == id)
            ((TimePickerDialog)dialog).updateTime(hour, minute);
    }

    public void onDateClick(View view){
        showDialog(DATE_DIALOG_ID);
    }

    public void onTimeClick(View view)
    {
        showDialog(TIME_DIALOG_ID);
    }

    public void onDoneClick(View view)
    {
        String errorMessage = null;

        if (task.getName().isEmpty()) {
            errorMessage = "Please specify a name for your task";
        }
        else if(task.getOutdated()){
            errorMessage = "Please specify future time for your task";
        }

        if (errorMessage!=null){
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Intent intent = new Intent();

        task.toIntent(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onCancelClick(View view)
    {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year_, int month_, int day_)
        {
            year = year_;
            month = month_;
            day = day_;

            calendar = new GregorianCalendar(year, month, day, hour, minute);
            task.setDate(calendar.getTimeInMillis());

            updateButtons();
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener()
    {
        public void onTimeSet(TimePicker view, int hour_, int minute_)
        {
            hour = hour_;
            minute = minute_;

            calendar = new GregorianCalendar(year, month, day, hour, minute);
            task.setDate(calendar.getTimeInMillis());

            updateButtons();
        }
    };

    private TextWatcher nameChangedListener = new TextWatcher()
    {
        public void afterTextChanged(Editable s)
        {
            task.setName(name.getText().toString());
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }
    };

    private CompoundButton.OnCheckedChangeListener enabledChangeListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            task.setEnabled(isChecked);
        }
    };

    private void updateButtons()
    {
        dateButton.setText(DAO.formatDate(task));
        Log.d(TAG, "updated date button");
        timeButton.setText(DAO.formatTime(task));
        Log.d(TAG, "updated time button");
    }

}

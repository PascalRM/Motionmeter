package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import ramanathan.pascal.motionmeter.model.Event;
import ramanathan.pascal.motionmeter.model.Events;

public class NewEvent2Activity extends AppCompatActivity {

    EditText beschreibung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event2);

        beschreibung = findViewById(R.id.editTextBeschreibung);
    }

    public void updateEvent() {
        Event newEvent = Events.getInstance().getNewEvent();

        try {
            newEvent.setBeschreibung(beschreibung.getText().toString());
        } catch (Exception e) {
            System.out.println(e);
        }

        Events.getInstance().setNewEvent(newEvent);
    }


    public void nextView(View view) {
        if (TextUtils.isEmpty(beschreibung.getText())) {
            beschreibung.setError("Bitte Beschreibung erstellen");
        } else {
            updateEvent();

            Intent intent = new Intent(this, NewEventPasswordActivity.class);
            startActivity(intent);
        }
    }
}

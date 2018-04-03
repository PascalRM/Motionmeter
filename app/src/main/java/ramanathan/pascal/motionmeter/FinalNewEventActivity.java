package ramanathan.pascal.motionmeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import ramanathan.pascal.motionmeter.model.Event;
import ramanathan.pascal.motionmeter.model.Events;

public class FinalNewEventActivity extends AppCompatActivity {

    TextView name;
    TextView datum;
    TextView startZeit;
    TextView endZeit;
    TextView beschreibung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_new_event);

        Event event = Events.getInstance().getNewEvent();

        name = findViewById(R.id.textViewName);
        datum = findViewById(R.id.textViewDate);
        startZeit = findViewById(R.id.textViewStartTime);
        endZeit = findViewById(R.id.textViewEndZeit);
        beschreibung = findViewById(R.id.textViewBeschreibung);

        name.setText(event.getName());
        startZeit.setText(event.getStartdate().toString());
        endZeit.setText(event.getEnddate().toString());
        beschreibung.setText(event.getBeschreibung());
    }

    public void onClickInsert(View view){
        //TODO Event eintragen in Datenbank
    }
}

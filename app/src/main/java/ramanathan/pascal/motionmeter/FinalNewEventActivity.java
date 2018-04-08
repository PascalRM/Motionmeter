package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import ramanathan.pascal.motionmeter.controller.EventController;
import ramanathan.pascal.motionmeter.model.Event;

public class FinalNewEventActivity extends AppCompatActivity {

    TextView name;
    TextView startZeit;
    TextView endZeit;
    TextView beschreibung;

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_new_event);

        Event event = EventController.getInstance().getNewEvent();

        name = findViewById(R.id.textViewName);
        startZeit = findViewById(R.id.textViewStartTime);
        endZeit = findViewById(R.id.textViewEndZeit);
        beschreibung = findViewById(R.id.textViewBeschreibung);

        name.setText(event.getName());
        startZeit.setText(formatter.format(event.getStartdate()));
        endZeit.setText(formatter.format(event.getEnddate()));
        beschreibung.setText(event.getBeschreibung());
    }

    public void onClickInsert(View view){
        Intent intent = new Intent(this,GenerateNewEventActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}

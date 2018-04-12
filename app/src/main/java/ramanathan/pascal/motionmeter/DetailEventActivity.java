package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ramanathan.pascal.motionmeter.model.Event;

public class DetailEventActivity extends AppCompatActivity {

    TextView name;
    TextView beschreibung;
    TextView zeitAnfang;
    TextView zeitEnde;

    Button eintragen;

    Event event;

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        event = (Event)getIntent().getSerializableExtra("event");

        name = findViewById(R.id.textView_nameEvent);
        beschreibung = findViewById(R.id.textView_beschreibung);
        zeitAnfang = findViewById(R.id.textView_zeitAnfang);
        zeitEnde = findViewById(R.id.textView_zeitEnde);
        eintragen = findViewById(R.id.button_eintragen);

        name.setText(event.getName());
        beschreibung.setText(event.getBeschreibung());
        zeitAnfang.setText(formatter.format(event.getStartdate()));
        zeitEnde.setText(formatter.format(event.getEnddate()));


        if(event.getEnddate().before(new Date())){
           eintragen.setEnabled(false);
        }
    }

    public void OnClickNext(View view){
        Intent m = new Intent(this, LoginEventActivity.class);
        m.putExtra("event",event);
        startActivity(m);
        finish();
    }

    public void OnClickBack(View view){
        finish();
    }
}

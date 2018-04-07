package ramanathan.pascal.motionmeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ramanathan.pascal.motionmeter.model.Event;
import ramanathan.pascal.motionmeter.model.Events;

public class GenerateNewEventActivity extends AppCompatActivity {


    ImageView infoIMG;
    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_new_event);
        infoIMG = findViewById(R.id.imageViewInfo);
        infoText = findViewById(R.id.textViewInfo);

        insert();
    }

    public void insert(){
        Event event = Events.getInstance().getNewEvent();
        event.setUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        event.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        try{
            Events.getInstance().addEvent(event);
            infoIMG.setImageResource(R.drawable.ic_success);
            infoText.setText("Event erfolgreich erstellt");
        }catch (Exception ex){
            Log.e("Fehler","Eintragen fehlgeschlagen");
        }
    }
}

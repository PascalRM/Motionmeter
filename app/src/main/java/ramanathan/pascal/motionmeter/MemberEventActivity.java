package ramanathan.pascal.motionmeter;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Date;

import ramanathan.pascal.motionmeter.model.Event;

public class MemberEventActivity extends AppCompatActivity {

    Event event;
    TextView name;
    TextView zufriedenheitAnzeige;
    SeekBar zufriedenheit;
    int zufriedenheitsWert;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DocumentReference sfDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_event);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_events:
                        return true;
                    case R.id.navigation_myEvent:
                        return true;
                    case R.id.navigation_Ich:
                        return true;
                }
                return false;
            }
        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_events);

        name = findViewById(R.id.textView_nameEventMember);
        event = (Event) getIntent().getSerializableExtra("event");
        sfDocRef =  db.collection("events").document(event.getDocument_name());
        zufriedenheit = findViewById(R.id.seekBar_zufriedenheit);
        zufriedenheitAnzeige = findViewById(R.id.textView_zufriedenheitMember);

        zufriedenheit.setProgress(10);
        zufriedenheit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                zufriedenheitsWert = seekBar.getProgress()-10;
                zufriedenheitAnzeige.setText(String.valueOf(zufriedenheitsWert));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        eventListener();

    }

    public void eventListener(){
        db.collection("events").document(event.getDocument_name()).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                event = documentSnapshot.toObject(Event.class);
                if(event.getEnddate().before(new Date())){
                    finish();
                }
            }
        });
    }

    public void onClickBewerten(View view){
        String id = FirebaseAuth.getInstance().getUid() + " " + new Date().toString();
        event.setBewertung(id,zufriedenheitsWert);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                transaction.update(sfDocRef, "bewertung", event.getBewertung());

                // Success
                return null;
            }
        });
    }


}

package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
    String document_name ="";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DocumentReference sfDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_event);

        name = findViewById(R.id.textView_nameEventMember);
        event = (Event) getIntent().getSerializableExtra("event");
        sfDocRef =  db.collection("events").document(event.getDocument_name());
        document_name = event.getDocument_name();
        zufriedenheit = findViewById(R.id.seekBar_zufriedenheit);
        zufriedenheitAnzeige = findViewById(R.id.textView_zufriedenheitMember);
        name.setText(event.getName().toString());
        zufriedenheit.setProgress(10);
        zufriedenheit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                zufriedenheitsWert = seekBar.getProgress()-10;
                zufriedenheitAnzeige.setText("Zufriedenheit:   " +String.valueOf(zufriedenheitsWert));
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
                    showEvents();
                }
            }
        });
    }

    public  void showEvents(){
        Intent intent = new Intent(this, EventsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickBewerten(View view){
        String id = FirebaseAuth.getInstance().getUid() + " " + new Date().toString();
        event.setBewertung(id,zufriedenheitsWert);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                transaction.update(sfDocRef, "bewertung", event.getBewertung());

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CharSequence text = "Bewertung gesendet";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }

    public void onClickBemerkung(View view){
        Intent intent = new Intent(this,BemerkungMemberEventActivity.class);
        event.setDocument_name(document_name);
        intent.putExtra("event",event);
        startActivity(intent);
    }

    public void onClickVerlassen(View view){
        Intent intent = new Intent(this,EventsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

package ramanathan.pascal.motionmeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Document;

import ramanathan.pascal.motionmeter.model.Event;

public class BemerkungMemberEventActivity extends AppCompatActivity {

    Event event;
    EditText bemerkung;
    DocumentReference sfDocRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bemerkung_member_event);

        bemerkung = findViewById(R.id.editText_bemerkung);
        event = (Event) getIntent().getSerializableExtra("event");
        sfDocRef =  db.collection("events").document(event.getDocument_name());
    }


    public void addBemerkung(View view){

        event.addBemerkungen(bemerkung.getText().toString());
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                transaction.update(sfDocRef, "bemerkungen", event.getBemerkungen());
                return null;
            }
        });
        finish();
    }
}

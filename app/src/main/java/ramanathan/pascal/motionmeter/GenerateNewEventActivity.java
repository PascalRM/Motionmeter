package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ramanathan.pascal.motionmeter.controller.EventController;
import ramanathan.pascal.motionmeter.model.Event;

public class GenerateNewEventActivity extends AppCompatActivity {


    ImageView infoIMG;
    TextView infoText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_new_event);
        infoIMG = findViewById(R.id.imageViewInfo);
        infoText = findViewById(R.id.textViewInfo);

        insert();
    }

    public void insert(){
        Event event = EventController.getInstance().getNewEvent();
        event.setUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        event.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        try{
            EventController.getInstance().addEvent(event);
            infoIMG.setImageResource(R.drawable.ic_success);
            infoText.setText("Event erfolgreich erstellt");
        }catch (Exception ex){
            Log.e("Fehler","Eintragen fehlgeschlagen: " + ex);
        }
    }

    public void onClickFinish(View view){
        Intent intent = new Intent(this,OwnerEventActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Betätigen Sie noch einmal die Zurück-Taste um zu beenden", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

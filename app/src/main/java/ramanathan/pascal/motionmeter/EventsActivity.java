package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import ramanathan.pascal.motionmeter.controller.EventController;
import ramanathan.pascal.motionmeter.controller.MyEventController;
import ramanathan.pascal.motionmeter.model.Event;

public class EventsActivity extends AppCompatActivity {

    ListView list;
    EventController eventController;

    ArrayList<Event> events = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>Motionmeter</font>"));

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case    R.id.navigation_events:
                        return true;
                    case R.id.navigation_myEvent:
                        showMyEvent();
                        return true;
                    case R.id.navigation_Ich:
                        showMe();
                        return true;
                }
                return false;
            }
        };

        list = findViewById(R.id.listViewEvents);
        eventController = EventController.getInstance();
        eventController.load(EventsActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, eventController.getEvents());
        eventController.setAdapter(adapter);

        list.setAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_events);
    }


    public void showMyEvent(){
       checkIfEventExists();
    }

    public void showOwnerEventActivity() {
        Intent m;
        m = new Intent(this,OwnerEventActivity.class);
        m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(m);
        finish();
        overridePendingTransition(0, 0);
    }

    public void showMyEventActivity(){
        Intent m;
        m = new Intent(this,MyEventActivity.class);
        m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(m);
        finish();
        overridePendingTransition(0, 0);
    }

    public void checkIfEventExists(){
        db.collection("events").whereEqualTo("uid", FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                events.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    if(document.toObject(Event.class).getEnddate().after(new Date()) &&  document.toObject(Event.class).getStartdate().before(new Date()) || document.toObject(Event.class).getStartdate().getTime() == new Date().getTime() ){
                        events.add(document.toObject(Event.class));
                    }
                }

                if(events.size() > 0){
                    showOwnerEventActivity();
                }else{
                    showMyEventActivity();
                }
            }
        });


    }

    public void showMe(){
        Intent m = new Intent(this,MeActivity.class);
        m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(m);
        finish();
        overridePendingTransition(0, 0);
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

package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ramanathan.pascal.motionmeter.model.Event;

public class OwnerEventActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Event> events = new ArrayList<>();
    //Aktueller Event
    Event event;
    TextView title;
    TextView bewertung;
    ArrayAdapter<String> adapter;
    ListView bemerkungen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_event);
        getEvents();

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_events:
                        showEvents();
                        return true;
                    case R.id.navigation_myEvent:
                        return true;
                    case R.id.navigation_Ich:
                        showMe();
                        return true;
                }
                return false;
            }
        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_myEvent);

        title = findViewById(R.id.textView_titleEvent);
        bewertung = findViewById(R.id.textView_satisfaction);
        bemerkungen = findViewById(R.id.listViewBemerkungen);

    }

    public void showEvents() {
        Intent m = new Intent(this, EventsActivity.class);
        m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(m);
        finish();
        overridePendingTransition(0, 0);
    }

    public void showMe() {
        Intent m = new Intent(this, MeActivity.class);
        m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(m);
        finish();
        overridePendingTransition(0, 0);
    }

    public void getEvents(){
        db.collection("events").whereEqualTo("uid", FirebaseAuth.getInstance().getUid()).addSnapshotListener(OwnerEventActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }
                if(events.size() == 0){
                    for (DocumentSnapshot document : value) {
                        events.add(document.toObject(Event.class));
                    }
                    getActualEvent();
                }else{
                    for (DocumentSnapshot document : value) {
                        if(document.toObject(Event.class).getUID() == event.getUID() && document.toObject(Event.class).getName() == event.getName()){
                            //event.setBewertung(document.toObject(Event.class).getBewertung());
                            event.getBemerkungen().clear();

                            for(String s:document.toObject(Event.class).getBemerkungen()){
                                event.addBemerkungen(s);
                            }

                        }
                    }
                }
            }
        });
    }

    public void getActualEvent(){
        event = null;
        for (Event e:events){
            if(e.getEnddate().after(new Date()) &&  e.getStartdate().before(new Date()) || e.getStartdate().getTime() == new Date().getTime() ){
                event = e;
            }
        }
        if(event != null){
            title.setText(event.toString());
            if(adapter == null){
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, event.getBemerkungen());
                bemerkungen.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
        setSatisfaction();
    }

    public void setSatisfaction(){
        //event.setBewertung("s",4);
        int sum = 0;
        for(Map.Entry<String,Integer> bewertung:event.getBewertung().entrySet()){
            sum += bewertung.getValue();
        }
        bewertung.setText(String.valueOf(sum));
    }
}

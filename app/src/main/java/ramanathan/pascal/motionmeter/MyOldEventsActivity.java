package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ramanathan.pascal.motionmeter.model.Event;

public class MyOldEventsActivity extends AppCompatActivity {

    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Event> events = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_old_events);

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


        listView = findViewById(R.id.listview_oldEvents);
        arrayAdapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, events);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) listView.getItemAtPosition(position);
                showOldEventDetail(event);
            }
        });

        getEvents();
    }

    public void showEvents(){
        Intent intent = new Intent(this,EventsActivity.class);
        startActivity(intent);
        finish();
    }

    public void showMe(){
        Intent intent = new Intent(this,MeActivity.class);
        startActivity(intent);
        finish();
    }

    public void getEvents(){
        db.collection("events")
                .whereEqualTo("over",true)
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(MyOldEventsActivity.this,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        for (DocumentSnapshot document:documentSnapshots){
                            events.add(document.toObject(Event.class));
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void showOldEventDetail(Event event){
        Intent intent = new Intent(this,OwnerEventActivity.class);
        intent.putExtra("event",event);
        startActivity(intent);
        finish();
    }
}

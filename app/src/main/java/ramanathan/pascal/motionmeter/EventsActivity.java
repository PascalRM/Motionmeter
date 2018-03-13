package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ramanathan.pascal.motionmeter.model.Event;
import ramanathan.pascal.motionmeter.model.Events;

public class EventsActivity extends AppCompatActivity {

    ListView list;
    Events events;

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
                    case R.id.navigation_events:
                        return true;
                    case R.id.navigation_myEvent:
                        showMyEvent();
                        return true;
                }
                return false;
            }
        };

        list = findViewById(R.id.listViewEvents);
        events = Events.getInstance();
        events.setAdapter(this);

        list.setAdapter(events.getAdapter());
        events.getEvents();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_events);
    }

    public void showMyEvent(){
        Intent m = new Intent(this,MyEventActivity.class);
        startActivity(m);
        overridePendingTransition(0, 0);
    }


}

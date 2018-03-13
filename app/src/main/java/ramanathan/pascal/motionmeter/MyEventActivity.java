package ramanathan.pascal.motionmeter;
import ramanathan.pascal.motionmeter.model.Event;
import ramanathan.pascal.motionmeter.model.Events;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;


public class MyEventActivity extends AppCompatActivity {

    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>Motionmeter</font>"));
        t = findViewById(R.id.textViewUsername);

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
                }
                return false;
            }
        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_myEvent);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        t.setText(user.getUid());

        Event event = new Event();
        event.setName("Pascal");
        event.setStartdate(new Date());

        Events e = Events.getInstance();
        //e.addEvent(event);
    }

    public void showEvents(){
        Intent m = new Intent(this,EventsActivity.class);
        startActivity(m);
        overridePendingTransition(0,0);
    }
}

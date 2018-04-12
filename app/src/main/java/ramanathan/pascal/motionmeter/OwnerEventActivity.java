package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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
    Event event = null;
    TextView title;
    TextView bewertung;
    ArrayAdapter<String> adapter;
    ListView bemerkungen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_event);


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
        event = (Event) getIntent().getSerializableExtra("event");

        Button b = findViewById(R.id.button_beendenEvent);
        b.setVisibility(View.VISIBLE);




        if (event == null) {
            getEvents();
        } else {
            getEvent();
        }
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

    public void getEvents() {
        db.collection("events")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Error", "Listen failed.", e);
                            return;
                        }
                        if (events.size() == 0) {
                            for (DocumentSnapshot document : value) {
                                Event ev = document.toObject(Event.class);
                                ev.setDocument_name(document.getId());
                                events.add(ev);
                            }

                            getActualEvent();
                        } else {
                            for (DocumentSnapshot document : value) {
                                Event ev = document.toObject(Event.class);
                                if (ev.getUID().equals(event.getUID()) && ev.getName().equals(event.getName())) {
                                    event.setBewertung(document.toObject(Event.class).getBewertung());
                                    event.getBemerkungen().clear();

                                    for (String s : document.toObject(Event.class).getBemerkungen()) {
                                        event.addBemerkungen(s);
                                    }

                                }
                            }
                            setSatisfaction();
                            adapter.notifyDataSetChanged();
                        }
                        setListViewHeightBasedOnChildren(bemerkungen);
                    }
                });

    }

    public void getEvent() {
        Button b = findViewById(R.id.button_beendenEvent);
        b.setVisibility(View.GONE);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, event.getBemerkungen());
        bemerkungen.setAdapter(adapter);
        db.collection("events")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .whereEqualTo("name", event.getName())
                .whereEqualTo("over", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Error", "Listen failed.", e);
                            return;
                        }
                        for (DocumentSnapshot document : value) {
                            Event ev = document.toObject(Event.class);
                            if (ev.getUID().equals(event.getUID()) && ev.getName().equals(event.getName()) && ev.getEnddate().equals(event.getEnddate())) {
                                event.setBewertung(document.toObject(Event.class).getBewertung());
                                event.getBemerkungen().clear();

                                for (String s : document.toObject(Event.class).getBemerkungen()) {
                                    event.addBemerkungen(s);
                                }

                            }
                        }
                        setSatisfaction();



                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(bemerkungen);
                    }
                });

    }

    public void getActualEvent() {
        event = null;
        for (Event e : events) {
            if (e.getEnddate().after(new Date()) && e.getStartdate().before(new Date()) || e.getStartdate().getTime() == new Date().getTime()) {
                event = e;
            }
        }
        if (event != null) {
            title.setText(event.toString());
            if (adapter == null) {
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, event.getBemerkungen());
                bemerkungen.setAdapter(adapter);

            } else {
                adapter.notifyDataSetChanged();
            }
        }
        setSatisfaction();
    }

    public void setSatisfaction() {
        int sum = 0;
        Button b = findViewById(R.id.button_diagram);
        if(event.getBewertung().size() == 0){
            b.setEnabled(false);
            bewertung.setText(String.valueOf(sum));
        }else{
            b.setEnabled(true);
            for (Map.Entry<String, Integer> bewertung : event.getBewertung().entrySet()) {
                sum += bewertung.getValue();
            }
            System.out.println("Size " + event.getBewertung().size());
            if (sum > 0) {
                sum = sum / event.getBewertung().size();
            }
            bewertung.setText(String.valueOf(sum));
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void OnClickClose(View view) {
        db.collection("events").document(event.getDocument_name()).update("enddate", new Date());
        db.collection("events").document(event.getDocument_name()).update("over", true);

        Intent intent = new Intent(this, EventsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickShowChart(View view) {
        Intent intent = new Intent(this, ChartOwnerEventActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

}


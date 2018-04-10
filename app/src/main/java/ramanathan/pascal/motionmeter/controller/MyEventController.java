package ramanathan.pascal.motionmeter.controller;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import ramanathan.pascal.motionmeter.model.Event;

public class MyEventController {

    private static final MyEventController ourInstance = new MyEventController();

    private Event myEvent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Event> events = new ArrayList<>();

    public static MyEventController getInstance() {
        return ourInstance;
    }

    private MyEventController(){}

    public boolean checkIfEventExists(){
        db.collection("events").whereEqualTo("uid", FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }
                events.clear();
                for (DocumentSnapshot document : value) {
                    if(document.toObject(Event.class).getEnddate().after(new Date()) &&  document.toObject(Event.class).getStartdate().before(new Date()) || document.toObject(Event.class).getStartdate().getTime() == new Date().getTime() ){
                        events.add(document.toObject(Event.class));
                    }
                }
            }
        });

        if(events.size() > 0){
            return true;
        }else{
            return false;
        }
    }

}

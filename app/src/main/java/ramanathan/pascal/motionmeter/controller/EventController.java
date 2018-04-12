package ramanathan.pascal.motionmeter.controller;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;

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

import ramanathan.pascal.motionmeter.EventsActivity;
import ramanathan.pascal.motionmeter.model.Event;

/**
 * Created by Pascal on 13.03.2018.
 */

public class EventController {
    private static final EventController ourInstance = new EventController();

    ArrayAdapter<Event> adapter = null;
    ArrayList<Event> events = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Event newEvent;
    String id = "";

    public static EventController getInstance() {
        return ourInstance;
    }

    private EventController() {
    }

    public ArrayList<Event> getEvents(){
        return events;
    }

    public void load(Activity activity){
        db.collection("events").whereEqualTo("over",false).addSnapshotListener(activity,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }
                events.clear();
                for (DocumentSnapshot document : value) {
                    Event ev = document.toObject(Event.class);
                    ev.setDocument_name(document.getId());
                    events.add(ev);
                }
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void addEvent(Event event){
        db.collection("events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        id = "";
                        //addOwner(documentReference.getId());
                        Log.i("Event hinzugefuegt", "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fehler", "Error adding document", e);
                        try {
                            throw new Exception("a",e);
                        } catch (Exception e1) {
                        }
                    }
                });
        this.newEvent = null;
    }


    public ArrayAdapter<Event> getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter<Event> adapter) {
        this.adapter = adapter;
    }

    public Event getNewEvent() {
        if(newEvent == null){
            this.newEvent = new Event();
        }
        return newEvent;
    }

    public void setNewEvent(Event newEvent) {
        this.newEvent = newEvent;
    }


}

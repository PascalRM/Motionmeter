package ramanathan.pascal.motionmeter.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ramanathan.pascal.motionmeter.EventsActivity;
import ramanathan.pascal.motionmeter.MainActivity;

/**
 * Created by Pascal on 13.03.2018.
 */

public class Events {
    private static final Events ourInstance = new Events();

    ArrayAdapter<Event> adapter = null;
    ArrayList<Event> events = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static Events getInstance() {
        return ourInstance;
    }

    private Events() {
    }

    public ArrayList<Event> getEvents(){
        return events;
    }

    public void load(){
       /* db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            events = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                events.add(document.toObject(Event.class));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("Fehler", "Error getting documents: ", task.getException());
                        }
                    }
                });*/
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }
                events.clear();
                for (DocumentSnapshot document : value) {
                    events.add(document.toObject(Event.class));
                }
                System.out.println("Änderungen erkannt");
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
                        Log.i("Event hinzugefuegt", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fehler", "Error adding document", e);
                    }
                });
    }

    public ArrayAdapter<Event> getAdapter() {
        return adapter;
    }

    public void setAdapter(Context context) {
        this.adapter = new ArrayAdapter<Event>(context, android.R.layout.simple_list_item_1, getEvents());
        this.adapter.notifyDataSetChanged();
    }
    public void setAdapter(ArrayAdapter<Event> adapter) {
        this.adapter = adapter;
    }
}

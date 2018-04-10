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

    public static MyEventController getInstance() {
        return ourInstance;
    }

    private MyEventController(){}

}

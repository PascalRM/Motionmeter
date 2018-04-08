package ramanathan.pascal.motionmeter.controller;

import android.widget.ArrayAdapter;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import ramanathan.pascal.motionmeter.model.Event;

public class MyEventController {

    private static final MyEventController ourInstance = new MyEventController();

    private Event myEvent;

    public static MyEventController getInstance() {
        return ourInstance;
    }

    private MyEventController(){}


}

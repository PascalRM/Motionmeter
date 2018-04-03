package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Text;

import ramanathan.pascal.motionmeter.model.Event;
import ramanathan.pascal.motionmeter.model.Events;

public class NewEventPasswordActivity extends AppCompatActivity {


    EditText password ;
    EditText passwordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_password);

        password = findViewById(R.id.editTextPassword);
        passwordRepeat = findViewById(R.id.editTextPasswordRepeat);
    }

    public boolean check(){
        if(TextUtils.isEmpty(password.getText())){
            password.setError("Bitte Passwort eingeben");
            return false;
        }

        if(TextUtils.isEmpty(passwordRepeat.getText())){
            passwordRepeat.setError("Bitte Passwort eingeben");
            return false;
        }

        if(!passwordRepeat.getText().toString().equals(password.getText().toString())){
            passwordRepeat.setError("Passwort stimmt nicht überein");
            return false;
        }

        return true;
    }

    public void updateEvent(){
        Event event = Events.getInstance().getNewEvent();
        event.setPasswort(password.getText().toString());
    }

    public void nextView(View view){
        if(check()){
            updateEvent();

            Intent intent = new Intent(this,FinalNewEventActivity.class);
            startActivity(intent);
        }
    }
}

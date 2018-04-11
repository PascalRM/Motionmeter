package ramanathan.pascal.motionmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ramanathan.pascal.motionmeter.model.Event;

public class LoginEventActivity extends AppCompatActivity {

    TextView name;
    EditText password;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_event);

        event = (Event) getIntent().getSerializableExtra("event");

        password = findViewById(R.id.editText_password);
        name = findViewById(R.id.textView_name);
        name.setText(event.getName());
    }

    public void OnClickLogin(View view) {
        System.out.println("Password is " + event.getPasswort());
        if (event.getPasswort().equals(password.getText().toString())) {
            Intent intent = new Intent(this, MemberEventActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
            finish();
        } else {
            password.setError("Falsches Password");
        }
    }
}

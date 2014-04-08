package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EventCreateActivity extends Activity {
    private EditText title;
    private EditText date;
    private EditText time;
    private EditText description;
    private Button save;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent);
        title = (EditText) findViewById(R.id.eventName);
        title.setText("hello");
        date = (EditText) findViewById(R.id.Date);
        time = (EditText) findViewById(R.id.Time);
        description = (EditText) findViewById(R.id.description);
        save = (Button) findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put server communication code here
            }
        });
        cancel = (Button) findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

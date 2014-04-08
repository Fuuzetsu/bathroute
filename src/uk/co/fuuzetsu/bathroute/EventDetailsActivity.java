package uk.co.fuuzetsu.bathroute;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.view.View;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.ViewGroup;

public class EventDetailsActivity extends Activity {

	//store the title
	private TextView title;
	//store event description
	private TextView time;
	private TextView date;
	private TextView description;
	
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventdetails);
        title = (TextView)findViewById(R.id.eventdetails_title);
        title.setText("hello");
        date = (TextView)findViewById(R.id.eventDate);
        time = (TextView)findViewById(R.id.eventTime);
        description = (TextView)findViewById(R.id.eventDescription);
        
    }
}

package bjk.ftny;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import bjk.ftny.DataObjects.Truck;
import bjk.ftny.DataObjects.TruckLocation;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class TruckSchedule extends Activity
{
	Truck SelectedTruck;
	TruckLocation[] AllLocations;
	Button btnGoToWeb;
	ListView lstTruckLocations;
	TextView lblTruckIntroduction;
	AdView ad;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trucklocations);
        
        btnGoToWeb = (Button)findViewById(R.id.btnGoToWeb);
        lstTruckLocations = (ListView)findViewById(R.id.lstTruckLocations);
        lblTruckIntroduction = (TextView)findViewById(R.id.lblTruckIntroduction);
        
        final Truck PassedTruck = new Truck();
        PassedTruck.SetID(getIntent().getExtras().getString("ID"));
        PassedTruck.SetTruckName(getIntent().getExtras().getString("TruckName"));
        PassedTruck.SetDescription(getIntent().getExtras().getString("Description"));
        PassedTruck.SetCity(getIntent().getExtras().getString("City"));
        PassedTruck.SetState(getIntent().getExtras().getString("State"));
        PassedTruck.SetWebsite(getIntent().getExtras().getString("Website"));
        PassedTruck.SetSpecialCase(getIntent().getExtras().getBoolean("SpecialCase"));
        SelectedTruck = PassedTruck;
        
        if(IsOnline())
        {
        	LocationParser me = new LocationParser(GetMainTruckLocationXML(), PassedTruck);
            AllLocations = me.GetAllTruckLocationsFromXML();
            lblTruckIntroduction.setText(PassedTruck.GetTruckName() + " WEEKLY SCHEDULE");
            
        	if(PassedTruck.GetSpecialCase())
        		lblTruckIntroduction.setText("This truck's schedule is not available or it is inconsistent, please see website");
        	else
        		lstTruckLocations.setAdapter(new TruckLocationAdapter(this));
        	
        	btnGoToWeb.setOnClickListener(new View.OnClickListener()
            {
            	@Override
    			public void onClick(View v)
            	{
            		Uri uri = Uri.parse(PassedTruck.GetWebsite());
            		startActivity(new Intent(Intent.ACTION_VIEW, uri));
            	}
    		});
        	
        	AdManager.setTestDevices( new String[] { 
            		AdManager.TEST_EMULATOR,
            		"E83D20734F72FB3108F104ABC0FFC738", 
            });
            
            ad = (AdView)findViewById(R.id.ad);
            ad.requestFreshAd();
        }
        else
        {
        	lblTruckIntroduction.setText("No Network Connectivity");
        	btnGoToWeb.setVisibility(4);
        }
    }
    
    public void onStart()
    {
    	super.onStart();
    }
    
    //Private Methods
    private String GetMainTruckLocationXML()
    {
    	String Url = "http://videoxpg.webs.com/Truck_Locations.xml";
    	String ResponseTxt = "";
    	
    	// Create an instance of HttpClient.
        HttpClient client = new DefaultHttpClient();

        // Create a method instance.
        HttpGet method = new HttpGet(Url);
        
        try
        {
        	ResponseHandler<String> responseHandler = new BasicResponseHandler();
        	ResponseTxt = client.execute(method, responseHandler);
        }
        catch(Exception e)
        {
        	ResponseTxt = e.getMessage();
        }
    	
    	return ResponseTxt;
    }
    
    public boolean IsOnline() {
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		
		if(ni == null)
			return false;
		else
			return ni.isConnected();
    	}
    
    class TruckLocationAdapter extends ArrayAdapter<TruckLocation>
    {
    	Activity m_context;
    	
    	TruckLocationAdapter(Activity context) {
    		super(context, R.layout.schedulerow, AllLocations);
    		this.m_context = context;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent)
    	{
    		View Row = convertView;
    		Button btnMapIt;
    		TextView lblLocation;
    		
    		if(Row == null)
    		{
    			LayoutInflater inflater = m_context.getLayoutInflater();
    			Row = inflater.inflate(R.layout.schedulerow, null);
    		}
    		
    		lblLocation = (TextView)Row.findViewById(R.id.lblLocation);
    		btnMapIt = (Button)Row.findViewById(R.id.btnMapIt);
    		lblLocation.setText(AllLocations[position].GetDay() + " (" + AllLocations[position].GetTime() + ")\n" + AllLocations[position].GetPlace());
    		final int myPos = position;
    		
    		btnMapIt.setOnClickListener(new View.OnClickListener()
            {
            	@Override
    			public void onClick(View v)
            	{
            		String source = "geo:0,0?q=" + AllLocations[myPos].GetPlace().replace(" ", "+") + "+" + 
    				SelectedTruck.GetCity().replace(" ", "+") + "+," + SelectedTruck.GetState().replace(" ", "+");
            		
            		try
            		{
            			Uri uri = Uri.parse(source);
            			startActivity(new Intent(Intent.ACTION_VIEW, uri));
            		}
            		catch(Exception e)
            		{
            			Toast.makeText(TruckSchedule.this, e.getMessage(), Toast.LENGTH_LONG);
            		}
            	}
    		});
    		
    		return Row;
    	}
    }
}
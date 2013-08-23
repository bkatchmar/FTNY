package bjk.ftny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import bjk.ftny.DataObjects.Truck;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class MainScreen extends Activity
{
	Truck[] AllTrucks;
	ListView lstTrucks;
	TextView lblIntro;
	AdView ad;
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			try
			{
				if(IsOnline())
				{
					lstTrucks.setAdapter(new TruckAdapter(MainScreen.this));
		        	lstTrucks.setOnItemClickListener(new OnItemClickListener() {
		        		@SuppressWarnings("unchecked")
		    			public void onItemClick(AdapterView parent, View v, int position, long id)
		            	{
		            		SetUpIndividualTruck(AllTrucks[position]);
		            	}
		        	});
		        	lblIntro.setText("Loading...");
					lblIntro.setVisibility(8);
					ad.requestFreshAd();
				}
				else
					Toast.makeText(MainScreen.this, "No Network Connectivity", Toast.LENGTH_LONG);
			}
			catch(Exception e)
			{
				Toast.makeText(MainScreen.this, e.getMessage(), Toast.LENGTH_LONG);
			}
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        lstTrucks = (ListView)findViewById(R.id.lstTrucks);
        lblIntro = (TextView)findViewById(R.id.lblIntro);
        

        AdManager.setTestDevices( new String[] { 
        		AdManager.TEST_EMULATOR,
        		"E83D20734F72FB3108F104ABC0FFC738", 
        });
        
        ad = (AdView)findViewById(R.id.ad);
    }
    
    public void onStart()
    {
    	super.onStart();
    	
    	Thread bg = new Thread(new Runnable()
    	{
    		public void run(){
    			try
    			{
    				lblIntro.setText("Loading..");
    				TruckParser me = new TruckParser(GetMainTruckXML());
					AllTrucks = me.GetAllTrucksFromXML();
    				handler.sendMessage(handler.obtainMessage());
    			}
    			catch(Exception e)
    			{
    				Toast.makeText(MainScreen.this, e.getMessage(), Toast.LENGTH_LONG);
    			}
    		}
    	});
    	
    	bg.start();
    }
    
    public void onStop()
    {
    	super.onStop();
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    		case R.id.close:
    			finish();
    			return true;
    	}
    	return(super.onOptionsItemSelected(item));
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
    	new MenuInflater(getApplication()).inflate(R.menu.ftnymenu, menu);
    	return(super.onCreateOptionsMenu(menu));
    }
    
    //Private Methods
    private String GetMainTruckXML()
    {
    	String Url = "http://videoxpg.webs.com/FTNY_Trucks.xml";
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
    private void SetUpIndividualTruck(Truck selectedTruck)
    {
    	try
    	{
    		final Intent goToTruck = new Intent();
    		goToTruck.setClass(this, TruckSchedule.class);
    		goToTruck.putExtra("ID", selectedTruck.GetID());
    		goToTruck.putExtra("TruckName", selectedTruck.GetTruckName());
    		goToTruck.putExtra("Description", selectedTruck.GetDescription());
    		goToTruck.putExtra("City", selectedTruck.GetCity());
    		goToTruck.putExtra("State", selectedTruck.GetState());
    		goToTruck.putExtra("Website", selectedTruck.GetWebsite());
    		goToTruck.putExtra("SpecialCase", selectedTruck.GetSpecialCase());
    		startActivity(goToTruck);
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(MainScreen.this, e.getMessage(), Toast.LENGTH_LONG);
    	}
    }
    
    public boolean IsOnline() {
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	return cm.getActiveNetworkInfo().isConnected();
    	}
    
    class TruckAdapter extends ArrayAdapter<Truck>
    {
    	Activity m_context;
    	
    	TruckAdapter(Activity context) {
    		super(context, R.layout.truckrow, AllTrucks);
    		this.m_context = context;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent)
    	{
    		View Row = convertView;
    		TextView lblTruckName;
    		
    		if(Row == null)
    		{
    			LayoutInflater inflater = m_context.getLayoutInflater();
    			Row = inflater.inflate(R.layout.truckrow, null);
    		}
    		
    		lblTruckName = (TextView)Row.findViewById(R.id.lblTruckName);
    		lblTruckName.setText(AllTrucks[position].GetTruckName());
    		
    		return Row;
    	}
    }
}
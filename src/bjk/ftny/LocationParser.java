package bjk.ftny;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.InputSource;
import java.io.StringReader;

import bjk.ftny.DataObjects.Truck;
import bjk.ftny.DataObjects.TruckLocation;

import java.util.Vector;

public class LocationParser
{
	protected Truck m_selectedTruck;
	private Vector<TruckLocation> m_AllTruckLocations;
	private String m_result;
	
	public LocationParser(String xmlData, Truck truckData)
	{
		m_result = "Didnt Start";
		m_AllTruckLocations = new Vector<TruckLocation>();
		m_selectedTruck = truckData;
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxHandler handler = new SaxHandler();
            handler.SetTruckToLookFor(m_selectedTruck);
            parser.parse(new InputSource(new StringReader(xmlData)), handler);
            m_AllTruckLocations = handler.GetAllTruckLocations();
            m_result = "Done";
        } catch (Exception ex) {
        	m_result = ex.getMessage();
        }
	}
	
	public String GetResult()
	{
		return m_result;
	}
	
	public TruckLocation[] GetAllTruckLocationsFromXML()
	{
		TruckLocation[] Location = new TruckLocation[m_AllTruckLocations.size()];
		
		for(int i = 0; i < m_AllTruckLocations.size(); i++)
			Location[i] = m_AllTruckLocations.elementAt(i);
		
		return Location;
	}
	
	private static final class SaxHandler extends DefaultHandler {
		private Vector<TruckLocation> AllTruckLocations;
		protected Truck SelectedTruck;
		private TruckLocation CurrentLocation;
		private boolean AtDay = false, AtPlace = false, AtTime = false, AtProperTruck = false, AtTruckID = false;
		
		public void startDocument() throws SAXException {
			AllTruckLocations = new Vector<TruckLocation>();
        }
		
		public void endDocument() throws SAXException {
			//Do Nothing
        }
		
		public void characters(char ch[], int start, int length) throws SAXException {
			if(AtDay)
			{
				CurrentLocation.SetDay((new String(ch, start, length)));
				AtDay = false;
			}
			else if(AtPlace)
			{
				CurrentLocation.SetPlace((new String(ch, start, length)));
				AtPlace = false;
			}
			else if(AtTime)
			{
				CurrentLocation.SetTime((new String(ch, start, length)));
				
				if(AtProperTruck)
					AllTruckLocations.add(CurrentLocation);
				
				AtTime = false;
			}
			else if(AtTruckID)
			{
				String TruckID = new String(ch, start, length);
				
				if(TruckID.equals(SelectedTruck.GetID()))
					AtProperTruck = true;
				else
					AtProperTruck = false;
			}
		}
		
		public void startElement(String uri, String localName, 
                String qName, Attributes attrs) throws SAXException {
            
            if (localName.equals("Day")) {
            	AtDay = true;
            	AtPlace = false;
            	AtTime = false;
            	AtTruckID = false;
            	CurrentLocation = new TruckLocation();
            }
            else if (localName.equals("Place")) {
            	AtDay = false;
            	AtPlace = true;
            	AtTime = false;
            	AtTruckID = false;
            }
            else if (localName.equals("Time")) {
            	AtDay = false;
            	AtPlace = false;
            	AtTime = true;
            	AtTruckID = false;
            }
            else if(localName.equals("TruckID"))
            {
            	AtDay = false;
            	AtPlace = false;
            	AtTime = false;
            	AtTruckID = true;
            }
            else if(localName.equals("FTNY"))
            {
            	AtDay = false;
            	AtPlace = false;
            	AtTime = false;
            	AtTruckID = false;
            }
            else if(localName.equals("Schedule"))
            {
            	AtDay = false;
            	AtPlace = false;
            	AtTime = false;
            	AtTruckID = false;
            }
            else if(localName.equals("Locations"))
            {
            	AtDay = false;
            	AtPlace = false;
            	AtTime = false;
            	AtTruckID = false;
            }
            else if(localName.equals("Location"))
            {
            	AtDay = false;
            	AtPlace = false;
            	AtTime = false;
            	AtTruckID = false;
            }
            else
            {
            	AtDay = false;
            	AtPlace = false;
            	AtTime = false;
            	AtTruckID = false;
            }
        }
		
		public void endElement(String uri, String localName, String qName) throws SAXException {
            // do nothing
        }
		
		public Vector<TruckLocation> GetAllTruckLocations()
		{
			return AllTruckLocations;
		}
		
		public void SetTruckToLookFor(Truck selectedTruck)
		{
			SelectedTruck = selectedTruck;
		}
	}
}

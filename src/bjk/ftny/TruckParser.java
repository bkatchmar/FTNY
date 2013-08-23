package bjk.ftny;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.InputSource;
import java.io.StringReader;

import bjk.ftny.DataObjects.Truck;
import java.util.Vector;

public class TruckParser
{
	private Vector<Truck> m_AllTrucks;
	private String m_result;
	
	public TruckParser(String xmlData)
	{
		m_result = "Didnt Start";
		m_AllTrucks = new Vector<Truck>();
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxHandler handler = new SaxHandler();
            parser.parse(new InputSource(new StringReader(xmlData)), handler);
            m_AllTrucks = handler.GetAllTrucks();
            m_result = "Done";
        } catch (Exception ex) {
        	m_result = ex.getMessage();
        }
	}
	
	public String GetResult()
	{
		return m_result;
	}
	
	public Truck[] GetAllTrucksFromXML()
	{
		Truck[] Trucks = new Truck[m_AllTrucks.size()];
		
		for(int i = 0; i < m_AllTrucks.size(); i++)
			Trucks[i] = m_AllTrucks.elementAt(i);
		
		return Trucks;
	}
	
	private static final class SaxHandler extends DefaultHandler {
		private Vector<Truck> AllTrucks;
		private Truck CurrentTruck;
		private boolean AtID = false, AtName = false, AtDescription = false, AtCity = false, AtState = false, AtWebsite = false, AtScase = false;
		
		public void startDocument() throws SAXException {
			AllTrucks = new Vector<Truck>();
        }
		
		public void endDocument() throws SAXException {
			//Do Nothing
        }
		
		public void characters(char ch[], int start, int length) throws SAXException {
			if (AtID)
			{ 
				CurrentTruck.SetID(new String(ch, start, length)); 
				AtID = false;
			}
			else if(AtName)
			{
				CurrentTruck.SetTruckName((new String(ch, start, length)));
				AtName = false;
			}
			
			else if(AtDescription)
			{
				CurrentTruck.SetDescription((new String(ch, start, length)));
				AtDescription = false;
			}
			else if(AtCity)
			{
				CurrentTruck.SetCity((new String(ch, start, length)));
				AtCity = false;
			}
			else if(AtState)
			{
				CurrentTruck.SetState((new String(ch, start, length)));
				AtState = false;
			}
			else if(AtScase)
			{
				String scase = new String(ch, start, length);
				
				if(scase.equals("0"))
					CurrentTruck.SetSpecialCase(false);
				else
					CurrentTruck.SetSpecialCase(true);
				
				AtScase = false;
			}
			else if(AtWebsite)
			{
				CurrentTruck.SetWebsite((new String(ch, start, length)));
				AllTrucks.add(CurrentTruck);
				AtWebsite = false;
			}
		}
		
		public void startElement(String uri, String localName, 
                String qName, Attributes attrs) throws SAXException {
            
            if (localName.equals("Truck")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = false;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = false;
            	CurrentTruck = new Truck();
            }
            else if (localName.equals("ID")) {
            	AtID = true;
            	AtName = false;
            	AtDescription = false;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = false;
            }
            else if (localName.equals("Name")) {
            	AtID = false;
            	AtName = true;
            	AtDescription = false;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = false;
            }
            else if (localName.equals("Description")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = true;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = false;
            }
            else if (localName.equals("City")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = false;
            	AtCity = true;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = false;
            }
            else if (localName.equals("State")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = false;
            	AtCity = false;
            	AtState = true;
            	AtWebsite = false;
            	AtScase = false;
            }
            else if (localName.equals("Website")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = false;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = true;
            	AtScase = false;
            }
            else if (localName.equals("SCase")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = false;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = true;
            }
            else if (localName.equals("FTNY")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = false;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = false;
            }
            else if (localName.equals("")) {
            	AtID = false;
            	AtName = false;
            	AtDescription = false;
            	AtCity = false;
            	AtState = false;
            	AtWebsite = false;
            	AtScase = false;
            }
            else {
            	throw new IllegalArgumentException("Element '" + 
            			qName + "' is not allowed here");
            	}
        }
		
		public void endElement(String uri, String localName, String qName) throws SAXException {
            // do nothing
        }
		
		public Vector<Truck> GetAllTrucks()
		{
			return AllTrucks;
		}
	}
}
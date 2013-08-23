package bjk.ftny.DataObjects;

public class Truck
{
	//Private Variables
	private String m_id;
	private String m_truckName;
	private String m_description;
	private String m_city;
	private String m_state;
	private String m_website;
	private boolean m_scase;
	
	//Constructor
	public Truck()
	{
		m_id = "";
		m_truckName = "";
		m_description = "";
		m_city = "";
		m_state = "";
		m_website = "";
		m_scase = false;
	}
	
	//Mutators
	public void SetID(String id)
	{
		m_id = id;
	}
	public void SetTruckName(String truckName)
	{
		m_truckName = truckName;
	}
	public void SetDescription(String description)
	{
		m_description = description;
	}
	public void SetCity(String city)
	{
		m_city = city;
	}
	public void SetState(String state)
	{
		m_state = state;
	}
	public void SetWebsite(String website)
	{
		m_website = website;
	}
	public void SetSpecialCase(boolean SCase)
	{
		m_scase = SCase;
	}
	
	//Access
	public String GetID()
	{
		return m_id;
	}
	public String GetTruckName()
	{
		return m_truckName;
	}
	public String GetDescription()
	{
		return m_description;
	}
	public String GetCity()
	{
		return m_city;
	}
	public String GetState()
	{
		return m_state;
	}
	public String GetWebsite()
	{
		return m_website;
	}
	public boolean GetSpecialCase()
	{
		return m_scase;
	}
}
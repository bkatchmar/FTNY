package bjk.ftny.DataObjects;

public class TruckLocation
{
	private String m_day;
	private String m_place;
	private String m_time;
	
	public TruckLocation()
	{
		m_day = "";
		m_place = "";
		m_time = "";
	}
	
	public void SetDay(String day)
	{
		m_day = day;
	}
	public void SetPlace(String place)
	{
		m_place = place;
	}
	public void SetTime(String time)
	{
		m_time = time;
	}
	
	public String GetDay()
	{
		return m_day;
	}
	public String GetPlace()
	{
		return m_place;
	}
	public String GetTime()
	{
		return m_time;
	}
}
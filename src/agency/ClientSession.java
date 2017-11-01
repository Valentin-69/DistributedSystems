package agency;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import agency.remote.ClientSessionRemote;
import rental.remote.Reservation;
import rental.serializable.Quote;
import rental.serializable.ReservationException;

public class ClientSession implements ClientSessionRemote {
	
	private final AgencyManager agency;
	private final String clientName;
	
	public ClientSession(AgencyManager agency, String clientName) {
		this.agency=agency;
		this.clientName=clientName;
	}

	@Override
	public void addQuoteToSession(String name, Date start, Date end, String carType, String region) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Quote> getCurrentQuotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Reservation> confirmQuotes(ClientSessionRemote session, String name) throws ReservationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAvailableCarTypes(Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCheapestCarType(Date start, Date end, String region) {
		// TODO Auto-generated method stub
		return null;
	}

}

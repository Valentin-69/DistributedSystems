package agency;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import agency.remote.ClientSessionRemote;
import rental.remote.Reservation;
import rental.serializable.Quote;
import rental.serializable.ReservationException;

public class ClientSession implements ClientSessionRemote {
	
	private final AgencyManager agency;
	private final String clientName;
	private final String id;
	
	private HashSet<Quote> unconfirmedQuotes = new HashSet<>();
	
	public ClientSession(AgencyManager agency, String clientName, String id) {
		this.agency=agency;
		this.clientName=clientName;
		this.id = id;
	}

	@Override
	public void addQuoteToSession(String name, Date start, Date end, String carType, String region) throws ReservationException, RemoteException{
		Quote newQuote = agency.createQuote(name,start,end,carType,region);
		unconfirmedQuotes.add(newQuote);
		System.out.println("quote added: "+newQuote);
	}

	@Override
	public Set<Quote> getCurrentQuotes() {
		return unconfirmedQuotes;
	}

	@Override
	public ArrayList<Reservation> confirmQuotes() throws ReservationException, RemoteException {
		return agency.atomicConfirm(unconfirmedQuotes);
	}

	@Override
	public Set<String> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		return agency.getAvailableCarTypes(start,end).stream().map(c->c.getName()).collect(Collectors.toSet());
	}

	@Override
	public String getCheapestCarType(Date start, Date end, String region) throws RemoteException {
		return agency.getCheapestCarType(start,end,region).getName();
	}
	
	public String getID(){
		return id;
	}

	@Override
	public void terminate() throws RemoteException {
		agency.stopSession(this);
	}
	
}

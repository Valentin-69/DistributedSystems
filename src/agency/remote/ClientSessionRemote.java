package agency.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import rental.remote.Reservation;
import rental.serializable.Quote;
import rental.serializable.ReservationException;

public interface ClientSessionRemote extends Remote{

	void addQuoteToSession(String name, Date start, Date end, String carType, String region) throws RemoteException;
	
	Set<Quote> getCurrentQuotes() throws RemoteException;
	
	ArrayList<Reservation> confirmQuotes(ClientSessionRemote session, String name) throws RemoteException, ReservationException;
	
	Set<String> getAvailableCarTypes(Date start, Date end) throws RemoteException;
	
	String getCheapestCarType(Date start, Date end, String region) throws RemoteException;
	
}

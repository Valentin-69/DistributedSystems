package rental.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import rental.serializable.CarType;
import rental.serializable.Quote;
import rental.serializable.ReservationConstraints;
import rental.serializable.ReservationException;

public interface CarRentalCompanyRemote extends Remote {

	Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
	
	Quote createQuote(ReservationConstraints constraints, String client) throws RemoteException, ReservationException;
	
	Reservation confirmQuote(Quote quote) throws RemoteException, ReservationException;
	
	void cancelReservation(Reservation res) throws RemoteException;

	List<Reservation> getReservationsByRenter(String clientName) throws RemoteException;
	
	int getNumberOfReservationsForCarType(String carType) throws RemoteException;
	
	String getName() throws RemoteException;
	
	HashMap<String,Integer> getClientToNbOfReservationsMap() throws RemoteException;

	CarType getMostPopularCarTypeIn(int year) throws RemoteException;

	boolean hasRegion(String region) throws RemoteException;

}

package agency;


import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import agency.remote.ClientSessionRemote;
import agency.remote.ManagerSessionRemote;
import agency.remote.NameServerRemote;
import rental.remote.CarRentalCompanyRemote;
import rental.remote.Reservation;
import rental.serializable.CarType;
import rental.serializable.Quote;
import rental.serializable.ReservationConstraints;
import rental.serializable.ReservationException;


public class AgencyManager implements NameServerRemote {

	//this map is not really used in our implementation but is there, should we have to implement saving the registered companies location (for example when the system crashes)
	private final HashMap<String, String> registeredCompanyNameToHostMap = new HashMap<>(); 

	private final HashMap<String, CarRentalCompanyRemote> registeredCompaniesByName = new HashMap<>();
	
	//these two maps are used to implement session handling
	private final HashMap<String, ManagerSession> managerSessionsByName = new HashMap<>();
	private final HashMap<String, ClientSession> clientSessionsByName = new HashMap<>();
	
	private static int nextID = 1;

	private final Registry registry;
	
	public AgencyManager() throws RemoteException {
		 registry = LocateRegistry.getRegistry();
		//if we allready have knowledge about existing companies they can be registered here
	}
	
	
	public synchronized void registerCompany(String name, String host) throws AccessException, RemoteException, NotBoundException{
		if(registeredCompanyNameToHostMap.containsKey(name)){
			throw new IllegalStateException("A company with that name already exists");
		}
		
		Registry registry = LocateRegistry.getRegistry(host);
		CarRentalCompanyRemote newCompany = (CarRentalCompanyRemote) registry.lookup(name);
		
		registeredCompaniesByName.put(name,newCompany);
		registeredCompanyNameToHostMap.put(name, host);
	}
	
	public synchronized void unregisterCompany(String name){
		registeredCompaniesByName.remove(name);
		registeredCompanyNameToHostMap.remove(name);
	}
	
	public Set<String> getRegisteredCompanies() {
		return registeredCompaniesByName.values().stream().map(c -> {
			try {
				return c.getName();
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toSet());
	}
	
	@Override
	public String getNewReservationSessionID(String clientName) throws AccessException, RemoteException {
		ClientSession newSession = new ClientSession(this, clientName, nextID());
		ClientSessionRemote stub = (ClientSessionRemote) UnicastRemoteObject.exportObject(newSession, 0);
		registry.rebind(newSession.getID(), stub);
		clientSessionsByName.put(newSession.getID(), newSession);
		return newSession.getID();
	}
	
	public void stopSession(ClientSession session){
		managerSessionsByName.remove(session.getID());
		try{
			registry.unbind(session.getID());
		}catch(Exception e){
			System.out.println("could not unbind session "+session.getID());
			e.printStackTrace();
		}
	}
	
	public void stopSession(ManagerSession session) {
		managerSessionsByName.remove(session.getID());
		try{
			registry.unbind(session.getID());
		}catch(Exception e){
			System.out.println("could not unbind session "+session.getID());
			e.printStackTrace();
		}
	}

	@Override
	public String getNewManagerSessionID(String managerName) throws AccessException, RemoteException{
		ManagerSession newSession = new ManagerSession(this, managerName, nextID());
		ManagerSessionRemote stub = (ManagerSessionRemote) UnicastRemoteObject.exportObject(newSession, 0);
		registry.rebind(newSession.getID(), stub);
		managerSessionsByName.put(newSession.getID(), newSession);
		return newSession.getID();
	}
	
	private String nextID(){
		return Integer.toString(nextID++);
	}


	// this is synchronized so that a company can't be removed between confirming and canceling a reservation
	public synchronized ArrayList<Reservation> atomicConfirm(HashSet<Quote> unconfirmedQuotes) throws ReservationException, RemoteException{
		HashSet<Reservation> reservations = new HashSet<>();
		for (Quote quoteToConfirm : unconfirmedQuotes) {
			try{
				reservations.add(registeredCompaniesByName.get(quoteToConfirm.getRentalCompany()).confirmQuote(quoteToConfirm));
			}catch(Exception e){
				for (Reservation reservationToCancel : reservations) {
					registeredCompaniesByName.get(reservationToCancel.getRentalCompany()).cancelReservation(reservationToCancel);
				}
				throw new ReservationException("failed to confirm all quotes");
			}
		}
		return new ArrayList<>(reservations);
	}
	
	public Quote createQuote(String name, Date start, Date end, String carType, String region) throws ReservationException, RemoteException{
		for (CarRentalCompanyRemote company : registeredCompaniesByName.values()) {
			try{
				return company.createQuote(new ReservationConstraints(start, end, carType, region), name);
			}catch(Exception e){	}
		}
		throw new ReservationException("<" + name+ "> No cars available to satisfy the given constraints.");
	}


	public Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		HashSet<CarType> result = new HashSet<>();
		for (CarRentalCompanyRemote company : registeredCompaniesByName.values()) {
			result.addAll(company.getAvailableCarTypes(start, end));
		}
		return result;
	}


	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException {
		CarType cheapestCarType = new CarType("No cartype found", 0, 0, Double.MAX_VALUE, false);
		for (CarRentalCompanyRemote company : registeredCompaniesByName.values()) {
			if(!company.hasRegion(region)){
				continue;
			}
			for(CarType newCarType : company.getAvailableCarTypes(start, end)){
				if(newCarType.getRentalPricePerDay()<cheapestCarType.getRentalPricePerDay()){
					cheapestCarType = newCarType;
				}
			}
		}
		return cheapestCarType;
	}


	public int getNumberOfReservationsForCarType(String carRentalName, String carType) throws RemoteException {
		return registeredCompaniesByName.get(carRentalName).getNumberOfReservationsForCarType(carType);
	}


	public Set<String> getBestClients() throws RemoteException {
		HashMap<String, Integer> clientsToNbReservation = new HashMap<>();
		for (CarRentalCompanyRemote company : registeredCompaniesByName.values()) {
			for(Entry<String, Integer> newClientToNReservation : company.getClientToNbOfReservationsMap().entrySet()){
				clientsToNbReservation.put(newClientToNReservation.getKey(),
						clientsToNbReservation.getOrDefault(newClientToNReservation.getKey(), 0)+newClientToNReservation.getValue());
			}
		}
		
		int maxReservations=-1;
		
		for(int nbOReservations : clientsToNbReservation.values()){
			maxReservations=Math.max(maxReservations, nbOReservations);
		}
		
		HashSet<String> result = new HashSet<>();
		for(Entry<String, Integer> clientToReservation : clientsToNbReservation.entrySet()){
			if(clientToReservation.getValue()==maxReservations){
				result.add(clientToReservation.getKey());
			}
		}
		return result;
	}


	public CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) throws RemoteException {
		return registeredCompaniesByName.get(carRentalCompanyName).getMostPopularCarTypeIn(year);
	}

}

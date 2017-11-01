package client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import agency.remote.ManagerSessionRemote;
import agency.remote.NameServerRemote;
import agency.remote.ClientSessionRemote;
import rental.remote.Reservation;
import rental.serializable.CarType;

public class Main extends AbstractTestManagement<ClientSessionRemote, ManagerSessionRemote>{

	private Registry agencyRegistry;
	
	private final NameServerRemote agency;

	public Main(String scriptFile) throws RemoteException, NotBoundException {
		super(scriptFile);
		agencyRegistry=LocateRegistry.getRegistry();
		agency = initAgency();
	}

	private NameServerRemote initAgency() throws AccessException, RemoteException, NotBoundException {
		return (NameServerRemote) agencyRegistry.lookup("Agency");
	}

	public static void main(String[] args) throws Exception {
		Main simulation = new Main("smallTrips");
		ManagerSessionRemote session = simulation.getNewManagerSession("Valentin", "whyDoesThisArgumentStillExist");
		session.registerCompany("Dockx", null);
		session.registerCompany("Hertz", null);
		session.terminate();
		simulation.run();
	}


	@Override
	protected Set<String> getBestClients(ManagerSessionRemote ms) throws Exception {
		return ms.getBestClients();
	}

	@Override
	protected String getCheapestCarType(ClientSessionRemote session, Date start, Date end, String region)
			throws Exception {
		return session.getCheapestCarType(start, end, region);
	}

	@Override
	protected CarType getMostPopularCarTypeIn(ManagerSessionRemote ms, String carRentalCompanyName, int year)
			throws Exception {
		return ms.getMostPopularCarTypeIn(carRentalCompanyName, year);
	}

	@Override
	protected ClientSessionRemote getNewReservationSession(String name) throws Exception {
		return (ClientSessionRemote) agencyRegistry.lookup(agency.getNewReservationSessionID(name));
	}

	@Override
	protected ManagerSessionRemote getNewManagerSession(String name, String carRentalName) throws Exception {
		return (ManagerSessionRemote) agencyRegistry.lookup(agency.getNewManagerSessionID(name));
	}

	@Override
	protected void checkForAvailableCarTypes(ClientSessionRemote session, Date start, Date end) throws Exception {
		System.out.println("Available car types: ");
		for (String carType : session.getAvailableCarTypes(start, end)) {
			System.out.println(carType);
		};
	}

	@Override
	protected void addQuoteToSession(ClientSessionRemote session, String name, Date start, Date end, String carType,
			String region) throws Exception {
		session.addQuoteToSession(name, start, end, carType, region);
	}

	@Override
	protected List<Reservation> confirmQuotes(ClientSessionRemote session, String name) throws Exception {
		return session.confirmQuotes();
	}

	@Override
	protected int getNumberOfReservationsForCarType(ManagerSessionRemote ms, String carRentalName, String carType)
			throws Exception {
		return ms.getNumberOfReservationsForCarType(carRentalName, carType);
	}

}

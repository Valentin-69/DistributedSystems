package client;

import java.util.Date;
import java.util.List;
import java.util.Set;

import agency.remote.ManagerSessionRemote;
import agency.remote.ClientSessionRemote;
import rental.remote.Reservation;
import rental.serializable.CarType;

public class Main extends AbstractTestManagement<ClientSessionRemote, ManagerSessionRemote>{

	public Main(String scriptFile) {
		super(scriptFile);
	}

	public static void main(String[] args) {
		System.out.println("starting main");
		new Main("trips");
	}


	@Override
	protected Set<String> getBestClients(ManagerSessionRemote ms) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCheapestCarType(ClientSessionRemote session, Date start, Date end, String region)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CarType getMostPopularCarTypeIn(ManagerSessionRemote ms, String carRentalCompanyName, int year)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ClientSessionRemote getNewReservationSession(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ManagerSessionRemote getNewManagerSession(String name, String carRentalName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void checkForAvailableCarTypes(ClientSessionRemote session, Date start, Date end) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addQuoteToSession(ClientSessionRemote session, String name, Date start, Date end, String carType,
			String region) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<Reservation> confirmQuotes(ClientSessionRemote session, String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getNumberOfReservationsForCarType(ManagerSessionRemote ms, String carRentalName, String carType)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	

}

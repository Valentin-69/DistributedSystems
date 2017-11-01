package agency;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

import agency.remote.ManagerSessionRemote;
import rental.serializable.CarType;

public class ManagerSession implements ManagerSessionRemote {
	
	private final AgencyManager agency;
	private final String managerName;
	private final String id;
	
	public ManagerSession(AgencyManager agency, String managerName, String id) {
		this.agency=agency;
		this.managerName=managerName;
		this.id = id;
	}

	@Override
	public void registerCompany(String name, String host) throws Exception {
		agency.registerCompany(name, host);
	}

	@Override
	public void unregisterCompany(String name) throws RemoteException {
		agency.unregisterCompany(name);
	}

	@Override
	public Set<String> getRegisteredCompanies() throws RemoteException {
		return agency.getRegisteredCompanies();
	}

	@Override
	public int getNumberOfReservationsForCarType(String carRentalName, String carType)
			throws RemoteException {
		return agency.getNumberOfReservationsForCarType(carRentalName, carType);
	}

	@Override
	public Set<String> getBestClients() throws RemoteException {
		return agency.getBestClients();
	}

	@Override
	public CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) throws RemoteException {
		return agency.getMostPopularCarTypeIn(carRentalCompanyName,year);
	}

	public String getID() {
		return id;
	}

	@Override
	public void terminate() throws RemoteException {
		agency.stopSession(this);
	}

}

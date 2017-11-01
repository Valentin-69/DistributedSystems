package agency;

import java.rmi.RemoteException;
import java.util.Set;

import agency.remote.ManagerSessionRemote;
import rental.serializable.CarType;

public class ManagerSession implements ManagerSessionRemote {
	
	private final AgencyManager agency;
	private final String managerName;
	
	public ManagerSession(AgencyManager agency, String managerName) {
		this.agency=agency;
		this.managerName=managerName;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> getBestClients(ManagerSessionRemote ms) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}

package agency.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import rental.serializable.CarType;

public interface ManagerSessionRemote extends Remote {
	
	void registerCompany(String name, String host) throws Exception;
	
	void unregisterCompany(String name) throws RemoteException;
	
	Set<String> getRegisteredCompanies() throws RemoteException;
	
	int getNumberOfReservationsForCarType(String carRentalName, String carType) throws RemoteException;
	
	Set<String> getBestClients() throws RemoteException;

	CarType getMostPopularCarTypeIn(String carRentalCompanyName, int year) throws RemoteException;
	
	void terminate() throws RemoteException;

}

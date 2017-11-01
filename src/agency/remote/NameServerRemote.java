package agency.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameServerRemote extends Remote {

	ClientSessionRemote getNewReservationSession(String clientName) throws RemoteException;

	ManagerSessionRemote getNewManagerSession(String managerName, String carRentalName) throws RemoteException, IllegalArgumentException;
	
}

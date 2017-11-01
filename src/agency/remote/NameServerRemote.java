package agency.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameServerRemote extends Remote {

	String getNewReservationSessionID(String clientName) throws RemoteException;

	String getNewManagerSessionID(String managerName) throws RemoteException;
	
}

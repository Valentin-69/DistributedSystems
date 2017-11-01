package agency;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import agency.remote.NameServerRemote;

public class AgencyServer {

	public static void main(String[] args) throws RemoteException {
		AgencyManager agency = new AgencyManager();
		NameServerRemote stub = (NameServerRemote) UnicastRemoteObject.exportObject(agency, 0);
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("Agency", stub);
	}

}

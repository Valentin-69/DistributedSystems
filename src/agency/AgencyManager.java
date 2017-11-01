package agency;


import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import agency.remote.ClientSessionRemote;
import agency.remote.ManagerSessionRemote;
import agency.remote.NameServerRemote;
import rental.remote.CarRentalCompanyRemote;


public class AgencyManager implements NameServerRemote {

	//this map is not really used in our implementation but is there, should we have to implement saving the registered companies location (for example when the system crashes)
	private final HashMap<String, String> registeredCompanyNameToHostMap = new HashMap<>(); 

	private final HashMap<String, CarRentalCompanyRemote> registeredCompaniesByName = new HashMap<>();
	
	//these two maps would be used to implement session handling
	private final HashSet<ManagerSession> managerSessionsByName = new HashSet<>();
	private final HashSet<ClientSession> clientSessionsByName = new HashSet<>();

	public AgencyManager() {
		//if we allready have knowledge about existing companies they can be registered here
	}
	
	
	public void registerCompany(String name, String host) throws AccessException, RemoteException, NotBoundException{
		if(registeredCompanyNameToHostMap.containsKey(name)){
			throw new IllegalStateException("A company with that name already exists");
		}
		
		Registry registry = LocateRegistry.getRegistry(host);
		CarRentalCompanyRemote newCompany = (CarRentalCompanyRemote) registry.lookup(name);
		
		registeredCompaniesByName.put(name,newCompany);
		registeredCompanyNameToHostMap.put(name, host);
	}
	
	public void unregisterCompany(String name){
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
	public ClientSessionRemote getNewReservationSession(String clientName) {
		return null;
	}
	
	public void stopSession(ClientSession session){
		clientSessionsByName.remove(session);
	}
	
	public void stopSession(ManagerSession session){
		managerSessionsByName.remove(session);
	}

	@Override
	public ManagerSessionRemote getNewManagerSession(String managerName, String carRentalName)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}


	

}

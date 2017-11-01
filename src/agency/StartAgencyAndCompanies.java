package agency;

import java.io.IOException;
import java.rmi.RemoteException;

import rental.RentalServer;
import rental.serializable.ReservationException;

/**
 * This class is used to simulate that the rental agency and the companies run on different servers (here different threads).
 */
public class StartAgencyAndCompanies {

	public static void main(String[] args){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					AgencyServer.main(null);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					RentalServer.main(null);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ReservationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}

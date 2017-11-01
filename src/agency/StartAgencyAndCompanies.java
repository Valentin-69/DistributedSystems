package agency;

import java.io.IOException;

import rental.RentalServer;
import rental.serializable.ReservationException;

/**
 * This class is used to simulate that the rental agency and the companies run on different servers (here different threads).
 */
public class StartAgencyAndCompanies {

	public static void main(String[] args) throws NumberFormatException, ReservationException, IOException{
		AgencyServer.main(null);
		RentalServer.main(null);
	}
	
}

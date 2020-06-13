package tcc;

import tcc.flight.FlightReservationDoc;
import tcc.hotel.HotelReservationDoc;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Simple non-transactional client. Can be used to populate the booking services
 * with some requests.
 */
public class TestClient {
    public static void main(String[] args) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(TestServer.BASE_URI);
            int NUMBER_OF_RETRIES = 3;
            GregorianCalendar tomorrow = new GregorianCalendar();
            tomorrow.setTime(new Date());
            tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 1);

            // book flight

            WebTarget webTargetFlight = target.path("flight");

            FlightReservationDoc docFlight = new FlightReservationDoc();
            docFlight.setName("Christian");
            docFlight.setFrom("Karlsruhe");
            docFlight.setTo("Berlin");
            docFlight.setAirline("airberlin");
            docFlight.setDate(tomorrow.getTimeInMillis());

            Response responseFlight = webTargetFlight.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docFlight));

            if (responseFlight.getStatus() != 200) {
                System.out.println("Failed : HTTP error code : " + responseFlight.getStatus());
            }

            FlightReservationDoc outputFlight = responseFlight.readEntity(FlightReservationDoc.class);
            System.out.println("Output from Server: " + outputFlight);

            // book hotel

            WebTarget webTargetHotel = target.path("hotel");

            HotelReservationDoc docHotel = new HotelReservationDoc();
            docHotel.setName("Christian");
            docHotel.setHotel("Interconti");
            docHotel.setDate(tomorrow.getTimeInMillis());

            Response responseHotel = webTargetHotel.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docHotel));

            if (responseHotel.getStatus() != 200) {
                System.out.println("Failed : HTTP error code : " + responseHotel.getStatus());
            }

            HotelReservationDoc outputHotel = responseHotel.readEntity(HotelReservationDoc.class);
            System.out.println("Output from Server: " + outputHotel);

            // both operation are into reserved state
            if (responseHotel.getStatus() == 200 && responseFlight.getStatus() == 200) {

                boolean hotelConformationSuccessful = false;

                // Hotel reservation confirmation try loop
                for(int i = 0; i < NUMBER_OF_RETRIES; i++) {
                    // confirm Hotel Reservation
                    WebTarget webTargetHotelConformation = client.target(outputHotel.getUrl());
                    Response responseHotelConformation = webTargetHotelConformation
                            .request()
                            .accept(MediaType.TEXT_PLAIN)
                            .put(Entity.xml(docHotel));

                    // stop looping if the confirmation was successful
                    if (responseHotelConformation.getStatus() == 200) {
                        System.out.println("Output from Server: Hotel Conformation successful");
                        hotelConformationSuccessful = true;
                        break;
                    }
                }

                // hotel conformation successful, now confirm flight reservation
                if (hotelConformationSuccessful) {
                    boolean flightConformationSuccessful = false;

                    // Flight reservation confirmation try loop
                    for(int i = 0; i < NUMBER_OF_RETRIES; i++) {
                        WebTarget webTargetFlightConformation = client.target(outputFlight.getUrl());
                        Response responseFlightConformation = webTargetFlightConformation
                                .request()
                                .accept(MediaType.TEXT_PLAIN)
                                .put(Entity.xml(docFlight));

                        // final state reached
                        if (responseFlightConformation.getStatus() == 200) {
                            System.out.println("Output from Server: Flight Conformation successful, Final State reached");
                            flightConformationSuccessful = true;
                            break;
                        }
                    }
                }
            }
            // hotel reservation not successful, rollback of flight reservation required
            else if (responseHotel.getStatus() != 200 && responseFlight.getStatus() == 200) {
                System.out.println("Hotel reservation unsuccessful, rolling back flight reservation.");
                WebTarget webTargetFlightRollback = client.target(outputFlight.getUrl());
                Response responseFlightRollback = webTargetFlightRollback
                        .request()
                        .accept(MediaType.TEXT_PLAIN)
                        .delete();
                if (responseFlightRollback.getStatus() == 200) {
                    System.out.println("Rollback of flight reservation successful");
                } else {
                    System.out.println("Rollback of flight reservation unsuccessful, leaving it for timeout.");
                }
            }
            // flight reservation not successful, but
            else if (responseFlight.getStatus() != 200 && responseHotel.getStatus() == 200) {
                System.out.println("Flight reservation unsuccessful, rolling back hotel reservation.");
                WebTarget webTargetHotelRollback = client.target(outputHotel.getUrl());
                Response responseHotelRollback = webTargetHotelRollback
                        .request()
                        .accept(MediaType.TEXT_PLAIN)
                        .delete();
                if (responseHotelRollback.getStatus() == 200) {
                    System.out.println("Rollback of hotel reservation successful");
                } else {
                    System.out.println("Rollback of hotel reservation unsuccessful, leaving it for timeout.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

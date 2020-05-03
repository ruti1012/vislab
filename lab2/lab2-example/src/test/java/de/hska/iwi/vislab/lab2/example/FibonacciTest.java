package de.hska.iwi.vislab.lab2.example;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FibonacciTest {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
		Client c = ClientBuilder.newClient();

		// uncomment the following line if you want to enable
		// support for JSON in the client (you also have to uncomment
		// dependency on jersey-media-json module in pom.xml and
		// Main.startServer())
		// --
		// c.configuration().enable(new
		// org.glassfish.jersey.media.json.JsonJaxbFeature());

		target = c.target(Main.BASE_URI);
	}

	@After
	public void tearDown() throws Exception {
		server.shutdown();
	}
	/**
	 * Sets the index to 5 and should calculate the 10th position for the fibonacci numbers as 5
	 */
	@Test
	public void testFibonacciCalc() {

		target.path("fibonacci").request().accept(MediaType.APPLICATION_JSON).get();
		target.path("fibonacci").request().accept(MediaType.APPLICATION_JSON).get();
		target.path("fibonacci").request().accept(MediaType.APPLICATION_JSON).get();
		target.path("fibonacci").request().accept(MediaType.APPLICATION_JSON).get();
		String response = target.path("fibonacci").request().accept(MediaType.APPLICATION_JSON).get(String.class);
		assertEquals("{\"currentFibIndex\":5,\"currentFibValue\":5}", response);
		target.path("fibonacci").request().delete();
		String responseAfterReset = target.path("fibonacci").request().accept(MediaType.APPLICATION_JSON).get(String.class);
		assertEquals("{\"currentFibIndex\":1,\"currentFibValue\":1}", responseAfterReset);
	}
}

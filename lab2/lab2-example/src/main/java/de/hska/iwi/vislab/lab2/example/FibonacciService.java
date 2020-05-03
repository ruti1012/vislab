package de.hska.iwi.vislab.lab2.example;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.annotation.Repeatable;
import java.util.HashMap;
import java.util.Map;

@Path("fibonacci")
public class FibonacciService {
	Gson gson = new Gson();
	private static int currentIndex = 0;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response currentFibonacciValue() {

		FibonacciService.currentIndex++;
		Map<String, Integer> response = new HashMap<>();
		response.put("currentFibIndex", FibonacciService.currentIndex);
		response.put("currentFibValue", this.calcFibValue(FibonacciService.currentIndex));
		String jsonResponse = gson.toJson(response);
		return Response
				.status(Response.Status.OK)
				.entity(jsonResponse)
				.build();
	}

	@DELETE
	public Response resetCurrentIndex() {
		FibonacciService.currentIndex = 0;
		return Response
				.status(Response.Status.OK)
				.build();
	}

	Integer calcFibValue(Integer n) {
		int result;
		if (n > 2) {

			result = calcFibValue(n - 1) + calcFibValue(n - 2);

		} else if (n == 0) {
			result = 0;
		} else {
			result = 1;
		}
		return result;
	}

}

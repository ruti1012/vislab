package de.hska.iwi.vislab.lab1.example.ws;

import javax.jws.WebService;

/** Dienstimplementierung */
@WebService(endpointInterface = "de.hska.iwi.vislab.lab1.example.ws.FibonacciService")
public class FibonacciServiceImpl implements FibonacciService {

	public Integer getFibonacci(Integer input) {
		return fib(input);
	}

	static int fib(int n) {

		int ergebnis = 0;

		if (n > 2) {

			ergebnis = fib(n - 1) + fib(n - 2);

		} else if (n == 0) {

			ergebnis = 0;
		} else { // f1 = 0 und f2 = 1
			ergebnis = 1;
		}

		return ergebnis;
	}
}
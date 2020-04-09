package de.hska.iwi.vislab.lab1.example.ws;

import javax.jws.*;

/** Dienst-Interface */
@WebService
public interface FibonacciService {
	Integer getFibonacci(@WebParam(name = "fibonacciNum") Integer fibNum);

}
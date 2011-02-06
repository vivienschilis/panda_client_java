package com.pandastream;

import java.util.*;

/**
 * A Abstract HTTP connection interface.
 */

public interface RestClientInterface
{
  
	void setUrl(String url);
	
  /**
   * Perform an HTTP GET request to the specified URL.
   * 
   * @param url the {@link Url} to HTTP GET.
   * @return a String containing the body of the HTTP GET response.
   */
  String get(String uri, TreeMap params);
  
  /**
   * Perform an HTTP POST request to the specified URL.
   * 
   * @param url the {@link Url} to HTTP POST.
   * @return a String containing the body of the HTTP POST response.
   */
  String post(String uri, TreeMap params);

	/**
	 * Perform an HTTP PUT request to the specified URL.
	 * 
	 * @param url the {@link Url} to HTTP GET.
	 * @return a String containing the body of the HTTP GET response.
	 */
	String put(String uri, TreeMap params);
	
	/**
   * Perform an HTTP DELETE request to the specified URL.
   * 
   * @param url the {@link Url} to HTTP GET.
   * @return a String containing the body of the HTTP GET response.
   */
  String delete(String uri, TreeMap params);

}

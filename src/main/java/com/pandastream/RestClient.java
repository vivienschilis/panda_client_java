package com.pandastream;

import java.util.*;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;

import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.pandastream.RestClientInterface;

public class RestClient implements RestClientInterface {
	private String apiUrl;

	public RestClient(String url) {
		setUrl(url);
	}
	
	public void setUrl(String url) {
		apiUrl = url;
	}
	
	public String getUrl() {
		return apiUrl;
	}
	
  public String get(String uri, TreeMap params) {
    assert (uri != null);
    GetMethod method = new GetMethod(getUrl() + uri);
    method.setQueryString(TreeMapToNameValuePairArray(params));
    method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    method.getParams().setParameter(
        HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    return execute(method);
  }

  public String put(String uri, TreeMap params) {
	  assert (uri != null);
    GetMethod method = new GetMethod(getUrl() + uri);
    method.setQueryString(TreeMapToNameValuePairArray(params));
    method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    method.getParams().setParameter(
        HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    return execute(method);
  }

  public String post(String uri, TreeMap params) {
    assert (uri != null);
    PostMethod method = new PostMethod(getUrl() + uri);
    // if (url.getPartsCount() > 0) {
    //   method.setRequestEntity(new MultipartRequestEntity(getParts(url), method
    //       .getParams()));
    // } else {
	  //   method.setRequestBody(getParametersAsNamedValuePairArray(url));
		// }
		
    // method.setRequestBody(getParametersAsNamedValuePairArray(url));
    method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    return execute(method);
  }
	
  public String delete(String uri, TreeMap params) {
    assert (uri != null);
    PostMethod method = new PostMethod(getUrl() + uri);
    method.setQueryString(TreeMapToNameValuePairArray(params));
    method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    method.getParams().setParameter(
        HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    return execute(method);
  }

  private String execute(HttpMethod method) {

    HttpClient httpClient = new HttpClient();

    try {
      int statusCode = httpClient.executeMethod(method);
      String responseBody = method.getResponseBodyAsString();
      if (responseBody == null) {
        throw new RuntimeException("Expected response body, got null");
      }
      return responseBody;
    } catch (HttpException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      // method.releaseConnection();
    }
  }

	private NameValuePair[] TreeMapToNameValuePairArray(TreeMap map) {
    List<NameValuePair> out = new ArrayList<NameValuePair>();

		Set entries = map.entrySet();
		Iterator it = entries.iterator();

		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();			
			out.add(new NameValuePair(entry.getKey().toString(), entry.getValue().toString()));
		}
		return out.toArray(new NameValuePair[out.size()]);
	}

}

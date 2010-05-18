package uk.co.newbamboo;

/**
 * @author <a href="mailto: vivien@new-bamboo.co.uk">Vivien Schilis</a>
 * @author Vivien Schilis
 */

import java.util.*;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;

public class Panda {
	
	private String apiHost = "staging.pandastream.com";
	private int apiPort = 80;
	private String accessKey;
	private String secretKey;
	private String cloudId;
	DefaultHttpClient httpclient;
	
	public Panda() {
		httpclient = new DefaultHttpClient();
	}
	
	public String apiUrl() {
		return "http://" + apiHost + ":" + apiPort + "/v2";
	}
	
	public void setAccessKey(String accessKey){
		this.accessKey = accessKey;
	}
	
	public void setSecretKey(String secretKey){
		this.secretKey = secretKey;
	}
	
	public void setCloudId(String cloudId){
		this.cloudId = cloudId;
	}
	
	public String getAccessKey(){
		return this.accessKey;
	}
	
	public String getSecretKey(){
		return this.secretKey;
	}
	
	public String getCloudId(){
		return this.cloudId;
	}
	
	public String get(String url, HashMap params) {
		HashMap sParams = signedParams("GET", url, params);
		String flattenParams = canonicalQueryString(sParams);
		
		String requestUrl = this.apiUrl() + url + "?" + flattenParams;
		HttpContext localContext = new BasicHttpContext();
		
		System.out.println(requestUrl);
		HttpGet httpget = new HttpGet(requestUrl);
		
		try {
			HttpResponse response = httpclient.execute(httpget, localContext);
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
			    InputStream instream = entity.getContent();
			    int l;
			    byte[] tmp = new byte[2048];
			    while ((l = instream.read(tmp)) != -1) {
			    }	
					return new String(tmp);
			}
			return "";
			
		}catch(IOException e){
			return "";
		}
	}
	
	public String post(String url, HashMap params) {
		return "";
	}
	
	
	public String put(String url, HashMap params) {
		return "";
	}
	
	
	public String delete(String url, HashMap params) {
		return "";
	}
	
	
	public HashMap signedParams(String method, String url, HashMap params) {
		params.put("cloud_id", this.cloudId);
		params.put("access_key", this.accessKey);
		params.put("timestamp", getTimestamp());
		params.put("signature", Panda.generateSignature(method, url, this.apiHost, this.secretKey, params));
		return params;
	}
	
	private String getTimestamp() {
  	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }
	
	public static String generateSignature(String method, String url, String host, String secretKey, HashMap params) {
		String queryString = Panda.canonicalQueryString(params);
		String stringToSign = method.toUpperCase() + "\n" + host + "\n" + url + "\n" + queryString;
		System.out.println(stringToSign);
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(stringToSign.getBytes("UTF-8"));
			Base64 encoder = new Base64();
			String signature = new String(encoder.encode(md.digest()),"UTF-8");
			System.out.println(signature);
			
			return signature;
		
		}catch (NoSuchAlgorithmException nsae) {
			System.out.println("Cannot find digest algorithm");
			return "";	
		}
		catch (UnsupportedEncodingException uee) {
			System.out.println("Unsupported Encoding");
			return "";
		}
	}
	
	public static String canonicalQueryString(HashMap map) {
		Set entries = map.entrySet();
		Iterator it = entries.iterator();
		String queryString = "";
		
		List qparams = new ArrayList();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			qparams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
		}
		queryString = URLEncodedUtils.format(qparams, "UTF-8");
		return queryString;
	}
	
}

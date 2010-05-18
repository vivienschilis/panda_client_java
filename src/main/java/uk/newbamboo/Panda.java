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
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;

import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


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
	
	public String get(String url, TreeMap params) {
		TreeMap sParams = signedParams("GET", url, params);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = this.apiUrl() + url + "?" + flattenParams;
		HttpGet httpget = new HttpGet(requestUrl);
		
		try {
			HttpResponse response = httpclient.execute(httpget);
			return getStringResponse(response);
		}catch(IOException e){
			return "{error: 'Unexpected'}";
		}
	}
	
	
	public String getStringResponse(HttpResponse response) throws IOException {
		return EntityUtils.toString(response.getEntity());
	}
	
	public String post(String url, TreeMap params) {
		TreeMap sParams = signedParams("POST", url, params);
		String requestUrl = this.apiUrl() + url;
		HttpContext localContext;
		localContext = new BasicHttpContext();
		
		try {
			
  
			// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(TreeMapToList(sParams), "UTF-8"); 
			HttpPost httppost = new HttpPost(requestUrl);
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			httppost.setEntity(new StringEntity(canonicalQueryString(sParams), "UTF-8"));
			HttpResponse response = httpclient.execute(httppost, localContext);
			
			return getStringResponse(response);
			
		}catch(IOException e){
			return "{error: 'Unexpected'}";
		}
	}
	
	
	public String put(String url, TreeMap params) {
		TreeMap sParams = signedParams("PUT", url, params);
		return "";
	}
	
	
	public String delete(String url, TreeMap params) {
		TreeMap sParams = signedParams("DELETE", url, params);
		return "";
	}
	
	
	public TreeMap signedParams(String method, String url, TreeMap params) {
		params.put("cloud_id", this.cloudId);
		params.put("access_key", this.accessKey);
		params.put("timestamp", getTimestamp());
		params.put("signature", Panda.generateSignature(method, url, this.apiHost, this.secretKey, params));
		return params;
	}
	
	private String getTimestamp() {
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
	  return df.format( new Date() );	  
  }
	
	public static String generateSignature(String method, String url, String host, String secretKey, TreeMap params) {
		String queryString = Panda.canonicalQueryString(params);
		String stringToSign = method.toUpperCase() + "\n" + host + "\n" + url + "\n" + queryString;
		
		try {
	
      SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(signingKey);

      byte[] rawHmac = mac.doFinal(stringToSign.getBytes());
     
			Base64 encoder = new Base64();
			String signature = new String(encoder.encodeBase64(rawHmac));
			return signature;
		
		}catch (NoSuchAlgorithmException nsae) {
			System.out.println("Cannot find digest algorithm");
			return "";	
		}catch (InvalidKeyException ike) {
			System.out.println("Unsupported Encoding");
			return "";
		}
	}
	
	public static String canonicalQueryString(TreeMap map) {
		Set entries = map.entrySet();
		Iterator it = entries.iterator();
		String queryString = "";
		
		List qparams = TreeMapToList(map);
		queryString = URLEncodedUtils.format(qparams, "UTF-8");
		return queryString;
	}
	
	private static List TreeMapToList(TreeMap map) {
		Set entries = map.entrySet();
		Iterator it = entries.iterator();
		
		List qparams = new ArrayList();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();			
			qparams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
		}
		return qparams;
	}
}

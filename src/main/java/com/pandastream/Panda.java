package com.pandastream;

/**
 * @author <a href="mailto: vivien@new-bamboo.co.uk">Vivien Schilis</a>
 * @author Vivien Schilis
 */

import java.util.*;
import java.io.IOException;

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


import com.pandastream.RestClient;

public class Panda {
	
	private String apiHost = "staging.pandastream.com";
	private int apiPort = 80;
	private String accessKey;
	private String secretKey;
	private String cloudId;
	
	public Panda() {
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
	
	public RestClient getHttpClient(){
		return new RestClient(apiUrl());
	}
	
	public String get(String uri, TreeMap params) {
		return getHttpClient().get(uri, signedParams("GET", uri, params));
	}

	public String post(String uri, TreeMap params) {
		return getHttpClient().post(uri, signedParams("POST", uri, params));
	}

	public String put(String uri, TreeMap params) {
		return getHttpClient().put(uri, signedParams("PUT", uri, params));
	}

	public String delete(String uri, TreeMap params) {
		return getHttpClient().delete(uri, signedParams("DELETE", uri, params));
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
		String queryString = canonicalQueryString(params);
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
		String queryString = "";
    Set entries = map.entrySet();
    Iterator it = entries.iterator();

    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();                        
			queryString += entry.getKey().toString() + '=' + entry.getValue().toString();
    }

    return queryString;
	}

}

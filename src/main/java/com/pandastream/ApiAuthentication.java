package com.pandastream;

import java.util.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

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

import java.net.URLEncoder;

public class ApiAuthentication {

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
	
	private static String canonicalQueryString(TreeMap map) {
		String queryString = "";
    Set entries = map.entrySet();
    Iterator it = entries.iterator();

    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();                        
			queryString += entry.getKey().toString() + '=' + URLEncoder.encode(entry.getValue().toString()); 
			if (it.hasNext())
				queryString += '&';
    }

    return queryString;
	}
	
}
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

import com.pandastream.ApiAuthentication;
import com.pandastream.RestClient;


import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

public class Panda {
	
	private String apiHost = "staging.pandastream.com";
	private int apiPort = 80;
	private String accessKey;
	private String secretKey;
	private String cloudId;
	
	public Panda(String accessKey, String secretKey, String cloudId) {
		setAccessKey(accessKey);
		setSecretKey(secretKey);
		setCloudId(cloudId);
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
		
		String signature = ApiAuthentication.generateSignature(method, url, this.apiHost, this.secretKey, params);
		params.put("signature", signature);
		
		return params;
	}
	
	private String getTimestamp() {
		
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
	  dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
	
		return dateFormatGmt.format( new Date() );
  }

	public List<Video> getVideos() {
		String videoPath = "/videos.json";
		String jsonString = get(videoPath, new TreeMap());
		JSONArray jsonObject = JSONArray.fromObject(jsonString);
		return newVideoList(jsonObject);
	}	

	public Video getVideo(String id) {
		String videoPath = "/videos/" + id + ".json";
		String jsonString = get(videoPath, new TreeMap());
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return new Video(jsonObject);
	}	

	public List<Encoding> getEncodings() {
		String encodingPath = "/encodings.json";
		String jsonString = get(encodingPath, new TreeMap());
		JSONArray jsonObject = JSONArray.fromObject(jsonString);
		return newEncodingList(jsonObject);
	}
	
	public Encoding getEncoding(String id) {
		String encodingPath = "/encodings/" + id + ".json";
		String jsonString = get(encodingPath, new TreeMap());
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return new Encoding(jsonObject);
	}	

  private List<Video> newVideoList(JSONArray jsonArray) {
		List<Video> videos = new ArrayList<Video>(jsonArray.size());
  	for (int i = 0; i < jsonArray.size(); i++) {
    	videos.add(new Video(jsonArray.getJSONObject(i)));
		}
		return videos;
  }

  private List<Encoding> newEncodingList(JSONArray jsonArray) {
		List<Encoding> encodings = new ArrayList<Encoding>(jsonArray.size());
  	for (int i = 0; i < jsonArray.size(); i++) {
    	encodings.add(new Encoding(jsonArray.getJSONObject(i)));
		}
		return encodings;
  }
}

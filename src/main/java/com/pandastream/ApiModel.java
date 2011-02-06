package com.pandastream;

import java.util.*;
import net.sf.json.JSONObject;

public class ApiModel
{
	
	private JSONObject attributes;	
	
	public ApiModel(TreeMap map) {
// > 		attributes = map;
	}

	public ApiModel(JSONObject json) {
		attributes = json;
	}

	public String get(String name){
		return attributes.get(name).toString();
	}

	public void set(String name, String value) {
		// return attributes.put(name, value);
	}

	public String getId(){
		return get("id");
	}

}
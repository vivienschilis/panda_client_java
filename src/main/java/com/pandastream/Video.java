package com.pandastream;
import java.util.*;

import net.sf.json.JSONObject;

public class Video extends AudioVideoModel {
	
	public Video(TreeMap map) {
		super(map);
	}
	
	public Video(JSONObject json) {
		super(json);
	}
	
	
	public String getOriginalFilename(){
		return get("original_filename");
	}
	
}
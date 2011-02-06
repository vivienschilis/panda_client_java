package com.pandastream;
import java.util.*;
import net.sf.json.JSONObject;

public class Profile extends ApiModel {

	public Profile(TreeMap map) {
		super(map);
	}

	public Profile(JSONObject json) {
		super(json);
	}
	
}
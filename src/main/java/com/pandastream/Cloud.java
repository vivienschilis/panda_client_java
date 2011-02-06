package com.pandastream;
import java.util.*;

import net.sf.json.JSONObject;

public class Cloud extends ApiModel {

	public Cloud(TreeMap map) {
		super(map);
	}
	
	public Cloud(JSONObject json) {
		super(json);
	}

}
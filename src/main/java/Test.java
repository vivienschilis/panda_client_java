import uk.co.newbamboo.*;
import java.util.*;

class Test {
	public static void main(String[] args) {
		Panda panda = new Panda();
		panda.setAccessKey("e6e9e362-3118-11df-90c6-12313909a9c2");
		panda.setSecretKey("fje5f58l12aeodw7hOt8GYnf9Gd9KKlCwH11rht+/");
		panda.setCloudId("3ea18e4753a8db3a96f3fedf163992a8");

		String result = panda.apiUrl();
		System.out.println(result);
		System.out.println("work?");
		
		HashMap params = new HashMap();
		params.put("profiles", "11232445");
		panda.signedParams("POST", "/videos.json", params);
	}
}





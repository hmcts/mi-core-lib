package net.hmcts.reform.mi.utils;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class IPFinder {

	
	private static final String ipAPIURL = "https://api.ipify.org";
	
	
	
	public String getPublicIP() {
		
	    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(IPFinder.ipAPIURL);
	    RestTemplate restTemplate = new RestTemplate();

	    String response = restTemplate.getForObject(builder.toUriString(), String.class);
	    
		return response.trim();
	}
	
	
	
	public static void main(String[] args) {
		
		IPFinder ipFinder = new IPFinder();
		System.out.println("*"+ipFinder.getPublicIP()+"*");

	}

}

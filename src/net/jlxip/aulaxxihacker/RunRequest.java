package net.jlxip.aulaxxihacker;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RunRequest {
	private static final String ContentType = "application/x-www-form-urlencoded";
	private static final String UserAgent = "Mozilla/4.0";
	private static final Pattern Pequals = Pattern.compile(Pattern.quote("="));
	
	public static String[] run(String URL, String METHOD, String query, String cookies) {
		AllowSSL_FIX.run();	// Use SSL despite the shitty MurciaEduca's certificate.
		
		String page = "";
		String returnCookies = "";
		String nextURL = "";
		
		try {
			URL url = new URL(URL);
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod(METHOD);
			con.setRequestProperty("Content-length", String.valueOf(query.length())); 
			con.setRequestProperty("Content-Type", ContentType); 
			con.setRequestProperty("User-Agent", UserAgent);
			if(cookies != null) {
				con.setRequestProperty("Cookie", cookies);
			}
			con.setDoOutput(true); 
			con.setDoInput(true);
			
			DataOutputStream output = new DataOutputStream(con.getOutputStream());
			output.writeBytes(query);
			output.close();
			
			InputStreamReader input = new InputStreamReader(con.getInputStream(), "UTF-8");	// Leemos la salida de la página codificada en UTF-8
			for(int c = input.read(); c != -1; c = input.read()) {
				page += (char)c;
			}
			input.close();
			
			String allcookies = "";
			ArrayList<String> arraycookies = getCookies(con);
			for(int i=0;i<arraycookies.size();i++) {
				if(Pequals.split(arraycookies.get(i))[0].equals("MoodleSession")) {
					allcookies += arraycookies.get(i);
					break;
				}
			}
			returnCookies = allcookies;
			nextURL = con.getHeaderField("Location");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new String[]{page, returnCookies, nextURL};
	}
	
	
	public static ArrayList<String> getCookies(HttpURLConnection con) {
		final String COOKIES_HEADER = "Set-Cookie";

		Map<String, List<String>> headerFields = con.getHeaderFields();
		List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);		

		ArrayList<String> cookies = new ArrayList<String>();
		if (cookiesHeader != null) {
		    for (String cookie : cookiesHeader) {
		    	cookies.add(HttpCookie.parse(cookie).get(0).toString());
		    }               
		}
		
		return cookies;
	}
}

package net.jlxip.aulaxxihacker;

import java.util.regex.Pattern;

public class Login {
	private static final String URL = "https://aulavirtual.murciaeduca.es/login/index.php";
	private static final String FailedPattern = "loginerrormessage";
	
	public static String login(String username, char[] password) {
		String Spassword = "";
		for(int i=0;i<password.length;i++) {
			Spassword += password[i];
		}
		
		String query = "username="+username+"&password="+Spassword+"&rememberusername=1";
		Spassword = null; username = null; password = null;		// Limpiamos los datos sensibles de la memoria
		
		String[] data = RunRequest.run(URL, "POST", query, null);	// Obtener la cookie de inicio de sesión con los datos
		query = null;	// Limpiamos los datos sensibles de la memoria
		
		String Output = RunRequest.run(data[2], "GET", "", data[1])[0];	// Iniciar sesión con esa cookie
		
		Pattern checkLogin = Pattern.compile(Pattern.quote(FailedPattern));
		
		if(checkLogin.split(Output).length > 1) {
			return null;
		} else {
			System.out.println("Logged in with cookie: "+data[1]);
			return data[1];
		}
	}
}
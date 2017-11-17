/**
 * 
 */
package app.bitbucket.thinbus.srp6.js;

import app.auth.CryptoParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

	/**
	 * Constructor 
	 * thinbus-srp6a-js THIS WILL GIVE ME THE K value for
	 * different N and g values i supplied
	 * then I can use the values to configure the javascript class
	 */
     static Logger logger = LoggerFactory.getLogger(Main.class);
     
	public Main() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
            String N_512 = CryptoParams.N_base10;
            String g = CryptoParams.g_base10;
            SRP6JavascriptServerSessionSHA256 server = new SRP6JavascriptServerSessionSHA256(N_512, g);
	    logger.info(server.toString());
		
	}
}
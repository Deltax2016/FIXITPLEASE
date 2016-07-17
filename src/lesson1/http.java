package lesson1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;


public class http {
     
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException, CertificateException, KeyStoreException
	   {
	        new http().testIt();
	   }
	
	public static String getHash(String str) throws NoSuchAlgorithmException,
	UnsupportedEncodingException {

MessageDigest m = MessageDigest.getInstance("MD5");
m.reset();

m.update(str.getBytes("utf-8"));

String s2 = new BigInteger(1, m.digest()).toString(16);
StringBuilder sb = new StringBuilder(32);

for (int i = 0, count = 32 - s2.length(); i < count; i++) {
	sb.append("0");
}

return sb.append(s2).toString();
}
	
	public void testIt() throws IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException {


	     URL url = new URL("https://192.168.1.1/api");

	     HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
	     con.setRequestMethod( "POST" );
	     con.setDoOutput(true);
	     con.setDoInput(true);
	     
	     HostnameVerifier hostnameVerifier = new HostnameVerifier() {

	         public boolean verify(String s, SSLSession sslSession) {
	             return s.equals(sslSession.getPeerHost());
	         }
	     };
	     con.setHostnameVerifier(hostnameVerifier);
	     SSLContext sslContext = SSLContext.getInstance("TLS");
	     con.setSSLSocketFactory(sslContext.getSocketFactory());
	     System.setProperty("javax.net.ssl.trustStore","trustedEntities.trustStore");
	     
	     CertificateFactory cf = CertificateFactory.getInstance("X.509");
	     
		 	InputStream caInput = new BufferedInputStream(new FileInputStream("server.crt"));
		 	
		 	Certificate ca;
		 	try {
		 	    ca = cf.generateCertificate(caInput);
		 	  
		 	} finally {
		 	    caInput.close();
		 	   
		 	}
		
		 	// Create a KeyStore containing our trusted CAs
		 	String keyStoreType = KeyStore.getDefaultType();
		 	KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		 	keyStore.load(null, null);
		 	keyStore.setCertificateEntry("ca", ca);
		 
		 	// Create a TrustManager that trusts the CAs in our KeyStore
		 	String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		 	TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		 	tmf.init(keyStore);
		 	
		 	
		     sslContext.init(null, tmf.getTrustManagers(), null);
	     
	     OutputStream output = con.getOutputStream();  
	     
	     String s = "data="+getHash("{\"method\":\"UPDATE\",\"type\":\"TEMPERATURE\",\"id\":234,\"temperature\":25.9}")+"{\"method\":\"UPDATE\",\"type\":\"TEMPERATURE\",\"id\":234,\"temperature\":25.9}";

	     output.write(s.getBytes());
	     output.flush();
	     output.close();
	    
	     int responseCode = con.getResponseCode();
	     
	     InputStream inputStream;
	     if (responseCode == HttpURLConnection.HTTP_OK) {
	         inputStream = con.getInputStream();
	     } else {
	         inputStream = con.getErrorStream();
	     }

	     BufferedReader reader;
	     String line = "";
	     reader = new BufferedReader( new InputStreamReader( inputStream ) );
	     while( ( line = reader.readLine() ) != null )
	     {
	    	 System.out.println(line);
	     }
	     inputStream.close(); 

	 }
}
/**
 * 
 */
package io.pratik;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * @author pratikdas
 *
 */
public class StreamingDataGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        try {
			new StreamingDataGenerator().generate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void generate() throws Exception {
        File file = getFile("apache_access_log");
        
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        BufferedReader br
            = new BufferedReader(new FileReader(file));
 
        // Declaring a string variable
        String st;
        // Condition holds true till
        // there is character in a string
        while ((st = br.readLine()) != null) {
            System.out.println(st);
            break;
        }
	}
	
	private File getFile(String fileName) throws IOException
	  {
	    ClassLoader classLoader = getClass().getClassLoader();
	        URL resource = classLoader.getResource(fileName);
	         
	        if (resource == null) {
	            throw new IllegalArgumentException("file is not found!");
	        } else {
	            return new File(resource.getFile());
	        }
	  }

}

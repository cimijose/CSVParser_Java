package com.survey.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * This class get the configuration from the properties file
 * @author Cimi
 * @version 1.1
 * @since 12-05-2016
 */
public class Configuration {

	/**
	 * get the value of properties for a given key from the config.properties file
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getProperty(String key) throws IOException {

		Properties prop = new Properties();
		String propFileName = "config.properties";
		String propValue = "";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		propValue = prop.getProperty(key);
		inputStream.close();
		return (propValue==null)?"":propValue;
	}
	
	/**
	 * Configure log properties file
	 * @throws IOException
	 */
	public void loadLogProperties() throws IOException {
		 String log4jConfigFile = System.getProperty("user.dir")+"/"+this.getProperty("LOG_PROPERTIES");
		 PropertyConfigurator.configure(log4jConfigFile);
	}


}

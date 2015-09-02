package main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 *
 * @author ahmadluky
 */

public class Configuration {

		private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
		public static final String ACCOUNT ="resources.account.news";
		
	    @SuppressWarnings("rawtypes")
		public static final Map CONFIG = readConfig();
	
	    @SuppressWarnings("rawtypes")
		public static Map readConfig() {
		    	//read yaml config
		        LOG.info("Try to load oauth config...");
		        Map conf 	= readConfigFile("resources/dict.conf.yaml", true);
		        return conf;
	    }
	
	    @SuppressWarnings({ "rawtypes"})
		public static Map readConfigFile(String file, boolean mustExist) {
	    	
		        Yaml yaml 		= new Yaml(new SafeConstructor());
		        Map ret 		= null;
		        InputStream in 	= null;
		        
				try {
					in = new BufferedInputStream(new FileInputStream(file));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
		        
		        if(in!= null){
		        	LOG.info("Loaded " + file);
		        	ret 		= (Map) yaml.load(new InputStreamReader(in));
		            try{
		                in.close();
		            } catch (IOException e) {
		                LOG.error("IOException: " + e.getMessage());
		            }
		        }else if (mustExist){
		            LOG.error("Config file " + file + " was not found!");
		        }
		        
		        if ((ret == null) && (mustExist))
		            throw new RuntimeException("Config file " + file + " was not found!");
		        
		        return ret;
	    }
	
	    public static <K, V> V get(K key) {
	        	return get(key, null);
	    }
	
	    @SuppressWarnings("unchecked")
		public static <K, V> V get(K key, V defaultValue) {
	        	return get((Map<K, V>) CONFIG, key, defaultValue);
	    }
	
	    public static <K, V> V get(Map<K, V> map, K key, V defaultValue) {
		        V value = map.get(key);
		        if (value == null) {
		                value = defaultValue;
		        }
		        return value;
	    }
	        
	    
	    public static String getAccount(){
				return (String) CONFIG.get(ACCOUNT);
	    }
	    
		
	    /*
	     * Main Function For test Configuration Class
	     */
		public static void main(String[] args) throws IOException, SAXException, InterruptedException {
	    	// exsample use a class
	    }    
} 

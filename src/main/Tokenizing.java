package main;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import tokenizer.Tokenizer;
import utils.AccountUtils;

/**
 * 
 * 	@author ahmadluky
 * 
 * 	Note :
   	Accses fied data twitter example : 
 	String id_str 			= (String) jsonObject.get("id_str");
	String text 			= (String) jsonObject.get("text");
	JSONObject users 		= jsonObject.getJSONObject("user");
	String screen_name 		= users.getString("screen_name");
	String coordinates 		= (String) jsonObject.get("coordinates");
	String retweeted 		= (String) jsonObject.get("retweeted");
	String retweet_count 	= (String) jsonObject.get("retweet_count");
	String favorited 		= (String) jsonObject.get("favorited");
	String favorite_count 	= (String) jsonObject.get("favorite_count");
	String lang 			= (String) jsonObject.get("lang");
	String created_at 		= (String) jsonObject.get("created_at");
	JSONArray friends 		= jsonObject.getJSONArray("friends");
	String friends_name 	= friends.getString(0);
	JSONObject address 		= jsonObject.getJSONObject("address");
	String country 			= address.getString("country");
	
 *  Save To :
 *  Databases dataTwitterTokenizing
 */

public class Tokenizing {
		
		private static final Logger LOG = LoggerFactory.getLogger(Tokenizing.class);
		
		public static void main(String[] args) throws IOException, ParseException
		{	
			// Connecting MongoDB Database dataTwitter
	        MongoClient mongo 		= new MongoClient("localhost", 27017);
	        DB db					= mongo.getDB("dataTwitter");
	        // Connecting MongoDB Database dataTwitter
	        DB db2					= mongo.getDB("dataTwitterTokenizing");
	        // Get collection list from database
	        Set<String> collections 		= db.getCollectionNames();
	        for (String collectionName : collections) 
	        {
	        	
	        	// collection form dataTwitter
	        	DBCollection collection 	= db.getCollection(collectionName);
		        DBCursor obj 				= collection.find();
		        
		        // create new collection for new tokenizing
	        	DBCollection collection2	= db2.getCollection(collectionName);
		        while (obj.hasNext()) 
				{
					DBObject dbObject 		= obj.next();
					String jsonString 		= JSON.serialize(dbObject);       
		            JSONObject jsonObject 	= new JSONObject(jsonString);

		            try {
		            	String lang 			= (String) jsonObject.get("lang");
		            	// Just Lang Indonesian
		            	if (lang.equals("in")) {

				            String id_str 			= (String) jsonObject.get("id_str");
				           	String text 			= (String) jsonObject.get("text");
				        	String created_at 		= (String) jsonObject.get("created_at");

				           	// get instance user
				        	JSONObject users 		= jsonObject.getJSONObject("user");
				        	String screen_name 		= users.getString("screen_name");
				        	
				           	/*
				           	 * Step 1) filter Account News
				           	 */
				            if(!AccountUtils.SearchScreenName(screen_name))
				            {
				            	// Tokennizing
				                List<String> tokens = Tokenizer.tokenize(text);
				                BasicDBObject doc 	= new BasicDBObject("id_str", id_str).
				                		append("created_at", created_at).
				                        append("screen_name", screen_name).
				                        append("text", text).
				                        append("tokens", tokens);
				                collection2.insert(doc);
				                
					            LOG.info("Collection: "+collectionName+" Id: "+id_str+" Tokenized: " + tokens);
				            }

						}
					} catch (Exception e) {
						continue;
					}				
		         }				
	        }
	        
			mongo.close();
	     }
}

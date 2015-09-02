package main;

/*
 * 	Accses fied data twitter example : 
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

 */

import java.io.IOException;
import java.util.List;
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

public class Tokenizing {
		
		private static final Logger LOG = LoggerFactory.getLogger(Tokenizing.class);
	    
		public static void main(String[] args) throws IOException, ParseException{
    		// Connecting MongoDB and Get Data
	        MongoClient mongo 		= new MongoClient("localhost", 27017);
	        DB db					= mongo.getDB("DocumentDB_DataSourceTwitter");
	        DBCollection collection = db.getCollection("DocumentDB_sample");
	        DBCursor obj 			= collection.find();
			while (obj.hasNext()) {
				DBObject dbObject 		= obj.next();
				String jsonString 		= JSON.serialize(dbObject);       
	            JSONObject jsonObject 	= new JSONObject(jsonString);
	            String id_str 	= (String) jsonObject.get("id_str");
	           	String text 	= (String) jsonObject.get("text");
	        	JSONObject users 	= jsonObject.getJSONObject("user");
	        	String screen_name 		= users.getString("screen_name");
	           	// Step 1) Filter Account News
	            if(!AccountUtils.SearchScreenName(screen_name)){// Tokennizing
	                List<String> tokens = Tokenizer.tokenize(text);
	                DBObject where = new BasicDBObject("id_str", id_str);
		            DBObject update = new BasicDBObject();
		            update.put("$set", new BasicDBObject("tokenizing",tokens));
		            collection.update(where, update);
		            LOG.info(" Screen_name: \""+ screen_name +"\" Tweet: \"" + text + "");
	                LOG.info("Tokenized: " + tokens + "\n");
	                
	            }
	            
			}
			
			mongo.close();
	    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.*;


public class StockScraper {
//Hello world!
	
	void getStock(String stockCode) throws MalformedURLException, IOException {
		System.out.println("Extracting stock: " + stockCode);
		
		String url = "https://query.yahooapis.com/v1/public/yql";
		String charset = "UTF-8";
		String YQL = "select * from yahoo.finance.quotes where symbol in (" + stockCode + ")";


		String query = String.format("q=%s", URLEncoder.encode(YQL, charset));
		
		String additionalArgs = "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
		//	&diagnostics=true removed
		
		url = url + "?" + query + additionalArgs;
		
		System.out.println("URL: " + url);
		
		
		URLConnection connection = new URL(url).openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		InputStream response = connection.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(response));
        String jsonString = "";
        String line;
        while ((line = br.readLine()) != null) {
            jsonString += line;
        }
        br.close();
        
        // Construct a JSONObject from a source JSON text string.
        // A JSONObject is an unordered collection of name/value pairs. Its external 
        // form is a string wrapped in curly braces with colons between the names 
        // and values, and commas between the values and names.
        JSONObject jo = new JSONObject(jsonString);
        System.out.println(jo.toString());
        
        // A JSONArray is an ordered sequence of values. Its external form is a 
        // string wrapped in square brackets with commas between the values.
        JSONArray ja;
        
        // Get the JSONObject value associated with the search result key.
        jo = jo.getJSONObject("query");
        
        System.out.println(jo.toString());
        
        System.out.println(jo.get("count") + " results found!");
        
        jo = jo.getJSONObject("results");
        
        System.out.println(jo.toString());
        
        // Get the JSONArray value associated with the Result key
        ja = jo.getJSONArray("quote");
        
        // Get the number of search results in this set
        int resultCount = ja.length();
        
        // Loop over each result and print the title, summary, and URL
        for (int i = 0; i < resultCount; i++)
        {
        	JSONObject resultObject = ja.getJSONObject(i);
        	System.out.println(resultObject.get("Name"));
        	System.out.println(resultObject.get("Symbol"));
        	System.out.println(resultObject.get("LastTradePriceOnly"));
        	System.out.println("--");
        }
		
//		int data = response.read();
//		while(data != -1) {
//		  //do something with data...
//		  System.out.print((char) data);
//
//		  data = response.read();
//		}
//		response.close();
		
	}
	
	void historical() throws MalformedURLException, IOException {
		   String request = "http://ichart.finance.yahoo.com/table.csv?s=WOW.AX&d=0&e=28&f=2010&g=d&a=3&b=12&c=2009&ignore=.csv"; 
		    // this is the URL we saw at the beginning 

		   String charset = "UTF-8";
			URLConnection connection = new URL(request).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			InputStream response = connection.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(response));
	        String DATA = "";
	        String line;
	        while ((line = br.readLine()) != null) {
	            DATA += line;
	        }
	        br.close();
	        
	        System.out.println(DATA);
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		new StockScraper().getStock("\"WOW.AX\", \"^AORD\"");
		new StockScraper().historical();
	}
}

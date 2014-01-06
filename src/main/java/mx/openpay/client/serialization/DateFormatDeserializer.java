/*
 * Copyright 2013 Opencard Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.client.serialization;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

/**
 * Formats the JSON-serialized dates.
 * @author elopez
 */
public class DateFormatDeserializer implements JsonDeserializer<Date> {

	 public Date deserialize(final JsonElement json, final Type paramType,
	            final JsonDeserializationContext paramJsonDeserializationContext) {
		 
		 String data = json.getAsJsonPrimitive().getAsString();
	        try {
	        	return parse(data);
//	            return DatatypeConverter.parseDateTime(json.getAsJsonPrimitive().getAsString()).getTime();
	        } catch (Exception e) {
	            return null;
	        } 
	    }

	    
	    public static Date parse( String input ) throws java.text.ParseException {

	        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
	        //things a bit.  Before we go on we have to repair this.
	    	SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
	    	if (input.length() > 10) {
	    		df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
	    		
	    		//this is zero time so we need to add that TZ indicator for 
		        if ( input.endsWith( "Z" ) ) {
		            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
		        } else {
		            int inset = 6;
		        
		            String s0 = input.substring( 0, input.length() - inset );
		            String s1 = input.substring( input.length() - inset, input.length() );

		            input = s0 + "GMT" + s1;
		        }
		        
	    	} 
	        
	        return df.parse( input );
	        
	    }
}

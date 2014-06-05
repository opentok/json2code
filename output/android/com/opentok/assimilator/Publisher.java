package com.opentok.assimilator;

import org.json.JSONObject;

public class AssimilatorError {
	
	 protected String errorDomain;
	 protected int errorCode;
	 protected String errorMessage;

	 public AssimilatorError(String errorDomain, int errorCode, String errorMessage) {
		 this.errorDomain = errorDomain;
		 this.errorCode = errorCode;
		 this.errorMessage = errorMessage;
	 }
}

public static String J2CPublisherErrorDomain = "J2CPublisherErrorDomain";

public class J2CPublisher {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
				jsonObj.put("publisherId", this.publisherId)
				jsonObj.put("name", this.name)
				jsonObj.put("stream", this.stream.jsonObj);
		
		return jsonObj;
	}

	public publisher (JSONObject obj) {
		super();
	
		if (!this.validateObj(obj))
		{
			return null;
		}
	
		this.init(obj);
	}

	private void init(JSONObj obj) {
			if(obj.get("publisherId")) {
					this.publisherId = obj.get("publisherId");
			}
			if(obj.get("name")) {
					this.name = obj.get("name");
			}
			if(obj.get("stream")) {
					//TODO this.stream = J2CStream.streamWithDictionary
			}
	}
	

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("J2CPublisher", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		
			    	if (!(obj.get("publisherId").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("publisherId").getClass() + "is not a valid type for publisherId", "publisherId");
			    		return false;
			    	}
			    	
			    	if (!(obj.get("name").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("name").getClass() + "is not a valid type for name", "name");
			    		return false;
			    	}
			    	
			    		if (obj.get("stream") && !J2CStream.validateObj(obj.get("stream"))) {
			    			return false;
			    		}
			    	  
			    	

		return true;
	}

	public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}
}

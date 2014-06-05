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

public static String J2CSubscriberErrorDomain = "J2CSubscriberErrorDomain";

public class J2CSubscriber {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
				jsonObj.put("subscriberId", this.subscriberId)
				jsonObj.put("stream", this.stream.jsonObj);
		
		return jsonObj;
	}

	public subscriber (JSONObject obj) {
		super();
	
		if (!this.validateObj(obj))
		{
			return null;
		}
	
		this.init(obj);
	}

	private void init(JSONObj obj) {
			if(obj.get("subscriberId")) {
					this.subscriberId = obj.get("subscriberId");
			}
			if(obj.get("stream")) {
					//TODO this.stream = J2CStream.streamWithDictionary
			}
	}
	

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("J2CSubscriber", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		
			    	if (!(obj.get("subscriberId").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("subscriberId").getClass() + "is not a valid type for subscriberId", "subscriberId");
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

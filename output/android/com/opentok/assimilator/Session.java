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

public static String J2CSessionErrorDomain = "J2CSessionErrorDomain";

public class J2CSession {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
				jsonObj.put("connection", this.connection.jsonObj);
				jsonObj.put("sessionId", this.sessionId)
				jsonObj.put("apiKey", this.apiKey)
				jsonObj.put("token", this.token)
		
		return jsonObj;
	}

	public session (JSONObject obj) {
		super();
	
		if (!this.validateObj(obj))
		{
			return null;
		}
	
		this.init(obj);
	}

	private void init(JSONObj obj) {
			if(obj.get("connection")) {
					//TODO this.connection = J2CConnection.connectionWithDictionary
			}
			if(obj.get("sessionId")) {
					this.sessionId = obj.get("sessionId");
			}
			if(obj.get("apiKey")) {
					this.apiKey = obj.get("apiKey");
			}
			if(obj.get("token")) {
					this.token = obj.get("token");
			}
	}
	

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("J2CSession", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		
			    		if (obj.get("connection") && !J2CConnection.validateObj(obj.get("connection"))) {
			    			return false;
			    		}
			    	  
			    	
			    	if (!(obj.get("sessionId").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("sessionId").getClass() + "is not a valid type for sessionId", "sessionId");
			    		return false;
			    	}
			    	
			    	if (!(obj.get("apiKey").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("apiKey").getClass() + "is not a valid type for apiKey", "apiKey");
			    		return false;
			    	}
			    	
			    	if (!(obj.get("token").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("token").getClass() + "is not a valid type for token", "token");
			    		return false;
			    	}
			    	

		return true;
	}

	public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}
}

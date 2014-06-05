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

public static String J2CStreamErrorDomain = "J2CStreamErrorDomain";

public class J2CStream {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
				jsonObj.put("connection", this.connection.jsonObj);
				jsonObj.put("streamId", this.streamId)
		
		return jsonObj;
	}

	public stream (JSONObject obj) {
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
			if(obj.get("streamId")) {
					this.streamId = obj.get("streamId");
			}
	}
	

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("J2CStream", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		
			    		if (obj.get("connection") && !J2CConnection.validateObj(obj.get("connection"))) {
			    			return false;
			    		}
			    	  
			    	
			    	if (!(obj.get("streamId").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("streamId").getClass() + "is not a valid type for streamId", "streamId");
			    		return false;
			    	}
			    	

		return true;
	}

	public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}
}

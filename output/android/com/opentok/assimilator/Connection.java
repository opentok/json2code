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

public static String J2CConnectionErrorDomain = "J2CConnectionErrorDomain";

public class J2CConnection {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
				jsonObj.put("connectionId", this.connectionId)
				jsonObj.put("data", this.data)
		
		return jsonObj;
	}

	public connection (JSONObject obj) {
		super();
	
		if (!this.validateObj(obj))
		{
			return null;
		}
	
		this.init(obj);
	}

	private void init(JSONObj obj) {
			if(obj.get("connectionId")) {
					this.connectionId = obj.get("connectionId");
			}
			if(obj.get("data")) {
					this.data = obj.get("data");
			}
	}
	

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("J2CConnection", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		
			    	if (!(obj.get("connectionId").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("connectionId").getClass() + "is not a valid type for connectionId", "connectionId");
			    		return false;
			    	}
			    	
			    	if (!(obj.get("data").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("data").getClass() + "is not a valid type for data", "data");
			    		return false;
			    	}
			    	

		return true;
	}

	public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}
}

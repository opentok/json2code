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

public static String J2CAssimilatorMessageErrorDomain = "J2CAssimilatorMessageErrorDomain";

public class J2CAssimilatorMessage {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
		
		return jsonObj;
	}

	public assimilatorMessage (JSONObject obj) {
		super();
	
		if (!this.validateObj(obj))
		{
			return null;
		}
	
		this.init(obj);
	}

	private void init(JSONObj obj) {
	}
	

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("J2CAssimilatorMessage", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		

		return true;
	}

	public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}
}

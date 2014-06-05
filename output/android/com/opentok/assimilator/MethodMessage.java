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

public static String J2CMethodMessageErrorDomain = "J2CMethodMessageErrorDomain";

public class J2CMethodMessage {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
				jsonObj.put("category", (J2CCategoryHelper.numStringFrom(this.category)))
				jsonObj.put("publisher", this.publisher.jsonObj);
				jsonObj.put("stream", this.stream.jsonObj);
				jsonObj.put("subscriber", this.subscriber.jsonObj);
				jsonObj.put("connection", this.connection.jsonObj);
				jsonObj.put("session", this.session.jsonObj);
				jsonObj.put("method", (J2CMethodHelper.numStringFrom(this.method)))
		
		return jsonObj;
	}

	public methodMessage (JSONObject obj) {
		super();
	
		if (!this.validateObj(obj))
		{
			return null;
		}
	
		this.init(obj);
	}

	private void init(JSONObj obj) {
			if(obj.get("category")) {
					this.category = J2CCategoryHelper.enumValueFor(obj.get("category"));
			}
			if(obj.get("publisher")) {
					//TODO this.publisher = J2CPublisher.publisherWithDictionary
			}
			if(obj.get("stream")) {
					//TODO this.stream = J2CStream.streamWithDictionary
			}
			if(obj.get("subscriber")) {
					//TODO this.subscriber = J2CSubscriber.subscriberWithDictionary
			}
			if(obj.get("connection")) {
					//TODO this.connection = J2CConnection.connectionWithDictionary
			}
			if(obj.get("session")) {
					//TODO this.session = J2CSession.sessionWithDictionary
			}
			if(obj.get("method")) {
					this.method = J2CMethodHelper.enumValueFor(obj.get("method"));
			}
	}
	

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("J2CMethodMessage", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		
				if (obj.isNull("category") || obj.get("category").equals("")) {
					this.error = this.validateError("category is required but not present or is null", "category");
					return false;
				}
			    	if (obj.get("category") && !(J2CCategoryHelper.isValidEnumValue(obj.get("category")))) {
			    		this.error = this.validateError(obj.get("category").getClass() + "is not a valid type for category", "category");
			    		return false;
			    	}
			    	
			    	
			    		if (obj.get("publisher") && !J2CPublisher.validateObj(obj.get("publisher"))) {
			    			return false;
			    		}
			    	  
			    	
			    		if (obj.get("stream") && !J2CStream.validateObj(obj.get("stream"))) {
			    			return false;
			    		}
			    	  
			    	
			    		if (obj.get("subscriber") && !J2CSubscriber.validateObj(obj.get("subscriber"))) {
			    			return false;
			    		}
			    	  
			    	
			    		if (obj.get("connection") && !J2CConnection.validateObj(obj.get("connection"))) {
			    			return false;
			    		}
			    	  
			    	
			    		if (obj.get("session") && !J2CSession.validateObj(obj.get("session"))) {
			    			return false;
			    		}
			    	  
			    	
				if (obj.isNull("method") || obj.get("method").equals("")) {
					this.error = this.validateError("method is required but not present or is null", "method");
					return false;
				}
			    	if (obj.get("method") && !(J2CMethodHelper.isValidEnumValue(obj.get("method")))) {
			    		this.error = this.validateError(obj.get("method").getClass() + "is not a valid type for method", "method");
			    		return false;
			    	}
			    	
			    	

		return true;
	}

	public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}
}

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

public static String J2CMethodErrorDomain = "J2CMethodErrorDomain";

public class J2CMethodHelper {
	
	
	public class EnumValuesSingleton {
		private JSONObject enumValues = null;
		   
		synchronized (this) {
			if ( enumValue == null ) {
					enumValues.put("RESET", (J2CMethodReset);
					enumValues.put("SESSION_INITIALIZE", (J2CMethodSessionInitialize);
					enumValues.put("SESSION_CONNECT", (J2CMethodSessionConnect);
					enumValues.put("SESSION_DISCONNECT", (J2CMethodSessionDisconnect);
					enumValues.put("SESSION_RELEASE", (J2CMethodSessionRelease);
					enumValues.put("PUBLISHER_PUBLISH", (J2CMethodPublisherPublish);
					enumValues.put("PUBLISHER_UNPUBLISH", (J2CMethodPublisherUnpublish);
					enumValues.put("PUBLISHER_INITIALIZE", (J2CMethodPublisherInitialize);
					enumValues.put("PUBLISHER_RELEASE", (J2CMethodPublisherRelease);
					enumValues.put("SUBSCRIBER_SUBSCRIBE", (J2CMethodSubscriberSubscribe);
					enumValues.put("SUBSCRIBER_UNSUBSCRIBE", (J2CMethodSubscriberUnsubscribe);
			}
		}
		return enumValues;
		
	}
	
	public class EnumStringsSingleton {
		private JSONObject enumStrings = null;
		   
		synchronized (this) {
			if ( enumStrings == null ) {
					enumStrings.put(J2CMethodReset, "RESET");
					enumStrings.put(J2CMethodSessionInitialize, "SESSION_INITIALIZE");
					enumStrings.put(J2CMethodSessionConnect, "SESSION_CONNECT");
					enumStrings.put(J2CMethodSessionDisconnect, "SESSION_DISCONNECT");
					enumStrings.put(J2CMethodSessionRelease, "SESSION_RELEASE");
					enumStrings.put(J2CMethodPublisherPublish, "PUBLISHER_PUBLISH");
					enumStrings.put(J2CMethodPublisherUnpublish, "PUBLISHER_UNPUBLISH");
					enumStrings.put(J2CMethodPublisherInitialize, "PUBLISHER_INITIALIZE");
					enumStrings.put(J2CMethodPublisherRelease, "PUBLISHER_RELEASE");
					enumStrings.put(J2CMethodSubscriberSubscribe, "SUBSCRIBER_SUBSCRIBE");
					enumStrings.put(J2CMethodSubscriberUnsubscribe, "SUBSCRIBER_UNSUBSCRIBE");
			}
		}
		return enumStrings;
		
	}
	
	public boolean isValidEnumValue(String value) {
		return (EnumValuesSingleton.get(value) != null)
	}
	
	public int enumValueFor(String value) {
		return (Integer.parseInt(EnumValuesSingleton.get(value)))
	}
	
	public String enumStringFrom(J2CMethod value) {
		return (EnumStringsSingleton.get(String.valueOf(value)));
	}	
	
}

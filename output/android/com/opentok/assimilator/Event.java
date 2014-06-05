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

public static String J2CEventErrorDomain = "J2CEventErrorDomain";

public class J2CEventHelper {
	
	
	public class EnumValuesSingleton {
		private JSONObject enumValues = null;
		   
		synchronized (this) {
			if ( enumValue == null ) {
					enumValues.put("SESSION_INITIALIZED", (J2CEventSessionInitialized);
					enumValues.put("SESSION_CONNECTED", (J2CEventSessionConnected);
					enumValues.put("SESSION_DISCONNECTED", (J2CEventSessionDisconnected);
					enumValues.put("SESSION_DESTROYED", (J2CEventSessionDestroyed);
					enumValues.put("SESSION_ERROR", (J2CEventSessionError);
					enumValues.put("PUBLISHER_INITIALIZED", (J2CEventPublisherInitialized);
					enumValues.put("PUBLISHER_PUBLISHED", (J2CEventPublisherPublished);
					enumValues.put("PUBLISHER_UNPUBLISHED", (J2CEventPublisherUnpublished);
					enumValues.put("PUBLISHER_DESTROYED", (J2CEventPublisherDestroyed);
					enumValues.put("PUBLISHER_ERROR", (J2CEventPublisherError);
					enumValues.put("SUBSCRIBER_SUBSCRIBED", (J2CEventSubscriberSubscribed);
					enumValues.put("SUBSCRIBER_UNSUBSCRIBED", (J2CEventSubscriberUnsubscribed);
					enumValues.put("SUBSCRIBER_INITIALIZED", (J2CEventSubscriberInitialized);
					enumValues.put("SUBSCRIBER_ERROR", (J2CEventSubscriberError);
					enumValues.put("CONNECTION_CREATED", (J2CEventConnectionCreated);
					enumValues.put("CONNECTION_DESTROYED", (J2CEventConnectionDestroyed);
					enumValues.put("STREAM_CREATED", (J2CEventStreamCreated);
					enumValues.put("STREAM_DESTROYED", (J2CEventStreamDestroyed);
			}
		}
		return enumValues;
		
	}
	
	public class EnumStringsSingleton {
		private JSONObject enumStrings = null;
		   
		synchronized (this) {
			if ( enumStrings == null ) {
					enumStrings.put(J2CEventSessionInitialized, "SESSION_INITIALIZED");
					enumStrings.put(J2CEventSessionConnected, "SESSION_CONNECTED");
					enumStrings.put(J2CEventSessionDisconnected, "SESSION_DISCONNECTED");
					enumStrings.put(J2CEventSessionDestroyed, "SESSION_DESTROYED");
					enumStrings.put(J2CEventSessionError, "SESSION_ERROR");
					enumStrings.put(J2CEventPublisherInitialized, "PUBLISHER_INITIALIZED");
					enumStrings.put(J2CEventPublisherPublished, "PUBLISHER_PUBLISHED");
					enumStrings.put(J2CEventPublisherUnpublished, "PUBLISHER_UNPUBLISHED");
					enumStrings.put(J2CEventPublisherDestroyed, "PUBLISHER_DESTROYED");
					enumStrings.put(J2CEventPublisherError, "PUBLISHER_ERROR");
					enumStrings.put(J2CEventSubscriberSubscribed, "SUBSCRIBER_SUBSCRIBED");
					enumStrings.put(J2CEventSubscriberUnsubscribed, "SUBSCRIBER_UNSUBSCRIBED");
					enumStrings.put(J2CEventSubscriberInitialized, "SUBSCRIBER_INITIALIZED");
					enumStrings.put(J2CEventSubscriberError, "SUBSCRIBER_ERROR");
					enumStrings.put(J2CEventConnectionCreated, "CONNECTION_CREATED");
					enumStrings.put(J2CEventConnectionDestroyed, "CONNECTION_DESTROYED");
					enumStrings.put(J2CEventStreamCreated, "STREAM_CREATED");
					enumStrings.put(J2CEventStreamDestroyed, "STREAM_DESTROYED");
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
	
	public String enumStringFrom(J2CEvent value) {
		return (EnumStringsSingleton.get(String.valueOf(value)));
	}	
	
}

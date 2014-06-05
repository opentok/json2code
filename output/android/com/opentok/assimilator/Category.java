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

public static String J2CCategoryErrorDomain = "J2CCategoryErrorDomain";

public class J2CCategoryHelper {
	
	
	public class EnumValuesSingleton {
		private JSONObject enumValues = null;
		   
		synchronized (this) {
			if ( enumValue == null ) {
					enumValues.put("RESET", (J2CCategoryReset);
					enumValues.put("SESSION", (J2CCategorySession);
					enumValues.put("PUBLISHER", (J2CCategoryPublisher);
					enumValues.put("SUBSCRIBER", (J2CCategorySubscriber);
					enumValues.put("CONNECTION", (J2CCategoryConnection);
					enumValues.put("STREAM", (J2CCategoryStream);
			}
		}
		return enumValues;
		
	}
	
	public class EnumStringsSingleton {
		private JSONObject enumStrings = null;
		   
		synchronized (this) {
			if ( enumStrings == null ) {
					enumStrings.put(J2CCategoryReset, "RESET");
					enumStrings.put(J2CCategorySession, "SESSION");
					enumStrings.put(J2CCategoryPublisher, "PUBLISHER");
					enumStrings.put(J2CCategorySubscriber, "SUBSCRIBER");
					enumStrings.put(J2CCategoryConnection, "CONNECTION");
					enumStrings.put(J2CCategoryStream, "STREAM");
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
	
	public String enumStringFrom(J2CCategory value) {
		return (EnumStringsSingleton.get(String.valueOf(value)));
	}	
	
}

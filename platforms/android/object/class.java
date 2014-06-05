package {{ package }};

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

public static String {{ prefix }}{{ name | classize }}ErrorDomain = "{{ prefix }}{{ name }}ErrorDomain";

{% if kind == 'enum' %}
public class {{ prefix }}{{ name | classize }}Helper {
	
	
	public class EnumValuesSingleton {
		private JSONObject enumValues = null;
		   
		synchronized (this) {
			if ( enumValue == null ) {
				{% for enum_val in enum %}
					enumValues.put("{{ enum_val }}", ({{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }});
				{% endfor %}
			}
		}
		return enumValues;
		
	}
	
	public class EnumStringsSingleton {
		private JSONObject enumStrings = null;
		   
		synchronized (this) {
			if ( enumStrings == null ) {
				{% for enum_val in enum %}
					enumStrings.put({{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }}, "{{ enum_val }}");
				{% endfor %}
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
	
	public String enumStringFrom({{ prefix }}{{ name | classize }} value) {
		return (EnumStringsSingleton.get(String.valueOf(value)));
	}	
	
}
{% else %}
public class {{ prefix }}{{ name | classize }} {
		
	AssimilatorError error;
		
	public class ObjectSingleton {
		private JSONObject jsonObj = null;
		   
		{% for property_name, property in properties.iteritems() %}
			{% if property.enum %}
				jsonObj.put("{{ property_name }}", (({{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}StringFrom)(this.{ property_name }})))
			{% elif property.type == "string" or property.type == "number" %}
				jsonObj.put("{{ property_name }}", this.{{ property_name }})
			{% elif allClasses[property.type].kind == 'enum' %}
				jsonObj.put("{{ property_name }}", ({{ prefix }}{{ property.type | classize }}Helper.numStringFrom(this.{{ property_name }})))
			{% else %}
				jsonObj.put("{{ property_name }}", this.{{ property_name }}.jsonObj);
			{% endif %}
		{% endfor %}
		
		return jsonObj;
	}

	public {{ name | instantize }} (JSONObject obj) {
		super();
	
		if (!this.validateObj(obj))
		{
			return null;
		}
	
		this.init(obj);
	}

	private void init(JSONObj obj) {
		{% for property_name, property in properties.iteritems() %}
			if(obj.get("{{ property_name }}")) {
				{% if property.enum %}
					this.{{ property_name }} = {{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}ValueFor (obj.get("{{ property_name }}"));
				{% elif property.type == "string" or property.type == "number" %}
					this.{{ property_name }} = obj.get("{{ property_name }}");
				{% elif allClasses[property.type].kind == 'enum' %}
					this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}Helper.enumValueFor(obj.get("{{ property_name }}"));
				{% else %}
					//TODO this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}.{{ property.type | instantize }}WithDictionary
				{% endif %}
			}
		{% endfor %}
	}
	
	{% for property_name, property in properties.iteritems() %}
	{% if property.enum %}
		
		public class EnumValuesFor{{ property_name | classize }}Singleton {
			private JSONObject enumValuesFor{{ property_name | classize }};
			
			synchronized (this) {
				
				if ( enumValuesFor{{ property_name | classize }} == null ) {
					{% for enum_val in property.enum %}
						enumValuesFor{{ property_name | classize }}.put("{{ enum_val }}", ({{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}));
						{% endfor %}
				}
			}
			return enumValuesFor{{ property_name | classize }};
		}
			
		public class EnumStringsFor{{ property_name | classize }}Singleton {
			private JSONObject enumStringsFor{{ property_name | classize }};
				
			synchronized (this) {
					
				if ( enumStringsFor{{ property_name | classize }} == null ) {
					{% for enum_val in property.enum %}
						enumStringsFor{{ property_name | classize }}.put(({{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}),"{{ enum_val }}");
			        {% endfor %}
				
				}
			}
			return enumStringsFor{{ property_name | classize }};
		}
		
		private boolean isValid{{ property_name | classize }}EnumValue(String value) {
			return (EnumValuesFor{{ property_name | classize }}Singleton.get(value) != null );
		}
	
		private int enum{{ property_name | classize }}ValueFor(String value) {
			return EnumValuesFor{{ property_name | classize }}Singleton.get(value)).getInt();
		}

		private String enum{{ property_name | classize }}StringFrom({{ prefix }}{{ name | classize }}{{ property_name | classize }} value) {
			return (EnumStringsFor{{ property_name | classize }}Singleton.get(value).toString());
		}

	{% endif %}
	{% endfor %}

	private AssimilatorError validateError (String msg, String property) {
		retur new AssimilatorError("{{ prefix }}{{ name | classize }}", -2, "Property " + property +"is not valid." + msg);
	}
	
	public boolean validateObj(JSONObject obj) {
		
		{% for property_name, property in properties.iteritems() %}
			{% if property_name in required %}
				if (obj.isNull("{{ property_name }}") || obj.get("{{ property_name }}").equals("")) {
					this.error = this.validateError("{{ property_name }} is required but not present or is null", "{{ property_name }}");
					return false;
				}
			{% endif %}
			{% if property.enum %}
			  	if (obj.get("{{ property_name }}") && !({{ prefix }}{{ name }}.isValid{{property_name | classize}}EnumValue(obj.get("{{property_name}}")))) {
			  		this.error = this.validateError(obj.get("{{ property_name }}" + "is not a valid value for {{ property_name }}", "{{ property_name }}");	
			  		return false;
			  	}
			  	{% elif property.type == "string" %}
			    	if (!(obj.get("{{ property_name }}").getClass().isInstance(String.class))) {
			    		this.error = this.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");
			    		return false;
			    	}
			    {% elif property.type == "number" %}
			    	
			    	if (!(obj.get("{{ property_name }}").getClass().isInstance(Integer.class))) {
			    		this.error = this.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");	
			    		return false;
			    	}
			    {% elif allClasses[property.type].kind == 'enum' %}
			    	if (obj.get("{{ property_name }}") && !({{ prefix }}{{ property.type | classize }}Helper.isValidEnumValue(obj.get("{{property_name}}")))) {
			    		this.error = this.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");
			    		return false;
			    	}
			    	
			    	{% else %}
			    		if (obj.get("{{ property_name }}") && !{{ prefix }}{{ property.type | classize }}.validateObj(obj.get("{{ property_name }}"))) {
			    			return false;
			    		}
			    	  
			  {% endif %}
			    	
		{% endfor %}

		return true;
	}

	public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}
}
{% endif %}
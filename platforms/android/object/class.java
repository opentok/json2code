package {{ package }};

import org.json.JSONObject;
import com.opentok.util.AssimilatorError;

{% if kind == 'enum' %}
public class {{ prefix }}{{ name | classize }}Helper {
	
	public enum {{ prefix }}{{ name | classize }} {
	{% set count = 0 %}
	{% for enum_val in enum  %}
		{% if count < loop.length - 1  %}
			{{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }}({{ count }}),	
		{% else %}
			{{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }}({{ count }});
		{% endif %}
		{% set count = count + 1 %}
	{% endfor %}
		
		private int value;
		
		private {{ prefix }}{{ name | classize }}(int value) {
			this.value = value;
		}
		private int getValue(){
			return value;
		}
	};
	
	public static class EnumValuesSingleton {
		private static JSONObject enumValues = null;
		
		public static synchronized JSONObject getInstance() {
				if ( enumValues == null ) {
				{% for enum_val in enum %}
					enumValues.put("{{ enum_val }}", {{ prefix }}{{ name | classize }}.{{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }});
				{% endfor %}
				}
				return enumValues;
			}	
	}
	
	public static class EnumStringsSingleton {
		private static JSONObject enumStrings = null;
		
		public static synchronized JSONObject getInstance() {   
				if ( enumStrings == null ) {
				{% for enum_val in enum %}
					enumStrings.put(({{ prefix }}{{ name | classize }}.{{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }}).toString(), "{{ enum_val }}");
				{% endfor %}
				return enumStrings;
				}
		}
	}
	
	public boolean isValidEnumValue(String value) {
		return ((EnumValuesSingleton.getInstance()).get(value) != null);
	}
	
	public int enumValueFor(String value) {
		return (Integer.parseInt((EnumValuesSingleton.getInstance()).get(value).toString()));
	}
	
	public String enumStringFrom({{ prefix }}{{ name | classize }} value) {
		return ((EnumStringsSingleton.getInstance()).get(value.toString()).toString());
	}	
	
}
{% else %}
{% for property_name, property in properties.iteritems() %}
{% if not(property.enum) and property.type != "string" and property.type != "number" %}
import {{package}}.{{ prefix }}{{ property_name | classize }}.java;
{% endif %}
{% endfor %}	

public class {{ prefix }}{{ name | classize }} {
	
	public static String {{ prefix }}{{ name | classize }}ErrorDomain = "{{ prefix }}{{ name }}ErrorDomain";

	public AssimilatorError error;
		
	public JSONObject serialize() {
		JSONObject jsonObj = null;
		   
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

	public {{ prefix }}{{ name | classize }}(JSONObject obj) {
		
		if (!this.validateObj(obj))
		{
			return;
		}
	
		this.init(obj);
	}

	public static {{ prefix }}{{ name | classize }} new{{ prefix }}{{ name | classize }}(JSONObject obj) {
		{{ prefix }}{{ name | classize }} instance = new {{ prefix }}{{ name | classize }}(obj);
		
		return instance;
	}
	
	private void init(JSONObject obj) {
		{% for property_name, property in properties.iteritems() %}
			if(obj.get("{{ property_name }}") != null) {
				{% if property.enum %}
					this.{{ property_name }} = {{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}ValueFor (obj.get("{{ property_name }}"));
				{% elif property.type == "string" or property.type == "number" %}
					this.{{ property_name }} = obj.get("{{ property_name }}");
				{% elif allClasses[property.type].kind == 'enum' %}
					this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}Helper.enumValueFor(obj.get("{{ property_name }}"));
				{% else %}
				 	this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}.new{{ prefix }}{{ property.type | classize }}(obj);
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
		return new AssimilatorError("{{ prefix }}{{ name | classize }}", -2, "Property " + property +"is not valid." + msg);
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

	/*public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}*/
	
}
{% endif %}
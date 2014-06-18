package {{ package }};

{% if kind == 'enum' %}
public class {{ prefix }}{{ name | classize }} {
	
	{% set count = 0 %}
	{% for enum_val in enum  %}
			private static final {{ prefix }}{{ name | classize }} {{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }} = new {{ prefix }}{{ name | classize }}({{ count }});	
		{% set count = count + 1 %}
	{% endfor %}
	
	int value;
	
	public {{ prefix }}{{ name | classize }}(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static boolean isValidEnumValue(String value) {
		boolean valid = false;
		switch(value) {
			{% for enum_val in enum  %}
			case  "{{ enum_val }}": 
				if ({{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }} != null) {
					valid = true;
				}
				break;
			{% endfor %}
		}
		return valid;
	}
	
	public static {{ prefix }}{{ name | classize }} enumValueFor(String value) {
		{{ prefix }}{{ name | classize }} enumValue = null;
		switch(value) {
			{% for enum_val in enum  %}
			case  "{{ enum_val }}": 
				enumValue = {{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }};
				break;
			{% endfor %}
		}
		return enumValue;
	}
	
	public static String enumStringFrom({{ prefix }}{{ name | classize }} obj) {
		String enumValue = null; 
		{% for enum_val in enum  %}
		if (obj.getValue() == {{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }}.getValue()) {
			enumValue = "{{ enum_val }}";
		}
		{% endfor %}
		return enumValue;
	}
}

{% else %}

import org.json.JSONObject;
import org.json.JSONException;
import com.opentok.util.AssimilatorError;

{% for property_name, property in properties.iteritems() %}
{% if not(property.enum) and property.type != "string" and property.type != "number" %}
import {{package}}.{{ prefix }}{{ property_name | classize }};
{% endif %}
{% endfor %}

public class {{ prefix }}{{ name | classize }} {
	
	
	public static String {{ prefix }}{{ name | classize }}ErrorDomain = "{{ prefix }}{{ name }}ErrorDomain";

	public AssimilatorError assimilatorError = null;

	{% for property_name, property in properties.iteritems() %}
	{% if property.enum %}
	public {{ prefix }}{{ name }}{{ property_name|capitalize }} {{ property_name }};
	{% elif property.type == "string" %}
	public String {{ property_name }};
	{% elif property.type == "number" %}
	public int {{ property_name }};
	{% elif allClasses[property.type].kind == 'oneOf' %}
	public {{ prefix }}{{ property.type|capitalize }} {{ property_name }};
	{% elif allClasses[property.type].kind == 'enum' %}
	public {{ prefix }}{{ property.type|capitalize }} {{ property_name }};
	{% else %}
	{{ prefix }}{{ property.type|capitalize }} {{ property_name }};
	{% endif %}
	{% endfor %}

	
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
	
	public JSONObject serialize() {
		JSONObject jsonObj = new JSONObject();;
		try {  
		{% for property_name, property in properties.iteritems() %}
			{% if property.enum %}
				jsonObj.put("{{ property_name }}", (({{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}StringFrom)(this.{ property_name }})));
			{% elif property.type == "string" or property.type == "number" %}
				jsonObj.put("{{ property_name }}", this.{{ property_name }});
			{% elif allClasses[property.type].kind == 'enum' %}
				jsonObj.put("{{ property_name }}", ({{ prefix }}{{ property.type | classize }}.enumStringFrom(this.{{ property_name }})));
			{% else %}
				jsonObj.put("{{ property_name }}", this.{{ property_name }});
			{% endif %}
		{% endfor %}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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
		try {  
		{% for property_name, property in properties.iteritems() %}
			if(obj.get("{{ property_name }}") != null) {
				{% if property.enum %}
					this.{{ property_name }} = {{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}ValueFor((String)obj.get("{{ property_name }}"));
				{% elif property.type == "string" or property.type == "number" %}
					this.{{ property_name }} = (String) obj.get("{{ property_name }}");
				{% elif allClasses[property.type].kind == 'enum' %}
					this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}.enumValueFor((String)obj.get("{{ property_name }}"));
				{% else %}
				 	this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}.new{{ prefix }}{{ property.type | classize }}(obj);
				{% endif %}
			}
		{% endfor %}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

	private AssimilatorError validateError (String msg, String property) {
		return new AssimilatorError("{{ prefix }}{{ name | classize }}", -2, "Property " + property +"is not valid." + msg);
	}
	
	public static boolean validateObj(JSONObject obj) {
		
		{{ prefix }}{{ name | classize }} instance = new {{ prefix }}{{ name | classize }}(obj); //accessing static variable by creating an instance of class

		try {  
		{% for property_name, property in properties.iteritems() %}
		{% if property_name in required %}
		if (obj.isNull("{{ property_name }}") || obj.get("{{ property_name }}").equals("")) {
			instance.assimilatorError = instance.validateError("{{ property_name }} is required but not present or is null", "{{ property_name }}");
			return false;
		}
		{% endif %}
		{% if property.enum %}
			if (obj.get("{{ property_name }}") != null && !({{ prefix }}{{ name }}.isValid{{property_name | classize}}EnumValue((String)obj.get("{{property_name}}")))) {
				instance.error = instance.validateError(obj.get("{{ property_name }}" + "is not a valid value for {{ property_name }}", "{{ property_name }}");	
			  	return false;
			 }
		{% elif property.type == "string" %}
			if (!(obj.get("{{ property_name }}").getClass().isInstance(String.class))) {
				instance.assimilatorError = instance.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");
			    return false;
			 }
		{% elif property.type == "number" %}
		   	if (!(obj.get("{{ property_name }}").getClass().isInstance(Integer.class))) {
		   		instance.assimilatorError = instance.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");	
			    return false;
			}
		{% elif allClasses[property.type].kind == 'enum' %}
			if (obj.get("{{ property_name }}") != null && !({{ prefix }}{{ property.type | classize }}.isValidEnumValue((String)obj.get("{{property_name}}")))) {
				instance.assimilatorError = instance.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");
			    return false;
			}
		{% else %}
			if (obj.get("{{ property_name }}") != null && !{{ prefix }}{{ property.type | classize }}.validateObj((JSONObject)obj.get("{{ property_name }}"))) {
				return false;
			 }
		{% endif %}
		{% endfor %}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	/*public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}*/
	
}
{% endif %}
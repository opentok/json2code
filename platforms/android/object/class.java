package {{ package }};

import java.util.HashMap;
{% if kind == 'enum' %}

public class {{ prefix }}{{ name | classize }} {

{% set count = 0 %}
{% for enum_val in enum  %}
	public static final {{ prefix }}{{ name | classize }} {{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }} = new {{ prefix }}{{ name | classize }}({{ count }});
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
		{% for enum_val in enum  %}

		if (value.equals("{{ enum_val }}") ) {
				if ({{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }} != null) {
					valid = true;
				}
		}
		{% endfor %}
		return valid;
	}

	public static {{ prefix }}{{ name | classize }} enumValueFor(String value) {
		{{ prefix }}{{ name | classize }} enumValue = null;
		{% for enum_val in enum  %}
		if (value.equals("{{ enum_val }}") ) {

			enumValue = {{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }};
		}
		{% endfor %}
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


	public static class Helper {

		public static HashMap<String, {{ prefix }}{{ name | classize }}> enumValues() {
			HashMap<String, {{ prefix }}{{ name | classize }}> {{ prefix }}{{ name | classize }}TypeObj = new HashMap<String, {{ prefix }}{{ name | classize }}>();

			{% for enum_val in enum %}
			{{ prefix }}{{ name | classize }}TypeObj.put("{{ enum_val }}", {{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }});
			{% endfor %}

			return {{ prefix }}{{ name | classize }}TypeObj;
		}

		public static HashMap<{{ prefix }}{{ name | classize }}, String> enumStrings() {
			HashMap<{{ prefix }}{{ name | classize }}, String> {{ prefix }}{{ name | classize }}TypeObj = new HashMap<{{ prefix }}{{ name | classize }}, String>();

			{% for enum_val in enum %}
				{{ prefix }}{{ name | classize }}TypeObj.put({{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }}, "{{ enum_val }}");
			{% endfor %}

			return {{ prefix }}{{ name | classize }}TypeObj;
		}

		public static boolean isValidEnumValue(String value) {
			boolean valid = false;

			valid = (enumValues().get(value) != null);

			return valid;
		}

		public static int enumValueFor(String value) {
			int valueInt = -1;

			valueInt = enumValues().get(value).getValue();

			return valueInt;
		}

		public static String enumStringFrom({{ prefix }}{{ name | classize }} value) {
			String valueStr = null;

			valueStr =  enumStrings().get(value);

			return valueStr;
		}
	}
}


{% else %}
import org.json.JSONObject;
import org.json.JSONException;
import com.opentok.util.AssimilatorError;

{% for property_name, property in properties %}
{% if not(property.enum) and property.type != "string" and property.type != "number" %}
import {{package}}.{{ prefix }}{{ property_name | classize }};
{% endif %}
{% endfor %}

public class {{ prefix }}{{ name | classize }} {

	public static String {{ prefix }}{{ name | classize }}ErrorDomain = "{{ prefix }}{{ name }}ErrorDomain";

	public AssimilatorError assimilatorError = null;
	{% for property_name, property in properties %}
	{% if property.enum %}

	public static enum {{ prefix }}{{ name | classize }}{{ property_name | classize }} {
	{% set count = 0 %}
	{% for enum_val in property.enum  %}

		{{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}({{ count }}){% if count == (property.enum | length) - 1 %};{% else %},{% endif %}
	{% set count = count + 1 %}
	{% endfor %}

		int value;

		private {{ prefix }}{{ name | classize }}{{ property_name | classize }}(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public static {{ prefix }}{{ name | classize }}{{ property_name | classize }} fromValue (int value) {
			for ({{ prefix }}{{ name | classize }}{{ property_name | classize }} code : {{ prefix }}{{ name | classize }}{{ property_name | classize }}.values()) {
				if (code.getValue() == value) {
					return code;
				}
			}
			return null;
		}

	}

	public {{ prefix }}{{ name }}{{ property_name|classize }} {{ property_name }};
	{% elif property.type == "string" %}
	public String {{ property_name }};
	{% elif property.type == "number" %}
	public double {{ property_name }};
	{% elif allClasses[property.type].kind == 'oneOf' %}
	public {{ prefix }}{{ property.type|classize }} {{ property_name }};
	{% elif allClasses[property.type].kind == 'enum' %}
	public {{ prefix }}{{ property.type|classize }} {{ property_name }};
	{% else %}
	public {{ prefix }}{{ property.type|classize }} {{ property_name }};
	{% endif %}
	{% endfor %}
	{% for property_name, property in properties %}
	{% if property.enum %}

	private static enum EnumValuesFor{{ property_name | classize }} {
		INSTANCE;

		private HashMap<String,{{ prefix }}{{ name | classize }}{{ property_name | classize }}>  enumValuesFor{{ property_name | classize }};
		public HashMap<String,{{ prefix }}{{ name | classize }}{{ property_name | classize }}> getObject() {
			synchronized (this) {
			if ( enumValuesFor{{ property_name | classize }} == null ) {
				enumValuesFor{{ property_name | classize }} = new HashMap<String,{{ prefix }}{{ name | classize }}{{ property_name | classize }}>();
			{% for enum_val in property.enum %}
				enumValuesFor{{ property_name | classize }}.put("{{ enum_val }}", ({{ prefix }}{{ name }}{{ property_name|classize }}.{{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}));
			{% endfor %}
			}
		}
			return enumValuesFor{{ property_name | classize }};
		}

	}

	private static enum EnumStringsFor{{ property_name | classize }} {
		INSTANCE;
		private HashMap<{{ prefix }}{{ name | classize }}{{ property_name | classize }}, String>  enumStringsFor{{ property_name | classize }};

		public HashMap<{{ prefix }}{{ name | classize }}{{ property_name | classize }}, String> getObject() {
			synchronized (this) {
				if ( enumStringsFor{{ property_name | classize }} == null ) {
					enumStringsFor{{ property_name | classize }} = new HashMap<{{ prefix }}{{ name | classize }}{{ property_name | classize }}, String>();
				{% for enum_val in property.enum %}
					enumStringsFor{{ property_name | classize }}.put(({{ prefix }}{{ name }}{{ property_name|classize }}.{{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}),"{{ enum_val }}");
		    {% endfor %}
				}
			}
			return enumStringsFor{{ property_name | classize }};
		}
	}

	private static boolean isValid{{ property_name | classize }}EnumValue(String value) {
		return EnumValuesFor{{ property_name | classize }}.INSTANCE.getObject().containsKey(value);
	}

	private static int enum{{ property_name | classize }}ValueFor(String value) {
		return EnumValuesFor{{ property_name | classize }}.INSTANCE.getObject().get(value).getValue();
	}

	private static String enum{{ property_name | classize }}StringFrom({{ prefix }}{{ name | classize }}{{ property_name | classize }} value) {
		return (String)(EnumStringsFor{{ property_name | classize }}.INSTANCE.getObject().get(value).toString());
	}

	{% endif %}
	{% endfor %}

{% if kind == 'oneOf' %}
{% for implementation_name in possibles %}
	public {{ prefix }}{{ implementation_name | classize }} {{ implementation_name | instantize }}Msg = null;
{% endfor %}

	public {{ prefix }}{{ name | classize }}(JSONObject obj) {
	{% for implementation_name in possibles %}
		if({{ prefix }}{{ implementation_name | classize }}.validateObj(obj)) {
			{{ implementation_name | instantize }}Msg = {{ prefix }}{{ implementation_name | classize }}.new{{ prefix }}{{ implementation_name | classize }}(obj);
			return;
		}

		{% endfor %}
		assimilatorError = new AssimilatorError("{{ prefix }}{{ name | classize }}", -1, "Could not create {{ prefix }}{{ name | classize }} from dictionary given");
	}

	public boolean validateObj (JSONObject obj){
		{% for implementation_name in possibles %}
		if({{ prefix }}{{ implementation_name | classize }}.validateObj(obj)) {
			return true;
		}

		{% endfor %}
		assimilatorError = new AssimilatorError("{{ prefix }}{{ name | classize }}", -1, "Could not create {{ prefix }}{{ name | classize }} from dictionary given");
		return false;
	}
{% else %}

	public {{ prefix }}{{ name | classize }}() {}

	public {{ prefix }}{{ name | classize }}(JSONObject obj) {

		if (!this.validateObj(obj))
		{
			return;
		}

		this.init(obj);
	}

	public JSONObject serialize() {
		JSONObject jsonObj = new JSONObject();;
		try {
		{% for property_name, property in properties %}
			{% if property.enum %}
				jsonObj.put("{{ property_name }}", (({{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}StringFrom({{ property_name }}))));
			{% elif property.type == "string" or property.type == "number" %}
				jsonObj.put("{{ property_name }}", this.{{ property_name }});
			{% elif allClasses[property.type].kind == 'enum' %}
				jsonObj.put("{{ property_name }}", ({{ prefix }}{{ property.type | classize }}.enumStringFrom({{ property_name }})));
			{% else %}
				jsonObj.put("{{ property_name }}", this.{{ property_name }});
			{% endif %}
		{% endfor %}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

	public static {{ prefix }}{{ name | classize }} new{{ prefix }}{{ name | classize }}(JSONObject obj) {
		{{ prefix }}{{ name | classize }} instance = new {{ prefix }}{{ name | classize }}(obj);

		return instance;
	}

	private void init(JSONObject obj) {
		try {
		{% for property_name, property in properties %}
			if(!obj.isNull("{{ property_name }}")) {
				{% if property.enum %}
					this.{{ property_name }} = {{ prefix }}{{ name | classize }}{{ property_name | classize }}.fromValue({{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}ValueFor((String)obj.get("{{ property_name }}")));
				{% elif property.type == "string" %}
					this.{{ property_name }} = (String) obj.get("{{ property_name }}");
				{% elif property.type == "number" %}
					this.{{ property_name }} = (Double) obj.getDouble("{{ property_name }}");
				{% elif allClasses[property.type].kind == 'enum' %}
					//this.{{ property_name }} = new {{ prefix }}{{ name | classize }}{{ property_name | classize }}({{ prefix }}{{ property.type | classize }}.enumValueFor((String)obj.get("{{ property_name }}")));
					this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}.enumValueFor((String)obj.get("{{ property_name }}"));
				{% else %}
				 	this.{{ property_name }} = {{ prefix }}{{ property.type | classize }}.new{{ prefix }}{{ property.type | classize }}((JSONObject)obj.get("{{ property_name }}"));
				{% endif %}
			}
		{% endfor %}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	private static AssimilatorError validateError (String msg, String property) {
		return new AssimilatorError("{{ prefix }}{{ name | classize }}", -2, "Property " + property +"is not valid." + msg);
	}

	public static boolean validateObj(JSONObject obj) {

		//{{ prefix }}{{ name | classize }} instance = new {{ prefix }}{{ name | classize }}(obj); //accessing static variable by creating an instance of class
		AssimilatorError assimilatorError = null;
		try {
		{% for property_name, property in properties %}
		{% if required.includes(property_name) %}
		if (obj.isNull("{{ property_name }}") || obj.get("{{ property_name }}").equals("")) {
			assimilatorError = {{ prefix }}{{ name | classize }}.validateError("{{ property_name }} is required but not present or is null", "{{ property_name }}");
			return false;
		}
		{% endif %}
		{% if property.enum %}
			if (obj.has("{{ property_name }}") && !({{ prefix }}{{ name }}.isValid{{property_name | classize}}EnumValue((String)obj.get("{{property_name}}")))) {
				assimilatorError = {{ prefix }}{{ name | classize }}.validateError(obj.get("{{ property_name }}") + "is not a valid value for {{ property_name }}", "{{ property_name }}");
			  	return false;
			 }
		{% elif property.type == "string" %}

			if (obj.has("{{ property_name }}") && !(String.class.isInstance(obj.get("{{ property_name }}")))) {
				assimilatorError = {{ prefix }}{{ name | classize }}.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");
			    return false;
			 }
		{% elif property.type == "number" %}
		   	if (obj.has("{{ property_name }}") && !(obj.get("{{ property_name }}") instanceof Number)) {
		   		assimilatorError = {{ prefix }}{{ name | classize }}.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");
			    return false;
			}
		{% elif allClasses[property.type].kind == 'enum' %}
			if (obj.has("{{ property_name }}") && !({{ prefix }}{{ property.type | classize }}.isValidEnumValue((String)obj.get("{{property_name}}")))) {
				assimilatorError = {{ prefix }}{{ name | classize }}.validateError(obj.get("{{ property_name }}").getClass() + "is not a valid type for {{ property_name }}", "{{ property_name }}");
			    return false;
			}
		{% else %}
			if (obj.has("{{ property_name }}")  && !{{ prefix }}{{ property.type | classize }}.validateObj((JSONObject)obj.get("{{ property_name }}"))) {
				return false;
			 }
		{% endif %}
		{% endfor %}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	public JSONObject getObj() {
		JSONObject jsonObj = new JSONObject();
		try {

			{% for property_name, property in properties %}
			{% if property.enum %}
			if (this.{{property_name}} != null)
				jsonObj.put("{{ property_name }}", {{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}StringFrom(this.{{ property_name }}));
			{% elif property.type == "string" %}
			if (this.{{property_name}} != null)
				jsonObj.put("{{ property_name }}", this.{{ property_name }});
			{% elif property.type == "number" %}
			if (this.{{property_name}} != 0)
				jsonObj.put("{{ property_name }}", this.{{ property_name }});
			{% elif allClasses[property.type].kind == 'enum' %}
			if (this.{{property_name}} != null)
				jsonObj.put("{{ property_name }}", {{ package }}.{{ prefix }}{{ property.type | classize }}.Helper.enumStringFrom(this.{{ property_name }}));
			{% else %}
			if (this.{{property_name}} != null)
				jsonObj.put("{{ property_name }}", this.{{ property_name }}.getObj());
			{% endif %}
			{% endfor %}

		}catch(JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	/*public byte[] getDataFromJSONOptions (JSONOptions opts) {
		//TODO
	}*/

{% endif %}
}
{% endif %}

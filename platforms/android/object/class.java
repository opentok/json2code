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

{% if kind == 'enum' %}
public class {{ prefix }}{{ name | classize }} {
	
	AssimilatorError error;
	
	public JSONObject jsonObj = new JSONObject(){{
		{% for property_name, property in properties.iteritems() %}
			{% if property.enum %}
				putString("{{ property_name }}", (({{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}StringFrom)(this.{ property_name }})))
			{% elif property.type == "string" or property.type == "number" %}
				putString("{{ property_name }}", this.{{ property_name }})
			{% elif allClasses[property.type].kind == 'enum' %}
				putString("{{ property_name }}", ({{ prefix }}{{ property.type | classize }}Helper.numStringFrom(this.{{ property_name }})))
			{% else %}
				putString("{{ property_name }}", this.{{ property_name }}.jsonObj);
			{% endif %}
		{% endfor %}
	}};	
	
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
					this.{{ property_name }} = {{ prefix }}{{ name | classize }}.enum{{ property_name | classize }}ValueFor (obj.get("{{ property_name }}"));
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
		public JSONObject enumValuesFor{{ property_name | classize }} {{
			
			synchronized (this) {
				{% for enum_val in property.enum %}
					putString("{{ enum_val }}", ({{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}));
		        	@"{{ enum_val }}": @({{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}),
		        {% endfor %}
			}
		}};
	
		private JSONObject enumStringsFor{{ property_name | classize }} {
			synchronized (this) {
				{% for enum_val in property.enum %}
					putString(({{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}),"{{ enum_val }}");
				{% endfor %}
			}
		}};
		
		private boolean isValid{{ property_name | classize }}EnumValue(String value) {
			return (this.enumValuesFor{{ property_name | classize }}(value) != null );
		}
	
		private int enum{{ property_name | classize }}ValueFor(String value) {
			return (this.enumValuesFor{{ property_name | classize }}(value)).getInt();
		}

		private String enum{{ property_name | classize }}StringFrom({{ prefix }}{{ name | classize }}{{ property_name | classize }} value) {
			return (this.enumStringsFor{{ property_name | classize }}(value).toString());
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

public class {{ prefix }}{{ name | classize }}Helper {

	public boolean isValidEnumValue(String value) {
		return (this.enumValues.get(value) != null)
	}
	
	public int enumValueFor(String value) {
		return (Integer.parseInt(this.enumValues.get(value)))
	}
	
	public String enumStringFrom({{ prefix }}{{ name | classize }} value) {
		return (this.enumStrings.get(String.valueOf(value)));
	}
	
	public JSONObject enumStrings = new JSONObject() {{
		synchronized (this) {
			{% for enum_val in enum %}
				putString({{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }}, "{{ enum_val }}");
			{% endfor %}
		}
	}};
	
	public JSONObject enumValues = new JSONObject() {{
		synchronized (this) {
			{% for enum_val in enum %}
			 	putString("{{ enum_val }}", ({{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }});
			{% endfor %}
		}
	}};
}

{% endif %}	
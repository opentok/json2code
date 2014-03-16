package {{ package }};

import org.json.JSONObject;

class {{ name|capitalize }} {
{% for name, property in properties.iteritems() %}
{% if property.type == "string" %}
	private String {{ name }};
	public String get{{ name|capitalize }}() {
		return this.{{ name }};
	}
	public void set{{ name|capitalize }}(String value) {
		this.{{ name }} = value;
	}
{% elif property.type == "number" %}
	private int {{ name }};
	public int get{{ name|capitalize }}() {
		return this.{{ name }};
	}
	public void set{{ name|capitalize }}(int value) {
		this.{{ name }} = value;
	}
{% endif %}

{% endfor %}

	public void deserialize(String json) {
		JSONObject obj = new JSONObject(json);

{% for name, property in properties.iteritems() %}
{% if property.type == "string" %}
		this.{{ name }} = obj.getString("{{ name }}");
{% elif property.type == "number" %}
		this.{{ name }} = obj.getInt("{{ name }}");
{% endif %}
{% endfor %}
	}

	public String serialize() {

	}
}
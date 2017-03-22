'use strict';

{% if kind == 'enum' %}
const {{ name | classize }} = {
  values: {
  {% for enum_val in enum  %}
    {{ enum_val }}: '{{ enum_val }}',
  {% endfor %}
  },
  isValidEnumValue(value) {
    return this.values.hasOwnProperty(value);
  },
};

module.exports = {{ name | classize }};

{% else %}
{% for property_name, property in properties.iteritems() %}
{% if not(property.enum) and property.type != "string" and property.type != "number" %}
const {{ property_name | classize }} = require('./{{ property_name | classize }}');
{% endif %}
{% endfor %}
{% for implementation_name in possibles %}
const {{ implementation_name | classize }} = require('./{{ implementation_name | classize }}');
{% endfor %}

{% if kind == 'oneOf' %}
const {{ name | classize }} = function(dict) {
  {% for implementation_name in possibles %}
  if (!{{ implementation_name | classize }}.validate(dict)) {
    return new {{ implementation_name | classize }}(dict);
  }
  {% endfor %}

  throw new Error('Could not create {{ name | classize }} from object given. The object does not conform to any of the valid oneOf types');
};

{{ name | classize }}.validate = function(dict) {
  {% for implementation_name in possibles %}
  if (!{{ implementation_name | classize }}.validate(dict)) {
    return undefined;
  }
  {% endfor %}

  return new Error('Could not create {{ name | classize }} from object given. The object does not conform to any of the valid oneOf types');
};

{% else %}
class {{ name | classize }} {
  static validate(dict, shallow) {
  {% for property_name, property in properties.iteritems() %}
  {% if property_name in required %}
    if (dict.{{property_name}} == null) {
      return new Error(`{{name}} is not valid: {{property_name}} is required but not present or is null`);
    }

  {% endif %}
  {% if property.enum %}
    if (dict.{{property_name}} != null &&
        !{{ name | classize }}.{{ property_name | classize }}.isValidEnumValue(dict.{{property_name}})) {
      return new Error(`{{name}} is not valid: ${dict.{{property_name}}} is not a valid value for {{property_name}}`);
    }

  {% elif property.type == "string" or property.type == "number" %}
    if (dict.{{property_name}} != null &&
      typeof dict.{{property_name}} != '{{ property.type }}') {
      return new Error(`{{name}} is not valid: ${typeof dict.{{property_name}}} is not a valid value for {{property_name}}, must be a {{ property.type }}.`);
    }

  {% elif allClasses[property.type].kind == 'enum' %}
    if (dict.{{property_name}} != null &&
      !{{ property.type | classize }}.isValidEnumValue(dict.{{property_name}})) {
      return new Error(`{{name}} is not valid: ${dict.{{property_name}}} is not a valid value for {{property_name}}, must be a {{ property.type }}`);
    }

  {% else %}
    if (dict.{{property_name}} != null && !shallow) {
      const error = {{ property.type | classize }}.validate(dict.{{property_name}});
      if (error) {
        return error;
      }
    }

  {% endif %}
  {% endfor %}
  {% if not additional_properties %}
    const additionalKeys = Object.keys(dict)
      .filter(key => {{ name | classize }}.Properties.indexOf(key) < 0);
    if (additionalKeys.length > 0) {
      return new Error(`{{name}} is not valid: it contains${additionalKeys.length > 1 ? '' : ' an'} additional key${additionalKeys.length > 1 ? 's' : ''}: ${additionalKeys.join(', ')}.`);
    }

  {% endif %}
    return undefined;
  }

  constructor(dict) {
    const validationError = {{ name | classize }}.validate(dict, true);
    if (validationError) {
      throw validationError;
    }
  {% for property_name, property in properties.iteritems() %}

    if (dict.{{property_name}} != null) {
  {% if property.enum %}
      this.{{ property_name }} = dict.{{property_name}};
  {% elif property.type == "string" or property.type == "number" %}
      this.{{ property_name }} = dict.{{property_name}};
  {% elif allClasses[property.type].kind == 'enum' %}
      this.{{ property_name }} = dict.{{property_name}};
  {% else %}
      this.{{ property_name }} = new {{ property.type | classize }}(dict.{{property_name}});
  {% endif %}
    }
  {% endfor %}
  }

  {% for property_name, property in properties.iteritems() %}
  // get {{ property_name}}() {} returns {{ property.type | classize }}
  {% endfor %}
}
{% endif %}
{% for property_name, property in properties.iteritems() %}
{% if property.enum %}

{{ name | classize }}.{{ property_name | classize }} = {
  values: {
  {% for enum_val in property.enum %}
    {{ enum_val }}: '{{ enum_val }}',
  {% endfor %}
  },
  isValidEnumValue(value) {
    return this.values.hasOwnProperty(value);
  },
};
{% endif %}
{% endfor %}

{{ name | classize }}.Properties = [
{% for property_name, property in properties.iteritems() %}
  '{{ property_name }}',
{% endfor %}
];

module.exports = {{ name | classize }};
{% endif %}

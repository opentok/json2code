#if !defined {{ name }}_H
#define {{ name }}_H

#include <string>
#include "SimpleJSON/JSON.h"
{% for property_name, property in properties.iteritems() %}
  {% if property.type != "string" and property.type != "number" %}
#include "{{ property.type | classize }}.h"
   {% endif %}
{% endfor %}
{% if kind == 'oneOf' %}
{% for option_type in possibles %}
#include "{{ option_type | classize }}.h"
{% endfor %}
{% endif %}


namespace quokka_tok {
  {% if kind == 'enum' %}
  enum e{{ name | classize }}  {
    {% for enum_val in enum %}
    {{name | classize }}_{{ enum_val | camelize | classize }},
    {% endfor %}
  };
  static const std::wstring& {{ name | classize }}_toString(e{{name | classize }} val) {
    static const std::wstring tbl[{{ enum | length }}] = {
      {% for enum_val in enum %}
      L"{{ enum_val }}",
      {% endfor %}
    };
    return tbl[val];
  }
  static e{{ name | classize }} {{ name | classize }}_toEnum(const std::wstring& rVal) {
    std::map<const wchar_t *, e{{ name | classize }}> tbl;
    {% for enum_val in enum %}
    tbl[L"{{ enum_val }}"] = {{name | classize }}_{{ enum_val | camelize | classize }};
    {% endfor %}
    return tbl[rVal.c_str()];
  }
  {% else %}
  class {{ name | classize }} {
  public:
    const std::string m_{{ prefix }}{{ name | classize }}ErrorDomain;
    {% for property_name, property in properties.iteritems() %}
    {% if property.enum %}
    enum e{{ property_name | classize }}  {
    {% for enum_val in property.enum %}
    {{property_name | classize }}_{{ enum_val | camelize | classize }},
    {% endfor %}
    };
    {% endif %}
    {% endfor %}
    {{ name | classize }}() : m_{{ prefix }}{{ name | classize }}ErrorDomain("{{ prefix }}{{ name }}ErrorDomain") {
      {% for property_name, property in properties.iteritems() %}
      {% if property.type != "string" and property.type != "number" and not property.enum %}
      {% if allClasses[property.type].kind == 'enum' %}
      m_{{ property_name }} =  {{property.type | classize }}_{{ allClasses[property.type].enum[0] | camelize | classize }};
      {% else %}
        m_p{{ property_name | classize }} = NULL;
      {% endif %}
      {% endif %}
      {% endfor %}
      {% if kind == 'oneOf' %}
      {% for optional_property_name in possibles %}
        m_p{{ optional_property_name | classize }} = NULL;
      {% endfor %}
      {% endif %}
    }

    {{ name | classize }}(const JSONObject& rJson) : m_{{ prefix }}{{ name | classize }}ErrorDomain("{{ prefix }}{{ name }}ErrorDomain") {
       {% for property_name, property in properties.iteritems() %}
       if (rJson.end() != rJson.find(L"{{ property_name }}")) {
          JSONValue * pField = const_cast<JSONObject&>(rJson)[L"{{ property_name }}"];
          {% if property.enum %}
          m_{{property_name}} =  {{ property_name | classize }}_toEnum(pField->AsString());
	  {% elif property.type == "string" %}
	  m_{{property_name}} = std::string(pField->AsString().begin(), pField->AsString().end());
          {% elif property.type == "number" %}
	  m_{{property_name}} = pField->AsNumber();
	  {% elif allClasses[property.type].kind == 'enum' %}
	  m_{{ property_name }} =  {{ property.type | classize }}_toEnum(pField->AsString());
	  {% else %}
	  m_p{{ property_name | classize }} = new {{property_name | classize}}(pField->AsObject());
       {% endif %}
       }
       {% endfor %}
       {% if kind == 'oneOf' %}
       /* This class is a union type, as such it requires a custom implementation to 
	* unmarshall, thus it should be used as a base class and extended. */
       {% endif %}
    }
    
    
    virtual ~{{ name | classize }}() {
    {% for property_name, property in properties.iteritems() %}
    {% if property.type != "string" and property.type != "number" and not property.enum and allClasses[property.type].kind != 'enum'%}
        delete m_p{{ property_name | classize }};
    {% endif %}
    {% endfor %}
    {% if kind == 'oneOf' %}
    {% for option_type in possibles %}
      delete m_p{{ option_type | classize }};
    {% endfor %}
    {% endif %}
    
    }

      {% for property_name, property in properties.iteritems() %}
      {% if property.enum %}
    const e{{ property_name | classize }} query{{ property_name | classize }}() const {
      return m_{{property_name}};
      {% elif property.type == "string" %}
    const std::string& query{{ property_name | classize }}() const {
      return m_{{property_name}};
      {% elif property.type == "number" %}
    const double query{{ property_name | classize }}() const {
      return m_{{property_name}};
      {% elif allClasses[property.type].kind == 'enum' %}
    const e{{ property.type | classize }} query{{ property_name | classize }}() const {
      return m_{{ property_name }};      
      {% else %}
    const {{ property.type | classize }} * query{{ property_name | classize }}() const {
      return m_p{{property_name | classize }};
      {% endif %}
    }

    

      {% if property.enum %}
    void set{{ property_name | classize }}(const e{{ property_name | classize }} val) {
      m_{{ property_name }} = val;    
      {% elif property.type == "string" %}
    void set{{ property_name | classize }}(const std::string& rVal) {
      m_{{ property_name }} = rVal;
      {% elif property.type == "number" %}
    void set{{ property_name | classize }}(const double val) {
      m_{{ property_name }} = val;
      {% elif allClasses[property.type].kind == 'enum' %}
    void set{{ property_name | classize }}(e{{property.type | classize}} val) {
      m_{{ property_name }} = val;      
      {% else %}
    void set{{ property_name | classize }}({{property.type | classize }} & rVal) {
      m_p{{ property_name | classize }} = &rVal;
      {% endif %}
    }
    
      {% endfor %}

      {% if kind == 'oneOf' %}
      {% for optional_property_name in possibles %}
   void set{{ optional_property_name | classize }}({{ optional_property_name | classize }} & rVal) {
      m_p{{ optional_property_name | classize }} = &rVal;
   }

   const {{ optional_property_name | classize }} * query{{ optional_property_name | classize }}() const {
      return m_p{{optional_property_name | classize }};
   }
      
      {% endfor %}
      {% endif %}

    
    virtual JSONValue * marshall() const {
      JSONObject obj;
      {% for property_name, property in properties.iteritems() %}
      {% if property.enum %}
      obj[L"{{ property_name }}"] = new JSONValue({{ property_name | classize }}_toString(m_{{property_name}}));
      {% elif property.type == "string" %}
      if (m_{{property_name}}.length()) {
         obj[L"{{ property_name }}"] = new JSONValue(std::wstring(m_{{property_name}}.begin(), m_{{property_name}}.end()));
      }
      {% elif property.type == "number" %}
      obj[L"{{ property_name }}"] = new JSONValue(m_{{property_name}});
      {% elif allClasses[property.type].kind == 'enum' %}
      obj[L"{{ property_name }}"] = new JSONValue({{ property.type | classize }}_toString(m_{{property_name }}));
      {% else %}
      if (m_p{{property_name | classize }}) {
         obj[L"{{ property_name }}"] = m_p{{property_name | classize }}->marshall();
      }
      {% endif %}
      {% endfor %}
       {% if kind == 'oneOf' %}
       /* This class is a union type, as such it requires a custom implementation to 
        * unmarshall, thus it should be used as a base class and extended. */
       {% endif %}      
      return new JSONValue(obj);
    }

    protected:
    {% for property_name, property in properties.iteritems() %}
    {% if property.enum %}
      e{{ property_name | classize }} m_{{ property_name }};
    {% elif property.type == "string" %}
      std::string m_{{ property_name }};
    {% elif property.type == "number" %}
      double m_{{ property_name }};
    {% elif allClasses[property.type].kind == 'enum' %}
      e{{ property.type | classize }} m_{{ property_name }};
    {% else %}
      {{ property.type | classize }} * m_p{{ property_name | classize }};
    {% endif %}
    {% endfor %}
    {% if kind == 'oneOf' %}
    {% for option_type in possibles %}
      {{ option_type | classize }} * m_p{{ option_type | classize }};
    {% endfor %}
    {% endif %}
    {% for property_name, property in properties.iteritems() %}
    {% if property.enum %}
    
    static const std::wstring& {{ property_name | classize }}_toString(e{{property_name | classize }} val) {
       static const std::wstring tbl[{{ property.enum | length }}] = {
       {% for enum_val in property.enum %}
          L"{{ enum_val }}",
       {% endfor %}
       };
       return tbl[val];
    }
    static e{{ property_name | classize }} {{ property_name | classize }}_toEnum(const std::wstring& rVal) {
      std::map<std::wstring, e{{ property_name | classize }}> tbl;
       {% for enum_val in enum %}
       tbl[L"{{ enum_val }}"] = {{property_name | classize }}_{{ enum_val | camelize | classize }};
       {% endfor %}
       return tbl[rVal];
    }    
    {% endif %}
    {% endfor %}    
  };
  {% endif %}
}
#endif // #if {{ name }}_H


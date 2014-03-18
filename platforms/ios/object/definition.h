//
//  J2CEntity.h
//  json2code
//
//  Created by json2code on 3/14/14.
//

#import <Foundation/Foundation.h>

{% for name, property in properties.iteritems() %}
{% if property.type != "string" and property.type != "number" %}
#import "{{ prefix }}{{ name|capitalize }}.h"
{% endif %}
{% endfor %}

@interface {{ prefix }}{{ name|capitalize }} : NSObject

{% for name, property in properties.iteritems() %}
{% if property.enum %}
typedef NS_ENUM(NSInteger, {{ name|capitalize }}Type) {
{% for enum_val in property.enum %}
  {{ enum_val }},
{% endfor %}
};
@property {{ name|capitalize }}Type {{ name }};
{% elif property.type == "string" %}
@property NSString* {{ name }};
{% elif property.type == "number" %}
@property NSNumber* {{ name }};
{% else %}
@property {{ prefix }}{{ property.type|capitalize }}* {{ name }};
{% endif %}

{% endfor %}

- (id)initWithDictionary:(NSDictionary*)dict;
- (void)deserialize:(NSData*)data;
- (NSData*)serialize;

@end

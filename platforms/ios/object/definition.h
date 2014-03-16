//
//  J2CEntity.h
//  json2code
//
//  Created by json2code on 3/14/14.
//

#import <Foundation/Foundation.h>

@interface {{ prefix }}{{ name|capitalize }} : NSObject

{% for name, property in properties.iteritems() %}
{% if property.type == "string" and property.enum %}
typedef NS_ENUM(NSInteger, {{ propertyname|capitalize }}Type) {
{% for enum_val in property.enum %}
  {{ enum_val }},
{% endfor %}
};
@property {{ propertyname|capitalize }}Type {{ name }};
{% elif property.type == "string" %}
@property NSString* {{ name }};
{% elif property.type == "number" %}
@property NSNumber* {{ name }};
{% endif %}
{% endfor %}

- (void)deserialize:(NSData*)data;
- (NSData*)serialize;

@end

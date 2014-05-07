//
//  J2CEntity.h
//  json2code
//
//  Created by json2code on 3/14/14.
//

#import <Foundation/Foundation.h>
{% for property_name, property in properties.iteritems() %}
{% if not(property.enum) and property.type != "string" and property.type != "number" %}
#import "{{ prefix }}{{ property_name | classize }}.h"
{% endif %}
{% endfor %}
{% for implementation_name in possibles %}
#import "{{ prefix }}{{ implementation_name | classize }}.h"
{% endfor %}

extern NSString *{{ prefix }}{{ name | classize }}ErrorDomain;

@interface {{ prefix }}{{ name | classize }} : NSObject

+ (id){{ name | instantize }}WithData:(NSData*)data error:(NSError**)err;
+ (id){{ name | instantize }}WithDictionary:(NSDictionary *)dict error:(NSError **)error;
+ (BOOL)validateDictionary:(NSDictionary*)dict error:(NSError **)err;

{% if not(oneOf) %}
- (NSDictionary*)dictionary;
- (NSData*)dataWithJSONOptions:(NSJSONWritingOptions)opts error:(NSError**)error;
{% endif %}

{% if oneOf %}
{% else %}
{% for property_name, property in properties.iteritems() %}
{% if property.enum %}
typedef NS_ENUM(NSInteger, {{ prefix }}{{ name | classize }}{{ property_name | classize }}) {
{% for enum_val in property.enum %}
  {{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }},
{% endfor %}
};
@property {{ prefix }}{{ name }}{{ property_name|capitalize }} {{ property_name }};
{% elif property.type == "string" %}
@property NSString* {{ property_name }};
{% elif property.type == "number" %}
@property NSNumber* {{ property_name }};
{% elif allClasses[property.type].oneOf %}
@property id {{ property_name }};
{% else %}
@property {{ prefix }}{{ property.type|capitalize }}* {{ property_name }};
{% endif %}

{% endfor %}
{% endif %}

@end

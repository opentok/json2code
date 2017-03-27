//
//  {{ prefix }}{{ name | classize }}.h - {{ kind }}
//
//  Created by json2code on (replace with date).
//

#import <Foundation/Foundation.h>

{% if kind == 'enum' %}
typedef NS_ENUM(NSInteger, {{ prefix }}{{ name | classize }}) {
{% for enum_val in enum %}
  {{ prefix }}{{ name | classize }}{{ enum_val | camelize | classize }},
{% endfor %}
};

@interface {{ prefix }}{{ name | classize }}Helper : NSObject

+ (NSDictionary *)enumValues;
+ (NSDictionary *)enumStrings;
+ (BOOL)isValidEnumValue:(NSString*)value;
+ (NSInteger)enumValueFor:(NSString*)value;
+ (NSString*)enumStringFrom:({{ prefix }}{{ name | classize }})value;

@end
{% else %}
{% for property_name, property in properties %}
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
{% if kind != "oneOf" %}

- (NSDictionary*)dictionary;
- (NSData*)dataWithJSONOptions:(NSJSONWritingOptions)opts error:(NSError**)error;

{% for property_name, property in properties %}
{% if property.enum %}
typedef NS_ENUM(NSInteger, {{ prefix }}{{ name | classize }}{{ property_name | classize }}) {
{% for enum_val in property.enum %}
  {{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }},
{% endfor %}
};
@property {{ prefix }}{{ name }}{{ property_name|classize }} {{ property_name }};
{% elif property.type == "string" %}
@property (copy, nonatomic) NSString* {{ property_name }};
{% elif property.type == "number" %}
@property (copy, nonatomic) NSNumber* {{ property_name }};
{% elif allClasses[property.type].kind == 'oneOf' %}
@property (retain, nonatomic) id {{ property_name }};
{% elif allClasses[property.type].kind == 'enum' %}
@property {{ prefix }}{{ property.type|classize }} {{ property_name }};
{% else %}
@property (retain, nonatomic) {{ prefix }}{{ property.type|classize }}* {{ property_name }};
{% endif %}
{% endfor %}

{% endif %}
@end
{% endif %}

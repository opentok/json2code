//
//  J2CEntity.m
//  json2code
//
//  Created by json2code on 3/14/14.
//

#define NIL_TO_NULL(a) (a ? a : [NSNull null])

#import "{{ prefix }}{{ name | classize }}.h"

NSString *{{ prefix }}{{ name | classize }}ErrorDomain = @"{{ prefix }}{{ name }}ErrorDomain";

@implementation {{ prefix }}{{ name | classize }}

+ {{ name | instantize }}WithData:(NSData*)data error:(NSError **)err {
  NSError *error = nil;

  id object = [NSJSONSerialization
               JSONObjectWithData:data
               options:0
               error:&error];

  if(!object) {
    *err = error;
    return nil;
  }

  if(![object isKindOfClass:[NSDictionary class]]) {
    *err = [NSError errorWithDomain:{{ prefix }}{{ name | classize }}ErrorDomain
                                code:-1
                            userInfo:@{
                                      NSLocalizedDescriptionKey: @"Could not create {{ prefix }}{{ name }} from data given",
                                      NSLocalizedFailureReasonErrorKey: @"The JSON root element is not an object"
                                     }];
    return nil;
  }

  {{ prefix }}{{ name | classize }} *deserialized =
    [{{ prefix }}{{ name | classize }} {{ name | instantize }}WithDictionary:object error:&error];

  if(!deserialized) {
    *err = error;
  }

  return deserialized;
}

{% if oneOf %}

+ (id) {{ name | instantize }}WithDictionary:(NSDictionary *)dict error:(NSError **)error {

  {% for implementation_name in possibles %}
  if([{{ prefix }}{{ implementation_name | classize }} validateDictionary:dict error:nil]) {
    return [{{ prefix }}{{ implementation_name | classize }} {{ implementation_name | instantize }}WithDictionary:dict error:nil];
  }

  {% endfor %}

  *error = [NSError errorWithDomain:{{ prefix }}{{ name | classize }}ErrorDomain
                                code:-1
                            userInfo:@{
                                      NSLocalizedDescriptionKey: @"Could not create {{ prefix }}{{ name | classize }} from dictionary given",
                                      NSLocalizedFailureReasonErrorKey: @"The object does not conform to any of the valid oneOf types"
                                     }];

  return nil;
}

+ (BOOL)validateDictionary:(NSDictionary*)dict error:(NSError **)err {
  {% for implementation_name in possibles %}
  if([{{ prefix }}{{ implementation_name | classize }} validateDictionary:dict error:nil]) {
    return YES;
  }
  {% endfor %}
  *err = [NSError errorWithDomain:{{ prefix }}{{ name | classize }}ErrorDomain
                             code:-1
                         userInfo:@{
                                    NSLocalizedDescriptionKey: @"Could not create {{ prefix }}{{ name | classize }} from dictionary given",
                                    NSLocalizedFailureReasonErrorKey: @"The object does not conform to any of the valid oneOf types"
                                  }];
  return NO;
}

{% else %}
+ (id) {{ name | instantize }}WithDictionary:(NSDictionary *)dict error:(NSError **)error {
  NSError *err;
  if(![self validateDictionary:dict error:&err]) {
    *error = err;
    return nil;
  }
  return [[{{ prefix }}{{ name | classize }} alloc] initWithDictionary:dict];
}

{% for property_name, property in properties.iteritems() %}
{% if property.enum %}
+ (NSDictionary *)enumValuesFor{{ property_name | classize }} {
  static NSDictionary *{{ prefix }}{{ name | classize }}{{ property_name | classize }}TypeDictionary;
   static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    {{ prefix }}{{ name | classize }}{{ property_name | classize }}TypeDictionary = @{
      {% for enum_val in property.enum %}
        @"{{ enum_val }}": @({{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}),
      {% endfor %}
    };
  });
  return {{ prefix }}{{ name | classize }}{{ property_name | classize }}TypeDictionary;
}

+ (NSDictionary *)enumStringsFor{{ property_name | classize }} {
  static NSDictionary *{{ prefix }}{{ name | classize }}{{ property_name | classize }}TypeDictionary;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    {{ prefix }}{{ name | classize }}{{ property_name | classize }}TypeDictionary = @{
      {% for enum_val in property.enum %}
        @({{ prefix }}{{ name | classize }}{{ property_name | classize }}{{ enum_val | camelize | classize }}): @"{{ enum_val }}",
      {% endfor %}
    };
  });
  return {{ prefix }}{{ name | classize }}{{ property_name | classize }}TypeDictionary;
}

+(BOOL)isValid{{ property_name | classize }}EnumValue:(NSString*)value {
  return [self enumValuesFor{{ property_name | classize }}][value] != nil;
}

+(NSInteger)enum{{ property_name | classize }}ValueFor:(NSString*)value {
  return [[self enumValuesFor{{ property_name | classize }}][value] integerValue];
}

+(NSString*)enum{{ property_name | classize }}StringFrom:({{ prefix }}{{ name | classize }}{{ property_name | classize }})value {
  return [self enumStringsFor{{ property_name | classize }}][@(value)];
}

{% endif %}
{% endfor %}
+ (NSError*)validateError:(NSString*)message withProperty:(NSString*)property {
  return [NSError errorWithDomain:@"{{ prefix }}{{ name | classize }}"
                  code:-2
                  userInfo:@{
                    NSLocalizedDescriptionKey: [NSString stringWithFormat:@"Property %@ is not valid", message],
                    NSLocalizedFailureReasonErrorKey: message
                  }];
}

+ (BOOL)validateDictionary:(NSDictionary*)dict error:(NSError **)err {

{% for property_name, property in properties.iteritems() %}
{% if property_name in required %}
  if (dict[@"{{ property_name }}"] == nil) {
    *err = [self validateError:@"{{ property_name }} is required but not present or is null"
                  withProperty:@"{{ property_name }}"];
    return NO;
  }
{% endif %}
{% if property.enum %}
  if (dict[@"{{ property_name }}"] &&
      ![{{ prefix }}{{ name }} isValid{{property_name | classize}}EnumValue:dict[@"{{property_name}}"]]) {
    *err = [self validateError:[NSString stringWithFormat:@"%@ is not a valid value for {{ property_name }}", dict[@"{{ property_name }}"]]
                  withProperty:@"{{ property_name }}"];
    return NO;
  }
{% elif property.type == "string" %}
  if(![dict[@"{{ property_name }}"] isKindOfClass:[NSString class]]) {
    *err = [self validateError:[NSString stringWithFormat:@"%@ is not a valid type for {{ property_name }}", NSStringFromClass([dict[@"{{ property_name }}"] class])]
                  withProperty:@"{{ property_name }}"];
    return NO;
  }
{% elif property.type == "number" %}
  if(![dict[@"{{ property_name }}" isKindOfClass:[NSNumber class]]) {
    *err = [self validateError:[NSString stringWithFormat:@"%@ is not a valid type for {{ property_name }}", NSStringFromClass([dict[@"{{ property_name }}"] class])]
                  withProperty:@"{{ property_name }}"];
    return NO;
  }
{% else %}
  if(dict[@"{{ property_name }}"] &&
    ![{{ prefix }}{{ property.type | classize }} validateDictionary:dict[@"{{ property_name }}"] error:err]) {
    return NO;
  }
{% endif %}
{% endfor %}

  return YES;
}

-(id)initWithDictionary:(NSDictionary *)dict {
  self = [super init];
  if (self != nil) {
{% for property_name, property in properties.iteritems() %}
{% if property.enum %}
    self.{{ property_name }} = [{{ prefix }}{{ name | classize }} enum{{ property_name | classize }}ValueFor:dict[@"{{ property_name }}"]];
{% elif property.type == "string" or property.type == "number" %}
    self.{{ property_name }} = dict[@"{{ property_name }}"];
{% else %}
    if(dict[@"{{ property_name }}"]) {
      self.{{ property_name }} = [{{ prefix }}{{ property.type | classize }} {{ property.type | instantize }}WithDictionary:dict[@"{{ property_name }}"] error:nil];
    }
{% endif %}
{% endfor %}
  }
  return self;
}

- (NSDictionary*)dictionary {
  return @{
{% for property_name, property in properties.iteritems() %}
{% if property.enum %}
    @"{{ property_name }}": NIL_TO_NULL([{{ prefix }}{{ name |classize }} enum{{ property_name | classize }}StringFrom:self.{{ property_name }}]),
{% elif property.type == "string" or property.type == "number" %}
    @"{{ property_name }}": NIL_TO_NULL(self.{{ property_name }}),
{% else %}
    @"{{ property_name }}": NIL_TO_NULL([self.{{ property_name }} dictionary]),
{% endif %}
{% endfor %}
  };
}

- (NSData*)dataWithJSONOptions:(NSJSONWritingOptions)opts error:(NSError**)error {
  return [NSJSONSerialization dataWithJSONObject:[self dictionary]
                                         options:opts
                                           error:error];
}

{% endif %}
@end

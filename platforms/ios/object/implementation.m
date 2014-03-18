//
//  J2CEntity.m
//  json2code
//
//  Created by json2code on 3/14/14.
//

#import "{{ prefix }}{{ name|capitalize }}.h"


{% for name, property in properties.iteritems() %}
{% if property.enum %}
NSDictionary* {{ name }}TypeDictionary = nil;
{% endif %}
{% endfor %}

@implementation {{ prefix }}{{ name|capitalize }}

+ (void)initialize {
  {% for name, property in properties.iteritems() %}
  {% if property.enum %}
  NSArray* {{ name }}KeyArray = [NSArray arrayWithObjects:
  {% for enum_val in property.enum %}
    @"{{ enum_val }}",
  {% endfor %}
  nil];
  NSArray* {{ name }}ObjectArray = [NSArray arrayWithObjects:
  {% for enum_val in property.enum %}
    [NSNumber numberWithInt:{{ enum_val }}],
  {% endfor %}
  nil];
  {{ name }}TypeDictionary = [NSDictionary dictionaryWithObjects:{{ name }}ObjectArray
                                                         forKeys:{{ name }}KeyArray];

  {% endif %}
  {% endfor %}
}

-(id)initWithDictionary:(NSDictionary *)dict {
  self = [super init];
  if (self != nil) {
{% for name, property in properties.iteritems() %}
{% if name in required %}
    if ([dict valueForKey:@"{{ name }}"] == nil) { return nil; }
{% endif %}
{% if property.enum %}
    self.{{ name }} = [[{{ name }}TypeDictionary objectForKey:[dict valueForKey:@"{{ name }}"]] intValue];
{% elif property.type == "string" or property.type == "number" %}
    self.{{ name }} = [dict valueForKey:@"{{ name }}"];
{% else %}
    self.{{ name }} = [dict valueForKey:@"{{ name }}"] ? [[{{ prefix }}{{ property.type|capitalize }} alloc] initWithDictionary:[dict valueForKey:@"{{ name }}"]] : nil;
{% endif %}
{% endfor %}
  }
  return self;
}

- (void)deserialize:(NSData*)data {
  NSError *error = nil;

  id object = [NSJSONSerialization
               JSONObjectWithData:data
               options:0
               error:&error];

  if([object isKindOfClass:[NSDictionary class]])
  {
    NSDictionary* results = object;
    [self initWithDictionary:results];
  }
}

- (NSData*)serialize {
  return nil;
}

@end

//
//  J2CEntity.m
//  json2code
//
//  Created by json2code on 3/14/14.
//

#import "{{ prefix }}{{ name|capitalize }}.h"

NSDictionary *{{ name }}TypeDictionary = [NSDictionary dictionaryWithObjects:objectArray
                                                  forKeys:keyArray];

@implementation {{ prefix }}{{ name|capitalize }}

- (void)deserialize:(NSData*)data {
  NSError *error = nil;

  id object = [NSJSONSerialization
               JSONObjectWithData:data
               options:0
               error:&error];

  if([object isKindOfClass:[NSDictionary class]])
  {
    NSDictionary* results = object;

{% for name, property in properties.iteritems() %}
{% if property.type == "string" and property.enum %}
    self.{{ name }} = [{{ name }}TypeDictionary objectForKey:[results valueForKey:@"{{ name }}"]]
{% else %}
    self.{{ name }} = [results valueForKey:@"{{ name }}"];
{% endif %}
{% endfor %}
  }
}

- (NSData*)serialize {
  return nil;
}

@end

#!/usr/bin/env python

import os
import sys
import json
import jinja2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader("platforms"),
    extensions=['jinja2.ext.autoescape'],
    trim_blocks=True,
    autoescape=True)

schema_path = sys.argv[1]
prefix = sys.argv[2]
package = sys.argv[3]

schema = json.load(open(schema_path))

def generate(template, name, definition):
	for name, property in definition.get('properties', []):
		if property.get("$ref"):
			property['type'] = 'object'

	result = template.render({
		'prefix': prefix,
		'package': package,
		'name': name,
		'properties': definition.get('properties', [])
	})

	folder = 'output/' + platform
	if platform == 'android':
		folder = folder  + '/' + '/'.join(package.split('.'))

	if not os.path.exists(folder):
		os.makedirs(folder)

	_, ext = os.path.splitext(template_file)
	with open(folder + '/' + (prefix if platform == 'ios' else '') + name[:1].upper() + name[1:] + ext, 'w') as file:
		file.write(result)


for platform in ['ios', 'android']:
	for template_file in os.listdir('platforms/' + platform + '/object'):
		# ['object/definition.h', 'object/implementation.m']:
		template = JINJA_ENVIRONMENT.get_template(platform + '/object/' + template_file)

		generate(template, schema.get('title').split()[0], schema)
		for name, definition in schema.get('definitions', {}).iteritems():
			if definition['type'] == 'object':
				generate(template, name, definition)

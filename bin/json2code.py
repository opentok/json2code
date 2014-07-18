#!/usr/bin/env python

import os
import sys
import json
import jinja2

def camelize(value):
  return "".join(x.capitalize() if x else '_' for x in value.split("_"))

def classize(value):
	return value[:1].upper() + value[1:]

def instantize(value):
	return value[:1].lower() + value[1:]

JINJA_ENVIRONMENT = jinja2.Environment(
  loader=jinja2.FileSystemLoader("platforms"),
  extensions=['jinja2.ext.autoescape'],
  trim_blocks=True,
  lstrip_blocks=True,
  autoescape=True)

JINJA_ENVIRONMENT.filters['classize'] = classize
JINJA_ENVIRONMENT.filters['instantize'] = instantize
JINJA_ENVIRONMENT.filters['camelize'] = camelize

schema_path = sys.argv[1]
prefix = sys.argv[2]
package = sys.argv[3]

schema = json.load(open(schema_path))

allClasses = {}

def parseClass(root, name):
	one_of = root.get('oneOf', {})
	if one_of:
		parseOneOf(one_of, name)
	else:
		properties = root.get('properties', {})
		for property_name in properties.iterkeys():
			property = properties[property_name]
			# Dereference references to objects and enums
			if property.get("$ref"):
				property['type'] = get_reference(property.get("$ref"))
				del property['$ref']

		allClasses[name] = {
			'kind': 'class',
			'properties': properties,
			'required': root.get('required', [])
		}

def parseEnum(root, name):
	allClasses[name] = {
		'kind': 'enum',
		'enum': root.get('enum', []),
		'type': root.get('type', 'string')
	}

def get_reference(reference):
	if reference[:2] != "#/":
		raise RuntimeError('Only internal absolute references are allowed at this time')

	refComponents = reference[2:].split('/')
	element = schema
	while len(refComponents) > 0:
		key = refComponents.pop(0)
		element = element.get(key)

	if not key in allClasses:
		if element.get('type') == 'object':
			parseClass(element, key)
		elif element.get('enum'):
			parseEnum(element, key)
		else:
			raise RuntimeError('Only enums are allowed by reference at this time')

	return key

def parseOneOf(one_of, name):
	possibles = []
	for item in one_of:
		if item.get('$ref'):
			ref = get_reference(item.get('$ref'))
			possibles.append(ref)
		else:
			raise RuntimeError('oneOf only supports referenced definitions at this time')

	allClasses[name] = {
		'kind': 'oneOf',
		'possibles': possibles
	}

def generate(platform, name, definition):
	name = classize(name)
	print "Generating %s %s ..." % (platform, name)

	for template_file in os.listdir('platforms/' + platform + '/object'):
		template = JINJA_ENVIRONMENT.get_template(platform + '/object/' + template_file)

		result = template.render({
			'prefix': prefix,
			'package': package,
			'name': name,
			'kind': definition.get('kind', 'class'),
			'required': definition.get('required', []),
			'properties': definition.get('properties', {}),
			'possibles': definition.get('possibles', []),
			'enum': definition.get('enum', []),
			'oneOf': definition.get('oneOf', False),
			'allClasses': allClasses
		})

		folder = 'output/' + platform
		if platform == 'android':
			folder = folder  + '/' + '/'.join(package.split('.'))

		if not os.path.exists(folder):
			os.makedirs(folder)

		_, ext = os.path.splitext(template_file)
		if platform == 'android':
			with open(folder + '/' + prefix + name + ext, 'w') as file:
				file.write(result)
		else:
			with open(folder + '/' + (prefix if platform == 'ios' else '') + name + ext, 'w') as file:
				file.write(result)
		
		

parseClass(schema, schema.get('title'))

platforms = os.listdir('platforms')
for platform in platforms:
	for name, definition in allClasses.iteritems():
		generate(platform, name, definition)

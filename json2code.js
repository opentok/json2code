'use strict';

const fs = require('fs');
const mkdirp = require('mkdirp');
const nunjucks = require('nunjucks');
const path = require('path');

nunjucks.installJinjaCompat();
const env = nunjucks.configure(path.join(__dirname, 'platforms'), {
  trimBlocks: true,
  lstripBlocks: true,
});

const [schemaPath, prefix, pkg] = process.argv.slice(2);

const schema = JSON.parse(fs.readFileSync(path.resolve(schemaPath)));

function classize(value) {
  return value.substr(0, 1).toUpperCase() + value.substr(1);
}

env.addFilter('classize', classize);

function instantize(value) {
  return value.substr(0, 1).toLowerCase() + value.substr(1);
}
env.addFilter('instantize', instantize);

function camelize(value) {
  const x = value.split('_').map((element) => {
    if (element) {
      return classize(element.toLowerCase());
    }
    return '_';
  }).join('');
  console.log('camelize', value, x);
  return x;
}
env.addFilter('camelize', camelize);

const allClasses = {};

function parseClass(root, name) {
  if (root.oneOf) {
    parseOneOf(root.oneOf, name);
  } else {
    const properties = Object.keys(root.properties).sort().reduce((previous, propertyName) => {
      let property = root.properties[propertyName];
      // Dereference references to objects and enums
      if (property.$ref) {
        property = Object.assign({}, property, {
          type: getReference(property.$ref),
        });
      }
      return previous.concat([[
        propertyName,
        property,
      ]]);
    }, []);

    // const properties = root.properties;
    // Object.keys(properties).forEach((propertyName) => {
    //   const property = properties[propertyName];
    //   if (property.$ref) {
    //     property.type = getReference(property.$ref);
    //     delete property.$ref;
    //   }
    // });

    allClasses[name] = {
      kind: 'class',
      properties,
      required: root.required || [],
    };
    console.log('Adding parsed class', allClasses[name]);
  }
}

function parseEnum(root, name) {
  allClasses[name] = {
    kind: 'enum',
    enum: root.enum || [],
    type: root.type,
  };
}

function getReference(reference) {
  if (reference.substr(0, 2) !== '#/') {
    throw new Error('Only internal absolute references are allowed at this time');
  }

  const refComponents = reference.substr(2).split('/');
  let element = schema;
  let key;
  while (refComponents.length > 0) {
    key = refComponents.shift();
    element = element[key];
  }

  if (!allClasses[key]) {
    if (element.type === 'object') {
      parseClass(element, key);
    } else if (element.enum) {
      parseEnum(element, key);
    } else {
      throw new Error('Only enums are allowed by reference at this time');
    }
  }

  return key;
}

function parseOneOf(oneOf, name) {
  const possibles = [];
  oneOf.forEach((item) => {
    if (item.$ref) {
      const ref = getReference(item.$ref);
      possibles.push(ref);
    } else {
      throw new Error('oneOf only supports referenced definitions at this time');
    }
  });

  allClasses[name] = {
    kind: 'oneOf',
    possibles,
  };
}

function generate(platform, name, definition) {
  name = classize(name);
  console.log(`Generating ${platform} ${name} ...`);

  const templateFiles = fs.readdirSync(path.join(__dirname, 'platforms', platform, 'object'));

  templateFiles.forEach((templateFile) => {
    console.log(templateFile);
    const templateFilePath = path.join(platform, 'object', templateFile);
    const result = env.render(templateFilePath, {
      prefix,
      package: pkg,
      name,
      kind: definition.kind || 'class',
      required: definition.required,
      properties: definition.properties,
      possibles: definition.possibles,
      enum: definition.enum,
      oneOf: definition.oneOf || false,
      allClasses,
    });

    let folder = path.join('output', platform);
    if (platform === 'android') {
      folder = path.join(folder, path.join(...pkg.split('.')));
    }

    mkdirp.sync(folder);

    const ext = path.extname(templateFile);
    let outputFileName = `${name}${ext}`;
    if (platform === 'ios' || platform === 'android') {
      outputFileName = `${prefix}${name}${ext}`;
    }
    console.log('writing to', path.join(folder, outputFileName));
    fs.writeFileSync(path.join(folder, outputFileName), result);
  });
}

console.log(schema);

parseClass(schema, schema.title);

console.log(allClasses);

const platforms = fs.readdirSync(path.join(__dirname, 'platforms'));

platforms.forEach((platform) => {
  console.log('platform', platform);
  Object.keys(allClasses).forEach((name) => {
    const definition = allClasses[name];
    generate(platform, name, definition);
  });
});

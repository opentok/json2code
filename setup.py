from setuptools import setup

VERSION = "0.1.0"


setup(
    name="pytok",
    version=VERSION,
    description="Rumor client library",
    long_description="",
    author="lokbox",
    author_email="tokbox.com",
    license="",
    url="http://www.tokbox.com",
    classifiers=[
        "Development Status :: 3 - Alpha",
        "License :: OSI Approved :: GNU Library or Lesser General Public License (LGPL)",
        "Programming Language :: Python",
        "Operating System :: MacOS :: MacOS X",
        "Operating System :: POSIX",
        "Operating System :: Microsoft :: Windows",
        "Topic :: Internet",
        "Topic :: Software Development :: Libraries :: Python Modules",
        "Intended Audience :: Developers",
    ],
    install_requires=['Jinja2'],
    keywords='json-schema',
    packages=['json2code'],
    scripts=['bin/json2code']
)


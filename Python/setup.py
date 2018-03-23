# -*- coding: utf-8 -*-
from setuptools import setup

LONG_DESCRIPTION = """
Semantria is a text analytics and sentiment analysis platform.

See https://semantria.readme.io for details.
"""

setup(
    name='semantria_sdk',
    version='4.2.92',
    packages=['semantria'],
    url='https://semantria.com',
    license='GPL v3',
    author='Semantria, LLC',
    author_email='support@semantria.com',
    description='Semantria Python SDK',
    long_description=LONG_DESCRIPTION,
    keywords='semantria sdk text analytics',
    classifiers=[
        'Programming Language :: Python',
        'Programming Language :: Python :: 3'
    ]
)

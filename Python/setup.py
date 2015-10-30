# -*- coding: utf-8 -*-
from setuptools import setup

LONG_DESCRIPTION = """
Semantria is a text analytics and sentiment analysis API. It allows you to gain valuable
insights from your unstructured text content. It is based on Lexalytics’ Salience – a text
analytics and sentiment analysis engine. It is the same engine as the one being used by
Oracle, Cisco, Thomson Reuters, Saleforce.com - Radian6, Visible, Lithium, and 50+ other
leaders in the space.

Semantria offers Python SDK, that is the most convenient way to get started with the Semantria API on Python.
SDK implements all the available Semantria features and demonstrate best practices of API usage.
"""

setup(
    name='semantria-sdk',
    version='3.8.82',
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

# encoding: utf-8

LONG_DESCRIPTION = '
Semantria is a text analytics and sentiment analysis platform.

See https://semantria.readme.io for API details.
'

Gem::Specification.new do |s|
  s.name = 'semantria_sdk'
  s.version = '4.2.86'
  s.summary = 'Semantria Ruby SDK'
  s.description  = LONG_DESCRIPTION
  s.license = 'GPL-3.0'
  s.author = 'Lexalytics, Inc'
  s.email = 'support@lexalytics.com'
  s.homepage = 'https://www.lexalytics.com/semantria'
  s.files = %w(
    lib/semantria.rb
    lib/semantria/session.rb
    lib/semantria/authrequest.rb
    lib/semantria/jsonserializer.rb
  )
end

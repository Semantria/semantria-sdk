# encoding: utf-8
require File.expand_path('../lib/semantria/version', __FILE__)

LONG_DESCRIPTION = '
Semantria is a text analytics and sentiment analysis API. It allows you to gain valuable
insights from your unstructured text content. It is based on Lexalytics’ Salience – a text
analytics and sentiment analysis engine. It is the same engine as the one being used by
Oracle, Cisco, Thomson Reuters, Saleforce.com - Radian6, Visible, Lithium, and 50+ other
leaders in the space.

Semantria offers Ruby SDK, that is the most convenient way to get started with the Semantria API on Ruby.
SDK implements all the available Semantria features and demonstrate best practices of API usage.'

Gem::Specification.new do |s|
  s.name = 'semantria_sdk'
  s.version = Semantria::VERSION
  s.summary = 'Semantria Ruby SDK'
  s.description  = LONG_DESCRIPTION
  s.license = 'GPL v3'
  s.author = 'Semantria, LLC'
  s.email = 'support@semantria.com'
  s.homepage = 'https://semantria.com'
  s.files = %w(
    lib/semantria.rb
    lib/semantria/session.rb
    lib/semantria/version.rb
    lib/semantria/authrequest.rb
    lib/semantria/jsonserializer.rb
  )
end
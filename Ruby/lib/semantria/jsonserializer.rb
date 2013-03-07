# encoding: utf-8
require 'json'

class JsonSerializer
  def gettype
    'json'
  end

  def serialize(obj, wrapper = nil)
    str = JSON.generate(obj)

    str.encoding.name != 'UTF-8' ? str.encode('UTF-8') : str
  end

  def deserialize(str, handler = nil)
    JSON.parse(str)
  end
end
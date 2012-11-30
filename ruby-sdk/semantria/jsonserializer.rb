# encoding: utf-8
require 'json'

class JsonSerializer
  def gettype()
    return "json"
  end
  
  def serialize(obj, wrapper = nil)
	if obj.is_a?(Hash) and obj.has_key?("cloned")
		if (obj["cloned"].is_a?(Array))
			obj["cloned"].each do |item| 
			  item["template"] = item["config_id"]
			  item.delete("config_id")
			  obj["added"] << item
			end
		elsif (obj["cloned"].is_a?(Hash)) 
			item = obj["cloned"]
			item["template"] = item["config_id"]
			item.delete("config_id")
			obj["added"] << item
		else
		  raise 'Unsupported object type: %s' % obj.class
		end
		
		obj.delete("cloned")
    end
	
    str = JSON.generate(obj)

	if str.encoding.name != "UTF-8"
		return str.encode('UTF-8')
	else
		return str
	end
  end
  
  def deserialize(str, handler = nil)
    obj = JSON.parse(str)
    return obj
  end
  
end
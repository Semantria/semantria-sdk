# encoding: utf-8
require 'rexml/document'                                             
require 'rexml/parsers/sax2parser'
require 'rexml/sax2listener'
require 'common'

class XmlSerializer
  def gettype()
    return "xml"
  end
  
  def serialize(obj, wrapper = nil)
    if wrapper.nil?
      raise 'Parameter is nil %s' % wrapper
    end
	
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
	
    if (obj.is_a?(Hash))
      str = '<%s>%s</%s>' % [wrapper["root"], dictToXML(obj, wrapper), wrapper["root"]]
    elsif (obj.is_a?(Array))
      str = '<%s>%s</%s>' % [wrapper["root"], listToXML(obj, wrapper), wrapper["root"]]
    else
      raise 'Unsupported object type: %s' % obj.class
    end

    if str.encoding.name != "UTF-8"
      return str.encode('UTF-8')
    else
      return str
    end
  end
  
  def deserialize(str, handler = nil)
    if handler.nil?
      raise 'Parameter is nil %s' % handler
    end

    parser = REXML::Parsers::SAX2Parser.new(str)
    parser.listen(handler)
    parser.parse()
    obj = handler.getData()
    return obj
  end
  
  private
  def dictToXML(obj, wrapper)
    str = ''
    obj.each do |key, value| 
      if (obj[key].is_a?(Hash))
        str = '%s<%s>%s</%s>' % [str, key, dictToXML(obj[key], wrapper), key]
      elsif (obj[key].is_a?(Array))
        str = '%s<%s>' % [str, key]
        
        obj[key].each do |item| 
          if (item.is_a?(Hash))
            str = '%s<%s>%s</%s>' % [str, wrapper[key], dictToXML(item, wrapper), wrapper[key]]
          else
            str = '%s<%s>%s</%s>' % [str, wrapper[key], item, wrapper[key]]
          end
        end
        
        str = '%s</%s>' % [str, key]
      else
        value = obj[key]
        if (value == true || value == false)
          if (value == true)
            value_str = "true"
          else
            value_str = "false"
          end

          str = '%s<%s>%s</%s>' % [str, key, value_str, key]
        else
          str = '%s<%s>%s</%s>' % [str, key, value, key]
        end
      end
    end 
    return str
  end

  def listToXML(obj, wrapper)
    str = ''
    obj.each do |item| 
      if (item.is_a?(Hash))
        str = '%s<%s>%s</%s>' % [str, wrapper["item"], dictToXML(item, wrapper), wrapper["item"]]
      else
        str = '%s<%s>%s</%s>' % [str, wrapper["item"], item, wrapper["item"]]
      end
    end
    return str
  end
end
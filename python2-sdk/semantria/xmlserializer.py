# -*- coding: utf-8 -*-
import xml.sax

class XmlSerializer:
    def gettype(self):
        return "xml"
    
    def serialize(self, obj, wrapper):
        if wrapper == None:
            raise Error('Parameter None: %s' % wrapper)

        if isinstance(obj, dict) and obj.has_key("cloned"):
            if isinstance(obj["cloned"], dict):
                item = obj["cloned"]
                item["template"] = item["config_id"]
                del item["config_id"]
                obj["added"].append(item)
            elif isinstance(obj["cloned"], list):
                for item in obj["cloned"]:
                    item["template"] = item["config_id"]
                    del item["config_id"]
                    obj["added"].append(item)
            else:
                raise Error('Unsupported object type: %s' % obj)

            del obj["cloned"]
        
        if isinstance(obj, dict):
            str = '<%s>%s</%s>' % (wrapper["root"], self.dictToXML(obj, wrapper), wrapper["root"])
        elif  isinstance(obj, list):
            str = '<%s>%s</%s>' % (wrapper["root"], self.listToXML(obj, wrapper), wrapper["root"])
        else:
            raise Error('Unsupported object type: %s' % obj)
        
        return str.encode('utf-8')

    def deserialize(self, str, handler=None):
        if handler == None:
            raise Error('Parameter None: %s' % handler)

        xml.sax.parseString(str, handler)
        obj = handler.getData()
        return obj

    def dictToXML(self, obj, wrapper):  
        str = ''
        for key in obj.keys():
            if isinstance(obj[key], dict):
                str = '%s<%s>%s</%s>' % (str, key, self.dictToXML(obj[key], wrapper), key)
            elif isinstance(obj[key], list):
                str = '%s<%s>' % (str, key)
                for item in obj[key]:
                    if isinstance(item, dict):
                        str = '%s<%s>%s</%s>' % (str, wrapper[key], self.dictToXML(item, wrapper), wrapper[key])
                    else:
                        str = '%s<%s>%s</%s>' % (str, wrapper[key], item, wrapper[key])
                str = '%s</%s>' % (str, key)
            else:
                value = obj[key]
                if isinstance(value, bool):
                    if value == True:
                       value_str = "true"
                    else:
                        value_str = "false"

                    str = '%s<%s>%s</%s>' % (str, key, value_str, key)
                else:
                    str = '%s<%s>%s</%s>' % (str, key, value, key)
        
        return str

    def listToXML(self, obj, wrapper):  
        str = ''
        for item in obj:
            if isinstance(item, dict):
                str = '%s<%s>%s</%s>' % (str, wrapper["item"], self.dictToXML(item, wrapper), wrapper["item"])
            else:
                str = '%s<%s>%s</%s>' % (str, wrapper["item"], item, wrapper["item"])
        
        return str       

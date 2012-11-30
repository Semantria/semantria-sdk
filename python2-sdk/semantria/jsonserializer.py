# -*- coding: utf-8 -*-
import json

class JsonSerializer:
    def gettype(self):
        return "json"

    def serialize(self, obj, wrapper=None):
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
        
        encoder = json.JSONEncoder(encoding='utf-8')
        return encoder.encode(obj)

    def deserialize(self, str, handler=None):
        decoder = json.JSONDecoder(encoding='utf-8')
        return decoder.decode(str)
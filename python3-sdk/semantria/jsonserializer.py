# -*- coding: utf-8 -*-

import json

class JsonSerializer:
    def gettype(self):
        return "json"

    def serialize(self, obj, wrapper=None):
        if isinstance(obj, dict) and "cloned" in obj:
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
        
        encoder = json.JSONEncoder()
        str = encoder.encode(obj)
        return str.encode('utf-8')

    def deserialize(self, st, handler=None):
        decoder = json.JSONDecoder()
        return decoder.decode(str(st, encoding = 'utf-8'))

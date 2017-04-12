# -*- coding: utf-8 -*-


import json
from semantria.error import SemantriaError

class JsonSerializer:
    def gettype(self):
        return "json"

    def serialize(self, obj):
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
                raise SemantriaError('Unsupported object type: %s' % obj)

            del obj["cloned"]

        #encoder = json.JSONEncoder()
        return json.dumps(obj)

    def deserialize(self, string):
        #decoder = json.JSONDecoder()
        if isinstance(string, bytes):
            return json.loads(string.decode('utf-8'))
        elif isinstance(string, str):
            return json.loads(string)
        else:
            raise RuntimeError("Can't deserialize a {}".format(type(string)))



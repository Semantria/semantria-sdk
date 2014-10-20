# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import json
from semantria.error import SemantriaError

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
                raise SemantriaError('Unsupported object type: %s' % obj)

            del obj["cloned"]

        #encoder = json.JSONEncoder()
        return json.dumps(obj)

    def deserialize(self, string, handler=None):
        #decoder = json.JSONDecoder()
        #return json.loads(string.decode('utf-8'))
        return json.loads(string)

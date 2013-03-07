//
//  DocRelEntities.h
//  semantria-sdk
//
//  Created by Michail Kropivka on 15.02.13.
//  Copyright (c) 2013 Michail Kropivka. All rights reserved.
//

#ifndef __semantria_sdk__DocRelEntities__
#define __semantria_sdk__DocRelEntities__

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class DocRelEntities: public JsonSerializable {
public:
    DocRelEntities();
    virtual ~DocRelEntities();
    
    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    
    string GetTitle()        {return title;}
    string GetEntityType()   {return entity_type;}
    
    void SetTitle(string title)               {this->title = title;}
    void SetEntityType(string entity_type)    {this->entity_type = entity_type;}
    
private:
    string title;
    string entity_type;
};

#endif /* defined(__semantria_sdk__DocRelEntities__) */

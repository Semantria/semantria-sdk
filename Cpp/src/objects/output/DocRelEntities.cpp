//
//  DocRelEntities.cpp
//  semantria-sdk
//
//  Created by Michail Kropivka on 15.02.13.
//  Copyright (c) 2013 Michail Kropivka. All rights reserved.
//

#include "DocRelEntities.h"

DocRelEntities::DocRelEntities() {}
DocRelEntities::~DocRelEntities() {}

void DocRelEntities::Serialize(Json::Value& root) {
    root["title"] = title;
    root["entity_type"] = entity_type;
}

void DocRelEntities::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    entity_type = root.get("entity_type", "").asString();
}


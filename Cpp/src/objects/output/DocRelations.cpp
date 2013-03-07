//
//  DocRelations.cpp
//  semantria-sdk
//
//  Created by Michail Kropivka on 15.02.13.
//  Copyright (c) 2013 Michail Kropivka. All rights reserved.
//

#include "DocRelations.h"

DocRelations::DocRelations() {
    this->entitles = new vector<DocRelEntities*>();
}

DocRelations::~DocRelations() {
    delete this->entitles;
}

void DocRelations::Serialize(Json::Value& root) {
    //TODO:
}

void DocRelations::Deserialize(Json::Value& root) {
    type = root.get("type", "").asString();
    relation_type = root.get("relation_type", "").asString();
    confidence_score = root.get("confidence_score", "").asString();
    extra = root.get("extra", "").asString();
    // Words
    if (NULL == this->entitles) {
        this->entitles = new vector<DocRelEntities*>();
    }
    
    Json::Value entitles = root["entitles"];
    for ( int i = 0; i < entitles.size(); ++i ) {
        DocRelEntities* w = new DocRelEntities();
        w->Deserialize(entitles[i]);
        this->entitles->push_back(w);
    }
}

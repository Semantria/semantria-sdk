//
//  DocDetails.cpp
//  semantria-sdk
//
//  Created by Michail Kropivka on 14.02.13.
//  Copyright (c) 2013 Michail Kropivka. All rights reserved.
//

#include "DocDetails.h"


DocDetails::DocDetails() {
    this->words = new vector<DocWords*>();
}

DocDetails::~DocDetails() {
    delete this->words;
}

void DocDetails::Serialize(Json::Value& root) {
    //TODO:
}

void DocDetails::Deserialize(Json::Value& root) {
    is_imperative = root.get("is_imperative", "").asBool();
    is_polar = root.get("is_polar", "").asBool();
    
    // Words
    if (NULL == this->words) {
        this->words = new vector<DocWords*>();
    }
    
    Json::Value words = root["words"];
    for ( int i = 0; i < words.size(); ++i ) {
        DocWords* w = new DocWords();
        w->Deserialize(words[i]);
        this->words->push_back(w);
    }
}

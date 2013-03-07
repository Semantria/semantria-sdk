//
//  DocWords.cpp
//  semantria-sdk
//
//  Created by Michail Kropivka on 14.02.13.
//  Copyright (c) 2013 Michail Kropivka. All rights reserved.
//

#include "DocWords.h"

DocWords::DocWords() {}
DocWords::~DocWords() {}

void DocWords::Serialize(Json::Value& root) {
    root["tag"] = tag;
    root["type"] = type;
    root["title"] = title;
    root["stemmed"] = stemmed;
    root["sentiment_score"] = sentiment_score;
}

void DocWords::Deserialize(Json::Value& root) {
    tag = root.get("tag", "").asString();
    type = root.get("type", "").asString();
    title = root.get("title", "").asString();
    stemmed = root.get("stemmed", "").asString();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
}

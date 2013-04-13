#include "Statistics.h"

Statistics::Statistics() {
    this->configurations = new vector<StatConfigurations*>();
}
Statistics::~Statistics() {
    delete this->configurations;
}

void Statistics::Serialize(Json::Value& root) {
    root["name"] = name;
    root["status"] = status;
    root["overall_texts"] = overall_texts;
    root["overall_batches"] = overall_batches;
    root["overall_calls"] = overall_calls;
    root["calls_system"] = calls_system;
    root["calls_data"] = calls_data;
    root["overall_docs"] = overall_docs;
    root["docs_processed"] = docs_processed;
    root["docs_failed"] = docs_failed;
    root["docs_responded"] = docs_responded;
    root["overall_colls"] = overall_colls;
    root["colls_processed"] = colls_processed;
    root["colls_failed"] = colls_failed;
    root["colls_responded"] = colls_responded;
    root["colls_documents"] = colls_documents;
    root["latest_used_app"] = latest_used_app;
    root["used_apps"] = used_apps;
    
	for (int i = 0; i < configurations->size(); ++i )  {
        Json::Value value;
        configurations->at(i)->Serialize(value);
        root["configurations"].append(value);
	}
}

void Statistics::Deserialize(Json::Value& root) {
    name = root.get("name", "").asString();
    status = root.get("status", "").asString();
    overall_texts = root.get("overall_texts", 0).asInt();
    overall_batches = root.get("overall_batches", 0).asInt();
    overall_calls = root.get("overall_calls", 0).asInt();
    calls_system = root.get("calls_system", 0).asInt();
    calls_data = root.get("calls_data", 0).asInt();
    overall_docs = root.get("overall_docs", 0).asInt();
    docs_processed = root.get("docs_processed", 0).asInt();
    docs_failed = root.get("docs_failed", 0).asInt();
    docs_responded = root.get("docs_responded", 0).asInt();
    overall_colls = root.get("overall_colls", 0).asInt();
    colls_processed = root.get("colls_processed", 0).asInt();
    colls_failed = root.get("colls_failed", 0).asInt();
    colls_responded = root.get("colls_responded", 0).asInt();
    colls_documents = root.get("colls_documents", 0).asInt();
    latest_used_app = root.get("latest_used_app", "").asString();
    used_apps = root.get("used_apps", "").asString();

    if (NULL == this->configurations) {
        this->configurations = new vector<StatConfigurations*>();
    }
    
    Json::Value configurations = root["configurations"];
    for ( int i = 0; i < configurations.size(); ++i ) {
        StatConfigurations* w = new StatConfigurations();
        w->Deserialize(configurations[i]);
        this->configurations->push_back(w);
    }
    
}


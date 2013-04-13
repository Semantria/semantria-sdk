#ifndef __semantria_sdk__DocWords__
#define __semantria_sdk__DocWords__

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class DocWords: public JsonSerializable {
public:
    DocWords();
    virtual ~DocWords();
    
    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    
    string GetTag()             {return tag;}
    string GetType()            {return type;}
    string GetTitle()           {return title;}
    string GetStemmed()         {return stemmed;}
    double GetSentimentScore()  {return sentiment_score;}
    
    void SetTag(string tag)         {this->tag = tag;}
    void SetType(string type)       {this->type = type;}
    void SetTitle(string title)     {this->title = title;}
    void SetStemmed(string stemmed) {this->stemmed = stemmed;}
    void SetSentimentScore(
            double sentiment_score) {this->sentiment_score = sentiment_score;}

private:
    string tag;
    string type;
    string title;
    string stemmed;
    double sentiment_score;
};

#endif /* defined(__semantria_sdk__DocWords__) */

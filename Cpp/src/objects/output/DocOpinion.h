#ifndef __semantria_sdk__DocOpinion__
#define __semantria_sdk__DocOpinion__

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class DocOpinion: public JsonSerializable {
public:
    DocOpinion();
    virtual ~DocOpinion();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetQuotation()       {return quotation;}
    string GetType()            {return type;}
    string GetSpeaker()         {return speaker;}
    string GetTopic()           {return topic;}
    double GetSentimentScore()          {return sentiment_score;}
    string GetSentimentPolarity()       {return sentiment_polarity;};

    void SetQuotation(string quotation)      {this->quotation = quotation;}
    void SetType(string type)               {this->type = type;}
    void SetSpeaker(string speaker)            {this->speaker = speaker;}
    void SetTopic(string topic)               {this->topic = topic;}
    void SetSentimentScore(double sentiment_score)    {this->sentiment_score = sentiment_score;}
    void SetSentimentPolarity(string sentiment_polarity) {this->sentiment_polarity = sentiment_polarity;}

private:
    string quotation;
    string type;
    string speaker;
    string topic;
    double sentiment_score;
    string sentiment_polarity;
};

#endif /* defined(__semantria_sdk__DocOpinion__) */

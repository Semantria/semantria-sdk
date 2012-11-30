# -*- coding: utf-8 -*-
import xml.sax

def str2bool(str): 
  if isinstance(str, basestring) and str.lower() in ['0','false','no']: 
    return False 
  else: 
    return bool(str) 


class GetStatusHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.data = None
      
      self.service_status = ""
      self.api_version = ""
      self.service_version = ""
      self.supported_encoding = ""
      self.supported_compression = ""
      self.supported_languages = None
      self.language = ""
   
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      if tag == "status":
         self.data = {}
      elif tag == "supported_languages":
         self.supported_languages = []

   # Call when an elements ends
   def endElement(self, tag):
      if tag == "status":
          self.data["service_status"] = self.service_status
          self.data["api_version"] = self.api_version
          self.data["service_version"] = self.service_version
          self.data["supported_encoding"] = self.supported_encoding
          self.data["supported_compression"] = self.supported_compression
      elif tag == "supported_languages":
         self.data["supported_languages"] = self.supported_languages
      elif tag == "language":
         self.supported_languages.append(self.language)

      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "service_status":
         self.service_status = content
      elif self.current == "api_version":
         self.api_version = content
      elif self.current == "service_version":
         self.service_version = content
      elif self.current == "supported_encoding":
         self.supported_encoding = content
      elif self.current == "supported_compression":
         self.supported_compression = content		 
      elif self.current == "language":
         self.language = content

class GetSubscriptionHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.data = None
      
      self.name = ""
      self.status = ""
      self.priority = ""
      self.expiration_date = 0
      self.calls_balance = 0
      self.calls_limit = 0
      self.calls_limit_interval = 0
      self.docs_balance = 0
      self.docs_limit = 0
      self.docs_limit_interval = 0
      self.configurations_limit = 0
      self.blacklist_limit = 0
      self.categories_limit = 0
      self.queries_limit = 0
      self.entities_limit = 0
      self.sentiment_limit = 0
      self.characters_limit = 0
      self.batch_limit = 0
      self.collection_limit = 0
      self.auto_response_limit = 0
      self.processed_batch_limit = 0
      self.callback_batch_limit = 0
      self.limit_type = ""
    
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      if tag == "subscription":
         self.data = {}

   # Call when an elements ends
   def endElement(self, tag):
      if tag == "subscription":
          self.data["name"] = self.name
          self.data["status"] = self.status
          self.data["priority"] = self.priority
          self.data["expiration_date"] = self.expiration_date
          self.data["calls_balance"] = self.calls_balance
          self.data["calls_limit"] = self.calls_limit
          self.data["calls_limit_interval"] = self.calls_limit_interval
          self.data["docs_balance"] = self.docs_balance
          self.data["docs_limit"] = self.docs_limit
          self.data["docs_limit_interval"] = self.docs_limit_interval
          self.data["configurations_limit"] = self.configurations_limit
          self.data["blacklist_limit"] = self.blacklist_limit
          self.data["categories_limit"] = self.categories_limit
          self.data["queries_limit"] = self.queries_limit
          self.data["entities_limit"] = self.entities_limit
          self.data["sentiment_limit"] = self.sentiment_limit
          self.data["characters_limit"] = self.characters_limit
          self.data["batch_limit"] = self.batch_limit
          self.data["collection_limit"] = self.collection_limit
          self.data["auto_response_limit"] = self.auto_response_limit
          self.data["processed_batch_limit"] = self.processed_batch_limit
          self.data["callback_batch_limit"] = self.callback_batch_limit
          self.data["limit_type"] = self.limit_type

      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "name":
            self.name += content
      elif self.current == "status":
         self.status = content
      elif self.current == "priority":
         self.priority = content
      elif self.current == "expiration_date":
         self.expiration_date = long(content)
      elif self.current == "calls_balance":
         self.calls_balance = int(content)
      elif self.current == "calls_limit":
         self.calls_limit = int(content)	 
      elif self.current == "calls_limit_interval":
         self.calls_limit_interval = int(content)	 
      elif self.current == "docs_balance":
         self.docs_balance = int(content)
      elif self.current == "docs_limit":
         self.docs_limit = int(content)
      elif self.current == "docs_limit_interval":
         self.docs_limit_interval = int(content)
      elif self.current == "configurations_limit":
         self.configurations_limit = int(content)
      elif self.current == "blacklist_limit":
         self.blacklist_limit = int(content)
      elif self.current == "categories_limit":
         self.categories_limit = int(content)	 
      elif self.current == "configurations_limit":
         self.configurations_limit = int(content)
      elif self.current == "queries_limit":
         self.queries_limit = int(content)
      elif self.current == "entities_limit":
         self.entities_limit = int(content)	
      elif self.current == "sentiment_limit":
         self.sentiment_limit = int(content)
      elif self.current == "characters_limit":
         self.characters_limit = int(content) 
      elif self.current == "batch_limit":
         self.batch_limit = int(content)
      elif self.current == "collection_limit":
         self.collection_limit = int(content)
      elif self.current == "auto_response_limit":
         self.auto_response_limit = int(content)
      elif self.current == "processed_batch_limit":
         self.processed_batch_limit = int(content)
      elif self.current == "callback_batch_limit":
         self.callback_batch_limit = int(content)
      elif self.current == "limit_type":
         self.limit_type = content         

class GetConfigurationsHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.parent = None
      self.hierarchy = []
      self.data = None
      self.item = None
      self.document = None
      self.collection = None
      
      self.config_id = ""
      self.name = ""
      self.one_sentence = False
      self.is_primary = False
      self.auto_responding = False
      self.language = ""
      self.chars_threshold = 0
      self.callback = ""
      
      self.concept_topics_limit = 0
      self.query_topics_limit = 0
      self.named_entities_limit = 0
      self.user_entities_limit = 0
      self.entity_themes_limit = 0
      self.themes_limit = 0
      self.phrases_limit = 0	  
      self.summary_limit = 0

      self.facets_limit = 0
      self.facet_atts_limit = 0
    
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag

      if tag == "document" or tag == "collection":
        self.parent = tag
        self.hierarchy.append(tag)

      if tag == "configurations":
         self.data = []
      elif tag == "document":
         self.document = {}
      elif tag == "collection":
         self.collection = {}
 
   # Call when an elements ends
   def endElement(self, tag):
      if tag == "document" or tag == "collection":
            self.parent = self.hierarchy.pop()
            if (len(self.hierarchy) > 0):
                self.parent = self.hierarchy[len(self.hierarchy) - 1]

      if tag == "configuration":
          self.item = {}
          self.item["config_id"] = self.config_id
          self.item["name"] = self.name
	  self.item["one_sentence"] = self.one_sentence
          self.item["is_primary"] = self.is_primary
          self.item["auto_responding"] = self.auto_responding
          self.item["language"] = self.language
          self.item["chars_threshold"] = self.chars_threshold
          self.item["callback"] = self.callback
          self.item["document"] = self.document
          self.item["collection"] = self.collection
          self.data.append(self.item)
          self.config_id = ""
          self.name = ""
	  self.one_sentence = False
          self.is_primary = False
          self.auto_responding = False
          self.language = ""
          self.chars_threshold = 0
          self.callback = ""
          self.document = None
          self.collection = None
      elif tag == "document":
          self.document["concept_topics_limit"] = self.concept_topics_limit
          self.document["query_topics_limit"] = self.query_topics_limit
          self.document["named_entities_limit"] = self.named_entities_limit
          self.document["user_entities_limit"] = self.user_entities_limit
          self.document["entity_themes_limit"] = self.entity_themes_limit
          self.document["themes_limit"] = self.themes_limit
          self.document["phrases_limit"] = self.phrases_limit		  
          self.document["summary_limit"] = self.summary_limit
          self.concept_topics_limit = 0
          self.query_topics_limit = 0
          self.named_entities_limit = 0
          self.user_entities_limit = 0
          self.entity_themes_limit = 0
          self.themes_limit = 0
          self.phrases_limit = 0		  
          self.summary_limit = 0
      elif tag == "collection":
          self.collection["facets_limit"] = self.facets_limit
          self.collection["facet_atts_limit"] = self.facet_atts_limit
          self.collection["concept_topics_limit"] = self.concept_topics_limit
          self.collection["query_topics_limit"] = self.query_topics_limit
          self.collection["named_entities_limit"] = self.named_entities_limit
          self.collection["themes_limit"] = self.themes_limit
          self.facets_limit = 0
          self.facet_atts_limit = 0
          self.concept_topics_limit = 0
          self.query_topics_limit = 0
          self.named_entities_limit = 0
          self.themes_limit = 0
      
      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "config_id":
         self.config_id = content
      elif self.current == "name":
         self.name += content
      elif self.current == "is_primary":
         self.is_primary = str2bool(content)
      elif self.current == "one_sentence":
         self.one_sentence = str2bool(content)		 
      elif self.current == "auto_responding":
         self.auto_responding = str2bool(content)
      elif self.current == "language":
         self.language = content
      elif self.current == "chars_threshold":
         self.chars_threshold = int(content)
      elif self.current == "callback":
         self.callback = content
      elif self.current == "concept_topics_limit":
         self.concept_topics_limit = int(content)
      elif self.current == "query_topics_limit":
         self.query_topics_limit = int(content)
      elif self.current == "named_entities_limit":
         self.named_entities_limit = int(content)
      elif self.current == "user_entities_limit":
         self.user_entities_limit = int(content)
      elif self.current == "entity_themes_limit":
         self.entity_themes_limit = int(content)
      elif self.current == "themes_limit":
         self.themes_limit = int(content)
      elif self.current == "phrases_limit":
         self.phrases_limit = int(content)		 
      elif self.current == "summary_limit":
         self.summary_limit = int(content)
      elif self.current == "facets_limit":
         self.facets_limit = int(content)
      elif self.current == "facet_atts_limit":
         self.facet_atts_limit = int(content)
     	     	     

class GetBlacklistHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.data = None
      self.item = ""
    
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      if tag == "blacklist":
         self.data = []

   # Call when an elements ends
   def endElement(self, tag):
      if tag == "item":
          self.data.append(self.item)
          self.item = ""
 
      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "item":
         self.item += content

class GetCategoriesHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.data = None
      self.category = None
      
      self.name = ""
      self.weight = None
      self.sample = ""
    
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      if tag == "categories":
         self.data = []
      elif tag == "category":
         self.category = {}
      elif tag == "samples":
         self.samples = []

   # Call when an elements ends
   def endElement(self, tag):
      if tag == "category":
          self.category["name"] = self.name
          self.category["weight"] = self.weight
          self.data.append(self.category)
          self.name = ""
          self.weight = None
      elif tag == "samples":
         self.category["samples"] = self.samples
      elif tag == "sample":
         self.samples.append(self.sample)
         self.sample = ""

      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "name":
         self.name += content
      elif self.current == "weight":
         self.weight = float(content)
      elif self.current == "sample":
         self.sample += content

class GetQueriesHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.data = None
      self.query = None
      
      self.name = ""
      self.content = ""
      self.idx = 0
    
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      if tag == "queries":
         self.data = []
      elif tag == "query":
         self.idx += 1 

   # Call when an elements ends
   def endElement(self, tag):
      if tag == "query" and self.idx == 1:
          self.idx -= 1 
          self.query = {}
          self.query["name"] = self.name
          self.query["query"] = self.content
          self.data.append(self.query)
          self.name = ""
          self.content = ""
      elif tag == "query" and self.idx == 2:
          self.idx -= 1

      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "name":
         self.name += content
      elif self.current == "query":
         self.content += content

class GetSentimentPhrasesHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.data = None
      self.phrase = None
      
      self.title = ""
      self.weight = None
      self.idx = 0
    
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      if tag == "phrases":
         self.data = []
      elif tag == "phrase":
         self.idx += 1 

   # Call when an elements ends
   def endElement(self, tag):
      if tag == "phrase" and self.idx == 1:
          self.idx -= 1 
          self.phrase = {}
          self.phrase["title"] = self.title
          self.phrase["weight"] = self.weight
          self.data.append(self.phrase)
          self.title = ""
          self.weight = None
      elif tag == "phrase" and self.idx == 2:
          self.idx -= 1

      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "title":
         self.title += content
      elif self.current == "weight" and self.weight is None:
         self.weight = float(content)

class GetEntitiesHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.data = None
      self.entity = None
      
      self.name = ""
      self.type = ""
    
   def getData(self):
      return self.data

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      if tag == "entities":
         self.data = []

   # Call when an elements ends
   def endElement(self, tag):
      if tag == "entity":
          self.entity = {}
          self.entity["name"] = self.name
          self.entity["type"] = self.type
          self.data.append(self.entity)
          self.name = ""
          self.type = ""

      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "name":
         self.name += content
      elif self.current == "type":
         self.type += content

class GetDocumentHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.hierarchy = []
      self.parent = ""
      self.document = None
      self.entity = None
      self.theme = None
      self.topic = None
      self.entities = None
      self.entity_themes = None
      self.doc_themes = None
      self.topics = None
      self.phrases = None
      self.phrase = None
      
      self.id = None
      self.config_id = None
      self.status = None
      self.sentiment_score = None 
      self.summary = None
      
      self.evidence = None
      self.confident = None
      self.is_about = None
      self.title = None
      self.strength_score = None
      self.type = None
      self.hitcount = None
      self.entity_type = None
      self.is_negated = None
      self.negating_phrase = None
                
   def getData(self):
      return self.document

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      
      if (tag == "document" or tag == "entity" or tag == "theme" or tag == "topic" or tag == "phrase"):
        self.parent = tag
        self.hierarchy.append(tag)     
      
      if tag == "document":
         self.document = {}
      elif tag == "entities":
         self.entities = []
      elif tag == "themes" and self.parent == 'entity':
         self.entity_themes = []
      elif tag == "themes" and self.parent == 'document':
         self.doc_themes = []
      elif tag == "topics":
         self.topics = []
      elif tag == "phrases":
         self.phrases = []		 
      elif tag == "entity":
         self.entity = {}
      elif tag == "theme":
         self.theme = {}
      elif tag == "topic":
         self.topic = {}
      elif tag == "phrase":
         self.phrase = {}

   # Call when an elements ends
   def endElement(self, tag):
      if (tag == "document" or tag == "entity" or tag == "theme" or tag == "topic" or tag == "phrase"):
          self.parent = self.hierarchy.pop()
          if (len(self.hierarchy) > 0):
              self.parent = self.hierarchy[len(self.hierarchy) - 1]     
      
      if tag == "entities":
         self.document["entities"] = self.entities
      elif tag == "topics":
         self.document["topics"] = self.topics
      elif tag == "phrases":
         self.document["phrases"] = self.phrases		 
      elif tag == "themes" and self.parent == 'entity':
         self.entity["themes"] = self.entity_themes
      elif tag == "themes" and self.parent == 'document':
         self.document["themes"] = self.doc_themes
      elif tag == "document":
         self.document["entities"] = self.entities 
         self.document["topics"] = self.topics 
         self.document["phrases"] = self.phrases 
         self.document["themes"] = self.doc_themes 
         self.entities = None
         self.topics = None
         self.phrases = None	 
         self.themes = None	 
      elif tag == "entity":
         self.entity["themes"] = self.entity_themes 
         self.entities.append(self.entity)
         self.entity = None
         self.entity_themes  = None
      elif tag == "theme":
         if self.parent == "entity":
            self.entity_themes.append(self.theme)
         elif self.parent == "document":
            self.doc_themes.append(self.theme)
         self.theme = None
      elif tag == "topic":
         self.topics.append(self.topic)
         self.topic = None
      elif tag == "phrase":
         self.phrases.append(self.phrase)
         self.phrase = None		 

      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "id":
         self.id = content
      elif self.current == "config_id":
         self.config_id = content
      elif self.current == "status":
         self.status = content
      elif self.current == "sentiment_score":
         self.sentiment_score = float(content) 
      elif self.current == "evidence":
         self.evidence = float(content)
      elif self.current == "title":
         self.title = content
      elif self.current == "strength_score":
         self.strength_score = float(content)
      elif self.current == "type":
         self.type = content
      elif self.current == "title":
         self.title = content
      elif self.current == "hitcount":
         self.hitcount = float(content)
      elif self.current == "summary":
         self.summary = content
      elif self.current == "is_about":
         self.is_about = str2bool(content)
      elif self.current == "confident":
         self.confident = str2bool(content)
      elif self.current == "entity_type":
         self.entity_type = content
      elif self.current == "negating_phrase":
         self.negating_phrase = content
      elif self.current == "is_negated":
         self.is_negated = str2bool(content)
         
      if self.parent == "document":
         if self.id is not None: 
             self.document["id"] = self.id 
             self.id = None
         if self.config_id is not None: 
             self.document["config_id"] = self.config_id 
             self.config_id = None
         if self.status is not None: 
             self.document["status"] = self.status 
             self.status = None
         if self.sentiment_score is not None: 
             self.document["sentiment_score"] = self.sentiment_score 
             self.sentiment_score = None
         if self.summary is not None:
             if "summary" in self.document:
                self.document["summary"] += self.summary
             else:
                self.document["summary"] = self.summary
             self.summary = None
      elif self.parent == "entity":
         if self.evidence is not None: 
             self.entity["evidence"] = self.evidence 
             self.evidence = None
         if self.sentiment_score is not None: 
             self.entity["sentiment_score"] = self.sentiment_score 
             self.sentiment_score = None
         if self.title is not None:
             if "title" in self.entity:
                self.entity["title"] += self.title
             else:
                self.entity["title"] = self.title
             self.title = None
         if self.type is not None: 
             self.entity["type"] = self.type 
             self.type = None
         if self.confident is not None:
             self.entity["confident"] = self.confident
             self.confident = None
         if self.is_about is not None: 
             self.entity["is_about"] = self.is_about 
             self.is_about = None	     
         if self.entity_type is not None: 
             self.entity["entity_type"] = self.entity_type 
             self.entity_type = None
      elif self.parent == "theme":
         if self.evidence is not None: 
             self.theme["evidence"] = self.evidence 
             self.evidence = None
         if self.is_about is not None: 
             self.theme["is_about"] = self.is_about 
             self.is_about = None
         if self.sentiment_score is not None: 
             self.theme["sentiment_score"] = self.sentiment_score 
             self.sentiment_score = None
         if self.strength_score is not None: 
             self.theme["strength_score"] = self.strength_score 
             self.strength_score = None
         if self.title is not None:
             if "title" in self.theme:
                self.theme["title"] += self.title
             else:
                self.theme["title"] = self.title
             self.title = None
      elif self.parent == "topic":
         if self.sentiment_score is not None: 
             self.topic["sentiment_score"] = self.sentiment_score 
             self.sentiment_score = None
         if self.strength_score is not None: 
             self.topic["strength_score"] = self.strength_score 
             self.strength_score = None
         if self.type is not None: 
             self.topic["type"] = self.type 
             self.type = None
         if self.hitcount is not None: 
             self.topic["hitcount"] = self.hitcount 
             self.hitcount = None
         if self.title is not None:
             if "title" in self.topic:
                self.topic["title"] += self.title
             else:
                self.topic["title"] = self.title
             self.title = None
      elif self.parent == "phrase":
         if self.sentiment_score is not None: 
             self.phrase["sentiment_score"] = self.sentiment_score 
             self.sentiment_score = None
         if self.is_negated is not None: 
             self.phrase["is_negated"] = self.is_negated 
             self.is_negated = None
         if self.negating_phrase is not None:
             if "negating_phrase" in self.phrase:
                self.phrase["negating_phrase"] += self.negating_phrase
             else:
                self.phrase["negating_phrase"] = self.negating_phrase
             self.negating_phrase = None
         if self.title is not None:
             if "title" in self.phrase:
                self.phrase["title"] += self.title
             else:
                self.phrase["title"] = self.title
             self.title = None			 
         
class GetProcessedDocumentsHandler( GetDocumentHandler ):
   def __init__(self):
     self.data = None
     GetDocumentHandler.__init__(self)
          
   def getData(self):
      return self.data

     # Call when an element starts
   def startElement(self, tag, attributes):
      if tag == "documents":
         self.data = []
      else:
         GetDocumentHandler.startElement(self, tag, attributes)

   # Call when an elements ends
   def endElement(self, tag):
       GetDocumentHandler.endElement(self, tag)
       if tag == "document":
           self.data.append(self.document)
           self.document = None
           
class GetCollectionHandler( xml.sax.ContentHandler ):
   def __init__(self):
      self.current = ""
      self.hierarchy = []
      self.parent = ""
      self.collection = None
      self.facets = None
      self.facet = None
      self.attributes = None
      self.attribute = None
      
      self.id = None
      self.config_id = None
      self.status = None
      self.label = None
      self.count = None
      self.negative_count = None
      self.positive_count = None
      self.neutral_count = None
      
      self.title = None
      self.phrases_count = None
      self.themes_count = None
      self.sentiment_score = None
      self.type = None
      self.entity_type = None
      self.hitcount = None
      
                
   def getData(self):
      return self.collection

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.current = tag
      
      if (tag == "collection" or tag == "facet" or tag == "attribute" or tag == "entity" or tag == "topic" or tag == "theme"):
        self.parent = tag
        self.hierarchy.append(tag)     
      
      if tag == "collection":
         self.collection = {}
      elif tag == "facets":
         self.facets = []
      elif tag == "facet":
         self.facet = {}
      elif tag == "attributes":
         self.attributes = []
      elif tag == "attribute":
         self.attribute = {}
      elif tag == "entities":
         self.entities = []
      elif tag == "entity":
         self.entity = {}
      elif tag == "topics":
         self.topics = []
      elif tag == "topic":
         self.topic = {}
      elif tag == "themes":
         self.themes = []
      elif tag == "theme":
         self.theme = {}

   # Call when an elements ends
   def endElement(self, tag):
      if (tag == "collection" or tag == "facet" or tag == "attribute" or tag == "entity" or tag == "topic" or tag == "theme"):
          self.parent = self.hierarchy.pop()
          if (len(self.hierarchy) > 0):
              self.parent = self.hierarchy[len(self.hierarchy) - 1]     
      
      if tag == "facets":
         self.collection["facets"] = self.facets
      elif tag == "attributes":
         self.facet["attributes"] = self.attributes
      elif tag == "collection":
         self.collection["facets"] = self.facets 
         self.facets = None
      elif tag == "facet":
         self.facet["attributes"] = self.attributes 
         self.facets.append(self.facet)
         self.facet = None
         self.attributes  = None
      elif tag == "attribute":
         if self.parent == "facet":
            self.attributes.append(self.attribute)
         self.attribute = None
      elif tag == "entities":
         self.collection["entities"] = self.entities
      elif tag == "entity":
         self.entities.append(self.entity)
         self.entity = None
      elif tag == "topics":
         self.collection["topics"] = self.topics
      elif tag == "topic":
         self.topics.append(self.topic)
         self.topic = None
      elif tag == "themes":
         self.collection["themes"] = self.themes
      elif tag == "theme":
         self.themes.append(self.theme)
         self.theme = None
      
      self.current = ""

   # Call when a character is read
   def characters(self, content):
      if self.current == "id":
         self.id = content
      elif self.current == "config_id":
         self.config_id = content
      elif self.current == "status":
         self.status = content
      elif self.current == "label":
         self.label = content
      elif self.current == "count":
         self.count = int(content)
      elif self.current == "negative_count":
         self.negative_count = int(content)
      elif self.current == "positive_count":
         self.positive_count = int(content)
      elif self.current == "neutral_count":
         self.neutral_count = int(content)
      elif self.current == "title":
         self.title = content
      elif self.current == "phrases_count":
         self.phrases_count = int(content)         
      elif self.current == "themes_count":
         self.themes_count = int(content)
      elif self.current == "sentiment_score":
         self.sentiment_score = float(content)  	 
      elif self.current == "type":
         self.type = content               
      elif self.current == "entity_type":
         self.entity_type = content
      elif self.current == "hitcount":
         self.hitcount = int(content)                  
      
      if self.parent == "collection":
         if self.id is not None: 
             self.collection["id"] = self.id 
             self.id = None
         if self.config_id is not None: 
             self.collection["config_id"] = self.config_id 
             self.config_id = None
         if self.status is not None: 
             self.collection["status"] = self.status 
             self.status = None
      elif self.parent == "facet":
         if self.label is not None:
             if "label" in self.facet:
                self.facet["label"] += self.label
             else:
                self.facet["label"] = self.label
             self.label = None
         if self.count is not None: 
             self.facet["count"] = self.count 
             self.count = None
         if self.positive_count is not None: 
             self.facet["positive_count"] = self.positive_count 
             self.count = None
         if self.negative_count is not None: 
             self.facet["negative_count"] = self.negative_count 
             self.negative_count = None
         if self.neutral_count is not None: 
             self.facet["neutral_count"] = self.neutral_count 
             self.neutral_count = None
      elif self.parent == "attribute":
         if self.label is not None:
             if "label" in self.attribute:
                self.attribute["label"] += self.label
             else:
                self.attribute["label"] = self.label
             self.label = None
         if self.count is not None: 
             self.attribute["count"] = self.count 
             self.count = None
      elif self.parent == "entity":
         if self.title is not None:
             if "title" in self.entity:
                self.entity["title"] += self.title
             else:
                self.entity["title"] = self.title
             self.title = None
         if self.type is not None: 
             self.entity["type"] = self.type
             self.type = None
         if self.entity_type is not None: 
             self.entity["entity_type"] = self.entity_type
             self.entity_type = None
         if self.count is not None: 
             self.entity["count"] = self.count
             self.count = None
         if self.negative_count is not None: 
             self.entity["negative_count"] = self.negative_count
             self.negative_count = None
         if self.neutral_count is not None: 
             self.entity["neutral_count"] = self.neutral_count
             self.neutral_count = None
         if self.positive_count is not None: 
             self.entity["positive_count"] = self.positive_count
             self.positive_count = None
      elif self.parent == "topic":
         if self.title is not None:
             if "title" in self.topic:
                self.topic["title"] += self.title
             else:
                self.topic["title"] = self.title
             self.title = None
         if self.type is not None: 
             self.topic["type"] = self.type
             self.type = None
         if self.hitcount is not None: 
             self.topic["hitcount"] = self.hitcount
             self.hitcount = None
         if self.sentiment_score is not None: 
             self.topic["sentiment_score"] = self.sentiment_score
             self.sentiment_score = None
      elif self.parent == "theme":
         if self.title is not None:
             if "title" in self.theme:
                self.theme["title"] += self.title
             else:
                self.theme["title"] = self.title
             self.title = None
         if self.phrases_count is not None:
             self.theme["phrases_count"] = self.phrases_count
             self.phrases_count = None
         if self.themes_count is not None: 
             self.theme["themes_count"] = self.themes_count
             self.themes_count = None
         if self.sentiment_score is not None: 
             self.theme["sentiment_score"] = self.sentiment_score
             self.sentiment_score = None



class GetProcessedCollectionsHandler( GetCollectionHandler ):
   def __init__(self):
     self.data = None
     GetCollectionHandler.__init__(self)
          
   def getData(self):
      return self.data

     # Call when an element starts
   def startElement(self, tag, attributes):
      if tag == "collections":
         self.data = []
      else:
         GetCollectionHandler.startElement(self, tag, attributes)

   # Call when an elements ends
   def endElement(self, tag):
       GetCollectionHandler.endElement(self, tag)
       if tag == "collection":
           self.data.append(self.collection)
           self.collection = None

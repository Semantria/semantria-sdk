<?php

class Semantria_XmlSerializer
{
    function getType()
    {
        return "xml";
    }

    function serialize($obj, $wrapper=null)
    {
        $obj = Semantria_Common::processUpdateProxyClonedElement($obj);
        $obj = Semantria_Common::utf8Encode($obj);

        if ($wrapper == null) {
            throw new Exception('Parameter is null or empty "'.$wrapper.'"');
        }

        if (Semantria_Common::is_assoc($obj)) {
            $str = Semantria_Common::string_format('<{0}>{1}</{0}>', $wrapper["root"], $this->dictToXML($obj, $wrapper));
        } elseif (is_array($obj)) {
            $str = Semantria_Common::string_format('<{0}>{1}</{0}>', $wrapper["root"], $this->listToXML($obj, $wrapper));
        } else {
            throw new Exception('Unsupported object type: '.$obj);
        }

        return $str;
    }

    function deserialize($str, $handler=null)
    {
        $parser = xml_parser_create("UTF-8");
        xml_set_object($parser, $handler);
        xml_set_element_handler($parser, 'startElement', 'endElement');
        xml_set_character_data_handler($parser, 'characters');
        xml_parser_set_option($parser, XML_OPTION_TARGET_ENCODING, "UTF-8");
        xml_parser_set_option($parser, XML_OPTION_CASE_FOLDING, false);
        xml_parse($parser, $str);
        $obj = $handler->getData();
        xml_parser_free($parser);
        return $obj;
    }

    private function dictToXML($obj, $wrapper)
    {
        $str = '';
        foreach ($obj as $key => $value) {
            if (Semantria_Common::is_assoc($obj[$key])) {
                $str = Semantria_Common::string_format('{0}<{1}>{2}</{1}>', $str, $key, $this->dictToXML($obj[$key], $wrapper));
            } elseif (is_array($obj[$key])) {
                $str = Semantria_Common::string_format('{0}<{1}>', $str, $key);

                foreach ($obj[$key] as $item) {
                    if (Semantria_Common::is_assoc($item)) {
                        $str = Semantria_Common::string_format('{0}<{1}>{2}</{1}>', $str, $wrapper[$key], $this->dictToXML($item, $wrapper));
                    } else {
                        $str = Semantria_Common::string_format('{0}<{1}>{2}</{1}>', $str, $wrapper[$key], $item);
                    }
                }

                $str = Semantria_Common::string_format('{0}</{1}>', $str, $key);
            } else {
                $value = $obj[$key];
                if (is_bool($value)) {
                    if ($value == true)
                        $value_str = "true";
                    else
                        $value_str = "false";

                    $str = Semantria_Common::string_format('{0}<{1}>{2}</{1}>', $str, $key, $value_str);
                }
                else
                    $str = Semantria_Common::string_format('{0}<{1}>{2}</{1}>', $str, $key, $value);
            }
        }
        return $str;
    }

    private function listToXML($obj, $wrapper)
    {
        $str = '';
        foreach ($obj as $item) {
            if (Semantria_Common::is_assoc($item)) {
                $str = Semantria_Common::string_format('{0}<{1}>{2}</{1}>', $str, $wrapper["item"], $this->dictToXML($item, $wrapper));
            }
            else
                $str = Semantria_Common::string_format('{0}<{1}>{2}</{1}>', $str, $wrapper["item"], $item);
        }

        return $str;
    }
}

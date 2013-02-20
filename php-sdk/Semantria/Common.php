<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_Common
{
    /**
    * Formats a string with zero-based placeholders
    * {0}, {1} etc corresponding to an array of arguments
    * Must pass in a string and 1 or more arguments
    */
    public static function string_format($str)
    {
        // replaces str "Hello {0}, {1}, {0}" with strings, based on
        // index in array
        $numArgs = func_num_args() - 1;

        if ($numArgs > 0) {
            $arg_list = array_slice(func_get_args(), 1);

            // start after $str
            for ($i=0; $i < $numArgs; $i++) {
                $str = str_replace('{' . $i . '}', $arg_list[$i], $str);
            }
        }

        return $str;
    }

    public static function str2bool($str)
    {
        if (strtolower($str) == "true") {
            return true;
        } else {
            return false;
        }
    }

    public static function is_assoc($a)
    {
        if (is_array($a)) {
            return (array_keys($a) != array_keys(array_keys($a)));
        }
        return false;
    }

    public static function utf8Encode($array)
    {
        $utf8EncodedArray = array();

        foreach ($array as $key => $value) {
            $key = utf8_encode($key);

            if (is_array($value)) {
                $utf8EncodedArray[$key] = self::utf8Encode($value);
                continue;
            }

            // todo: use strict mode for mb_detect_encoding? (3rd arg = true)
            if (is_string($value)
                && mb_detect_encoding($value, 'UTF-8', true) != "UTF-8"
            ) {
                $utf8EncodedArray[$key] = utf8_encode($value);
            } else {
                $utf8EncodedArray[$key] = $value;
            }
        }

        return $utf8EncodedArray;
    }

    public static function utf8Decode($array)
    {
        $utf8DecodeArray = array();

        if (! is_array($array))

        foreach ($array as $key => $value) {
            $key = utf8_decode($key);

            if (is_array($value)) {
                $utf8DecodeArray[$key] = self::utf8Decode($value);
                continue;
            }

            if (is_string($value)) {
                $utf8DecodeArray[$key] = utf8_decode($value);
            } else {
                $utf8DecodeArray[$key] = $value;
            }
        }

        return $utf8DecodeArray;
    }

    public static function processUpdateProxyClonedElement($obj)
    {
        if (@isset($obj["cloned"])) {
            if (is_array($obj["cloned"])) {
                foreach ($obj["cloned"] as $clonedConfig) {
                    $clonedConfig['template'] = $clonedConfig['config_id'];
                    unset($clonedConfig['config_id']);
                    $obj["added"][] = $clonedConfig;
                }
            } elseif (Semantria_Common::is_assoc($obj["cloned"])) {
                $clonedConfig = $obj["cloned"];
                $clonedConfig['template'] = $clonedConfig['config_id'];
                unset($clonedConfig['config_id']);
                $obj["added"][] = $clonedConfig;
            } else {
                throw new Exception('Unsupported object type: '.$obj);
            }

            unset($obj["cloned"]);
        }

        return $obj;
    }
}

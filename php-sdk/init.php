<?php
/**
 * Initialization script
 *
 * @package SemantriaSdk
 */

$paths = array(
    get_include_path(),
    realpath(dirname(dirname(__FILE__))),
);
set_include_path(implode(PATH_SEPARATOR, $paths));

/**
 * Container
 *
 * @package SemantriaSdk
 * @author Jansen Price <jansen.price@nerdery.com>
 * @version $Id$
 */
class Config
{
    /**
     * The consumer key
     *
     * @var string
     */
    public static $consumerKey = '';

    /**
     * The consumer secret
     *
     * @var string
     */
    public static $consumerSecret = '';
}

$configFile = realpath(dirname(__FILE__)) . '/config.php';
if (file_exists($configFile)) {
    include_once $configFile;
}

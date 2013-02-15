<?php

abstract class Semantria_CallbackHandler
{
    abstract public function onRequest($sender, $args);
    abstract public function onResponse($sender, $args);
    abstract public function onError($sender, $args);
    abstract public function onDocsAutoResponse($sender, $args);
    abstract public function onCollsAutoResponse($sender, $args);
}

<?php

interface Semantria_CallbackHandler_Interface
{
    public function onRequest($sender, $args);
    public function onResponse($sender, $args);
    public function onError($sender, $args);
    public function onDocsAutoResponse($sender, $args);
    public function onCollsAutoResponse($sender, $args);
}
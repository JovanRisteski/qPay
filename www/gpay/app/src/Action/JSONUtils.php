<?php

namespace App\Action;

use Psr\Http\Message\StreamInterface;

class JSONUtils
{
    /**
     * @return object|string Object on success, error msg on failure.
     */
    public static function parseAsObj(StreamInterface $body)
    {
        $obj = json_decode($body);
        if (json_last_error() !== JSON_ERROR_NONE) {
            return json_last_error_msg();
        }

        if (!is_object($obj)) {
            return 'json root must be an object';
        }

        return $obj;
    }
}

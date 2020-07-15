<?php

namespace App\Payment;

use Ramsey\Uuid\Exception\InvalidUuidStringException;
use Ramsey\Uuid\Rfc4122\UuidV4;
use Ramsey\Uuid\UuidInterface;

class PaymentID
{
    /**
     * @return UuidInterface|string UuidInterface on success, error msg on failure.
     * @param mixed $uuid
     */
    public static function parse($uuid)
    {
        if (!is_string($uuid)) {
            return 'must be a string';
        }

        try {
            return UuidV4::fromString(trim($uuid));
        } catch (InvalidUuidStringException $e) {
            return $e->getMessage();
        }
    }
}

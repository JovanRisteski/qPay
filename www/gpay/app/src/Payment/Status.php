<?php

namespace App\Payment;

class Status
{
    public const CREATED = 'created';

    public const INITIATED = 'initiated';

    public const ACCEPTED = 'accepted';
    public const CANCELLED = 'cancelled';

    public const APPROVED = 'approved';
    public const DENIED = 'denied';
}

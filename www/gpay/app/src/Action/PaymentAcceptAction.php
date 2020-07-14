<?php

namespace App\Action;

use App\Payment\PaymentService;
use Ramsey\Uuid\Rfc4122\UuidV4;
use Slim\Http\Request;
use Slim\Http\Response;

class PaymentAcceptAction
{
    private $storage;

    public function __construct(PaymentService $storage)
    {
        $this->storage = $storage;
    }

    public function __invoke(Request $request, Response $response)
    {
        // Parse JSON.
        $data = json_decode($request->getBody(), true);
        if (json_last_error() !== JSON_ERROR_NONE) {
            return $response
                ->withStatus(400)
                ->withJson(['code' => 'invalid_json', 'msg' => json_last_error_msg()]);
        }

        // Accept payment.
        $paymentID = UuidV4::fromString($data['payment_id']);
        if (is_string($payment = $this->storage->Accept($paymentID))) {
            return $response->withStatus(400)->withJson(['code' => $payment]);
        }

        return $response->withJson([
            'timestamp' => $payment['modified']
        ]);
    }
}

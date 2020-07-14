<?php

namespace App\Action;

use App\Payment\PaymentService;
use Ramsey\Uuid\Rfc4122\UuidV4;
use Slim\Http\Request;
use Slim\Http\Response;

class PaymentStatusAction
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

        // Read payment.
        $paymentID = UuidV4::fromString($data['payment_id']);
        if (is_null($payment = $this->storage->read($paymentID))) {
            return $response->withStatus(400)->withJson(['code' => 'invalid_payment_id']);
        }

        return $response->withJson([
            'status'   => $payment['status'],
            'modified' => $payment['modified']
        ]);
    }
}

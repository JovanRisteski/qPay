<?php

namespace App\Action;

use App\Payment\PaymentID;
use App\Payment\PaymentService;
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
        if (is_string($json = JSONUtils::parseAsObj($request->getBody()))) {
            return $response
                ->withStatus(400)
                ->withJson(['code' => 'invalid_json', 'msg' => $json]);
        }

        // Read PaymentID.
        if (!property_exists($json, 'payment_id')) {
            return $response
                ->withStatus(400)
                ->withJson(['code' => 'missing_payment_id']);
        }

        // Parse PaymentID.
        if (is_string($paymentID = PaymentID::parse($json->payment_id))) {
            return $response
                ->withStatus(400)
                ->withJson(['code' => 'invalid_payment_id', 'msg' => $paymentID]);
        }

        // Fetch payment.
        if (is_null($payment = $this->storage->read($paymentID))) {
            return $response
                ->withStatus(400)
                ->withJson(['code' => 'payment_not_found']);
        }

        return $response->withJson([
            'status'   => $payment['status'],
            'modified' => $payment['modified']
        ])->withHeader('Access-Control-Allow-Origin', '*');
    }
}

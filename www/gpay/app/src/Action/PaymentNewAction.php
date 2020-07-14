<?php

namespace App\Action;

use App\Payment\Status;
use App\Payment\PaymentService;
use Endroid\QrCode\QrCode;
use Ramsey\Uuid\Uuid;
use Slim\Http\Request;
use Slim\Http\Response;

class PaymentNewAction
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

        // Create new payment.
        $payment_id = Uuid::uuid4();

        $data['status'] = Status::CREATED;
        $data['uuid'] = $payment_id->toString();
        $data['details'] = json_encode($data['details']);
        $this->storage->new($data);

        // Generate QR code.
        $qrCode = new QrCode($payment_id->toString());
        $qrCode->setWriterByName('png');
        $qrCode->setEncoding('UTF-8');
        $qrCode->setSize(150); // In pixels, default is 300.
        $qrCode->setMargin(5); // Whitespace around code, default is 10.

        return $response->withJson([
            'qr_code'    => base64_encode($qrCode->writeString()),
            'payment_id' => $payment_id->toString(),
        ]);
    }
}

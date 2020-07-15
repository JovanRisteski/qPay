<?php

namespace App\Payment;

use Ramsey\Uuid\Uuid;

class PaymentService
{
    private $pdo;

    public function __construct(\PDO $driver)
    {
        $this->pdo = $driver;
    }

    public function new(array $data): void
    {
        $sql =
            "INSERT INTO `payment` (
                `uuid`,
                `status`,
                `reference_number`,
                `details`,
                `amount`
            ) VALUES (?, ?, ?, ?, ?);";

        $statement = $this->pdo->prepare($sql);
        $statement->execute([
            $data['uuid'],
            $data['status'],
            $data['reference_number'] ?? null,
            $data['details'] ?? null,
            $data['amount']
        ]);
    }

    /**
     * @return array|string Payment data on success, err msg on failure.
     */
    public function init(Uuid $paymentID)
    {
        // Read payment.
        $this->pdo->beginTransaction();
        $stmt = $this->pdo->prepare(
            "SELECT * FROM `payment` WHERE `uuid` = ? FOR UPDATE;"
        );

        $stmt->execute([$paymentID->toString()]);
        $payment = $stmt->fetch();

        // Payment not found.
        if (empty($payment)) {
            $this->pdo->commit();
            return 'payment_not_found';
        }

        // Payment has bad status.
        if ($payment['status'] !== Status::CREATED) {
            $this->pdo->commit();
            return 'conflict_payment_status_' . $payment['status'];
        }

        // Update status to INITIATED.
        $stmt = $this->pdo->prepare(
            "UPDATE `payment`
            SET `status` = ?, `modified` = NOW()
            WHERE `uuid` = ?;"
        );

        $stmt->execute([Status::INITIATED, $paymentID->toString()]);
        $this->pdo->commit();

        return [
            'payment_id' => $payment['uuid'],
            'merchant'   => [
                'name'     => 'Demo Shop',
                'category' => 'Retailer',
            ],
            'reference_number' => $payment['reference_number'],
            'details'          => json_decode($payment['details']),
            'amount'           => $payment['amount'],
        ];
    }

    /**
     * @return array|string Payment data on success, err msg on failure.
     */
    public function cancel(Uuid $paymentID)
    {
        // Read payment.
        $this->pdo->beginTransaction();
        $stmt = $this->pdo->prepare(
            "SELECT * FROM `payment` WHERE `uuid` = ? FOR UPDATE;"
        );

        $stmt->execute([$paymentID->toString()]);
        $payment = $stmt->fetch();

        // Payment not found.
        if (empty($payment)) {
            $this->pdo->commit();
            return 'payment_not_found';
        }

        // Payment has bad status.
        if (!in_array($payment['status'], [Status::CREATED, Status::INITIATED, Status::CANCELLED])) {
            $this->pdo->commit();
            return 'conflict_payment_status_' . $payment['status'];
        }

        // Payment already cancelled, don't update status.
        if ($payment['status'] === Status::CANCELLED) {
            $this->pdo->commit();
            return $payment;
        }

        // Update status to CANCELLED.
        $timestamp = date('Y-m-d H:i:s');
        $stmt = $this->pdo->prepare(
            "UPDATE `payment`
            SET `status` = ?, `modified` = '$timestamp'
            WHERE `uuid` = ?;"
        );

        $stmt->execute([Status::CANCELLED, $paymentID->toString()]);
        $this->pdo->commit();

        return array_merge($payment, ['status' => Status::CANCELLED, 'modified' => $timestamp]);
    }

    /**
     * @return array|string Payment data on success, err msg on failure.
     */
    public function accept(Uuid $paymentID)
    {
        // Read payment.
        $this->pdo->beginTransaction();
        $stmt = $this->pdo->prepare(
            "SELECT * FROM `payment` WHERE `uuid` = ? FOR UPDATE;"
        );

        $stmt->execute([$paymentID->toString()]);
        $payment = $stmt->fetch();

        // Payment not found.
        if (empty($payment)) {
            $this->pdo->commit();
            return 'payment_not_found';
        }

        // Payment has bad status.
        if ($payment['status'] !== Status::INITIATED) {
            $this->pdo->commit();
            return 'conflict_payment_status_' . $payment['status'];
        }

        // Update status to APPROVED (for demo only, otherwise ACCEPTED).
        $timestamp = date('Y-m-d H:i:s');
        $stmt = $this->pdo->prepare(
            "UPDATE `payment`
            SET `status` = ?, `modified` = '$timestamp'
            WHERE `uuid` = ?;"
        );

        $stmt->execute([Status::APPROVED, $paymentID->toString()]);
        $this->pdo->commit();

        return array_merge($payment, ['status' => Status::APPROVED, 'modified' => $timestamp]);
    }

    /**
     * @return array|null Payment data on success, null if not found.
     */
    public function read(Uuid $paymentID): ?array
    {
        $stmt = $this->pdo->prepare(
            "SELECT * FROM `payment` WHERE `uuid` = ?;"
        );

        $stmt->execute([$paymentID->toString()]);
        $payment = $stmt->fetch();
        return $payment ? $payment : null;
    }
}

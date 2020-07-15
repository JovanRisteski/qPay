<?php

use App\Action\PaymentAcceptAction;
use App\Action\PaymentCancelAction;
use App\Action\PaymentInitAction;
use App\Action\PaymentNewAction;
use App\Action\PaymentStatusAction;
use App\Action\HomeAction;

// Routes
$app->post('/new', PaymentNewAction::class);

$app->post('/init', PaymentInitAction::class);

$app->post('/accept', PaymentAcceptAction::class);

$app->post('/cancel', PaymentCancelAction::class);

$app->post('/status', PaymentStatusAction::class);

// Test.
$app->get('/', HomeAction::class)
    ->setName('test');

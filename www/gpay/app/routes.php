<?php

use App\Action\PaymentAcceptAction;
use App\Action\PaymentCancelAction;
use App\Action\PaymentInitAction;
use App\Action\PaymentNewAction;
use App\Action\PaymentStatusAction;
use App\Action\HomeAction;

// Routes
$app->get('/new', PaymentNewAction::class);

$app->get('/init', PaymentInitAction::class);

$app->get('/accept', PaymentAcceptAction::class);

$app->get('/cancel', PaymentCancelAction::class);

$app->get('/status', PaymentStatusAction::class);

// Test.
$app->get('/', HomeAction::class)
    ->setName('test');

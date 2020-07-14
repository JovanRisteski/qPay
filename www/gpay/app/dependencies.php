<?php
// DIC configuration

use App\Action\PaymentAcceptAction;
use App\Action\PaymentCancelAction;
use App\Action\PaymentInitAction;
use App\Action\PaymentNewAction;
use App\Action\PaymentStatusAction;
use App\Payment\PaymentService;

$container = $app->getContainer();

// -----------------------------------------------------------------------------
// Service providers
// -----------------------------------------------------------------------------

// Flash messages
$container['flash'] = function ($c) {
    return new Slim\Flash\Messages;
};

// -----------------------------------------------------------------------------
// Service factories
// -----------------------------------------------------------------------------

// monolog
$container['logger'] = function ($c) {
    $settings = $c->get('settings');
    $logger = new Monolog\Logger($settings['logger']['name']);
    $logger->pushProcessor(new Monolog\Processor\UidProcessor());
    $logger->pushHandler(new Monolog\Handler\StreamHandler($settings['logger']['path'], Monolog\Logger::DEBUG));
    return $logger;
};

// PDO.
$container[\PDO::class] = function (\Slim\Container $c) {
    $config = $c->get('settings')['mysql'];

    $dsn = sprintf(
        'mysql:host=%s;dbname=%s;port=%s;charset=%s',
        $config['host'],
        $config['dbname'],
        $config['port'],
        $config['charset'],
    );

    $options = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    ];

    return new \PDO($dsn, $config['user'], $config['password'], $options);
};

// PaymentService.
$container[PaymentService::class] = function (\Slim\Container $c) {
    return new PaymentService($c[\PDO::class]);
};

// -----------------------------------------------------------------------------
// Action factories
// -----------------------------------------------------------------------------

$container[PaymentNewAction::class] = function (\Slim\Container $c) {
    return new PaymentNewAction($c[PaymentService::class]);
};

$container[PaymentInitAction::class] = function (\Slim\Container $c) {
    return new PaymentInitAction($c[PaymentService::class]);
};

$container[PaymentCancelAction::class] = function (\Slim\Container $c) {
    return new PaymentCancelAction($c[PaymentService::class]);
};

$container[PaymentAcceptAction::class] = function (\Slim\Container $c) {
    return new PaymentAcceptAction($c[PaymentService::class]);
};

$container[PaymentStatusAction::class] = function (\Slim\Container $c) {
    return new PaymentStatusAction($c[PaymentService::class]);
};

$container[App\Action\HomeAction::class] = function ($c) {
    return new App\Action\HomeAction($c->get('logger'));
};

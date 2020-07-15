<?php
// Application middleware

use App\Middleware\CORSMiddleware;

$app->add(CORSMiddleware::class);

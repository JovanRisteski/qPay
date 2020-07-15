<?php

namespace App\Middleware;

use Slim\Http\Request;
use Slim\Http\Response;

class CORSMiddleware
{
    public function __invoke(Request $request, Response $response, callable $next)
    {
        /**
         * @var Response $response
         */
        $response = $next($request, $response);
        return $response->withHeader('Access-Control-Allow-Origin', '*');
    }
}

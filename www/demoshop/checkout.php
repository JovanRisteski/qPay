<?php
$p1 = $_POST['1'];
$p2 = $_POST['2'];
$p3 = $_POST['3'];
$total = $p1 * 10 + $p2 * 20 + $p3 * 50;
$prices = [
    1 => 10,
    2 => 20,
    3 => 50
];
?>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">

    <title>Demo Shop</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/album/">

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="assets/css/shop.css">
  </head>

  <body>

    <form id="checkout_form" action="checkout.php" method="post">
    <header>
      <div class="collapse bg-dark" id="navbarHeader">
        <div class="container">
          <div class="row">
            <div class="col-sm-8 col-md-7 py-4">
              <h4 class="text-white"> </h4>
            </div>
            <div class="col-sm-4 offset-md-1 py-4">
              <h4 class="text-white">Cart</h4>
              <ul class="list-unstyled">
                <li class="text-white">Product 1 x <?php echo $p1;?></li>
                <li class="text-white">Product 2 x <?php echo $p2;?></li>
                <li class="text-white">Product 3 x <?php echo $p3;?></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="navbar navbar-dark bg-dark box-shadow">
        <div class="container d-flex justify-content-between">
          <a href="/" class="navbar-brand d-flex align-items-center">
            <strong>Demo Shop</strong>
          </a>
          <div class="d-flex justify-content-between">
            <div class="text-white navbar-brand d-flex align-items-center" id="total">Total: <?php echo $total;?></div>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarHeader" aria-controls="navbarHeader" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <button id="checkout" onclick="document.getElementById('checkout_form').submit()">
                <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-cart2" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path fill-rule="evenodd" d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5zM3.14 5l1.25 5h8.22l1.25-5H3.14zM5 13a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0zm9-1a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0z"/>
                </svg>
            </button>
          </div>
        </div>
      </div>
    </header>
    </form>

    <main role="main">

      <section class="text-center">
        <div class="container">
          <h1 class="jumbotron-heading">Demo Shop Checkout</h1>
        </div>
      </section>

      <div class="album py-5 bg-light">
        <div class="container text-center">
        <table class="table">
            <thead>
                <tr>
                    <th>
                        Product name
                    </th>
                    <th>
                        Price
                    </th>
                    <th>
                        Amount
                    </th>
                    <th>
                        Total
                    </th>
                </tr>
            </thead>
            <?php
            for ($i = 1; $i <= 3; $i++) {
                if ($_POST[$i] > 0) {
                    echo "<tr>";
                    echo "<td>Product ".$i."</td>";
                    echo "<td>".$prices[$i]."</td>";
                    echo "<td>".$_POST[$i]."</td>";
                    echo "<td>".$_POST[$i] * $prices[$i]."</td>";
                    echo "</tr>";
                }
            }
            ?>
                <tr>
                    <td>

                    </td>
                    <td>

                    </td>
                    <td>
                        Total
                    </td>
                    <td>
                        <?php echo $total; ?>
                    </td>
                </tr>
            </table>
            <div class="container" >
                <img id="QR" class="d-none" style="height: 250px;" src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/QR_code_for_mobile_English_Wikipedia.svg/1200px-QR_code_for_mobile_English_Wikipedia.svg.png">
            </div>
            <div id="instructions" class="container d-none">
                Scan the code with your gPay app
            </div>
            <div class="container">
                <button id="initPayment" class="btn btn-success" onclick="initiatePayment()">Pay with gPay</button>
                <button id="cancelPayment" class="btn btn-danger d-none" onclick="cancelPayment()">Cancel</button>
            </div>
        </div>
      </div>

    </main>



    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')</script>
    <script src="https://unpkg.com/@popperjs/core@2"></script>
    <script src="../../assets/js/vendor/bootstrap.min.js"></script>
    <script src="../../assets/js/vendor/holder.min.js"></script>
    <script src="../../assets/js/shop.js"></script>


<svg xmlns="http://www.w3.org/2000/svg" width="348" height="225" viewBox="0 0 348 225" preserveAspectRatio="none" style="display: none; visibility: hidden; position: absolute; top: -100%; left: -100%;"><defs><style type="text/css"></style></defs><text x="0" y="17" style="font-weight:bold;font-size:17pt;font-family:Arial, Helvetica, Open Sans, sans-serif">Thumbnail</text></svg></body></html>
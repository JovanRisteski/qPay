package mk.grabit.gpay.ui.barcode

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.google.zxing.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.transaction_error_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dm7.barcodescanner.zxing.ZXingScannerView
import mk.grabit.gpay.R
import mk.grabit.gpay.databinding.FragmentBarcodeBinding
import mk.grabit.gpay.util.ErrorCode
import mk.grabit.gpay.util.RequestCode
import mk.grabit.gpay.util.Resource

@AndroidEntryPoint
class BarcodeFragment : Fragment(R.layout.fragment_barcode), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    private var binding: FragmentBarcodeBinding? = null
    private val viewModel: BarcodeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
        mScannerView = binding?.surfaceView

        observeUi()
    }

    private fun observeUi() {
        viewModel.transactionAction.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.surfaceView?.visibility = View.VISIBLE
                    sharedViewModel.setTransaction(it.data!!)
                    viewModel.acknowledgeTransactionActionStatus()
                    navigateToPaymentFragment()
                }
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.surfaceView?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.surfaceView?.visibility = View.VISIBLE
                    showErrorDialog(it.message!!)

                }
                is Resource.None -> {

                }
            }
        })
    }

    private fun showErrorDialog(error: String) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.transaction_error_dialog, null)

        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
            setCancelable(false)
        }.create()

        dialogView.apply {
            message.text = when (error) {
                ErrorCode.CONNECTION_ERROR -> "Connection Error"
                ErrorCode.NETWORK_ERROR -> "Network Error"
                ErrorCode.INVALID_PAYMENT_ID -> "Invalid payment id"
                ErrorCode.PAYMENT_CANCELED -> "Payment is already canceled"
                ErrorCode.PAYMENT_NOT_FOUND -> "Payment was not found in our qPay database"
                else -> "An error occured"
            }
            try_again_button.setOnClickListener {
                viewModel.acknowledgeTransactionActionStatus()
                alertDialog.dismiss()
            }

            dialogView.cancel_button.setOnClickListener {
                viewModel.acknowledgeTransactionActionStatus()
                alertDialog.dismiss()
                navigateToDashboard()
            }
        }
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                RequestCode.CAMERA
            )
        } else {
            mScannerView?.startCamera()          // Start camera on resume
            mScannerView?.setResultHandler(this) // Register ourselves as a handler for scan results.
        }
    }

    override fun handleResult(rawResult: Result) {
        // Resume scanning
        mScannerView?.resumeCameraPreview(this);

        if (viewModel.isReady.value!!) {
            viewModel.pauseScanning(1500)
            updateBarcode(rawResult.text)
        }
    }

    private fun updateBarcode(barcode: String) {
        binding?.barcodeTextView?.text = barcode
        viewModel.initPayment(barcode)
    }

    private fun navigateToPaymentFragment() {
        val action = BarcodeFragmentDirections.actionBarcodeFragmentToPaymentFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun navigateToDashboard() {
        val action = BarcodeFragmentDirections.actionBarcodeFragmentToHomeFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RequestCode.CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mScannerView?.startCamera()          // Start camera on resume
                    mScannerView?.setResultHandler(this) // Register ourselves as a handler for scan results.
                } else {
                    Toast.makeText(requireContext(), "No camera permission", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}
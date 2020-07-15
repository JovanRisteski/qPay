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
import kotlinx.android.synthetic.main.proceed_transaction_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dm7.barcodescanner.zxing.ZXingScannerView
import mk.grabit.gpay.R
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.databinding.FragmentBarcodeBinding
import mk.grabit.gpay.util.RequestCode
import mk.grabit.gpay.util.Resource

@AndroidEntryPoint
class BarcodeFragment : Fragment(R.layout.fragment_barcode), ZXingScannerView.ResultHandler {

    private var isReady = true
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
                    showProceedDialog(it.data!!)
                }
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.surfaceView?.visibility = View.GONE
                    // TODO show progress dialog, transaction in progress
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    // TODO Show transaction failed
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    viewModel.acknowledgeTransactionActionStatus()

                }
                is Resource.None -> {

                }
            }
        })
    }

    private fun showProceedDialog(transaction: Transaction) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.proceed_transaction_dialog, null)

        val alertDialog = AlertDialog.Builder(context).apply {
            setView(dialogView)
            setCancelable(false)
        }.create()

        dialogView.apply {
            merchant.text = getString(R.string.merchant, transaction.merchant.name)
            amount.text = getString(R.string.amount, transaction.amount.toString())
            proceed_button.setOnClickListener {
                sharedViewModel.setTransaction(transaction)
                navigateToPaymentFragment(transaction)
                viewModel.acknowledgeTransactionActionStatus()
                alertDialog.dismiss()
            }
            dialogView.cancel_button.setOnClickListener {
                viewModel.cancelTransaction(transaction)
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

    private fun pauseScanning(milliseconds: Long) =
        GlobalScope.launch(Dispatchers.Main) {
            delay(milliseconds)
            isReady = true
        }

    override fun handleResult(rawResult: Result) {
        // Do something with the result here
        if (isReady) {
            isReady = false
            pauseScanning(1500)
            updateBarcode(rawResult.text)
        }
        // Resume scanning
        mScannerView?.resumeCameraPreview(this);
    }

    private fun updateBarcode(barcode: String) {
        binding?.barcodeTextView?.text = barcode
        pauseScanning(1500)
        viewModel.initPayment(barcode)
    }

    private fun navigateToPaymentFragment(transaction: Transaction) {
        val action = BarcodeFragmentDirections.actionBarcodeFragmentToPaymentFragment()
        NavHostFragment.findNavController(this).navigate(action)
        //TODO add transaction to shared view model
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
                if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
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
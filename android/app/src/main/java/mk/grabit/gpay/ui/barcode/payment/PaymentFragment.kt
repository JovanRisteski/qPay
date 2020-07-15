package mk.grabit.gpay.ui.barcode.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.transaction_response_dialog.view.*
import mk.grabit.gpay.R
import mk.grabit.gpay.data.model.CreditCard
import mk.grabit.gpay.databinding.FragmentPaymentBinding
import mk.grabit.gpay.ui.barcode.SharedViewModel
import mk.grabit.gpay.ui.creditcards.CreditCardAdapter
import mk.grabit.gpay.util.Data
import mk.grabit.gpay.util.Resource

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val viewModel: PaymentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var binding: FragmentPaymentBinding? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private var card: CreditCard = Data.CARDS[0]
    private var alertDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)

        bottomSheetBehavior =
            BottomSheetBehavior.from<NestedScrollView>(binding?.cardListLayout!!)

        binding?.cardLayout?.setOnClickListener {
            viewModel.shouldShowCardChooser(true)
        }

        val adapter = CreditCardAdapter {
            viewModel.setCreditCard(it)
            viewModel.shouldShowCardChooser(false)
        }

        binding?.cardList?.adapter = adapter
        binding?.cardList?.setHasFixedSize(true)
        adapter.submitList(Data.CARDS)

        binding?.transaction = sharedViewModel.transaction.value
        binding?.executePendingBindings()

        observeUi()

        binding?.approveButton?.setOnClickListener {
            viewModel.approveTransaction(sharedViewModel.transaction.value!!)
        }

        binding?.cancelButton?.setOnClickListener {
            viewModel.cancelTransaction(sharedViewModel.transaction.value!!)
        }
    }

    private fun observeUi() {
        viewModel.creditCard.observe(viewLifecycleOwner, Observer {
            setCreditCard(it)
        })

        viewModel.showCardChooser.observe(viewLifecycleOwner, Observer {
            if (it) showCreditCards() else hideCreditCards()
        })

        viewModel.transactionApproveStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    showResponseDialog(true)
                }
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.buttonsGroup?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.buttonsGroup?.visibility = View.GONE
                    showResponseDialog(false)
                }

                is Resource.None -> {

                }
            }
        })

        viewModel.transactionCancelStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    showResponseDialog(false)
                }
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.buttonsGroup?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.buttonsGroup?.visibility = View.GONE
                    showResponseDialog(false)
                }

                is Resource.None -> {

                }
            }
        })

        viewModel.navigateToLoginDelay.observe(viewLifecycleOwner, Observer {
            if (it) {
                alertDialog?.dismiss()

                //TODO check if acknowledgeStatus is needed, same thing is done in PaymentViewModel
                viewModel.acknowledgeTransactionApproveStatusNone()
                viewModel.acknowledgeTransactionCancelStatusNone()
                navigateToDashboard()
            }
        })
    }

    private fun showCreditCards() {
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding?.cardListLayout?.visibility = View.VISIBLE
    }

    private fun hideCreditCards() {
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding?.cardListLayout?.visibility = View.GONE
    }

    private fun setCreditCard(card: CreditCard) {
        this.card = card
        binding?.cardImageView?.setImageResource(card.image)
        binding?.cardNumber?.text = getString(R.string.credit_card_number, card.lastDigits)
        binding?.cardType?.text = card.manufacturer
    }

    private fun showResponseDialog(isSuccessful: Boolean) {
        val dialogView =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.transaction_response_dialog, null)

        alertDialog = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
            setCancelable(false)
        }.create()

        dialogView.transaction_image_view?.setImageResource(if (isSuccessful) R.drawable.transaction_success else R.drawable.transaction_error)
        dialogView.transaction_status?.text =
            if (isSuccessful) getString(R.string.payment_completed_successfully)
            else getString(R.string.payment_completed_error)
        dialogView.transaction_message?.text =
            if (isSuccessful)
                getString(R.string.success_transaction, card.manufacturer, card.lastDigits)
            else
                getString(R.string.error_transaction)
        dialogView.label_transaction_info?.visibility =
            if (isSuccessful) View.VISIBLE else View.GONE
        alertDialog?.show()
        viewModel.delayDialog(10000)
    }

    private fun navigateToDashboard() {
        val action = PaymentFragmentDirections.actionPaymentFragmentToHomeFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }
}
package mk.grabit.gpay.ui.barcode.payment

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
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

        viewModel.transactionStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                }

                is Resource.None -> {

                }
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
        binding?.cardImageView?.setImageResource(card.image)
        binding?.cardNumber?.text = getString(R.string.credit_card_number, card.lastDigits)
        binding?.cardType?.text = card.manufacturer
    }
}
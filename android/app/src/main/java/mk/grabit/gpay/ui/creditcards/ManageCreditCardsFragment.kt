package mk.grabit.gpay.ui.creditcards

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import mk.grabit.gpay.R
import mk.grabit.gpay.data.model.CreditCard
import mk.grabit.gpay.databinding.FragmentManageCreditCardsBinding
import mk.grabit.gpay.util.CreditCardType

class ManageCreditCardsFragment : Fragment(R.layout.fragment_manage_credit_cards) {

    private lateinit var viewModel: ManageCreditCardsFragmentViewModel
    private var binding: FragmentManageCreditCardsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
        viewModel =
            ViewModelProviders.of(this).get(ManageCreditCardsFragmentViewModel::class.java)


        val cards = arrayListOf(
            CreditCard(1, "09/25", "John Doe", "Mastercard", CreditCardType.CREDIT),
            CreditCard(2, "01/22", "John Doe", "Visa", CreditCardType.DEBIT)
        )
        val cardsAdapter = CreditCardAdapter {
            Toast.makeText(requireContext(), it.id.toString(), Toast.LENGTH_SHORT).show()
        }

        binding?.creditCardsRecyclerView?.adapter = cardsAdapter

        cardsAdapter.submitList(cards)
    }
}
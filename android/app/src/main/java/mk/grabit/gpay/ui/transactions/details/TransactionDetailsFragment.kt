package mk.grabit.gpay.ui.transactions.details

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import mk.grabit.gpay.R
import mk.grabit.gpay.databinding.FragmentTransactionDetailsBinding
import mk.grabit.gpay.databinding.FragmentTransactionsBinding

class TransactionDetailsFragment : Fragment(R.layout.fragment_transaction_details) {

    private lateinit var transactionDetailsViewModel: TransactionDetailsViewModel
    private var binding: FragmentTransactionDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
        transactionDetailsViewModel =
            ViewModelProviders.of(this).get(TransactionDetailsViewModel::class.java)
    }

}
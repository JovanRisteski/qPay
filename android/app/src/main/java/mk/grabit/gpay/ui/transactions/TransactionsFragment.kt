package mk.grabit.gpay.ui.transactions

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import mk.grabit.gpay.R
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.databinding.FragmentTransactionsBinding
import mk.grabit.gpay.ui.home.TransactionAdapter
import mk.grabit.gpay.util.TransactionType

class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private lateinit var transactionsViewModel: TransactionsViewModel
    private var binding: FragmentTransactionsBinding? = null

    private val transactions = arrayListOf(
        Transaction(
            1,
            "#9asf2HK5q2",
            840.00f,
            "Tinex MKD",
            "Shopping",
            TransactionType.OUTCOME
        ),
        Transaction(
            2,
            "#9mxa0rt78",
            267.00f,
            "Burger King",
            "Food & Drink",
            TransactionType.OUTCOME
        ),
        Transaction(
            3,
            "#80fa-uymaf42",
            1299.00f,
            "SportVision MK",
            "Clothes",
            TransactionType.OUTCOME
        ),
        Transaction(
            4,
            "#cn059@K2I",
            45.00f,
            "Ramstore Mall Centar",
            "Shopping",
            TransactionType.OUTCOME
        ),
        Transaction(
            5,
            "#p9eMFk=90s",
            990.00f,
            "Jumbo VERO",
            "Toys & Groceries",
            TransactionType.OUTCOME
        ),
        Transaction(
            6,
            "#0a8fn-FY2",
            4899.99f,
            "Timberland Ramstore Mall",
            "Clothes",
            TransactionType.OUTCOME
        ),
        Transaction(
            7,
            "#1jaFJs-12",
            2480.00f,
            "Bet365",
            "Bet & Win",
            TransactionType.INCOME
        ),
        Transaction(
            7,
            "#jkaJKRAHs-23",
            250.00f,
            "Spizzicato Ramstore Mall",
            "Food",
            TransactionType.OUTCOME
        ),
        Transaction(
            6,
            "#0a8fn-FY2",
            380.00f,
            "Prosvetno Delo",
            "Books",
            TransactionType.OUTCOME
        )
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
        transactionsViewModel =
            ViewModelProviders.of(this).get(TransactionsViewModel::class.java)

        val transactionAdapter = TransactionAdapter {
            showTransactionDetails(id)
        }

        binding?.transactionsRecyclerView?.adapter = transactionAdapter

        transactionAdapter.submitList(transactions)
    }

    private fun showTransactionDetails(id: Int) {
        val action =
            TransactionsFragmentDirections.actionTransactionsFragmentToTransactionDetailsFragment(id)
        NavHostFragment.findNavController(this).navigate(action)
    }

}
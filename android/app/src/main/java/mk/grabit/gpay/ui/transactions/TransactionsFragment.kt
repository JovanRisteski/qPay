package mk.grabit.gpay.ui.transactions

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import mk.grabit.gpay.R
import mk.grabit.gpay.databinding.FragmentTransactionsBinding
import mk.grabit.gpay.ui.home.TransactionAdapter

@AndroidEntryPoint
class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val viewModel: TransactionsViewModel by viewModels()
    private var binding: FragmentTransactionsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)

        val transactionAdapter = TransactionAdapter {
            showTransactionDetails(id)
        }

        binding?.transactionsRecyclerView?.adapter = transactionAdapter

        observeUi(transactionAdapter)
    }

    private fun observeUi(transactionAdapter: TransactionAdapter) {
        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            transactionAdapter.submitList(it) {
                viewModel.setIsLoading(false)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding?.progressBar?.visibility = View.VISIBLE
                binding?.transactionsRecyclerView?.visibility = View.GONE
            } else {
                binding?.progressBar?.visibility = View.GONE
                binding?.transactionsRecyclerView?.visibility = View.VISIBLE
            }
        })
    }

    private fun showTransactionDetails(id: Int) {
        val action =
            TransactionsFragmentDirections.actionTransactionsFragmentToTransactionDetailsFragment(id)
        NavHostFragment.findNavController(this).navigate(action)
    }

}

/*
                when (it) {
            is Resource.Success -> {
                transactionAdapter.submitList(it.data) {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.transactionsRecyclerView?.visibility = View.VISIBLE
                }
            }
            is Resource.Loading -> {
                binding?.transactionsRecyclerView?.visibility = View.GONE
                binding?.progressBar?.visibility = View.VISIBLE
            }
            is Resource.Error -> {
                binding?.progressBar?.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Error loading transactions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

 */
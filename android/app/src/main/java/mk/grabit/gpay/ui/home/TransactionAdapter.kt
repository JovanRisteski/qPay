package mk.grabit.gpay.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.databinding.TransactionItemBinding

class TransactionAdapter(
    val clickListener: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TransactionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.apply {
            bind(createOnClickListener(transaction), transaction)
            itemView.tag = transaction
        }
    }

    private fun createOnClickListener(transaction: Transaction): View.OnClickListener {
        return View.OnClickListener { clickListener(transaction) }
    }

    class ViewHolder(private val binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            listener: View.OnClickListener,
            item: Transaction
        ) {
            binding.apply {
                clickListener = listener
                transaction = item
                executePendingBindings()
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<Transaction>() {

    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.paymentId == newItem.paymentId
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}
package mk.grabit.gpay.ui.creditcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mk.grabit.gpay.data.model.CreditCard
import mk.grabit.gpay.databinding.CreditCardItemBinding

class CreditCardAdapter(
    val clickListener: (CreditCard) -> Unit
) : ListAdapter<CreditCard, CreditCardAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CreditCardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = getItem(position)
        holder.apply {
            bind(createOnClickListener(card), card)
            itemView.tag = card
        }
    }

    private fun createOnClickListener(card: CreditCard): View.OnClickListener {
        return View.OnClickListener { clickListener(card) }
    }

    class ViewHolder(private val binding: CreditCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            listener: View.OnClickListener,
            item: CreditCard
        ) {
            binding.apply {
                clickListener = listener
                card = item
                executePendingBindings()
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<CreditCard>() {

    override fun areItemsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CreditCard, newItem: CreditCard): Boolean {
        return oldItem == newItem
    }
}
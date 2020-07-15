package mk.grabit.gpay.ui.barcode.payment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.model.CreditCard
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.repository.MainRepository
import mk.grabit.gpay.util.Data

class PaymentViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    val transactionStatus = repository.status

    private val _creditCard = MutableLiveData<CreditCard>()
    val creditCard: LiveData<CreditCard>
        get() = _creditCard

    // Always choose first inserted credit card
    private val _showCardChooser = MutableLiveData<Boolean>()
    val showCardChooser: LiveData<Boolean>
        get() = _showCardChooser

    init {
        // Always choose the first inserted credit card
        _creditCard.value = Data.CARDS[0]
    }

    fun shouldShowCardChooser(show: Boolean) {
        _showCardChooser.value = show
    }

    fun setCreditCard(card: CreditCard) {
        _creditCard.value = card
    }

    fun approveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.acceptTransaction(transaction)
        }
    }

    fun cancelTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.cancelTransaction(transaction)
        }
    }
}
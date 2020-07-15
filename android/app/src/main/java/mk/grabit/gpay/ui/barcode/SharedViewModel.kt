package mk.grabit.gpay.ui.barcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mk.grabit.gpay.data.model.Transaction

class SharedViewModel : ViewModel() {

    private val _transaction = MutableLiveData<Transaction>()
    val transaction: LiveData<Transaction>
        get() = _transaction

    fun setTransaction(transaction: Transaction) {
        _transaction.value = transaction
    }

    override fun onCleared() {
        println("SHARED VIEW MODEL CLEARED")
        super.onCleared()

    }
}
package mk.grabit.gpay.ui.transactions

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mk.grabit.gpay.data.repository.MainRepository

class TransactionsViewModel @ViewModelInject constructor(
    repository: MainRepository
) : ViewModel() {


    private val _isLoading = MutableLiveData<Boolean>().apply { false }
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val transactions = repository.transactions

    fun setIsLoading(loadingStatus: Boolean) {
        _isLoading.value = loadingStatus
    }
}
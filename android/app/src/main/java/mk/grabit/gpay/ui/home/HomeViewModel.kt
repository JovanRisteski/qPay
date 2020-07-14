package mk.grabit.gpay.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.repository.MainRepository
import mk.grabit.gpay.util.Resource

class HomeViewModel @ViewModelInject constructor(
    repository: MainRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val transactions = repository.lastTransactions

    fun setIsLoading(loadingStatus: Boolean) {
        _isLoading.value = loadingStatus
    }
}
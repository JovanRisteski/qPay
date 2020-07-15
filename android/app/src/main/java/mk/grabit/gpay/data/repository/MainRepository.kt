package mk.grabit.gpay.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.model.TransactionResponse
import mk.grabit.gpay.db.TransactionDao
import mk.grabit.gpay.networking.GPayService
import mk.grabit.gpay.util.Data
import mk.grabit.gpay.util.Resource
import java.net.ConnectException
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val gPayService: GPayService
) {

    val transactions = transactionDao.getAllTransactions()
    val lastTransactions = transactionDao.getLastTransactions()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _status = MutableLiveData<Resource<TransactionResponse>>()
    val status: LiveData<Resource<TransactionResponse>>
        get() = _status

    private val _transactionStatus = MutableLiveData<Resource<TransactionResponse>>()
    val transactionStatus: LiveData<Resource<TransactionResponse>>
        get() = _transactionStatus

    private var _transactionAction = MutableLiveData<Resource<Transaction>>()
    val transactionAction: LiveData<Resource<Transaction>>
        get() = _transactionAction

    init {
        // Insert initial values
        GlobalScope.launch {
            transactionDao.insertAll(Data.INITIAL_TRANSACTIONS)
        }
    }

    suspend fun initTransaction(paymentId: String) {
        _transactionAction.postValue(Resource.Loading())
        coroutineScope {
            delay(1000)
//            _transactionAction.postValue(Resource.Success(Data.INITIAL_TRANSACTIONS[0]))
            try {
                val res = gPayService.initTransaction(paymentId)
                when {
                    res.isSuccessful -> {
                        val transaction = res.body().also { it?.paymentId = paymentId }
                        _transactionAction.postValue(Resource.Success(transaction!!))
                    }
                    else -> _transactionAction.postValue(Resource.Error("error"))
                }
            } catch (e: Exception) {
                checkException(e)
            }
        }
    }

    suspend fun acceptTransaction(transaction: Transaction) {
        _status.value = Resource.Loading()
        try {
            coroutineScope {
                val res = gPayService.acceptTransaction(transaction.paymentId)
                when {
                    res.isSuccessful -> {
                        val transactionResponse = res.body()!!
                        transaction.apply {
                            // TODO ADD timestamp
                        }
                        _status.postValue(Resource.Success(transactionResponse))
                        addTransaction(transaction)
                    }
                    else -> _status.postValue(Resource.Error("error"))
                }
            }
        } catch (e: Exception) {
            checkException(e)
        }
    }

    suspend fun cancelTransaction(transaction: Transaction) {
        _status.value = Resource.Loading()
        try {
            coroutineScope {
                val res = gPayService.cancelTransaction(transaction.paymentId)
                when {
                    res.isSuccessful -> _status.postValue(Resource.Success(res.body()!!))
                    else -> _status.postValue(Resource.Error("error"))
                }
            }
        } catch (e: Exception) {
            checkException(e)
        }
    }

    suspend fun transactionStatus(paymentId: String) {
        _transactionStatus.value = Resource.Loading()
        try {
            coroutineScope {
                val res = gPayService.cancelTransaction(paymentId)
                when {
                    res.isSuccessful -> _transactionStatus.postValue(Resource.Success(res.body()!!))
                    else -> _transactionStatus.postValue(Resource.Error(("error")))
                }
            }
        } catch (e: Exception) {
            checkException(e)
        }
    }

    private suspend fun addTransaction(transaction: Transaction) {
        coroutineScope {
            transactionDao.insertTransaction(transaction)
        }
    }

    suspend fun removeTransaction(transaction: Transaction) {
        coroutineScope {
            transactionDao.deleteTransaction(transaction)
        }
    }

    private fun checkException(e: Exception) {
        e.printStackTrace()
        when (e) {
            is ConnectException -> {
                _error.postValue("ConnectException")
            }
            else -> {
                _error.postValue("error")
            }
        }

        //TODO Observe error in each fragment
    }

    fun acknowledgeTransactionActionNone() {
        _transactionAction.value = Resource.None()
    }

    fun acknowledgeTransactionStatusNone() {
        _transactionStatus.value = Resource.None()
    }
}
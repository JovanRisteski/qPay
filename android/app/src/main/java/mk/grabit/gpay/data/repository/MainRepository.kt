package mk.grabit.gpay.data.repository

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.model.TransactionResponse
import mk.grabit.gpay.db.TransactionDao
import mk.grabit.gpay.networking.GPayService
import mk.grabit.gpay.util.Data
import mk.grabit.gpay.util.ErrorCode
import mk.grabit.gpay.util.Helper
import mk.grabit.gpay.util.Resource
import retrofit2.Response
import java.net.ConnectException
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val gPayService: GPayService
) {

    val transactions = transactionDao.getAllTransactions()
    val lastTransactions = transactionDao.getLastTransactions()

    private val _transactionApproveStatus = MutableLiveData<Resource<TransactionResponse>>()
    val transactionApproveStatus: LiveData<Resource<TransactionResponse>>
        get() = _transactionApproveStatus

    private val _transactionCancelStatus = MutableLiveData<Resource<TransactionResponse>>()
    val transactionCancelStatus: LiveData<Resource<TransactionResponse>>
        get() = _transactionCancelStatus

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
            try {
                val body = hashMapOf("payment_id" to paymentId)
                val res = gPayService.initTransaction(body)
                when {
                    res.isSuccessful -> {
                        val transaction = res.body().also { it?.paymentId = paymentId }
                        _transactionAction.postValue(Resource.Success(transaction!!))
                    }
                    else -> {
                        _transactionAction.postValue(Resource.Error(getErrorMessage(res)))
                    }
                }
            } catch (e: Exception) {
                checkException(e, _transactionAction)
            }
        }
    }

    suspend fun acceptTransaction(transaction: Transaction) {
        _transactionApproveStatus.value = Resource.Loading()
        try {
            coroutineScope {
                val res =
                    gPayService.acceptTransaction(hashMapOf("payment_id" to transaction.paymentId))
                when {
                    res.isSuccessful -> {
                        val transactionResponse = res.body()!!
                        transaction.apply {
                            timestamp = transactionResponse.timestamp?: Helper.getCurrentTimestamp()
                        }
                        addTransaction(transaction)
                        _transactionApproveStatus.postValue(Resource.Success(transactionResponse))
                    }
                    else -> _transactionApproveStatus.postValue(Resource.Error(getErrorMessage(res)))
                }
            }
        } catch (e: Exception) {
            checkException(e, _transactionApproveStatus)
        }
    }

    suspend fun cancelTransaction(transaction: Transaction) {
        _transactionCancelStatus.value = Resource.Loading()
        try {
            coroutineScope {
                val res =
                    gPayService.cancelTransaction(hashMapOf("payment_id" to transaction.paymentId))
                when {
                    res.isSuccessful -> _transactionCancelStatus.postValue(Resource.Success(res.body()!!))
                    else -> _transactionCancelStatus.postValue(Resource.Error(getErrorMessage(res)))
                }
            }
        } catch (e: Exception) {
            checkException(e, _transactionApproveStatus)
        }
    }

    suspend fun transactionStatus(paymentId: String) {
        _transactionApproveStatus.value = Resource.Loading()
        try {
            coroutineScope {
                val res = gPayService.cancelTransaction(hashMapOf("payment_id" to paymentId))
                when {
                    res.isSuccessful -> _transactionApproveStatus.postValue(Resource.Success(res.body()!!))
                    else -> _transactionApproveStatus.postValue(Resource.Error(ErrorCode.ERROR))
                }
            }
        } catch (e: Exception) {
            checkException(e, _transactionApproveStatus)
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

    private fun <T> checkException(e: Exception, response: MutableLiveData<Resource<T>>) {
        e.printStackTrace()
        when (e) {
            is ConnectException -> {
                response.postValue(Resource.Error(ErrorCode.CONNECTION_ERROR))
            }
            is NetworkErrorException -> {
                response.postValue(Resource.Error(ErrorCode.NETWORK_ERROR))
            }
            else -> {
                response.postValue(Resource.Error(ErrorCode.ERROR))
            }
        }
    }

    private fun <T> getErrorMessage(res: Response<T>): String {
        val gson = Gson()
        val type = object : TypeToken<TransactionResponse>() {}.type
        var errorResponse: TransactionResponse? = gson.fromJson(res.errorBody()?.charStream(), type)
        return errorResponse?.code ?: "error"
    }

    fun acknowledgeTransactionActionNone() {
        _transactionAction.value = Resource.None()
    }

    fun acknowledgeTransactionApproveStatusNone() {
        _transactionApproveStatus.value = Resource.None()
    }

    fun acknowledgeTransactionCancelStatusNone() {
        _transactionCancelStatus.value = Resource.None()
    }
}
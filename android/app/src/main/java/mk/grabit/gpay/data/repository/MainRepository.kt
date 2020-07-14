package mk.grabit.gpay.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mk.grabit.gpay.data.model.Cart
import mk.grabit.gpay.data.model.Merchant
import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.model.TransactionResponse
import mk.grabit.gpay.db.TransactionDao
import mk.grabit.gpay.networking.GPayService
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

    init {
        // Insert initial values
        GlobalScope.launch {
            transactionDao.insertAll(
                arrayListOf(
                    Transaction(
                        "123",
                        Merchant(1, "Tinex", "Shopping"),
                        arrayListOf(Cart("kafe", "Paskalin", 12, 70f)),
                        840.00f
                    ),
                    Transaction(
                        "123",
                        Merchant(1, "SportVision MK", "Clothes & Shoes"),
                        arrayListOf(
                            Cart(
                                "T-Shirt",
                                "SportVision collection summer 2020",
                                2,
                                1200f
                            )
                        ),
                        840.00f
                    ),
                    Transaction(
                        "124",
                        Merchant(1, "Burger King", "Food & Drinks"),
                        arrayListOf(
                            Cart(
                                "Double Burger",
                                "Double burger with cheese, extra sauce",
                                2,
                                270f
                            ),
                            Cart("CocaCola", "CocaCola, big cup", 2, 140f)
                        ),
                        840.00f
                    ), Transaction(
                        "125",
                        Merchant(1, "Timberland Center", "Shoes Store"),
                        arrayListOf(Cart("Shoes", "Leather Shoes model #482", 1, 3480f)),
                        840.00f
                    ), Transaction(
                        "126",
                        Merchant(1, "Bet365", "Bet & Win"),
                        arrayListOf(Cart("Sport ticket", "Bet & Win sport tickets 24/7", 5, 100f)),
                        100.00f
                    )
                    , Transaction(
                        "127",
                        Merchant(1, "Ramstore Mall Centar", "Shopping"),
                        arrayListOf(
                            Cart("kafe", "Paskalin", 12, 123f),
                            Cart("Illy Coffee", "Coffee Illy ARABICA 500g", 1, 540f),
                            Cart("kafe", "Paskalin", 12, 123f)
                        ),
                        840.00f
                    )
                    , Transaction(
                        "128",
                        Merchant(1, "GANT", "Clothes"),
                        arrayListOf(Cart("Pants", "Gant pants season summer 2020", 1, 1800f)),
                        1800.00f
                    )
                )
            )
        }
    }

    suspend fun initTransaction(paymentId: String) {
        coroutineScope {
            delay(1000)
            try {
                val res = gPayService.initTransaction(paymentId)
                when {
                    res.isSuccessful -> {
                        val transaction = res.body().also { it?.paymentId = paymentId }
                        addTransaction(transaction!!)
                    }
                    else -> _error.postValue("error")
                }
            } catch (e: Exception) {
                checkException(e)
            }
        }
    }

    suspend fun acceptTransaction(paymentId: String) {
        _status.value = Resource.Loading()
        try {
            coroutineScope {
                val res = gPayService.acceptTransaction(paymentId)
                when {
                    res.isSuccessful -> _status.postValue(Resource.Success(res.body()!!))
                    else -> _status.postValue(Resource.Error("error"))
                }
            }
        } catch (e: Exception) {
            checkException(e)
        }
    }

    suspend fun cancelTransaction(paymentId: String) {
        _status.value = Resource.Loading()
        try {
            coroutineScope {
                val res = gPayService.cancelTransaction(paymentId)
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
        when (e) {
            is ConnectException -> {
                _error.postValue("ConnectException")
            }
            else -> {
                _error.postValue("error")
            }
        }
    }

}
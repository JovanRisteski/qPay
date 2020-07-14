package mk.grabit.gpay.networking

import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.model.TransactionResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface GPayService {

    @GET("transactions")
    suspend fun getAllTransactions(): Response<List<Transaction>>

    @FormUrlEncoded
    @POST("/init")
    suspend fun initTransaction(@Field("payment_id") id: String): Response<Transaction>

    @FormUrlEncoded
    @POST("/accept")
    suspend fun acceptTransaction(@Field("payment_id") id: String): Response<TransactionResponse>

    @FormUrlEncoded
    @POST("/cancel")
    suspend fun cancelTransaction(@Field("payment_id") id: String): Response<TransactionResponse>

    @FormUrlEncoded
    @POST("/status")
    suspend fun transactionStatus(@Field("payment_id") id: String): Response<TransactionResponse>
}
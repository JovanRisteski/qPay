package mk.grabit.gpay.networking

import mk.grabit.gpay.data.model.Transaction
import mk.grabit.gpay.data.model.TransactionResponse
import retrofit2.Response
import retrofit2.http.*

interface GPayService {

    @Headers("Content-Type: application/json")
    @GET("transactions")
    suspend fun getAllTransactions(): Response<List<Transaction>>

    @Headers("Content-Type: application/json")
    @POST("/init")
    suspend fun initTransaction(@Body body: Map<String, String>): Response<Transaction>

    @Headers("Content-Type: application/json")
    @POST("/accept")
    suspend fun acceptTransaction(@Body body: Map<String, String>): Response<TransactionResponse>

    @Headers("Content-Type: application/json")
    @POST("/cancel")
    suspend fun cancelTransaction(@Body body: Map<String, String>): Response<TransactionResponse>

    @Headers("Content-Type: application/json")
    @POST("/status")
    suspend fun transactionStatus(@Body body: Map<String, String>): Response<TransactionResponse>
}
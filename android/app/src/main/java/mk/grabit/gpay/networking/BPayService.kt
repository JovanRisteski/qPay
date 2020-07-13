package mk.grabit.gpay.networking
import android.view.SurfaceControl
import retrofit2.Response
import retrofit2.http.GET

interface BPayService {

    @GET("transactions")
    suspend fun getAllMedications(): Response<List<SurfaceControl.Transaction>>
}
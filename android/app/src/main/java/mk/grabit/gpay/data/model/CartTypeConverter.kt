package mk.grabit.gpay.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*

internal class CartTypeConverter: Serializable {
    @TypeConverter
    fun fromOptionValuesList(cartList: List<Cart?>?): String? {
        if (cartList == null || cartList.isEmpty()) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<Cart?>?>() {}.type
        return gson.toJson(cartList, type)
    }

    @TypeConverter
    fun toOptionValuesList(optionValuesString: String?): List<Cart> {
        if (optionValuesString == null) {
            return ArrayList()
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<Cart?>?>() {}.type
        return gson.fromJson(optionValuesString, type)
    }
}
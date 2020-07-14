package mk.grabit.gpay.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class MerchantTypeConverter: Serializable {
    @TypeConverter
    fun fromOptionValuesList(merchant: Merchant?): String? {
        if (merchant == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<Merchant?>() {}.type
        return gson.toJson(merchant, type)
    }

    @TypeConverter
    fun toOptionValuesList(optionValuesString: String?): Merchant {
        if (optionValuesString == null) {
            return Merchant(0, "", "")
        }
        val gson = Gson()
        val type =
            object : TypeToken<Merchant?>() {}.type
        return gson.fromJson(optionValuesString, type)
    }
}
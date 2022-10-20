package hgh.project.baemin_clone.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class RestaurantFoodEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description:String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long
):Parcelable
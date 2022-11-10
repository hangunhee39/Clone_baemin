package hgh.project.baemin_clone.data.respository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import hgh.project.baemin_clone.data.entity.OrderEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultOrderRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val fireStore: FirebaseFirestore
) : OrderRepository {

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle: String
    ): Result = withContext(ioDispatcher) {
        //firebaseDB 에 넣을 hashMap
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList,
            "restaurantTitle" to restaurantTitle
        )

        val result = try {
            fireStore.collection("order")
                .add(orderMenuData)
            Result.Success<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
        return@withContext result
    }

    //firebaseDB 에서 가져오기
    override suspend fun getAllOrderMenus(userId: String): Result = withContext(ioDispatcher){
        return@withContext try {
            val result: QuerySnapshot =fireStore
                .collection("order")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            Result.Success(result.documents.map {
                OrderEntity(
                    id = it.id,
                    restaurantTitle = it.get("restaurantTitle") as String,
                    userId = it.get("userId") as String,
                    restaurantId = it.get("restaurantId") as Long,
                    foodMenuList = (it.get("orderMenuList") as ArrayList<Map<String, Any>>).map {  food ->
                        RestaurantFoodEntity(
                            id = food["id"] as String,
                            title = food["title"] as String,
                            description = food["description"] as String,
                            price = (food["price"] as Long).toInt(),
                            imageUrl = food["imageUrl"] as String,
                            restaurantId = food["restaurantId"] as Long,
                            restaurantTitle = food["restaurantTitle"] as String
                        )
                    }
                )
            })
        }catch (e: Exception){
            e.printStackTrace()
            Result.Error(e)
        }

    }

    //결과 반환하는 로직
    sealed class Result {

        data class Success<T>(
            val data: T? = null
        ) : Result()

        data class Error(
            val e: Throwable
        ) : Result()
    }
}
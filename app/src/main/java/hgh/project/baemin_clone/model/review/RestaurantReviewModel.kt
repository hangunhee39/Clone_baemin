package hgh.project.baemin_clone.model.review

import android.net.Uri
import hgh.project.baemin_clone.model.CellType
import hgh.project.baemin_clone.model.Model

data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Int,
    val thumbnailImageUri: Uri? =null
) : Model(id, type)

package hgh.project.baemin_clone.screen.main.home.restaurant

import androidx.annotation.StringRes
import hgh.project.baemin_clone.R

enum class RestaurantCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {
    ALL(R.string.all, R.string.all_type)
}
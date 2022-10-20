package hgh.project.baemin_clone.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import hgh.project.baemin_clone.R

enum class RestaurantCategoryDetail(
    @StringRes val categoryNameId: Int
) {

    MENU(R.string.menu), REVIEW(R.string.review)
}
package hgh.project.baemin_clone.util.provider

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

//String 이나 Color res 에 있는 값을 가져 오기 위해 필요함
interface ResourceProvider {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg  formArgs: Any): String

    fun getColor(@ColorRes resId: Int): Int

    fun getColorStateList(@ColorRes resId: Int): ColorStateList
}
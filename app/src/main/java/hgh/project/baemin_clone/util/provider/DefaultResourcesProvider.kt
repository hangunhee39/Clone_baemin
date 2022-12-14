package hgh.project.baemin_clone.util.provider

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class DefaultResourcesProvider(
    private val context: Context
): ResourceProvider {

    override fun getString(@StringRes resId: Int): String = context.getString(resId)

    override fun getString(@StringRes resId: Int, vararg formArgs: Any): String
        = context.getString(resId, *formArgs)       //가변인자에 * 붙여서 표시해야 한다

    override fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)

    override fun getColorStateList(@ColorRes resId: Int): ColorStateList =context.getColorStateList(resId)

}
package hgh.project.baemin_clone.extiensions

import android.content.res.Resources

fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}
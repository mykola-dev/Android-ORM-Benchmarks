package ds.dbtests

import android.content.Context
import android.os.Looper
import android.text.TextUtils
import android.util.TypedValue

fun isMainThread(): Boolean = (Looper.myLooper() == Looper.getMainLooper())

operator fun CharSequence.plus(operand: CharSequence): CharSequence = TextUtils.concat(this, operand)

fun Context.dp(i: Int): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i.toFloat(), resources.displayMetrics)
package ds.dbtests

import android.os.Looper
import android.text.TextUtils

fun isMainThread(): Boolean = (Looper.myLooper() == Looper.getMainLooper())
operator fun CharSequence.plus(operand:CharSequence): CharSequence = TextUtils.concat(this,operand)

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.nurik.calendar.extension.getColorInt

@ColorInt
fun View.getColorInt(@ColorRes colorRes: Int): Int {
    return this.context.getColorInt(colorRes)
}
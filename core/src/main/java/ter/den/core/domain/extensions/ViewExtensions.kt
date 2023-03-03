package ter.den.core.domain.extensions

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd

private const val VISIBILITY_DURATION = 300L


fun View.toInvisible() {
    if (this.visibility == View.VISIBLE) {
        ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f).apply {
            this.duration = VISIBILITY_DURATION
            interpolator = LinearInterpolator()
            start()
        }.doOnEnd { this.visibility = View.INVISIBLE }
    }
}

fun View.toVisible() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f).apply {
            this.duration = VISIBILITY_DURATION
            interpolator = LinearInterpolator()
            start()
        }
    }
}
package leo.me.la.flitory.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

internal class ThrottlingClickListener(
    private val coroutineScope: CoroutineScope,
    private val onClickListener: () -> Unit,
    private val throttlePeriod: Long = 500
) : View.OnClickListener {

    constructor(
        lifecycleOwner: LifecycleOwner,
        onClickListener: () -> Unit,
        throttlePeriod: Long = 500
    ) : this(lifecycleOwner.lifecycleScope, onClickListener, throttlePeriod)

    private val channel = Channel<Unit>(Channel.CONFLATED)

    init {
        coroutineScope.launch {
            channel.consumeAsFlow()
                .sample(throttlePeriod)
                .collect {
                    onClickListener()
                }
        }
    }

    override fun onClick(v: View) {
        coroutineScope.launch {
            channel.send(Unit)
        }
    }
}

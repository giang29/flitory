package leo.me.la.flitory.util

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

internal class DebouncingQueryTextListener(
    lifecycleOwner: LifecycleOwner,
    private val debouncePeriod: Long = 500,
    private val onDebouncingQueryTextChange: (String?) -> Unit
) : SearchView.OnQueryTextListener {

    private val coroutineScope = lifecycleOwner.lifecycleScope

    private val channel = ConflatedBroadcastChannel<String?>()

    init {
        coroutineScope.launch {
            channel.asFlow()
                .debounce(debouncePeriod)
                .distinctUntilChanged()
                .collect {
                    onDebouncingQueryTextChange(it)
                }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        coroutineScope.launch {
            channel.send(newText)
        }
        return false
    }
}

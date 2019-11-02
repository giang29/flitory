package leo.me.la.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext
private const val TAG_COMPUTATION_CONTEXT = "TAG_COMPUTATION_CONTEXT"
@FlowPreview
@ExperimentalCoroutinesApi
val presentationModule = module {
    factory<CoroutineContext>(named(TAG_COMPUTATION_CONTEXT)) { Dispatchers.Default }
    baseViewModel {
        SearchPhotoViewModel(get(), get(), get(named(TAG_COMPUTATION_CONTEXT)))
    }
}

inline fun <reified VS : BaseViewState, reified VM: BaseViewModel<VS>> Module.baseViewModel(
    name: String? = null,
    override: Boolean = false,
    noinline definition: Definition<VM>
) {
    viewModel(name?.let(::named), override, false, definition)
}

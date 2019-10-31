package leo.me.la.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val presentationModule = module {
    baseViewModel {
        SearchPhotoViewModel(get(), get())
    }
}

inline fun <reified VS : BaseViewState, reified VM: BaseViewModel<VS>> Module.baseViewModel(
    name: String? = null,
    override: Boolean = false,
    noinline definition: Definition<VM>
) {
    viewModel(name?.let(::named), override, false, definition)
}

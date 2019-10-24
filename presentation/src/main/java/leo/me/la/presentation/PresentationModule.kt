package leo.me.la.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {

}

inline fun <reified T : BaseViewState> Module.baseViewModel(
    name: String = "",
    override: Boolean = false,
    noinline definition: Definition<BaseViewModel<T>>
) {
    viewModel(named(name), override, false, definition)
}

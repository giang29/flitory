package leo.me.la.flitory.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import leo.me.la.presentation.BaseViewModel
import leo.me.la.presentation.BaseViewState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass
import android.app.Activity
import android.view.inputmethod.InputMethodManager


internal abstract class BaseFragment<VM : BaseViewModel<VS>, VS : BaseViewState>(clazz: KClass<VM>) :
    Fragment() {
    protected abstract val layout: Int

    protected val viewModel by viewModel(clazz)

    abstract fun render(viewState: VS)

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layout, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates.observe(viewLifecycleOwner, Observer {
            render(it)
        })
    }

    fun hideSoftKeyboard() {
        activity?.run {
            window?.decorView?.windowToken?.run {
                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(this, 0)
            }
        }
    }
}

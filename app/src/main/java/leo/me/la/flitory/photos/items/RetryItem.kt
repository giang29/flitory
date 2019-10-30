package leo.me.la.flitory.photos.items

import android.widget.TextView
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import leo.me.la.flitory.base.KotlinEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import leo.me.la.flitory.R
import leo.me.la.flitory.util.ThrottlingClickListener


@EpoxyModelClass(layout = R.layout.i_retry)
internal abstract class RetryItem: EpoxyModelWithHolder<RetryingHolder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: ThrottlingClickListener

    override fun bind(holder: RetryingHolder) {
        super.bind(holder)
        holder.retryButton.setOnClickListener(clickListener)
    }
}

class RetryingHolder: KotlinEpoxyHolder() {
    val retryButton by bind<TextView>(R.id.retryButton)
}

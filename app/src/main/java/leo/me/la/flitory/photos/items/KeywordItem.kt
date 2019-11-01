package leo.me.la.flitory.photos.items

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.coroutines.CoroutineScope
import leo.me.la.flitory.R
import leo.me.la.flitory.base.KotlinEpoxyHolder
import leo.me.la.flitory.util.ThrottlingClickListener
import leo.me.la.flitory.util.toPx
import leo.me.la.presentation.model.KeywordWithState
import kotlin.math.max
import kotlin.math.min

@EpoxyModelClass(layout = R.layout.i_keyword)
internal abstract class KeywordItem : EpoxyModelWithHolder<KeywordHolder>() {

    @EpoxyAttribute
    lateinit var keyword: KeywordWithState

    @EpoxyAttribute
    var nestedLevel: Int = 0

    @EpoxyAttribute
    lateinit var expandCollapseClickListener: (KeywordWithState) -> Unit

    @EpoxyAttribute
    lateinit var clickListener: (String) -> Unit

    @EpoxyAttribute
    lateinit var coroutineScope: CoroutineScope

    override fun bind(holder: KeywordHolder) {
        holder.rootItem.apply {
            updatePadding(left = (16 + min(nestedLevel, 2) * 16).toPx())
            setOnClickListener(
                ThrottlingClickListener(coroutineScope, { clickListener(keyword.keyword) })
            )
        }
        holder.keywordTextView.apply {
            text = keyword.keyword
            setTextSize(TypedValue.COMPLEX_UNIT_SP, max(20f - 2 * nestedLevel, 16f))
        }
        holder.extendCollapseButton.apply {
            isVisible = !keyword.subKeywords.isNullOrEmpty()
            setImageResource(
                if (keyword.closed)
                    R.drawable.ic_down
                else
                    R.drawable.ic_up
            )
            setOnClickListener(
                ThrottlingClickListener(coroutineScope, {
                    expandCollapseClickListener(keyword)
                })
            )
        }
    }
}

class KeywordHolder : KotlinEpoxyHolder() {
    val extendCollapseButton by bind<ImageView>(R.id.extendCollapseButton)
    val keywordTextView by bind<TextView>(R.id.keywordTextView)
    val rootItem by bind<View>(R.id.rootItem)
}

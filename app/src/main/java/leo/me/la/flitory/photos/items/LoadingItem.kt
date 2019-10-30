package leo.me.la.flitory.photos.items

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import leo.me.la.flitory.R
import leo.me.la.flitory.base.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.i_loading)
abstract class LoadingItem: EpoxyModelWithHolder<LoadingHolder>()

class LoadingHolder: KotlinEpoxyHolder()

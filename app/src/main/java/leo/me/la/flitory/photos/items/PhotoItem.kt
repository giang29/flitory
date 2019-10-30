package leo.me.la.flitory.photos.items

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import leo.me.la.flitory.R
import leo.me.la.flitory.base.KotlinEpoxyHolder
import leo.me.la.presentation.model.PhotoPresentationModel
import loadUri

@EpoxyModelClass(layout = R.layout.i_photo)
abstract class PhotoItem: EpoxyModelWithHolder<PhotoHolder>() {

    @EpoxyAttribute lateinit var photo: PhotoPresentationModel

    override fun bind(holder: PhotoHolder) {
        holder.imageView.loadUri(photo.thumbnail)
    }
}

class PhotoHolder: KotlinEpoxyHolder() {
    val imageView by bind<ImageView>(R.id.imageView)
}

package leo.me.la.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import leo.me.la.common.model.Photo

@Parcelize
data class PhotoPresentationModel(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
) : Parcelable {
    @IgnoredOnParcel
    val thumbnail = "https://farm$farm.staticflickr.com/$server/${id}_${secret}_m.jpg"

    companion object {
        fun fromPhoto(photo: Photo): PhotoPresentationModel {
            val (id, owner, secret, server, farm, title) = photo
            return PhotoPresentationModel(id, owner, secret, server, farm, title)
        }
    }
}

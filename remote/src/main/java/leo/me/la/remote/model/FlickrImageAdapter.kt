package leo.me.la.remote.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import leo.me.la.exception.FlickrException
import leo.me.la.exception.UnexpectedException

internal class FlickrImageAdapter {

    private val responseAdapter: JsonAdapter<ImageResponseRemoteModel>
    private val errorAdapter: JsonAdapter<FlickrException>

    init {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        responseAdapter = moshi.adapter(ImageResponseRemoteModel::class.java)
        errorAdapter = moshi.adapter(FlickrException::class.java)
    }

    @FromJson
    fun fromJson(
        reader: JsonReader
    ): ImageResponseRemoteModel {
        try {
            return responseAdapter.fromJson(reader.peekJson())!!
        } catch (ignored: JsonDataException) {
            try {
                errorAdapter.fromJson(reader.peekJson())
            } catch (_: Throwable) {
                null
            }?.let {
                throw it
            } ?: throw UnexpectedException()
        } catch (t: Throwable) {
            throw UnexpectedException(t)
        } finally {
            reader.beginObject()
            while(reader.hasNext())
                reader.skipValue()
            reader.endObject()
        }
    }

    @Suppress("Unused", "UNUSED_PARAMETER")
    @ToJson
    fun toJson(
        writer: JsonWriter,
        content: ImageResponseRemoteModel?
    ) {
        throw UnsupportedOperationException("Cannot deserialize ImageResponseRemoteModel")
    }
}

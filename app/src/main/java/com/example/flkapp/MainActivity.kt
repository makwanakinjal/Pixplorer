package com.example.flkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class PhotosViewModel : ViewModel() {
    private val imagesLiveData = MutableLiveData<List<Image>>()
    var photosAdapter = PhotosAdapter()

    fun loadPhotos(): LiveData<List<Image>> {
        viewModelScope.launch {
            try {
                val results: PhotoSearchResponse = ImagesRepository.flickrApi.fetchPhotos()

                val imgList = results.photos.photo.map { photo ->
                    Image(
                        id = photo.id,
                        url = "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                        title = photo.title
                    )
                }

                imagesLiveData.postValue(imgList)
            } catch (e: Exception) {
                Log.i("MyTag", "Error Occurred!")
                imagesLiveData.postValue(emptyList()) // Set an empty list in case of error
            }
        }
        return imagesLiveData
    }
}

data class PhotoResponse(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    var title: String
)

data class PhotoMetaData(val page: Int, val photo: List<PhotoResponse>)

data class PhotoSearchResponse(val photos: PhotoMetaData)

data class Image(val id: String, val url: String, val title: String)

private const val FlickerApiKey = "9c86bab69659ee4bed7faec927f8af37"
interface FlickerApi {
    @GET("?method=flickr.photos.getRecent&format=json&nojsoncallback=1&api_key=$FlickerApiKey")
    suspend fun fetchPhotos(): PhotoSearchResponse
}

object ImagesRepository {
    private const val baseUrl = "https://www.flickr.com/services/rest/"


    val flickrApi: FlickerApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrApi = retrofit.create(FlickerApi::class.java)
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadImagesButton = findViewById<Button>(R.id.loadimagesButton)
        val imageRecyclerView = findViewById<RecyclerView>(R.id.imagesRecycleView)


        imageRecyclerView.layoutManager = GridLayoutManager(this, 3)
        val photosViewModel: PhotosViewModel by viewModels()
        imageRecyclerView.adapter = photosViewModel.photosAdapter


        loadImagesButton.setOnClickListener {
            photosViewModel.loadPhotos().observe(this, Observer { list ->
                photosViewModel.photosAdapter.apply {
                    images.clear()
                    images.addAll(list)
                    notifyDataSetChanged()
                }
            })
        }

    }
}


class PhotosAdapter(val images: MutableList<Image> = mutableListOf()) :

    RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    inner class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)

        fun bind(image: Image) {

            titleTextView.text = image.title

            // Load the image using Glide
            Glide.with(itemView)
                .load(image.url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(photoImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val vh = PhotosViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.single_photo, parent, false))
        return vh
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(images[position])
    }
}


package com.martist.musicplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.martist.musicplayer.R
import com.martist.musicplayer.data.models.TrackDTO

class TrackListAdapter(
    val isMyMusic: Boolean,
     val onClick:(Long)->Unit,
     val onDownloadClick:(Long)->Unit
) : RecyclerView.Adapter<TrackListAdapter.ViewHolder>() {
    var dataSet: List<TrackDTO> = emptyList()
    private var filteredItems: List<TrackDTO> = dataSet

        fun submitList(newItems: List<TrackDTO>) {
        dataSet = newItems
            filteredItems = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val author: TextView
        val image: ImageView
        val download: ImageView

        init {

            title = view.findViewById(R.id.track_title)
            author = view.findViewById(R.id.track_author)
            image = view.findViewById(R.id.image_list)
            download = view.findViewById(R.id.download)
            if (isMyMusic)
                download.visibility = View.GONE
        }
        fun bind(item: TrackDTO){
            itemView.setOnClickListener { onClick(item.id) }
            download.setOnClickListener { onDownloadClick(item.id) }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = filteredItems.get(position).title
        holder.author.text = filteredItems.get(position).artist.name
        val md5 = filteredItems.get(position).md5_image
        val url = "https://e-cdns-images.dzcdn.net/images/cover/$md5/250x250.jpg"
        holder.image.load(url){
            crossfade(true)
            transformations(RoundedCornersTransformation(16f))
            placeholder(R.drawable.rec_list_image)
        }
        holder.bind(filteredItems.get(position))

    }

    override fun getItemCount(): Int {

            return filteredItems.size

    }
    fun filter(query:String?){
        filteredItems =  dataSet.filter { track -> track.title.lowercase().contains(query?.lowercase().toString()) }
        notifyDataSetChanged()
    }
}
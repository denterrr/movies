package ter.den.feature_home.presentation

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ter.den.core.databinding.ItemMovieBinding
import ter.den.core.domain.MovieDB
import ter.den.feature_home.domain.model.Popular

class MovieAdapter(
    private val onClickListener: ((id: Int, title: String) -> Unit)? = null,
    private val onClickFavoriteListener: ((id: Int) -> Unit)? = null,
) :
    PagingDataAdapter<Popular, MovieAdapter.PopularViewHolder>(PopularDiffItemCallback) {

    inner class PopularViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Popular?) {
            if (item?.isFavorite == true) binding.ivFavorite.setColorFilter(Color.RED)
            else binding.ivFavorite.clearColorFilter()
            binding.tvTitle.text = item?.title?.trim()
            Glide.with(binding.root)
                .load("${MovieDB.IMAGE_BASE_URL}${item?.poster_path}")
                .into(binding.ivPoster)
            binding.root.setOnClickListener {
                item?.id?.let { id -> onClickListener?.invoke(id, item.title) }
            }
            binding.ivFavorite.setOnClickListener{
                onClickFavoriteListener?.invoke(item?.id ?: 0)
                if(item?.isFavorite == true) binding.ivFavorite.clearColorFilter()
                else binding.ivFavorite.setColorFilter(Color.RED)
                item?.isFavorite = !(item?.isFavorite  ?: false)
            }
        }
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopularViewHolder(binding)
    }


}

private object PopularDiffItemCallback : DiffUtil.ItemCallback<Popular>() {

    override fun areItemsTheSame(oldItem: Popular, newItem: Popular): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Popular, newItem: Popular): Boolean {
        return oldItem == newItem
    }
}
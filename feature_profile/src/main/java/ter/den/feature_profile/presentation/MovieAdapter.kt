package ter.den.feature_profile.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ter.den.core.databinding.ItemMovieBinding
import ter.den.core.domain.MovieDB
import ter.den.feature_profile.domain.model.Movie

class MovieAdapter(
    private val onClickListener: ((id: Int, title: String) -> Unit)? = null,
    private val onClickFavoriteListener: ((id: Int) -> Unit)? = null,
) :
    PagingDataAdapter<Movie, MovieAdapter.PopularViewHolder>(PopularDiffItemCallback) {

    inner class PopularViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie?) {
            if (item?.isFavorite == true) binding.ivFavorite.setColorFilter(Color.RED)
            else binding.ivFavorite.clearColorFilter()
            binding.tvTitle.text = item?.title?.trim()
            Glide.with(binding.root)
                .load("${MovieDB.IMAGE_BASE_URL}${item?.poster_path}")
                .into(binding.ivPoster)
            binding.root.setOnClickListener {
                item?.id?.let { id -> onClickListener?.invoke(id, item.title) }
            }
            binding.ivFavorite.setOnClickListener {
                onClickFavoriteListener?.invoke(item?.id ?: 0)
                if (item?.isFavorite == true) binding.ivFavorite.clearColorFilter()
                else binding.ivFavorite.setColorFilter(Color.RED)
                item?.isFavorite = !(item?.isFavorite ?: false)
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

private object PopularDiffItemCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
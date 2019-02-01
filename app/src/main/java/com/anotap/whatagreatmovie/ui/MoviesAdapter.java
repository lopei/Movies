package com.anotap.whatagreatmovie.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anotap.whatagreatmovie.R;
import com.anotap.whatagreatmovie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * displays list of the movies depending on if favorites tab is selected
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolderItem> implements View.OnClickListener {
    private List<Movie> movieList;
    private ArrayList<Movie> displayList = new ArrayList<>();
    private OnMovieClickListener onMovieClickListener;
    private boolean favoritesOnly;


    public MoviesAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public MoviesAdapter() {

    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolderItem(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderItem holder, int position) {
        Movie movie = displayList.get(position);
        holder.title.setText(movie.getName());
        Picasso.get().load(movie.getThumbnail()).placeholder(R.drawable.placeholder_movie).fit().centerCrop().into(holder.poster);

        holder.main.setTag(movie);
        holder.main.setOnClickListener(this);
        holder.buttonFavorite.setTag(movie);
        holder.buttonFavorite.setOnClickListener(this);

        holder.buttonFavorite.setImageResource(movie.isListedFavorite() ? R.drawable.ic_heart_outline : R.drawable.ic_heart);
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }


    @Override
    public void onClick(View view) {
        if (onMovieClickListener != null) {
            if (view.getId() == R.id.buttonFavorite) {
                onMovieClickListener.onMovieFavoriteClick((Movie) view.getTag());
            } else {
                onMovieClickListener.onMovieClick((Movie) view.getTag());
            }
        }
    }

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setFavoritesOnly(boolean favoritesOnly) {
        this.favoritesOnly = favoritesOnly;
        filter();
        notifyDataSetChanged();
    }

    public void updateMovie(Movie movie) {
        int pos = displayList.indexOf(movie);
        notifyItemChanged(pos);
        if (favoritesOnly && !movie.isListedFavorite()) {
            displayList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    /**
     * filters displayed movies depending on if favorites tab is selected
     */
    private void filter() {
        displayList = new ArrayList<>();

        if (favoritesOnly) {
            for (Movie movie : movieList) {
                if (movie.isListedFavorite()) {
                    displayList.add(movie);
                }
            }
        } else {
            displayList.addAll(movieList);
        }
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {
        View main;
        @BindView(R.id.textTitle)
        TextView title;
        @BindView(R.id.imagePoster)
        ImageView poster;
        @BindView(R.id.buttonFavorite)
        ImageView buttonFavorite;

        ViewHolderItem(View itemView) {
            super(itemView);
            main = itemView;
            ButterKnife.bind(this, itemView);
        }


        public Context getContext() {
            return main.getContext();
        }
    }

    interface OnMovieClickListener {
        void onMovieClick(Movie movie);

        void onMovieFavoriteClick(Movie movie);
    }

}

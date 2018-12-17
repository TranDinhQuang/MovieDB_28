package vunt.com.vn.moviedb_28.util.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

import vunt.com.vn.moviedb_28.BuildConfig;
import vunt.com.vn.moviedb_28.R;
import vunt.com.vn.moviedb_28.data.model.Actor;
import vunt.com.vn.moviedb_28.data.model.Company;
import vunt.com.vn.moviedb_28.data.model.Genre;
import vunt.com.vn.moviedb_28.data.model.Movie;
import vunt.com.vn.moviedb_28.data.model.Video;
import vunt.com.vn.moviedb_28.screen.actors.ActorsAdapter;
import vunt.com.vn.moviedb_28.screen.home.CategoriesAdapter;
import vunt.com.vn.moviedb_28.screen.home.GenresAdapter;
import vunt.com.vn.moviedb_28.screen.movieinfo.GenresDetailAdapter;
import vunt.com.vn.moviedb_28.screen.producer.ProduceAdapter;
import vunt.com.vn.moviedb_28.screen.trailers.TrailerAdapter;
import vunt.com.vn.moviedb_28.util.StringUtils;

public class BindingUtils {
    private static final int IMAGE_SIZE_200 = 1280;

    @BindingAdapter({"app:bindMovies"})
    public static void setMoviesForRecyclerView(RecyclerView recyclerView,
                                                List<Movie> movies) {
        CategoriesAdapter adapter = (CategoriesAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(movies);
        }
    }

    @BindingAdapter("app:bindGenres")
    public static void setGenresForRecyclerView(RecyclerView recyclerView,
                                                List<Genre> genres) {
        GenresAdapter adapter = (GenresAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(genres);
        }
    }

    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(RecyclerView recyclerView,
                                        LinearLayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    @BindingAdapter({"setVisibility"})
    public static void setVisibleForView(View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
            return;
        }
        view.setVisibility(View.GONE);
    }

    @BindingAdapter("imageUrl")
    public static void setImage(ImageView imageView, String url) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_loading);
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_loading);
        requestOptions.error(R.drawable.ic_loading);
        String imageLink = StringUtils.getImageLink(IMAGE_SIZE_200, url);
        Glide.with(imageView.getContext())
                .load(imageLink)
                .apply(requestOptions)
                .into(imageView);
    }

    @BindingAdapter("android:scaleType")
    public static void setScaleType(ImageView imageView, String backdropPath) {
        if (backdropPath.isEmpty() || backdropPath == null) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @BindingAdapter("app:bindImage")
    public static void setRoundedImage(ImageView imageView, String url) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_my_music);
        }
        String imageLink = StringUtils.getImageLink(IMAGE_SIZE_200, url);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_loading);
        requestOptions.error(R.drawable.ic_my_music);
        Glide.with(imageView.getContext())
                .load(imageLink)
                .apply(requestOptions.circleCropTransform())
                .into(imageView);
    }

    @BindingAdapter("app:bindProduces")
    public static void setProducesForRecyclerView(RecyclerView recyclerView,
                                                  List<Company> companies) {
        ProduceAdapter adapter = (ProduceAdapter) recyclerView.getAdapter();
        if (adapter != null && companies != null) {
            adapter.replaceData(companies);
        }
    }

    @BindingAdapter("app:bindVideos")
    public static void setVideosForRecyclerView(RecyclerView recyclerView,
                                                List<Video> videos) {
        TrailerAdapter adapter = (TrailerAdapter) recyclerView.getAdapter();
        if (adapter != null && videos != null) {
            adapter.replaceData(videos);
        }
    }

    @BindingAdapter("app:bindActors")
    public static void setActorsForRecyclerView(RecyclerView recyclerView,
                                                List<Actor> actors) {
        ActorsAdapter adapter = (ActorsAdapter) recyclerView.getAdapter();
        if (adapter != null && actors != null) {
            adapter.replaceData(actors);
        }
    }

    @BindingAdapter("app:bindGenresDetail")
    public static void setGenresDetailForRecyclerView(RecyclerView recyclerView,
                                                      List<Genre> genres) {
        GenresDetailAdapter adapter = (GenresDetailAdapter) recyclerView.getAdapter();
        if (adapter != null && genres != null) {
            adapter.replaceData(genres);
        }
    }

    @BindingAdapter("app:youTubeThumbnailView")
    public static void setYouTubeThumbnailViewForTrailer(YouTubeThumbnailView thumbnailView,
                                                         final String videoKey) {
        if (videoKey == null) {
            thumbnailView.setImageResource(R.drawable.ic_play_circle);
            return;
        }
        YouTubeThumbnailView.OnInitializedListener listener =
                new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView view,
                                                        final YouTubeThumbnailLoader loader) {
                        loader.setVideo(videoKey);
                        loader.setOnThumbnailLoadedListener(
                                new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                            @Override
                            public void onThumbnailLoaded(
                                    YouTubeThumbnailView youTubeThumbnailView, String s) {
                                loader.release();
                            }

                            @Override
                            public void onThumbnailError(YouTubeThumbnailView view,
                                                         YouTubeThumbnailLoader.ErrorReason error) {
                                //TODO Handle load thumbnail error
                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView view,
                                                        YouTubeInitializationResult result) {
                        //TODO Handle init error
                    }
                };
        thumbnailView.initialize(BuildConfig.YOUTUBE_API_KEY, listener);
    }
}

package vunt.com.vn.moviedb_28.screen.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vunt.com.vn.moviedb_28.R;
import vunt.com.vn.moviedb_28.data.model.Genre;
import vunt.com.vn.moviedb_28.data.model.Movie;
import vunt.com.vn.moviedb_28.data.repository.MovieRepository;
import vunt.com.vn.moviedb_28.data.source.local.FavoriteReaderDbHelper;
import vunt.com.vn.moviedb_28.data.source.local.MovieLocalDataSource;
import vunt.com.vn.moviedb_28.data.source.remote.MovieRemoteDataSource;
import vunt.com.vn.moviedb_28.databinding.FragmentHomeBinding;
import vunt.com.vn.moviedb_28.screen.moviedetail.MovieDetailActivity;
import vunt.com.vn.moviedb_28.screen.movies.MoviesActivity;

import static vunt.com.vn.moviedb_28.screen.home.HomeViewModel.GENRE_SOURCE;

public class HomeFragment extends Fragment implements HomeNavigator,
        GenresAdapter.ItemClickListener, CategoriesAdapter.ItemClickListener {

    private HomeViewModel mViewModel;

    private FragmentHomeBinding mBinding;

    private CategoriesAdapter mPopularAdapter, mNowPlayingAdapter,
            mUpComingAdapter, mTopRateAdapter;
    private GenresAdapter mGenresAdapter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initViewModel();
        setNavigator();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,
                container, false);
        mBinding.setViewModel(mViewModel);

        setupAdapters();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }

    @Override
    public void showMovies(Genre genre, int getBy) {
        Intent intent = MoviesActivity.getMoviesIntent(getActivity(), genre, getBy);
        startActivity(intent);
    }

    @Override
    public void showMovieDetail(Movie movie) {
        startActivity(MovieDetailActivity.getMovieDetailIntent(getActivity(), movie));
    }

    @Override
    public void onGenreItemClick(Genre genre) {
        showMovies(genre, GENRE_SOURCE);
    }

    @Override
    public void onMovieItemClick(Movie movie) {
        showMovieDetail(movie);
    }

    @Override
    public void onDeleteFavoritiesClick(Movie movie) {

    }

    private void setupAdapters() {
        RecyclerView popularRecycler = mBinding.recyclerPopular;
        mPopularAdapter = new CategoriesAdapter(new ArrayList<Movie>(0));
        mPopularAdapter.setItemClickListener(this);
        popularRecycler.setAdapter(mPopularAdapter);
        popularRecycler.setNestedScrollingEnabled(false);

        RecyclerView nowPlayingRecycler = mBinding.recyclerNowPlaying;
        mNowPlayingAdapter = new CategoriesAdapter(new ArrayList<Movie>(0));
        mNowPlayingAdapter.setItemClickListener(this);
        nowPlayingRecycler.setAdapter(mNowPlayingAdapter);
        nowPlayingRecycler.setNestedScrollingEnabled(false);

        RecyclerView upComingRecycler = mBinding.recyclerUpComing;
        mUpComingAdapter = new CategoriesAdapter(new ArrayList<Movie>(0));
        mUpComingAdapter.setItemClickListener(this);
        upComingRecycler.setAdapter(mUpComingAdapter);
        upComingRecycler.setNestedScrollingEnabled(false);

        RecyclerView topRateRecycler = mBinding.recyclerTopRate;
        mTopRateAdapter = new CategoriesAdapter(new ArrayList<Movie>(0));
        mTopRateAdapter.setItemClickListener(this);
        topRateRecycler.setAdapter(mTopRateAdapter);
        topRateRecycler.setNestedScrollingEnabled(false);

        RecyclerView genresRecycler = mBinding.recyclerGenre;
        LinearLayoutManager genresLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        genresRecycler.setLayoutManager(genresLayoutManager);
        mGenresAdapter = new GenresAdapter(new ArrayList<Genre>(0));
        mGenresAdapter.setItemClickListener(this);
        genresRecycler.setAdapter(mGenresAdapter);
        genresRecycler.setNestedScrollingEnabled(false);
    }

    private void setNavigator() {
        if (mViewModel != null) {
            mViewModel.setNavigator(this);
        }
    }

    private void initViewModel() {
        FavoriteReaderDbHelper dbHelper = new FavoriteReaderDbHelper(getContext());
        MovieRepository movieRepository = MovieRepository.getInstance(
                MovieRemoteDataSource.getInstance(),
                MovieLocalDataSource.getInstance(dbHelper));
        mViewModel = new HomeViewModel(movieRepository);
    }
}

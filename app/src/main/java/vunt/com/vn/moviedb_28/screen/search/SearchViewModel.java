package vunt.com.vn.moviedb_28.screen.search;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import vunt.com.vn.moviedb_28.data.model.Movie;
import vunt.com.vn.moviedb_28.data.repository.MovieRepository;
import vunt.com.vn.moviedb_28.data.source.remote.config.response.CategoryResult;
import vunt.com.vn.moviedb_28.util.Constant;

public class SearchViewModel extends BaseObservable {
    private static final String[] SEARCH_TYPE = {"movie"};
    private MovieRepository mMovieRepository;

    public final ObservableList<Movie> searchedMoviesObservable = new ObservableArrayList<>();
    public final ObservableInt totalResultObservable = new ObservableInt();

    private SearchNavigator mSearchNavigator;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mKey;
    private int mCurrentPage;
    private String mSearchType;
    public final ObservableBoolean isLoadMore = new ObservableBoolean(false);
    public final ObservableBoolean isLoadingSuccess = new ObservableBoolean(true);

    public SearchViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
        mSearchType = SEARCH_TYPE[0];
        mCurrentPage = Constant.INDEX_UNIT;
    }

    public void setSearchNavigator(SearchNavigator searchNavigator) {
        mSearchNavigator = searchNavigator;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public void setSearchType(String searchType) {
        mSearchType = searchType;
    }

    public void onFilterClick() {
        mSearchNavigator.showSearchFilter();
    }

    public void searchMovie() {
        isLoadingSuccess.set(false);
        if (mSearchType.isEmpty() || mKey.isEmpty() || mCurrentPage <= 0) {
            handleError(null);
            return;
        }
        if (mCurrentPage == Constant.INDEX_UNIT) {
            isLoadingSuccess.set(false);
        }
        Disposable disposable = mMovieRepository.searchMovie(mSearchType, mKey, mCurrentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResult>() {
                    @Override
                    public void accept(CategoryResult categoryResult) {
                        if (mCurrentPage > Constant.FIRST_PAGE) {
                            searchedMoviesObservable.addAll(categoryResult.getMovies());
                        } else {
                            totalResultObservable.set(categoryResult.getTotalResults());
                            searchedMoviesObservable.clear();
                            searchedMoviesObservable.addAll(categoryResult.getMovies());
                        }
                        isLoadMore.set(false);
                        isLoadingSuccess.set(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        handleError(throwable.getMessage());
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void clear() {
        mCompositeDisposable.clear();
    }

    private void handleError(String message) {
        isLoadMore.set(false);
        totalResultObservable.set(0);
        searchedMoviesObservable.clear();
        isLoadingSuccess.set(true);
    }

    public void increaseCurrentPage() {
        mCurrentPage += Constant.INDEX_UNIT;
    }
}

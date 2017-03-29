package com.rakuishi.postalcode.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rakuishi.postalcode.R;
import com.rakuishi.postalcode.databinding.FragmentToolbarRecyclerViewBinding;
import com.rakuishi.postalcode.model.PostalCode;
import com.rakuishi.postalcode.repository.PostalCodeRepository;
import com.rakuishi.postalcode.view.activity.PostalCodeActivity;
import com.rakuishi.postalcode.view.adapter.PostalCodeListAdapter;
import com.rakuishi.postalcode.view.helper.DividerItemDecoration;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class PrefectureFragment extends BaseFragment implements PostalCodeListAdapter.Callback {

    private FragmentToolbarRecyclerViewBinding binding;
    private PostalCodeListAdapter adapter;
    @Inject
    PostalCodeRepository postalCodeRepository;
    @Inject
    CompositeDisposable compositeDisposable;

    public static PrefectureFragment newInstance() {
        return new PrefectureFragment();
    }

    public PrefectureFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        appComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_toolbar_recycler_view, container, false);

        adapter = new PostalCodeListAdapter(getContext(), PostalCodeListAdapter.Type.PREFECTURE, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        binding.recyclerView.setAdapter(adapter);
        binding.view.toolbar.setTitle(R.string.prefecture);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Disposable disposable = postalCodeRepository.findPrefectures()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((postalCodes, throwable) -> {
                    if (throwable == null) {
                        adapter.addAll(postalCodes);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    // region PostalCodeListAdapter.Callback

    @Override
    public void onItemClick(PostalCode postalCode) {
        startActivity(PostalCodeActivity.newInstance(getContext(), PostalCodeActivity.Type.CITY, postalCode.prefectureId, postalCode.prefecture));
    }

    // endregion
}
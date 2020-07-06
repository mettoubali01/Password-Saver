package com.example.passwordsaverb.fragments;

import android.os.Bundle;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import android.view.*;
import com.example.passwordsaverb.R;

public class SplashFragment extends Fragment {

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

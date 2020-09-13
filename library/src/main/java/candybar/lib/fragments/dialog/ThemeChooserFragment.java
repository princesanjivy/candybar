package candybar.lib.fragments.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import candybar.lib.R;
import candybar.lib.adapters.ThemeAdapter;
import candybar.lib.helpers.ThemeHelper;
import candybar.lib.helpers.TypefaceHelper;
import candybar.lib.items.Theme;
import candybar.lib.preferences.Preferences;

/*
 * CandyBar - Material Dashboard
 *
 * Copyright (c) 2014-2016 Dani Mahardhika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ThemeChooserFragment extends DialogFragment {

    private ListView mListView;
    private int mTheme;

    public static final String TAG = "candybar.dialog.theme";

    private static ThemeChooserFragment newInstance() {
        return new ThemeChooserFragment();
    }

    public static void showThemeChooser(@NonNull FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        try {
            DialogFragment dialog = ThemeChooserFragment.newInstance();
            dialog.show(ft, TAG);
        } catch (IllegalArgumentException | IllegalStateException ignored) {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.fragment_languages, false)
                .typeface(TypefaceHelper.getMedium(getActivity()), TypefaceHelper.getRegular(getActivity()))
                .title(R.string.pref_theme_header)
                .negativeText(R.string.close)
                .build();

        dialog.show();

        mListView = (ListView) dialog.findViewById(R.id.listview);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Theme> themes = ThemeHelper.getThemes();
        int currentTheme = Preferences.get(getActivity()).getTheme();
        int currentThemeIndex = 0;

        for (int i = 0; i < themes.size(); i++) {
            int shape = themes.get(i).getTheme();
            if (shape == currentTheme) {
                currentThemeIndex = i;
                break;
            }
        }

        mListView.setAdapter(new ThemeAdapter(getActivity(), themes, currentThemeIndex));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Preferences.get(getActivity()).setTheme(mTheme);
        getActivity().recreate();
        super.onDismiss(dialog);
    }

    public void setTheme(int theme) {
        mTheme = theme;
        dismiss();
    }
}

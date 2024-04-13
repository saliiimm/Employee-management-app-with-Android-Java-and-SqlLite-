package com.example.employeemanagementapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            ListPreference listPreference = findPreference("language");  // Make sure the key matches your ListPreference key in XML
            if (listPreference != null) {
                listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    String value = (String) newValue;

                    updateLocale(value);

                    return true;  // True to update the state of the Preference with the new value.
                });

                // Optionally update the locale to match current preference at fragment creation
                updateLocale(listPreference.getValue());
            }


        }

        private void updateLocale(String lang) {
            Locale locale;
            if (lang.equals("العربية")) {
                locale = new Locale("ar");
            } else {
                locale = Locale.ENGLISH; // Default language
            }
            if (!locale.equals(Locale.getDefault())) {
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.setLocale(locale);

                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                // Inside updateLocale() method in SettingsFragment
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                preferences.edit().putString("selected_language", lang).apply();
                requireActivity().recreate(); // Only recreate if locale has actually changed
            }// Refresh the activity to apply changes
        }
    }
}
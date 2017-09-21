package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceActivity;

import admin.pv.projects.mediasoft.com.abacus_admin.R;

public class ReglageActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}

package com.android.messaging.ui.appsettings;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.preference.EditTextPreference;

import com.android.messaging.R;

/**
 * Custom preference for setting auto-delete days (0-999)
 */
public class AutoDeleteDaysPreference extends EditTextPreference {
    
    private static final int MAX_DAYS = 999;
    
    public AutoDeleteDaysPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToHierarchy(androidx.preference.PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        
        // Set input type to number only
        setOnBindEditTextListener(editText -> {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            // Limit to 3 digits (max 999)
            editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
            editText.setSelection(editText.getText().length());
        });
        
        // Update summary with current value
        updateSummary();
    }
    
    @Override
    public void setText(String text) {
        // Validate and constrain the value
        try {
            int days = Integer.parseInt(text);
            if (days < 0) days = 0;
            if (days > MAX_DAYS) days = MAX_DAYS;
            // Store as integer in preferences
            persistInt(days);
            // Also update the text for display
            super.setText(String.valueOf(days));
        } catch (NumberFormatException e) {
            // Invalid input, don't change the value
            return;
        }
        updateSummary();
    }
    
    @Override
    public String getText() {
        // Get the integer value and convert to string for display
        int days = getPersistedInt(getContext().getResources().getInteger(R.integer.auto_delete_days_default));
        return String.valueOf(days);
    }
    
    @Override
    protected String getPersistedString(String defaultReturnValue) {
        // Override to get int value as string
        int days = getPersistedInt(getContext().getResources().getInteger(R.integer.auto_delete_days_default));
        return String.valueOf(days);
    }
    
    @Override
    protected boolean persistString(String value) {
        // Override to persist as integer
        try {
            int days = Integer.parseInt(value);
            return persistInt(days);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private void updateSummary() {
        String currentValue = getText();
        if (currentValue == null || currentValue.isEmpty()) {
            // Preference not yet initialized, summary will be set from XML
            return;
        }
        
        try {
            int days = Integer.parseInt(currentValue);
            String summary;
            if (days == 0) {
                summary = getContext().getString(R.string.auto_delete_immediately_summary);
            } else {
                summary = getContext().getString(R.string.auto_delete_days_pref_summary, days);
            }
            setSummary(summary);
        } catch (NumberFormatException e) {
            // Invalid value, don't update summary
        }
    }
    
    @Override
    protected void onSetInitialValue(Object defaultValue) {
        // The defaultValue parameter comes from android:defaultValue in XML
        // Since we use @integer/auto_delete_days_default, it comes as an Integer
        int defaultDays;
        if (defaultValue instanceof Integer) {
            defaultDays = (Integer) defaultValue;
        } else {
            // Fallback to resource default if not an integer
            defaultDays = getContext().getResources().getInteger(R.integer.auto_delete_days_default);
        }
        int days = getPersistedInt(defaultDays);
        // Don't call setText as it would persist again, just update display
        super.setText(String.valueOf(days));
        updateSummary();
    }
}
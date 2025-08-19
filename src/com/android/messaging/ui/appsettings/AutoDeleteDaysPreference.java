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
    private static final int DEFAULT_DAYS = 14;
    
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
        int days = DEFAULT_DAYS;
        try {
            days = Integer.parseInt(text);
            if (days < 0) days = 0;
            if (days > MAX_DAYS) days = MAX_DAYS;
        } catch (NumberFormatException e) {
            // Use default
        }
        
        super.setText(String.valueOf(days));
        updateSummary();
    }
    
    @Override
    public String getText() {
        String text = super.getText();
        if (text == null || text.isEmpty()) {
            return String.valueOf(DEFAULT_DAYS);
        }
        return text;
    }
    
    private void updateSummary() {
        String currentValue = getText();
        int days = DEFAULT_DAYS;
        try {
            days = Integer.parseInt(currentValue);
        } catch (NumberFormatException e) {
            // Use default
        }
        
        String summary = getContext().getString(R.string.auto_delete_days_pref_summary, days);
        setSummary(summary);
    }
    
    @Override
    protected void onSetInitialValue(Object defaultValue) {
        String value = getPersistedString(String.valueOf(DEFAULT_DAYS));
        setText(value);
    }
}
package util;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Manages internationalization (i18n) using ResourceBundles.
 * Allows the application to support multiple languages.
 * 
 * @author Student Grade Tracker Team
 * @version 1.0
 */
public class ResourceBundleManager {
    private static ResourceBundle bundle;
    private static final String BUNDLE_NAME = "messages";
    private static Locale currentLocale;
    
    static {
        currentLocale = Locale.ENGLISH;
        loadBundle();
    }
    
    /**
     * Loads the resource bundle for the current locale.
     */
    private static void loadBundle() {
        try {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        } catch (Exception e) {
            System.err.println("Failed to load resource bundle: " + e.getMessage());
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
        }
    }
    
    /**
     * Gets a string resource by key.
     * 
     * @param key the resource key
     * @return the localized string, or the key if not found
     */
    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return key; // Return key if resource not found
        }
    }
    
    /**
     * Gets a string resource with formatted parameters.
     * 
     * @param key the resource key
     * @param params format parameters
     * @return the formatted localized string
     */
    public static String getString(String key, Object... params) {
        try {
            String message = bundle.getString(key);
            return String.format(message, params);
        } catch (Exception e) {
            return key;
        }
    }
    
    /**
     * Sets the current locale and reloads the resource bundle.
     * 
     * @param locale the locale to switch to
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        loadBundle();
    }
    
    /**
     * Gets the current locale.
     * 
     * @return the current Locale
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }
    
    /**
     * Checks if a resource key exists.
     * 
     * @param key the key to check
     * @return true if the key exists in the bundle
     */
    public static boolean containsKey(String key) {
        return bundle.containsKey(key);
    }
}

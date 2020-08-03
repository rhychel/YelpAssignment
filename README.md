# Yelp Assignment

## To configure:
From the app `build.gradle`, put your **API_KEY** from the app you created from yelp.com.

```gradle
apply plugin: 'com.android.application'
// other codes

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.0"

    defaultConfig {
        // other configurations
        /**
         * Put your API_KEY here
         */
        buildConfigField "String", "API_KEY", "\"PUT YOUR API KEY HERE\""
    }

    // other attributes

}

```
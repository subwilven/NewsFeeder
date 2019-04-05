# News Feeder App

# Why this project ?

The focus of this project is on Android Jetpacka and DI by Dagger2.

# Screens


# Libraries Used
* Android Jetpack.-
  * [Data Binding][1] - Declaratively bind  data to UI elements.
  * [LiveData][2] - Build data objects that notify views when the data changes.
  * [Room][3] - Access your app's SQLite database for offline storage.
  * [ViewModel][4] - Store UI-related data that isn't destroyed on app rotations.
  * [WorkManager][5] - Manage repeated background jobs.
  * [Paging][15] - Loading partial data on demand reduces usage of network bandwidth and system resources.
  * [Palette][16] - For good visual design.
  
* Third party
  * [Dagger2][6] Compile-time dependency injection framework
  * [Picasoo][7] For image loading
  * [Retrofit][8] Type-safe HTTP client for Android
  * [Chrome Custom Tap][10] when a user taps a URL to either launch a browser, or build their own in-app browser using WebViews
  * [Material Dialog][9] Beautiful and fluid dialogs API


 # Libraries for upcoming Releases
   The next releases shoud include
   * [RxJava][13] - For composing asynchronous and event-based programs by using observable sequences
   * [Navigation][11] - Handle everything needed for in-app navigation.
   * [Animations & Transitions][12] - Move widgets and transition between screens.

[1]: https://developer.android.com/topic/libraries/data-binding/
[2]: https://developer.android.com/topic/libraries/architecture/livedata
[3]: https://developer.android.com/topic/libraries/architecture/room
[4]: https://developer.android.com/topic/libraries/architecture/viewmodel
[5]: https://developer.android.com/topic/libraries/architecture/workmanager
[6]: https://google.github.io/dagger/users-guide
[7]: https://github.com/square/picasso
[8]: https://square.github.io/retrofit/
[9]: https://github.com/afollestad/material-dialogs
[10]: https://developer.chrome.com/multidevice/android/customtabs
[11]: https://developer.android.com/topic/libraries/architecture/navigation/
[12]: https://developer.android.com/training/animation/
[13]: https://github.com/ReactiveX/RxJava
[14]: https://getpocket.com/developer/
[15]: https://developer.android.com/topic/libraries/architecture/paging/
[15]: https://developer.android.com/training/material/palette-colors

 # Features
  * Render a list of all news articles form different resoucres
  * Loading partial data on demand to reduces usage of network bandwidth and system resourcesv(Paging).
  * The user can select and preview an article details.
  * User can open news article URL.
  * User can refresh the contents of the screen.
  * User can add Article resources (Providers) manually in the app.
  * Articles are stored for offline usage.
  * Fetch latest news every 3 hours automatically
  * Integrate with read later service [Pocket] to save news articles and read them later
  * Friendly user interface design

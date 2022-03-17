
# skyss-companion

Side project app for showing bus departure times from different stops using widgets. Built using Kotlin.

This project is in no way affiliated with Skyss or any other providers.

Minimum SDK version: 23

Notable project dependencies:
* Hilt (https://developer.android.com/training/dependency-injection/hilt-android)
* Moshi + OKHttp (https://github.com/square/moshi) (https://square.github.io/okhttp/)
* Room (https://developer.android.com/jetpack/androidx/releases/room)

## Installation 
1. Clone the repository and open in Android Studio.
2. Build the project using `gradlew installRelease` or run `app` using android studio.

## Usage

### Searching for stops
Searching for stops can be done using the search tab located at the main fragment's tab view. Inputting at least three characters triggers an auto-search: 

![](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_search_1_resized.png) ![](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_search_2_resized.png)   

### Viewing departure times
Departure times for a specific stop can be viewed by tapping its list entry in the search tab. The view presented following this action should display the different departures from the selected stop, with a filterable list triggered by tapping the horizontal route number list elements.
 
Departure times for a specific line from the specified stop place can be viewed by tapping its entry in the list. 
Bookmarking (top-right icon) can be used to enable widget pinning (see further below).

![](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_stopgroup_1_resized.png) ![enter image description here](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_route_direction_1_resized.png)

Bookmarking a stop group or line adds it to the corresponding section in the main fragment. 

![](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_bookmark_1_resized.png) ![enter image description here](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_bookmark_2_resized.png)

### Notifications

Notifications can be registered by tapping a departure time in the time table for a line. A notification trigger time can be provided (measured in __minutes__ before scheduled departure). This creates an entry in the corrsponding main fragment tab.

![](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_route_direction_2_resized.png) ![enter image description here](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_alerts_1_resized.png)

### Homescreen Widgets

The application enables two homescreen widgets for bookmarked stop groups and stop group departures. Tapping either one brings up a list of elements which can be pinned to your homescreen.

![](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_widget_1_resized.png) ![enter image description here](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_widget_2_resized.png)

This should result in a widget displaying the selected line or stop group. 

![](https://raw.githubusercontent.com/martinheitmann/skyss-companion/image-assets/assets/screenshot_widget_3_resized.png)

## Known issues

* [__IN PROGRESS__] Restarting the device results in registered notifications being deleted. Since the notification won't trigger (which prevents the removal of the list entry) the entries in the notification list can be safely deleted. 

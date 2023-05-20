# Pixplorer(Photo Gallery Application / Flicker Application)

## Description:
FlickerApp is a photo gallery application that allows users to explore and view photos from the popular online photo-sharing platform, Flickr. The app provides a seamless user experience with a modern and intuitive interface.

## Features:

1. Fetch Photos: Users can click on the "Load Images" button to fetch a collection of photos from the Flickr API.
2. Grid Layout: The fetched photos are displayed in a grid layout using a RecyclerView with a customizable number of columns.
3. Image Loading: The app utilizes the Glide library to efficiently load and display the photos in the grid layout. It supports caching and provides smooth scrolling performance.
4. Title Display: Each photo is accompanied by its title, which is shown below the image in the grid.
5. Error Handling: The app gracefully handles errors that may occur during the API request or image loading process, providing appropriate feedback to the user.

## Architecture and Technologies:

1. MVVM Architecture: The app follows the Model-View-ViewModel (MVVM) architectural pattern, separating concerns and promoting a clean and maintainable code structure.
2. Retrofit: Retrofit is used for making HTTP requests to the Flickr API, handling network communication, and parsing JSON responses into Kotlin objects.
3. LiveData: The app leverages LiveData to observe and update the UI in a reactive manner, ensuring data consistency and synchronization between the ViewModel and the UI components.
4. Glide: Glide is used as the image loading library to efficiently load, cache, and display the fetched photos in the RecyclerView. It offers smooth image loading and supports various transformations and customization options.

## Usage
1. Clone the repository: git clone https://github.com/your-username/flicker-app.git
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.
4. Click the "Load Images" button to fetch and display the photos.
5. Scroll through the grid layout to explore the photo gallery.

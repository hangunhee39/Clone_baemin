package hgh.project.baemin_clone.data.respository.gallery

import hgh.project.baemin_clone.screen.review.gallery.GalleryPhoto

interface GalleryPhotoRepository {

    suspend fun getAllPhotos(): MutableList<GalleryPhoto>
}
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coopt2_fughetabout_it_inc.data.CategoriesViewModel
import com.example.coopt2_fughetabout_it_inc.data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.data.NoteDao
import com.example.coopt2_fughetabout_it_inc.data.NotesViewModel

/**
 * Factory for creating [NotesViewModel] instances.
 *
 * @property noteDao The data access object for notes.
 */
class NotesViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    /**
     * Creates and returns a new [NotesViewModel] instance.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A new [NotesViewModel] instance.
     * @throws IllegalArgumentException if the provided modelClass is unknown.
     */
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class, notes")
    }
}

/**
 * Factory for creating [CategoriesViewModel] instances.
 *
 * @property categoryDao The data access object for categories.
 */
class CategoriesViewModelFactory(private val categoryDao: CategoryDao) : ViewModelProvider.Factory {
    /**
     * Creates and returns a new [CategoriesViewModel] instance.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A new [CategoriesViewModel] instance.
     * @throws IllegalArgumentException if the provided modelClass is unknown.
     */
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(categoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class, categories")
    }
}

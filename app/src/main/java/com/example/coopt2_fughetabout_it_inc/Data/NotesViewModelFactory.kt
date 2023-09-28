import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coopt2_fughetabout_it_inc.Data.NoteDao

class NotesViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    // Add a separate create function with a different name
    fun <T : ViewModel?> createViewModel(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
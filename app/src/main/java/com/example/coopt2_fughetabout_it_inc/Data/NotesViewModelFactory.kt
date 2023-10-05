import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coopt2_fughetabout_it_inc.Data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.Data.NoteDao
import com.example.coopt2_fughetabout_it_inc.Data.ReminderDao

class NotesViewModelFactory(
    private val noteDao: NoteDao,
    private val categoryDao: CategoryDao,
    private val reminderDao: ReminderDao
) : ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(noteDao, categoryDao, reminderDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    fun <T : ViewModel?> createViewModel(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(noteDao, categoryDao, reminderDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
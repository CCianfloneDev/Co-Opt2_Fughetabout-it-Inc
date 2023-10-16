import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coopt2_fughetabout_it_inc.data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.data.NoteDao
import com.example.coopt2_fughetabout_it_inc.data.ReminderDao

class NotesViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CategoriesViewModelFactory(private val categoryDao: CategoryDao) : ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(categoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RemindersViewModelFactory(private val reminderDao: ReminderDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemindersViewModel::class.java)) {
            return RemindersViewModel(reminderDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coopt2_fughetabout_it_inc.data.Category
import com.example.coopt2_fughetabout_it_inc.data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.data.Note
import com.example.coopt2_fughetabout_it_inc.data.NoteDao
import com.example.coopt2_fughetabout_it_inc.data.Reminder
import com.example.coopt2_fughetabout_it_inc.data.ReminderDao
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteDao: NoteDao
) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
    fun insertOrUpdate(
        note: Note
    ) {
        viewModelScope.launch {
            if (note.id == 0L) {
                noteDao.insert(note)
            } else {
                noteDao.update(note)
            }
        }
    }
}
class CategoriesViewModel(
    private val categoryDao: CategoryDao
) : ViewModel() {

    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

     fun insertOrUpdate(category: Category) {
        viewModelScope.launch {
            if (category.id == 0L) {
                categoryDao.insert(category)
            } else {
                categoryDao.update(category)
            }
        }
    }
}
class RemindersViewModel(
    private val reminderDao: ReminderDao
) : ViewModel() {

    val allReminders: LiveData<List<Reminder>> = reminderDao.getAllReminders()

    fun insertOrUpdate(reminder: Reminder) {
        viewModelScope.launch {
            if (reminder.id == 0L) {
                reminderDao.insert(reminder)
            } else {
                reminderDao.update(reminder)
            }
        }
    }
}
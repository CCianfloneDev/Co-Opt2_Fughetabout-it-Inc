import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coopt2_fughetabout_it_inc.Data.Category
import com.example.coopt2_fughetabout_it_inc.Data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.Data.Note
import com.example.coopt2_fughetabout_it_inc.Data.NoteDao
import com.example.coopt2_fughetabout_it_inc.Data.Reminder
import com.example.coopt2_fughetabout_it_inc.Data.ReminderDao
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteDao: NoteDao,
    private val categoryDao: CategoryDao,
    private val reminderDao: ReminderDao
) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()
    val allReminders: LiveData<List<Reminder>> = reminderDao.getAllReminders()
    fun insertOrUpdate(
        note: Note,
        category: Category,
        reminder: Reminder
    ) {
        viewModelScope.launch {
            if (note.id == 0L) {
                noteDao.insert(note)
            } else {
                noteDao.update(note)
            }

            if (category.id == 0L) {
                categoryDao.insert(category)
            } else {
                categoryDao.update(category)
            }

            if (reminder.id == 0L) {
                reminderDao.insert(reminder)
            } else {
                reminderDao.update(reminder)
            }
        }
    }
}

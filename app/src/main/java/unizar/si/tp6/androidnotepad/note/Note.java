package unizar.si.tp6.androidnotepad.note;

import android.database.Cursor;

import unizar.si.tp6.androidnotepad.db.NotesTable;

/**
 * Created by dbarelop on 28/11/14.
 */
public class Note {
	private String title;
	private String body;
	private Category category;

    /**
     * creates a new Note
     * @param title the title of the note
     * @param body the body of the note
     * @param category the category of the note
     */
	public Note(String title, String body, Category category) {
		this.title = title;
		this.body = body;
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public Category getCategory() {
		return category;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

    public static Note fetchNote(Cursor cursor) {
        if(cursor.isClosed()) {
            return null;
        } else {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_TITLE));
            String body = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_BODY));
            String c = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
            Category category = new Category(c);
            return new Note(title, body, category);
        }
    }

    @Override
    public String toString() {
        return title;
    }
}

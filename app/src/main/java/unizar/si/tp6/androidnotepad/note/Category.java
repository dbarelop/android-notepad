package unizar.si.tp6.androidnotepad.note;

/**
 * Created by dbarelop on 28/11/14.
 */
public class Category {
	private String name;

	public Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Override
    public String toString() {
        return name;
    }
}

package mtr.data;

public final class DataConverter extends NameColorDataBase {

	public DataConverter(String name, int color) {
		this.name = name;
		this.color = color;
	}

	public DataConverter(long id, String name, int color) {
		super(id);
		this.name = name;
		this.color = color;
	}

	@Override
	protected boolean hasTransportMode() {
		return false;
	}
}

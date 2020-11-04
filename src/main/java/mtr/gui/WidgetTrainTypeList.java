package mtr.gui;

import mtr.data.Train;

import java.util.List;

public class WidgetTrainTypeList extends WidgetListBase<Train.TrainType> {

	public WidgetTrainTypeList(int width) {
		super(width);
	}

	public void refreshList(List<Train.TrainType> listMain, OnClick<Train.TrainType> onAdd) {
		refreshList(listMain, null, null, null, null, "icon_add", onAdd, null);
	}

	@Override
	protected String getName(Train.TrainType item) {
		return item.getName();
	}

	@Override
	protected int getColor(Train.TrainType item) {
		return item.getColor();
	}
}

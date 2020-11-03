package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import mtr.data.Train;

import java.util.List;

public class WidgetTrainTypeList extends WBox implements IGui {

	public WidgetTrainTypeList(int width) {
		super(Axis.VERTICAL);
		spacing = 0;
		this.width = width;
	}

	protected void refreshList(List<Train.TrainType> setMain, String button3Path, OnClick onButton3) {
		children.clear();

		for (Train.TrainType trainType : setMain) {
			WidgetNameColor panel = new WidgetNameColor(width, trainType.getName(), trainType.getColor(), null, null, button3Path);
			panel.setOnClick(null, null, () -> onButton3.onClick(trainType));
			add(panel);
		}
	}

	@FunctionalInterface
	public interface OnClick {
		void onClick(Train.TrainType t);
	}
}
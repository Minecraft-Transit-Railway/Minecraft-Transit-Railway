package org.mtr.core.data;

import org.mtr.core.generated.VehicleResourceSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.core.tools.Utilities;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.render.DynamicVehicleModel;

import java.util.Locale;
import java.util.function.Consumer;

public final class VehicleResource extends VehicleResourceSchema {

	public VehicleResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public void print() {
		Init.LOGGER.info(String.format("%s:%s", transportMode.toString().toLowerCase(Locale.ENGLISH), id));
	}

	public String getId() {
		return id;
	}

	public MutableText getName() {
		return TextHelper.translatable(name);
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	public TransportMode getTransportMode() {
		return transportMode;
	}

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public double getBogie1Position() {
		return bogie1Position;
	}

	public double getBogie2Position() {
		return bogie2Position;
	}

	public MutableText getDescription() {
		return TextHelper.translatable(description);
	}

	public String getWikipediaArticle() {
		return wikipediaArticle;
	}

	public void iterateModels(Consumer<DynamicVehicleModel> consumer) {
		models.forEach(vehicleModel -> {
			if (vehicleModel.model != null) {
				consumer.accept(vehicleModel.model);
			}
		});
	}

	public void iterateBogieModels(int index, Consumer<DynamicVehicleModel> consumer) {
		if (Utilities.isBetween(index, 0, 1)) {
			(index == 0 ? bogie1Models : bogie2Models).forEach(vehicleModel -> {
				if (vehicleModel.model != null) {
					consumer.accept(vehicleModel.model);
				}
			});
		}
	}
}

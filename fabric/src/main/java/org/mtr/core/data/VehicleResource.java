package org.mtr.core.data;

import org.mtr.core.generated.VehicleResourceSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;

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
		return colorStringToInt(color);
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

	public void iterateModels(Consumer<VehicleModel> consumer) {
		models.forEach(consumer);
	}

	static int colorStringToInt(String color) {
		try {
			return Integer.parseInt(color.toUpperCase(Locale.ENGLISH).replaceAll("[^\\dA-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}
}

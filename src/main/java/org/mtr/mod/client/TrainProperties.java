package mtr.client;

import mtr.mappings.Text;
import mtr.render.JonModelTrainRenderer;
import mtr.render.TrainRendererBase;
import mtr.sound.JonTrainSound;
import mtr.sound.TrainSoundBase;
import net.minecraft.network.chat.Component;

public class TrainProperties {

	public final String baseTrainType;
	public final Component name;
	public final String description;
	public final String wikipediaArticle;
	public final int color;
	public final float riderOffset;
	public final float riderOffsetDismounting;
	public final float bogiePosition;
	public final boolean isJacobsBogie;
	public final boolean hasGangwayConnection;
	public final TrainRendererBase renderer;
	public final TrainSoundBase sound;

	public TrainProperties(String baseTrainType, Component name, String description, String wikipediaArticle, int color, float riderOffset, float riderOffsetDismounting, float bogiePosition, boolean isJacobsBogie, boolean hasGangwayConnection, TrainRendererBase renderer, TrainSoundBase sound) {
		this.baseTrainType = baseTrainType;
		this.name = name;
		this.description = description;
		this.wikipediaArticle = wikipediaArticle;
		this.color = color;
		this.riderOffset = riderOffset;
		this.riderOffsetDismounting = riderOffsetDismounting;
		this.bogiePosition = bogiePosition;
		this.isJacobsBogie = isJacobsBogie;
		this.hasGangwayConnection = hasGangwayConnection;
		this.renderer = renderer;
		this.sound = sound;
	}

	public static TrainProperties getBlankProperties() {
		return new TrainProperties(
				"", Text.translatable(""), null, null, 0, 0, 0, 0, false, false,
				new JonModelTrainRenderer(null, "", "", ""),
				new JonTrainSound("", new JonTrainSound.JonTrainSoundConfig(null, 0, 0.5F, false))
		);
	}
}

package org.mtr.model.render.obj;

import net.minecraft.util.Identifier;
import org.mtr.MTR;
import org.mtr.model.render.model.RawMesh;
import org.mtr.model.render.vertex.Vertex;

public final class AtlasSprite {

	public final Identifier sheet;
	public final int sheetWidth, sheetHeight;
	public final int frameX, frameY, frameWidth, frameHeight;
	public final int spriteX, spriteY, spriteWidth, spriteHeight;
	public final int sourceWidth, sourceHeight;
	public final boolean rotated; // TODO

	public AtlasSprite(
			Identifier sheet,
			int sheetWidth, int sheetHeight,
			int frameX, int frameY, int frameWidth, int frameHeight,
			int spriteX, int spriteY, int spriteWidth, int spriteHeight,
			int sourceWidth, int sourceHeight,
			boolean rotated
	) {
		this.sheet = sheet;
		this.sheetWidth = sheetWidth;
		this.sheetHeight = sheetHeight;
		this.frameX = frameX;
		this.frameY = frameY;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.spriteX = spriteX;
		this.spriteY = spriteY;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.sourceWidth = sourceWidth;
		this.sourceHeight = sourceHeight;
		this.rotated = rotated;
	}

	public void applyToMesh(RawMesh mesh) {
		boolean uvBleeding = false;
		for (final Vertex vertex : mesh.vertices) {
			vertex.u = mapRange(vertex.u, (float) spriteX / sourceWidth, (float) (spriteX + spriteWidth) / sourceWidth, 0, 1);
			vertex.v = mapRange(vertex.v, (float) spriteY / spriteHeight, (float) (spriteY + spriteHeight) / sourceHeight, 0, 1);
			if (vertex.u < 0 || vertex.u > 1 || vertex.v < 0 || vertex.v > 1) {
				uvBleeding = true;
			}
			vertex.u = mapRange(vertex.u, 0, 1, (float) frameX / sheetWidth, (float) (frameX + frameWidth) / sheetWidth);
			vertex.v = mapRange(vertex.v, 0, 1, (float) frameY / sheetHeight, (float) (frameY + frameHeight) / sheetHeight);
		}
		if (uvBleeding) {
			MTR.LOGGER.warn("UV bleeding into adjacent sprite in {}", mesh.materialProperties.getTexture());
		}
		mesh.materialProperties.setTexture(sheet);
	}

	private static float mapRange(float x, float inMin, float inMax, float outMin, float outMax) {
		return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}
}

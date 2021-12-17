package mapper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

public class ModelMapper extends ModelPart {

	public final String name = "";

	public ModelMapper(ModelDataWrapper modelDataWrapper) {
		super(modelDataWrapper.model);
	}

	public void setRotationAngle(float rotationX, float rotationY, float rotationZ) {
		xRot = rotationX;
		yRot = rotationY;
		zRot = rotationZ;
	}

	public void setModelPart() {
	}

	public void setModelPart(String child) {
	}

	public void setOffset(float x, int y, float z) {
		setPos(x, y, z);
	}

	public void render(PoseStack matrices, VertexConsumer vertices, float x, float z, float rotateY, int light, int overlay) {
		setPos(x, 0, z);
		yRot = rotateY;
		render(matrices, vertices, light, overlay);
	}
}

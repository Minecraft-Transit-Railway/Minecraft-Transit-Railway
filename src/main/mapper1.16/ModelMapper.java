package mapper;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelMapper extends ModelPart {

	public final String name = "";

	public ModelMapper(ModelDataWrapper modelDataWrapper) {
		super(modelDataWrapper.model);
	}

	public void setRotationAngle(float rotationX, float rotationY, float rotationZ) {
		pitch = rotationX;
		yaw = rotationY;
		roll = rotationZ;
	}

	public void setModelPart() {
	}

	public void setModelPart(String child) {
	}

	public void setOffset(float x, int y, float z) {
		setPivot(x, y, z);
	}

	public void render(MatrixStack matrices, VertexConsumer vertices, float x, float z, float rotateY, int light, int overlay) {
		setPivot(x, 0, z);
		yaw = rotateY;
		render(matrices, vertices, light, overlay);
	}
}

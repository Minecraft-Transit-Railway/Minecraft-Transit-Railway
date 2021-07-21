package mtr.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;

public class ModelMapper {

	private float tempPivotX, tempPivotY, tempPivotZ;
	private float tempRotationX, tempRotationY, tempRotationZ;
	private int tempU, tempV;
	private ModelPart modelPart;
	private ModelPartData modelPartData;
	private ModelMapper parent;

	public final String name;
	private final ModelPartData mainModelPartData;

	public ModelMapper(ModelPartData mainModelPartData) {
		this.mainModelPartData = mainModelPartData;
		name = getRandomPartName();
	}

	public void setPivot(float x, float y, float z) {
		tempPivotX = x;
		tempPivotY = y;
		tempPivotZ = z;
	}

	public ModelMapper setTextureOffset(int u, int v) {
		tempU = u;
		tempV = v;
		return this;
	}

	public void setRotationAngle(float rotationX, float rotationY, float rotationZ) {
		tempRotationX = rotationX;
		tempRotationY = rotationY;
		tempRotationZ = rotationZ;
	}

	public void addChild(ModelMapper modelMapper) {
		modelMapper.parent = this;
	}

	public void addCuboid(float x, float y, float z, int sizeX, int sizeY, int sizeZ, float inflation, boolean mirrored) {
		final ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().mirrored(mirrored).cuboid(name, x, y, z, sizeX, sizeY, sizeZ, new Dilation(inflation), tempU, tempV);
		final ModelTransform modelTransform = ModelTransform.of(tempPivotX, tempPivotY, tempPivotZ, tempRotationX, tempRotationY, tempRotationZ);
		if (parent != null) {
			if (parent.modelPartData == null) {
				parent.addCuboid(0, 0, 0, 0, 0, 0, 0, false);
			}
			modelPartData = parent.modelPartData.addChild(name, modelPartBuilder, modelTransform);
			parent = null;
		} else {
			if (modelPartData == null) {
				modelPartData = mainModelPartData.addChild(name, modelPartBuilder, modelTransform);
			} else {
				modelPartData.addChild(getRandomPartName(), modelPartBuilder, ModelTransform.of(0, 0, 0, 0, 0, 0));
			}
		}
	}

	public void setModelPart(ModelPart modelPartParent) {
		modelPart = modelPartParent.getChild(name);
	}

	public void setOffset(float x, int y, float z) {
		modelPart.setPivot(x, y, z);
	}

	public void render(MatrixStack matrices, VertexConsumer vertices, float x, float z, float rotateY, int light, int overlay) {
		modelPart.setPivot(x, 0, z);
		modelPart.yaw = rotateY;
		modelPart.render(matrices, vertices, light, overlay);
	}

	private static String getRandomPartName() {
		return "part" + Math.abs(new Random().nextLong());
	}
}

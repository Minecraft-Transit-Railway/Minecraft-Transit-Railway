package mtr.gui;

import mtr.data.IGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.function.Function;

public class RenderingInstruction {

	private final RenderingInstructionType renderingInstructionType;
	private final Function<VertexConsumerProvider, VertexConsumer> getVertexConsumer;
	private final String text;
	private final DrawingCallback drawingCallback;
	private final float[] parameters;

	private RenderingInstruction(RenderingInstructionType renderingInstructionType, Function<VertexConsumerProvider, VertexConsumer> getVertexConsumer, String text, DrawingCallback drawingCallback, float... parameters) {
		this.renderingInstructionType = renderingInstructionType;
		this.getVertexConsumer = getVertexConsumer;
		this.text = text;
		this.drawingCallback = drawingCallback;
		this.parameters = parameters;
	}

	private VertexConsumer render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumer vertexConsumer, TextRenderer textRenderer, VertexConsumerProvider.Immediate immediate) {
		switch (renderingInstructionType) {
			case PUSH:
				matrices.push();
				return null;
			case POP:
				matrices.pop();
				return null;
			case GET_VERTEX_CONSUMER:
				return getVertexConsumer.apply(vertexConsumers);
			case TRANSLATE:
				if (parameters.length == 3) {
					matrices.translate(parameters[0], parameters[1], parameters[2]);
				}
				return null;
			case SCALE:
				if (parameters.length == 3) {
					matrices.scale(parameters[0], parameters[1], parameters[2]);
				}
				return null;
			case ROTATE_Y_DEGREES:
				if (parameters.length == 1) {
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(parameters[0]));
				}
				return null;
			case ROTATE_Y_RADIANS:
				if (parameters.length == 1) {
					matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(parameters[0]));
				}
				return null;
			case ROTATE_Z_DEGREES:
				if (parameters.length == 1) {
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(parameters[0]));
				}
				return null;
			case ROTATE_Z_RADIANS:
				if (parameters.length == 1) {
					matrices.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(parameters[0]));
				}
				return null;
			case DRAW_TEXT:
				if (parameters.length == 11 && text != null) {
					final IGui.HorizontalAlignment horizontalAlignment = IGui.HorizontalAlignment.values()[(int) parameters[0]];
					final IGui.VerticalAlignment verticalAlignment = IGui.VerticalAlignment.values()[(int) parameters[1]];
					final IGui.HorizontalAlignment xAlignment = IGui.HorizontalAlignment.values()[(int) parameters[2]];
					final float x = parameters[3];
					final float y = parameters[4];
					final float maxWidth = parameters[5];
					final float maxHeight = parameters[6];
					final float scale = parameters[7];
					final int textColor = (int) parameters[8];
					final boolean shadow = parameters[9] > 0;
					final int light = (int) parameters[10];
					IDrawing.drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, xAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, (x1, y1, x2, y2) -> {
						if (drawingCallback != null) {
							drawingCallback.drawingCallback(matrices, vertexConsumers, vertexConsumer, textRenderer, immediate, x1, y1, x2, y2);
						}
					});
				}
				return null;
			case DRAW_TEXTURE:
				if (parameters.length == 19) {
					final float x1 = parameters[0];
					final float y1 = parameters[1];
					final float z1 = parameters[2];
					final float x2 = parameters[3];
					final float y2 = parameters[4];
					final float z2 = parameters[5];
					final float x3 = parameters[6];
					final float y3 = parameters[7];
					final float z3 = parameters[8];
					final float x4 = parameters[9];
					final float y4 = parameters[10];
					final float z4 = parameters[11];
					final float u1 = parameters[12];
					final float v1 = parameters[13];
					final float u2 = parameters[14];
					final float v2 = parameters[15];
					final Direction facing = Direction.values()[(int) parameters[16]];
					final int color = (int) parameters[17];
					final int light = (int) parameters[18];
					IDrawing.drawTexture(matrices, vertexConsumer, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, facing, color, light);
				}
				return null;
			case DRAW_RECTANGLE:
				if (parameters.length == 5) {
					final float x1 = parameters[0];
					final float y1 = parameters[1];
					final float x2 = parameters[2];
					final float y2 = parameters[3];
					final int color = (int) parameters[4];
					IDrawing.drawRectangle(vertexConsumer, x1, y1, x2, y2, color);
				}
				return null;
			default:
				return null;
		}
	}

	public static void addPush(List<RenderingInstruction> renderingInstructions) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.PUSH, null, null, null));
	}

	public static void addPop(List<RenderingInstruction> renderingInstructions) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.POP, null, null, null));
	}

	public static void addVertexConsumer(List<RenderingInstruction> renderingInstructions, Function<VertexConsumerProvider, VertexConsumer> getVertexConsumer) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.GET_VERTEX_CONSUMER, getVertexConsumer, null, null));
	}

	public static void addTranslate(List<RenderingInstruction> renderingInstructions, float x, float y, float z) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.TRANSLATE, null, null, null, x, y, z));
	}

	public static void addScale(List<RenderingInstruction> renderingInstructions, float x, float y, float z) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.SCALE, null, null, null, x, y, z));
	}

	public static void addRotateYDegrees(List<RenderingInstruction> renderingInstructions, float rotation) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.ROTATE_Y_DEGREES, null, null, null, rotation));
	}

	public static void addRotateYRadians(List<RenderingInstruction> renderingInstructions, float rotation) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.ROTATE_Y_RADIANS, null, null, null, rotation));
	}

	public static void addRotateZDegrees(List<RenderingInstruction> renderingInstructions, float rotation) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.ROTATE_Z_DEGREES, null, null, null, rotation));
	}

	public static void addRotateZRadians(List<RenderingInstruction> renderingInstructions, float rotation) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.ROTATE_Z_RADIANS, null, null, null, rotation));
	}

	public static void addDrawText(List<RenderingInstruction> renderingInstructions, String text, float x, float y, int light) {
		addDrawText(renderingInstructions, text, IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, x, y, 1, IGui.ARGB_WHITE, true, light, null);
	}

	public static void addDrawText(List<RenderingInstruction> renderingInstructions, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float scale, int textColor, boolean shadow, int light, DrawingCallback drawingCallback) {
		addDrawText(renderingInstructions, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, -1, -1, scale, textColor, shadow, light, drawingCallback);
	}

	public static void addDrawText(List<RenderingInstruction> renderingInstructions, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, DrawingCallback drawingCallback) {
		addDrawText(renderingInstructions, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, drawingCallback);
	}

	public static void addDrawText(List<RenderingInstruction> renderingInstructions, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, DrawingCallback drawingCallback) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.DRAW_TEXT, null, text, drawingCallback, horizontalAlignment.ordinal(), verticalAlignment.ordinal(), xAlignment.ordinal(), x, y, maxWidth, maxHeight, scale, textColor, shadow ? 1 : 0, light));
	}

	public static void addDrawTexture(List<RenderingInstruction> renderingInstructions, float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int color, int light) {
		addDrawTexture(renderingInstructions, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1, facing, color, light);
	}

	public static void addDrawTexture(List<RenderingInstruction> renderingInstructions, float x, float y, float width, float height, Direction facing, int light) {
		addDrawTexture(renderingInstructions, x, y, 0, x + width, y + height, 0, 0, 0, 1, 1, facing, -1, light);
	}

	public static void addDrawTexture(List<RenderingInstruction> renderingInstructions, float x, float y, float width, float height, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		addDrawTexture(renderingInstructions, x, y, 0, x + width, y + height, 0, u1, v1, u2, v2, facing, color, light);
	}

	public static void addDrawTexture(List<RenderingInstruction> renderingInstructions, float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		addDrawTexture(renderingInstructions, x1, y2, z1, x2, y2, z2, x2, y1, z2, x1, y1, z1, u1, v1, u2, v2, facing, color, light);
	}

	public static void addDrawTexture(List<RenderingInstruction> renderingInstructions, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.DRAW_TEXTURE, null, null, null, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, facing.ordinal(), color, light));
	}

	public static void addDrawRectangle(List<RenderingInstruction> renderingInstructions, double x1, double y1, double x2, double y2, int color) {
		renderingInstructions.add(new RenderingInstruction(RenderingInstructionType.DRAW_RECTANGLE, null, null, null, (float) x1, (float) y1, (float) x2, (float) y2, color));
	}

	public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumerProvider.Immediate immediate, List<RenderingInstruction> renderingInstructions) {
		VertexConsumer vertexConsumer = null;
		for (final RenderingInstruction renderingInstruction : renderingInstructions) {
			final VertexConsumer newVertexConsumer = renderingInstruction.render(matrices, vertexConsumers, vertexConsumer, MinecraftClient.getInstance().textRenderer, immediate);
			if (newVertexConsumer != null) {
				vertexConsumer = newVertexConsumer;
			}
		}
		if (immediate != null) {
			immediate.draw();
		}
	}

	private enum RenderingInstructionType {PUSH, POP, GET_VERTEX_CONSUMER, TRANSLATE, SCALE, ROTATE_Y_DEGREES, ROTATE_Y_RADIANS, ROTATE_Z_DEGREES, ROTATE_Z_RADIANS, DRAW_TEXT, DRAW_TEXTURE, DRAW_RECTANGLE}

	@FunctionalInterface
	public interface DrawingCallback {
		void drawingCallback(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumer vertexConsumer, TextRenderer textRenderer, VertexConsumerProvider.Immediate immediate, float x1, float y1, float x2, float y2);
	}
}
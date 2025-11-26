package org.mtr.screen;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.vertex.UVertexConsumer;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Rail;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.packet.PacketUpdateLastRailStyles;
import org.mtr.registry.RegistryClient;
import org.mtr.registry.UConverters;
import org.mtr.render.MainRenderer;
import org.mtr.tool.BlockRendererHelper;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;
import java.util.stream.Collectors;

public final class RailModifierScreen extends WindowBase {

	@Nullable
	private Color hoverColor;
	private Rail.Shape shape;
	private final Rail rail;

	private final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_RAIL_APPEARANCE.getString()),
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.DIAMOND_PICKAXE_TEXTURE.get(), TranslationProvider.GUI_MTR_RAIL_TILT.getString())
	));

	private final PreviewBoxComponent previewBoxComponent1;
	private final PreviewBoxComponent previewBoxComponent2;

	@Nullable
	private final NumberInputComponent speedInputComponent;
	@Nullable
	private final ButtonComponent shapeButtonComponent;
	@Nullable
	private final NumberInputComponent radiusInputComponent;

	private final NumberInputComponent tiltPointsComponent;

	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDegrees1;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDistance1a;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDegrees1a;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDegrees1b;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDistance1b;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDegreesMiddle;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDistance2b;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDegrees2b;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDegrees2a;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDistance2a;
	private final ObjectObjectImmutablePair<UIContainer, NumberInputComponent> tiltAngleDegrees2;

	private final ObjectArrayList<UIComponent> elementsToHide = new ObjectArrayList<>();
	private final ObjectArrayList<UIComponent> elementsToShow = new ObjectArrayList<>();
	private final long renderKey1 = new Random().nextLong();
	private final long renderKey2 = new Random().nextLong();

	private static final int LEFT_WIDTH = 96;
	private static final float COLOR_HOVER_RADIUS = 0.0625F;
	private static final float COLOR_RADIUS = COLOR_HOVER_RADIUS / 2;
	private static final Color[] TILT_ANGLE_COLORS = {
			new Color(0xFF3333),
			new Color(0xFF9933),
			new Color(0xFFFF33),
			new Color(0x99FF33),
			new Color(0x33FF33),
			new Color(0x33FF99),
			new Color(0x33FFFF),
			new Color(0x3399FF),
			new Color(0x3333FF),
			new Color(0x9933FF),
			new Color(0xFF33FF),
	};

	public RailModifierScreen(Rail rail) {
		this.rail = rail;
		shape = rail.railMath.getShape();

		final ObjectObjectImmutablePair<ScrollComponent, PreviewBoxComponent> mainComponents1 = createMainComponents(0, TranslationProvider.GUI_MTR_RAIL_APPEARANCE);
		final ScrollComponent scrollComponent1 = mainComponents1.left();
		previewBoxComponent1 = mainComponents1.right();

		createLabel(scrollComponent1, TranslationProvider.GUI_MTR_RAIL_STYLES);
		final ButtonComponent editStylesButtonComponent = (ButtonComponent) new ButtonComponent(false)
				.setChildOf(scrollComponent1)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

		editStylesButtonComponent.setText(Text.translatable("selectWorld.edit").getString());
		editStylesButtonComponent.onMouseClickConsumer(clickEvent -> MinecraftClient.getInstance().setScreen(RailStyleSelectorScreen.create(rail)));

		final ButtonComponent flipStylesButtonComponent = (ButtonComponent) new ButtonComponent(false)
				.setChildOf(scrollComponent1)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

		flipStylesButtonComponent.setText(TranslationProvider.GUI_MTR_FLIP_STYLES.getString());
		flipStylesButtonComponent.onMouseClickConsumer(clickEvent -> {
			final ObjectArrayList<String> styles = rail.getStyles().stream().map(style -> {
				final boolean isForwards = style.endsWith("_1");
				final boolean isBackwards = style.endsWith("_2");
				if (isForwards || isBackwards) {
					return style.substring(0, style.length() - 1) + (isForwards ? "2" : "1");
				} else {
					return style;
				}
			}).collect(Collectors.toCollection(ObjectArrayList::new));
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(Rail.copy(rail, styles))));
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
			if (clientPlayerEntity != null) {
				RegistryClient.sendPacketToServer(new PacketUpdateLastRailStyles(clientPlayerEntity.getUuid(), rail.getTransportMode(), styles));
			}
			MinecraftClient.getInstance().setScreen(null);
		});

		if (rail.canAccelerate() && !rail.isPlatform() && !rail.isSiding()) {
			createSpacing(scrollComponent1);
			createLabel(scrollComponent1, TranslationProvider.GUI_MTR_RAIL_SPEED);
			speedInputComponent = (NumberInputComponent) new NumberInputComponent(1, 999, 1, false, null)
					.setChildOf(scrollComponent1)
					.setY(new SiblingConstraint())
					.setWidth(new RelativeConstraint());

			speedInputComponent.setValue(Math.max(rail.getSpeedLimitKilometersPerHour(false), rail.getSpeedLimitKilometersPerHour(true)));
			speedInputComponent.setSuffix(" km/h");
		} else {
			speedInputComponent = null;
		}

		if (rail.railMath.minY != rail.railMath.maxY) {
			createSpacing(scrollComponent1);
			createLabel(scrollComponent1, TranslationProvider.GUI_MTR_RAIL_SHAPE);
			shapeButtonComponent = (ButtonComponent) new ButtonComponent(true)
					.setChildOf(scrollComponent1)
					.setY(new SiblingConstraint())
					.setWidth(new RelativeConstraint());

			shapeButtonComponent.onClick(() -> {
				shape = shape == Rail.Shape.QUADRATIC ? Rail.Shape.TWO_RADII : Rail.Shape.QUADRATIC;
				update(false);
			});

			createSpacing(scrollComponent1);
			createLabel(scrollComponent1, TranslationProvider.GUI_MTR_RAIL_VERTICAL_RADIUS);
			radiusInputComponent = (NumberInputComponent) new NumberInputComponent(0, rail.railMath.getMaxVerticalRadius(), 0.01, true, null)
					.setChildOf(scrollComponent1)
					.setY(new SiblingConstraint())
					.setWidth(new RelativeConstraint());

			radiusInputComponent.setValue(rail.railMath.getVerticalRadius());
			radiusInputComponent.setSuffix(" m");
		} else {
			shapeButtonComponent = null;
			radiusInputComponent = null;
		}

		final ObjectObjectImmutablePair<ScrollComponent, PreviewBoxComponent> mainComponents2 = createMainComponents(1, TranslationProvider.GUI_MTR_RAIL_TILT);
		final ScrollComponent scrollComponent2 = mainComponents2.left();
		previewBoxComponent2 = mainComponents2.right();

		createLabel(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_POINTS);
		tiltPointsComponent = (NumberInputComponent) new NumberInputComponent(2, 7, 1, false, this::changeTiltPoints)
				.setChildOf(scrollComponent2)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

		tiltPointsComponent.setValue(rail.getTiltPoints());

		tiltAngleDegrees1 = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DEGREES_1, 0, -180, 180, "°", rail.getTiltAngleDegrees1());
		tiltAngleDistance1a = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DISTANCE_1A, 1, 0.1, getFlooredRailLength(rail), " m", rail.getTiltAngleDistance1a());
		tiltAngleDegrees1a = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DEGREES_1A, 2, -180, 180, "°", rail.getTiltAngleDegrees1a());
		tiltAngleDegrees1b = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DEGREES_1B, 3, -180, 180, "°", rail.getTiltAngleDegrees1b());
		tiltAngleDistance1b = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DISTANCE_1B, 4, 0.1, getFlooredRailLength(rail), " m", rail.getTiltAngleDistance1b());
		tiltAngleDegreesMiddle = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DEGREES_MIDDLE, 5, -180, 180, "°", rail.getTiltAngleDegreesMiddle());
		tiltAngleDistance2b = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DISTANCE_2B, 6, 0.1, getFlooredRailLength(rail), " m", rail.getTiltAngleDistance2b());
		tiltAngleDegrees2b = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DEGREES_2B, 7, -180, 180, "°", rail.getTiltAngleDegrees2b());
		tiltAngleDegrees2a = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DEGREES_2A, 8, -180, 180, "°", rail.getTiltAngleDegrees2a());
		tiltAngleDistance2a = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DISTANCE_2A, 9, 0.1, getFlooredRailLength(rail), " m", rail.getTiltAngleDistance2a());
		tiltAngleDegrees2 = createTiltControl(scrollComponent2, TranslationProvider.GUI_MTR_RAIL_TILT_ANGLE_DEGREES_2, 10, -180, 180, "°", rail.getTiltAngleDegrees2());
		update(false);
	}

	@Override
	public void onTick() {
		super.onTick();
		elementsToHide.forEach(element -> element.hide(true));
		elementsToHide.clear();
		elementsToShow.forEach(element -> element.unhide(true));
		elementsToShow.clear();
	}

	@Override
	public void onScreenClose() {
		super.onScreenClose();
		update(true);
	}

	private void onDrawPreview(MatrixStack matrixStack, boolean isStylesTab) {
		final Rail newRail = createRailCopy();
		final double length = newRail.railMath.getLength();
		final ObjectImmutableList<DoubleDoubleImmutablePair> tiltPointsAndAngles = newRail.getTiltPointsAndAngles();
		final int tiltPointsAndAnglesCount = tiltPointsAndAngles.size();
		final int tiltPoints = newRail.getTiltPoints();

		final float offsetX = (newRail.railMath.minX + newRail.railMath.maxX) / 2F;
		final float offsetY = (newRail.railMath.minY + newRail.railMath.maxY) / 2F;
		final float offsetZ = (newRail.railMath.minZ + newRail.railMath.maxZ) / 2F;
		final UMatrixStack uMatrixStack = UConverters.convert(matrixStack);

		ImageComponentBase.drawRectangle(vertexConsumer -> {
			newRail.railMath.render((x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, tiltAngle) -> ImageComponentBase.drawDoubleSidedShadedQuad(
					uMatrixStack, vertexConsumer,
					x1 - offsetX, y1 - offsetY, z1 - offsetZ,
					x2 - offsetX, y2 - offsetY, z2 - offsetZ,
					x3 - offsetX, y3 - offsetY, z3 - offsetZ,
					x4 - offsetX, y4 - offsetY, z4 - offsetZ,
					Color.WHITE
			), 0.01F, -0.5F, 0.5F);

			if (!isStylesTab) {
				Vector previousVector = null;
				for (int i = 0; i < tiltPointsAndAnglesCount; i++) {
					final double tiltPoint = tiltPointsAndAngles.get(i).leftDouble();
					final double point1 = Utilities.clampSafe(tiltPoint - 0.001F, 0, length);
					final double point2 = Utilities.clampSafe(tiltPoint, 0, length);
					final double point3 = Utilities.clampSafe(tiltPoint + 0.001F, 0, length);
					final Vector vector1 = newRail.railMath.getPosition(point1, false);
					final Vector vector2 = newRail.railMath.getPosition(point2, false);
					final Vector vector3 = newRail.railMath.getPosition(point3, false);

					// Draw distances between points
					if (tiltPoints >= 4) {
						if (i == 1) {
							drawDistanceMarker(uMatrixStack, vertexConsumer, previousVector, vector2, offsetX, offsetY, offsetZ, TILT_ANGLE_COLORS[1]);
						} else if (i == tiltPointsAndAnglesCount - 1) {
							drawDistanceMarker(uMatrixStack, vertexConsumer, previousVector, vector2, offsetX, offsetY, offsetZ, TILT_ANGLE_COLORS[9]);
						} else if (i == 3) {
							drawDistanceMarker(uMatrixStack, vertexConsumer, previousVector, vector2, offsetX, offsetY, offsetZ, TILT_ANGLE_COLORS[4]);
						} else if (i == tiltPointsAndAnglesCount - 3) {
							drawDistanceMarker(uMatrixStack, vertexConsumer, previousVector, vector2, offsetX, offsetY, offsetZ, TILT_ANGLE_COLORS[6]);
						}
					}

					// Draw perpendicular lines at points
					drawPointMarker(uMatrixStack, vertexConsumer, vector1, vector2, vector3, tiltPointsAndAngles.get(i).rightDouble(), offsetX, offsetY, offsetZ, getColor(i, tiltPointsAndAnglesCount));
					previousVector = vector2;
				}
			}
		}, false);

		if (isStylesTab) {
			final ClientWorld clientWorld = MinecraftClient.getInstance().world;
			if (clientWorld != null) {
				for (int i = 0; i < 2; i++) {
					final Vector vector = newRail.railMath.getPosition(i == 0 ? 0 : newRail.railMath.getLength(), false);
					final BlockPos blockPos = BlockPos.ofFloored(vector.x(), vector.y() - 1, vector.z());
					BlockRendererHelper.renderBlock(uMatrixStack, clientWorld.getBlockState(blockPos), i == 0 ? renderKey1 : renderKey2, blockPos.getX() - offsetX, blockPos.getY() - offsetY, blockPos.getZ() - offsetZ, 0.5);
				}
			}
		}

		if (isStylesTab) {
			previewBoxComponent2.updateFrom(previewBoxComponent1);
		} else {
			previewBoxComponent1.updateFrom(previewBoxComponent2);
		}
	}

	private void drawDistanceMarker(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, Vector vector1, Vector vector2, float offsetX, float offsetY, float offsetZ, Color color) {
		final double offset = Math.max(vector1.y(), vector2.y()) + 1;
		ImageComponentBase.drawCylinder(
				matrixStack, vertexConsumer,
				vector1.x() - offsetX, vector1.y() - offsetY, vector1.z() - offsetZ, COLOR_RADIUS / 4,
				vector1.x() - offsetX, offset - offsetY, vector1.z() - offsetZ, COLOR_RADIUS / 4,
				Color.GRAY
		);
		ImageComponentBase.drawCylinder(
				matrixStack, vertexConsumer,
				vector2.x() - offsetX, vector2.y() - offsetY, vector2.z() - offsetZ, COLOR_RADIUS / 4,
				vector2.x() - offsetX, offset - offsetY, vector2.z() - offsetZ, COLOR_RADIUS / 4,
				Color.GRAY
		);

		final float radius = hoverColor == color ? COLOR_HOVER_RADIUS : COLOR_RADIUS;
		ImageComponentBase.drawCylinderWithArrows(
				matrixStack, vertexConsumer,
				vector1.x() - offsetX, offset - offsetY, vector1.z() - offsetZ,
				vector2.x() - offsetX, offset - offsetY, vector2.z() - offsetZ, radius,
				hoverColor == color ? MainRenderer.getFlashingColor(color, 1) : color
		);
	}

	private void drawPointMarker(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, Vector vector1, Vector vector2, Vector vector3, double tiltAngle, float offsetX, float offsetY, float offsetZ, Color color) {
		final Vector offsetVector = new Vector(1, 0, 0).rotateZ(tiltAngle).rotateY((float) (-Math.atan2(vector3.z() - vector1.z(), vector3.x() - vector1.x()) - Math.PI / 2));
		final float radius = hoverColor == color ? COLOR_HOVER_RADIUS : COLOR_RADIUS;
		ImageComponentBase.drawCylinder(
				matrixStack, vertexConsumer,
				vector2.x() - offsetVector.x() - offsetX, vector2.y() - offsetVector.y() - offsetY, vector2.z() - offsetVector.z() - offsetZ, radius,
				vector2.x() + offsetVector.x() - offsetX, vector2.y() + offsetVector.y() - offsetY, vector2.z() + offsetVector.z() - offsetZ, radius,
				hoverColor == color ? MainRenderer.getFlashingColor(color, 1) : color
		);
	}

	private void update(boolean sendPacket) {
		if (shapeButtonComponent != null) {
			shapeButtonComponent.setText((shape == Rail.Shape.QUADRATIC ? TranslationProvider.GUI_MTR_RAIL_SHAPE_QUADRATIC : TranslationProvider.GUI_MTR_RAIL_SHAPE_TWO_RADII).getString());
		}

		if (radiusInputComponent != null) {
			radiusInputComponent.setDisabled(shape != Rail.Shape.TWO_RADII);
		}

		if (sendPacket) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(createRailCopy())));
		}
	}

	private ObjectObjectImmutablePair<ScrollComponent, PreviewBoxComponent> createMainComponents(int index, TranslationProvider.TranslationHolder title) {
		final UIContainer leftContainer = (UIContainer) new UIContainer()
				.setChildOf(backgroundComponent.containers[index])
				.setWidth(new CoerceAtMostConstraint(new RelativeConstraint(0.5F), new PixelConstraint(LEFT_WIDTH)))
				.setHeight(new RelativeConstraint());

		new UIWrappedText(title.getString(), false)
				.setChildOf(leftContainer)
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final ScrollComponent scrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
				.setChildOf(leftContainer)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint())
				.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;

		final PreviewBoxComponent previewBoxComponent = (PreviewBoxComponent) new PreviewBoxComponent(true, true, true, matrixStack -> onDrawPreview(matrixStack, index == 0))
				.setChildOf(backgroundComponent.containers[index])
				.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))
				.setHeight(new RelativeConstraint());

		return new ObjectObjectImmutablePair<>(scrollComponent, previewBoxComponent);
	}

	private void createLabel(UIContainer container, TranslationProvider.TranslationHolder translationHolder) {
		final UIContainer innerContainer = (UIContainer) new UIContainer()
				.setChildOf(container)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new ChildBasedSizeConstraint());

		new UIWrappedText(translationHolder.getString(), false)
				.setChildOf(innerContainer)
				.setWidth(new RelativeConstraint())
				.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR))
				.setTextScale(new PixelConstraint(0.8F));

		new UIContainer()
				.setChildOf(innerContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(1));
	}

	private void createSpacing(UIContainer container) {
		new UIContainer()
				.setChildOf(container)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(GuiHelper.DEFAULT_PADDING));
	}

	private ObjectObjectImmutablePair<UIContainer, NumberInputComponent> createTiltControl(ScrollComponent scrollComponent, TranslationProvider.TranslationHolder translationHolder, int colorIndex, double min, double max, @Nullable String suffix, double initialValue) {
		final UIContainer outerContainer = (UIContainer) new UIContainer()
				.setChildOf(scrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new ChildBasedSizeConstraint());

		final UIContainer innerContainer = (UIContainer) new UIContainer()
				.setChildOf(outerContainer)
				.setWidth(new RelativeConstraint())
				.setHeight(new ChildBasedSizeConstraint());

		createSpacing(innerContainer);
		createLabel(innerContainer, translationHolder);

		final UIContainer numberInputContainer = (UIContainer) new UIContainer()
				.setChildOf(innerContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(28))
				.onMouseEnterRunnable(() -> hoverColor = TILT_ANGLE_COLORS[colorIndex])
				.onMouseLeaveRunnable(() -> hoverColor = null);

		final NumberInputComponent numberInputComponent = (NumberInputComponent) new NumberInputComponent(min, max, 0.1, true, null)
				.setChildOf(numberInputContainer)
				.setWidth(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(2)));

		new UIBlock(TILT_ANGLE_COLORS[colorIndex])
				.setChildOf(numberInputContainer)
				.setX(new SiblingConstraint())
				.setWidth(new FillConstraint())
				.setHeight(new PixelConstraint(28));

		numberInputComponent.setValue(initialValue);
		if (suffix != null) {
			numberInputComponent.setSuffix(suffix);
		}

		return new ObjectObjectImmutablePair<>(innerContainer, numberInputComponent);
	}

	private void changeTiltPoints(double tiltPoints) {
		elementsToHide.clear();
		elementsToShow.clear();

		if (tiltPoints >= 4) {
			elementsToShow.add(tiltAngleDistance1a.left());
			elementsToShow.add(tiltAngleDegrees1a.left());
			elementsToShow.add(tiltAngleDegrees2a.left());
			elementsToShow.add(tiltAngleDistance2a.left());
		} else {
			elementsToHide.add(tiltAngleDistance1a.left());
			elementsToHide.add(tiltAngleDegrees1a.left());
			elementsToHide.add(tiltAngleDegrees2a.left());
			elementsToHide.add(tiltAngleDistance2a.left());
		}

		if (tiltPoints >= 6) {
			elementsToShow.add(tiltAngleDegrees1b.left());
			elementsToShow.add(tiltAngleDistance1b.left());
			elementsToShow.add(tiltAngleDistance2b.left());
			elementsToShow.add(tiltAngleDegrees2b.left());
		} else {
			elementsToHide.add(tiltAngleDegrees1b.left());
			elementsToHide.add(tiltAngleDistance1b.left());
			elementsToHide.add(tiltAngleDistance2b.left());
			elementsToHide.add(tiltAngleDegrees2b.left());
		}

		if (tiltPoints % 2 == 1) {
			elementsToShow.add(tiltAngleDegreesMiddle.left());
		} else {
			elementsToHide.add(tiltAngleDegreesMiddle.left());
		}
	}

	private Rail createRailCopy() {
		return Rail.copy(
				rail, shape,
				radiusInputComponent == null ? rail.railMath.getVerticalRadius() : radiusInputComponent.getValue(),
				(long) tiltPointsComponent.getValue(),
				tiltAngleDegrees1.right().getValue(),
				tiltAngleDistance1a.right().getValue(),
				tiltAngleDegrees1a.right().getValue(),
				tiltAngleDegrees1b.right().getValue(),
				tiltAngleDistance1b.right().getValue(),
				tiltAngleDegreesMiddle.right().getValue(),
				tiltAngleDistance2b.right().getValue(),
				tiltAngleDegrees2b.right().getValue(),
				tiltAngleDegrees2a.right().getValue(),
				tiltAngleDistance2a.right().getValue(),
				tiltAngleDegrees2.right().getValue()
		);
	}

	private static Color getColor(int index, int tiltPointsAndAnglesCount) {
		final int colorIndex;
		if (index == 0) {
			colorIndex = 0;
		} else if (index == tiltPointsAndAnglesCount - 1) {
			colorIndex = 10;
		} else if (tiltPointsAndAnglesCount % 2 == 1 && index == tiltPointsAndAnglesCount / 2) {
			colorIndex = 5;
		} else if (index == 1) {
			colorIndex = 2;
		} else if (index == tiltPointsAndAnglesCount - 2) {
			colorIndex = 8;
		} else if (index == 2) {
			colorIndex = 3;
		} else if (index == tiltPointsAndAnglesCount - 3) {
			colorIndex = 7;
		} else {
			colorIndex = 0;
		}

		return TILT_ANGLE_COLORS[colorIndex];
	}

	private static double getFlooredRailLength(Rail rail) {
		return Math.floor(rail.railMath.getLength() * 10) / 10;
	}
}

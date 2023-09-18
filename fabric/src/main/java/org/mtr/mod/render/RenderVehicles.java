package org.mtr.mod.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.tools.Utilities;
import org.mtr.core.tools.Vector;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.TrainClientRegistry;
import org.mtr.mod.model.ModelTrainBase;

public class RenderVehicles {

	public static void render(GraphicsHolder graphicsHolder) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return;
		}

		final Vector3d playerOffset = RenderTrains.getPlayerOffset();

		ClientData.instance.vehicles.forEach(vehicle -> {
			final ObjectArrayList<Vector> positions = vehicle.getPositions();
			final int totalCars = vehicle.vehicleExtraData.newVehicleCars.size();
			for (int i = 0; i < totalCars; i++) {
				if (i + 1 < positions.size()) {
					final VehicleCar vehicleCar = vehicle.vehicleExtraData.newVehicleCars.get(i);
					final LegacyVehicleRenderer legacyVehicleRenderer = TrainClientRegistry.getTrainProperties(vehicleCar.getVehicleId()).legacyVehicleRenderer;
					final ModelTrainBase model = legacyVehicleRenderer.model;
					if (model != null) {
						final Vector position1 = positions.get(i);
						final Vector position2 = positions.get(i + 1);
						final double x1 = position1.x;
						final double y1 = position1.y;
						final double z1 = position1.z;
						final double x2 = position2.x;
						final double y2 = position2.y;
						final double z2 = position2.z;
						final double x = Utilities.getAverage(x1, x2);
						final double y = Utilities.getAverage(y1, y2);
						final double z = Utilities.getAverage(z1, z2);
						final double yaw = Math.atan2(z2 - z1, x2 - x1);
						final double pitch = Math.atan2(y2 - y1, position1.distanceTo(position2));
						final BlockPos blockPos = Init.newBlockPos(x, y, z);
						final int light = LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
						graphicsHolder.push();
						graphicsHolder.translate(x - playerOffset.getXMapped(), y - playerOffset.getYMapped() + 1, z - playerOffset.getZMapped());
						graphicsHolder.rotateYRadians((float) (-yaw - Math.PI / 2));
						graphicsHolder.rotateXRadians((float) (pitch + Math.PI));
						model.render(graphicsHolder, vehicle, new Identifier(legacyVehicleRenderer.textureId + ".png"), light, 0, 0, false, i, totalCars, vehicle.getReversed(), vehicle.getIsOnRoute(), false, true, false);
						graphicsHolder.pop();
					}
				}
			}
		});
	}
}

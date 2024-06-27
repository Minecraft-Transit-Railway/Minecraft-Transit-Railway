package org.mtr.mod;

import io.nayuki.qrcodegen.QrCode;
import org.mtr.core.Main;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;

import javax.annotation.Nullable;
import java.net.Inet4Address;

public final class QrCodeHelper implements IGui {

	@Nullable
	private QrCode qrCode;
	private String serverTunnelUrl = "";
	private String clientTunnelUrl = "";
	private String defaultTunnelUrl = "";

	public static final QrCodeHelper INSTANCE = new QrCodeHelper();

	public void setServerTunnelUrl(String url) {
		if (!url.isEmpty() && !url.equals(serverTunnelUrl)) {
			serverTunnelUrl = url;
			update();
		}
	}

	public void setClientTunnelUrl(int defaultPort, String url) {
		clientTunnelUrl = url;
		try {
			defaultTunnelUrl = String.format("%s://%s:%s", "http", Inet4Address.getLocalHost().getHostAddress(), defaultPort);
		} catch (Exception e) {
			Main.LOGGER.error("", e);
			defaultTunnelUrl = "http://localhost:" + defaultPort;
		}
		update();
	}

	private void update() {
		final String url = serverTunnelUrl.isEmpty() ? clientTunnelUrl.isEmpty() ? defaultTunnelUrl : clientTunnelUrl : serverTunnelUrl;
		qrCode = QrCode.encodeText(url, QrCode.Ecc.MEDIUM);
	}

	public void renderQrCode(StoredMatrixTransformations storedMatrixTransformations, QueuedRenderLayer queuedRenderLayer, float size) {
		renderQrCode(qrCode, storedMatrixTransformations, queuedRenderLayer, size);
	}

	public static void renderQrCode(@Nullable QrCode qrCode, StoredMatrixTransformations storedMatrixTransformations, QueuedRenderLayer queuedRenderLayer, float size) {
		final int padding = 4;
		MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, queuedRenderLayer, (graphicsHolder, offset) -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);

			if (qrCode == null) {
				IDrawing.drawTexture(graphicsHolder, 0, 0, size, size, Direction.UP, GraphicsHolder.getDefaultLight());
			} else {
				final int pixels = qrCode.size;
				final int pixelsWithPadding = pixels + padding * 2;
				graphicsHolder.scale(size / pixelsWithPadding, size / pixelsWithPadding, 0);

				// Main QR code
				for (int x = 0; x < pixels; x++) {
					for (int y = 0; y < pixels; y++) {
						IDrawing.drawTexture(graphicsHolder, x + padding, y + padding, 0, x + padding + 1, y + padding + 1, 0, Direction.UP, qrCode.getModule(x, y) ? ARGB_BLACK : ARGB_WHITE, GraphicsHolder.getDefaultLight());
					}
				}

				// Border
				IDrawing.drawTexture(graphicsHolder, 0, 0, pixelsWithPadding, padding, Direction.UP, GraphicsHolder.getDefaultLight());
				IDrawing.drawTexture(graphicsHolder, 0, padding, padding, pixelsWithPadding - padding * 2, Direction.UP, GraphicsHolder.getDefaultLight());
				IDrawing.drawTexture(graphicsHolder, pixelsWithPadding - padding, padding, padding, pixelsWithPadding - padding * 2, Direction.UP, GraphicsHolder.getDefaultLight());
				IDrawing.drawTexture(graphicsHolder, 0, pixelsWithPadding - padding, pixelsWithPadding, padding, Direction.UP, GraphicsHolder.getDefaultLight());
			}

			graphicsHolder.pop();
		});
	}
}

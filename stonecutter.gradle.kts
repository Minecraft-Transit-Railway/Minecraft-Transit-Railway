plugins {
	id("dev.kikugie.stonecutter")
	id("net.neoforged.moddev") version "2.0.146" apply false
}

stonecutter active "1.21.4-fabric"

stonecutter parameters {
	constants.match(node.metadata.project.substringAfterLast("-"), "fabric", "neoforge")

	// Minecraft 1.21.11 renamed ResourceLocation back to Identifier and left it in the same
	// net.minecraft.resources package, so rewriting the token alone covers the imports, every usage
	// and the Javadoc links. That keeps roughly 150 sites free of version guards.
	//
	// The replacement is declared only for the newer targets instead of relying on a reversible
	// direction. Reversing it would rewrite unrelated names that merely contain the word, such as
	// formatIdentifier, along with a log message that mentions it in prose.
	if (current.parsed >= "26.1") {
		replacements {
			// The direction is fixed forwards; the guard above already restricts this to 26.1 and newer.
			string(true) { replace("ResourceLocation", "Identifier") }

			// Pure package moves. The type names are unchanged, so only the import lines differ and the
			// call sites throughout the code need no attention at all.
			string(true) { replace("net.minecraft.Util", "net.minecraft.util.Util") }
			string(true) { replace("net.minecraft.world.level.GameRules", "net.minecraft.world.level.gamerules.GameRules") }
			string(true) { replace("net.minecraft.client.renderer.RenderType", "net.minecraft.client.renderer.rendertype.RenderType") }
			string(true) { replace("net.minecraft.client.renderer.block.model.BakedQuad", "net.minecraft.client.resources.model.geometry.BakedQuad") }

			// LightTexture both moved package and was renamed, but LightCoordsUtil kept pack(...) and
			// block(...) with identical signatures. The import and the call sites are therefore rewritten
			// by two rules: the first matches only the import, which ends in a semicolon, and the second
			// only the static calls, which are followed by a dot.
			string(true) { replace("net.minecraft.client.renderer.LightTexture", "net.minecraft.util.LightCoordsUtil") }
			string(true) { replace("LightTexture.", "LightCoordsUtil.") }

			// NeoForge dropped the bus attribute and the Bus enum from @EventBusSubscriber; events now
			// declare which bus they belong to themselves. Only the attribute needs removing, and it is
			// stripped here rather than guarded in the four subscriber classes because those files sit
			// entirely inside a comment-toggled "if neoforge" block, and nesting a second condition inside
			// one is a pattern this codebase does not use anywhere.
			string(true) { replace(", bus = EventBusSubscriber.Bus.GAME", "") }
			string(true) { replace(", bus = EventBusSubscriber.Bus.MOD", "") }

			// GuiGraphics became GuiGraphicsExtractor in the same net.minecraft.client.gui package, so
			// the token covers the imports, the parameter types and the locals alike. Most of the drawing
			// vocabulary survived the rename; the calls that genuinely changed shape, such as pose() and
			// blitSprite, still fail to compile and are guarded separately rather than rewritten here.
			string(true) { replace("GuiGraphics", "GuiGraphicsExtractor") }

			// drawString and drawCenteredString were renamed with their argument lists unchanged. Both
			// are anchored to the receiver: this codebase also draws with java.awt.Graphics2D, which has
			// its own drawString, and rewriting that one would corrupt the font atlas generation.
			string(true) { replace("context.drawString(", "context.text(") }
			string(true) { replace("context.drawCenteredString(", "context.centeredText(") }

			// Renamed with an identical argument list.
			string(true) { replace(".absMoveTo(", ".absSnapTo(") }

			// blockUpdated became updateNeighborsAt, which also takes a redstone orientation. Every
			// call here notifies neighbours after a plain block change rather than a redstone one, and
			// the parameter is annotated nullable, so null is the faithful translation. Written out per
			// receiver name because a pattern spanning the argument would need a regular expression.
			string(true) { replace(".blockUpdated(pos, Blocks.AIR);", ".updateNeighborsAt(pos, Blocks.AIR, null);") }
			string(true) { replace(".blockUpdated(blockPos, Blocks.AIR);", ".updateNeighborsAt(blockPos, Blocks.AIR, null);") }
			string(true) { replace(".blockUpdated(blockPos.relative(rotatedDirection), Blocks.AIR);", ".updateNeighborsAt(blockPos.relative(rotatedDirection), Blocks.AIR, null);") }

			// Block colours on NeoForge. RegisterColorHandlersEvent.Block became BlockTintSources and
			// now takes a list, matching the interface change. Rewritten rather than guarded because
			// these lines sit inside the comment-toggled "if neoforge" block, and Stonecutter cannot
			// parse a condition nested inside a commented-out region.
			string(true) { replace("RegisterColorHandlersEvent.Block", "RegisterColorHandlersEvent.BlockTintSources") }
			string(true) { replace("event.getBlockColors().register(createTintSource(blockColorProvider), ", "event.register(List.of(createTintSource(blockColorProvider)), ") }

			// The render type factories moved from RenderType to RenderTypes, keeping their argument
			// lists. Only itemEntityTranslucentCull was also renamed, to entityTranslucentCullItemTarget.
			// The RenderType type itself is unaffected beyond its package, handled further above.
			string(true) { replace("RenderType.beaconBeam(", "RenderTypes.beaconBeam(") }
			string(true) { replace("RenderType.entityCutout(", "RenderTypes.entityCutout(") }
			string(true) { replace("RenderType.text(", "RenderTypes.text(") }
			string(true) { replace("RenderType.lines(", "RenderTypes.lines(") }
			string(true) { replace("RenderType.itemEntityTranslucentCull(", "RenderTypes.entityTranslucentCullItemTarget(") }

			// ServerPlayer.level() now returns a ServerLevel directly, so the separate accessor went.
			string(true) { replace("serverPlayerEntity.serverLevel()", "serverPlayerEntity.level()") }

			// The daylight rule was renamed as well as moved: reading the old name in the new package
			// would still fail. Rules are now fetched by their GameRule constant rather than by type.
			string(true) { replace("getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)", "getGameRules().get(GameRules.ADVANCE_TIME)") }

			// Command permissions moved from a numeric level to a named check.
			string(true) { replace("serverCommandSource -> serverCommandSource.hasPermission(4)", "Commands.hasPermission(Commands.LEVEL_OWNERS)") }
			string(true) { replace("serverCommandSource -> serverCommandSource.hasPermission(2)", "Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)") }

			// ResourceKey follows ResourceLocation: its accessor is identifier() now. Anchored to the
			// dimension key, because other types in this codebase still have a location() of their own.
			string(true) { replace("dimension().location()", "dimension().identifier()") }

			// Two Fabric modules were renamed wholesale rather than moved, along with their entry points.
			string(true) { replace("KeyBindingHelper.registerKeyBinding(", "KeyMappingHelper.registerKeyMapping(") }
			string(true) { replace("FabricItemGroup.builder()", "FabricCreativeModeTab.builder()") }

			// A style now names its font through a description rather than an identifier directly.
			string(true) { replace("withFont(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, \"mtr\"))", "withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(MTR.MOD_ID, \"mtr\")))") }

			// The level exposes this through a method now; the field itself is private.
			string(true) { replace("world.isClientSide &&", "world.isClientSide() &&") }
			string(true) { replace("world.isClientSide ?", "world.isClientSide() ?") }

			// The font helper takes an identifier and hands it to a style, so it wraps it too.
			string(true) { replace("Style.EMPTY.withFont(font)", "Style.EMPTY.withFont(new FontDescription.Resource(font))") }

			// The array writers take primitive arrays now rather than boxed lists, so the fastutil sets
			// hand over their own contents directly instead of being copied into an ArrayList first.
			string(true) { replace("putLongArray(KEY_PLATFORM_IDS, new ArrayList<>(platformIds))", "putLongArray(KEY_PLATFORM_IDS, platformIds.toLongArray())") }
			string(true) { replace("putLongArray(KEY_SELECTED_IDS + i, new ArrayList<>(selectedIds[i]))", "putLongArray(KEY_SELECTED_IDS + i, selectedIds[i].toLongArray())") }
			string(true) { replace("putLongArray(KEY_ROUTE_IDS, new ArrayList<>(filterRouteIds))", "putLongArray(KEY_ROUTE_IDS, filterRouteIds.toLongArray())") }
			string(true) { replace("putIntArray(KEY_SIGNAL_COLORS_1, new ArrayList<>(signalColors1))", "putIntArray(KEY_SIGNAL_COLORS_1, signalColors1.toIntArray())") }
			string(true) { replace("putIntArray(KEY_SIGNAL_COLORS_2, new ArrayList<>(signalColors2))", "putIntArray(KEY_SIGNAL_COLORS_2, signalColors2.toIntArray())") }
			string(true) { replace("putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList)", "putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList.stream().mapToLong(Long::longValue).toArray())") }

			// The texture constructor takes a name for debugging alongside the image.
			string(true) { replace("new DynamicTexture(newNativeImage)", "new DynamicTexture(() -> \"MTR dynamic texture\", newNativeImage)") }

			// The server field on a player is private now; the level it is in still exposes the server.
			string(true) { replace("context.player().server", "context.player().level().getServer()") }
			string(true) { replace("serverPlayerEntity.server::execute", "serverPlayerEntity.level().getServer()::execute") }

			// Render targets take a name for debugging, as the textures do.
			string(true) { replace("new TextureTarget(", "new TextureTarget(\"MTR preview\", ") }

			// Widgets follow the same retained model as everything else drawn on screen: what was a
			// render method is now an extraction one. Only the name changed, the arguments being the
			// same once the graphics context rename above has applied.
			string(true) { replace("renderWidget(", "extractWidgetRenderState(") }

			// The interface matrix is two dimensional now. Every transform in these two screens passes
			// zero for the translation's third axis and one for the scale's, so flattening them costs
			// nothing. Anchored to the graphics context, because the world renderers still push and pop
			// a three dimensional stack of their own.
			string(true) { replace("context.pose().pushPose()", "context.pose().pushMatrix()") }
			string(true) { replace("context.pose().popPose()", "context.pose().popMatrix()") }
			string(true) { replace("context.pose().translate(width / 2F, SQUARE_SIZE, 0)", "context.pose().translate(width / 2F, SQUARE_SIZE)") }
			string(true) { replace("context.pose().scale(2, 2, 1)", "context.pose().scale(2, 2)") }
			string(true) { replace("context.pose().translate(width / 2F, i + TEXT_HEIGHT + TEXT_PADDING / 2F, 0)", "context.pose().translate(width / 2F, i + TEXT_HEIGHT + TEXT_PADDING / 2F)") }
			string(true) { replace("context.pose().scale(0.5F, 0.5F, 1)", "context.pose().scale(0.5F, 0.5F)") }
			string(true) { replace("context.pose().translate(width / 2F - newWidth / 2F, height / 2F - newHeight / 2F, 0)", "context.pose().translate(width / 2F - newWidth / 2F, height / 2F - newHeight / 2F)") }
			string(true) { replace("context.pose().scale(newWidth / width, newHeight / height, 1)", "context.pose().scale(newWidth / width, newHeight / height)") }

			// Click validation takes the button information rather than a bare button number.
			string(true) { replace("isValidClickButton(int button)", "isValidClickButton(MouseButtonInfo mouseButtonInfo)") }
			string(true) { replace("super.isValidClickButton(button)", "super.isValidClickButton(mouseButtonInfo)") }
			string(true) { replace("if (isValidClickButton(button)) {", "if (isValidClickButton(mouseButtonEvent.buttonInfo())) {") }

			// Several value carriers became records, or were tidied to match the ones that did, and
			// dropped the get prefix from their accessors. Each rule is anchored to its receiver rather
			// than rewritten as a bare token, because the old names all survive on other types this code
			// calls in the very same expressions: Entity kept getYRot, which the vehicle rendering reads
			// off the player one argument away from reading it off the camera.
			string(true) { replace("getMainCamera().getPosition()", "getMainCamera().position()") }
			string(true) { replace("getMainCamera().getBlockPosition()", "getMainCamera().blockPosition()") }
			string(true) { replace("getMainCamera().getYRot()", "getMainCamera().yRot()") }
			string(true) { replace("camera.getPosition()", "camera.position()") }
			string(true) { replace("camera.getYRot()", "camera.yRot()") }
			string(true) { replace("biomeEffects.getGrassColorOverride()", "biomeEffects.grassColorOverride()") }
			string(true) { replace("biomeEffects.getFoliageColorOverride()", "biomeEffects.foliageColorOverride()") }
			string(true) { replace("biomeEffects.getWaterColor()", "biomeEffects.waterColor()") }
			string(true) { replace("getGameProfile().getName()", "getGameProfile().name()") }

			// The world clock is read through the overworld now, so that the value a client shows and the
			// value the server schedules against cannot drift apart per dimension. Both call sites here
			// already meant the overworld: one asks the server for it directly, and the other renders the
			// clock block, which has always shown overworld time.
			string(true) { replace(".getDayTime()", ".getOverworldClockTime()") }

			// Pack versions carry a major and a minor part now. Only the major is compared against the
			// versions recorded in the custom resource packs, which is what this code read before.
			string(true) { replace("getPackVersion(PackType.CLIENT_RESOURCES)", "packVersion(PackType.CLIENT_RESOURCES).major()") }
			string(true) { replace("getPackVersion(PackType.SERVER_DATA)", "packVersion(PackType.SERVER_DATA).major()") }

			// The modifier key helpers moved off Screen and onto the Minecraft instance, which is where
			// the keyboard handler they read has always lived.
			string(true) { replace("Screen.hasShiftDown()", "Minecraft.getInstance().hasShiftDown()") }

			// Fabric renamed the world tick events to level, matching the type they have always carried.
			// The callback interfaces are otherwise unchanged, so the method references still bind.
			string(true) { replace("ClientTickEvents.START_WORLD_TICK", "ClientTickEvents.START_LEVEL_TICK") }
			string(true) { replace("ClientTickEvents.END_WORLD_TICK", "ClientTickEvents.END_LEVEL_TICK") }
			string(true) { replace("ServerTickEvents.START_WORLD_TICK", "ServerTickEvents.START_LEVEL_TICK") }
			string(true) { replace("ServerTickEvents.END_WORLD_TICK", "ServerTickEvents.END_LEVEL_TICK") }

			// The payload registries are named after the direction they serve rather than abbreviated.
			string(true) { replace("PayloadTypeRegistry.playS2C()", "PayloadTypeRegistry.clientboundPlay()") }
			string(true) { replace("PayloadTypeRegistry.playC2S()", "PayloadTypeRegistry.serverboundPlay()") }

			// Scissoring now has to name what it applies to, because the immediate scissor state no
			// longer follows every draw. Both call sites bracket render type draws.
			string(true) { replace("RenderSystem.enableScissor(", "RenderSystem.enableScissorForRenderTypeDraws(") }
			string(true) { replace("RenderSystem.disableScissor()", "RenderSystem.disableScissorForRenderTypeDraws()") }

			// The renderer is asked whether it draws off screen at all, rather than being asked about one
			// block entity. All three overrides here answered true unconditionally, so nothing is lost.
			string(true) { replace("shouldRenderOffScreen(T blockEntity)", "shouldRenderOffScreen()") }

			// Screens extract render state instead of drawing, so their entry points were renamed. The
			// three argument render(context, mouseX, mouseY) that the widgets in this mod declare is
			// their own and keeps its name, which is why the parameter list is spelled out in full here.
			//
			// The two declarations carry the parameter type through themselves. Patterns are matched
			// against the original text and replacements are not looked at again, so the rule that
			// renames GuiGraphics on its own never sees a span another rule has already claimed.
			string(true) { replace("void render(GuiGraphics context, int mouseX, int mouseY, float delta)", "void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta)") }
			string(true) { replace("super.render(context, mouseX, mouseY, delta)", "super.extractRenderState(context, mouseX, mouseY, delta)") }
			string(true) { replace("void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta)", "void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta)") }
			string(true) { replace("renderBackground(context, mouseX, mouseY, delta)", "extractBackground(context, mouseX, mouseY, delta)") }

			// blitSprite selects the pipeline directly now instead of being handed a factory that built a
			// render type from the texture.
			string(true) { replace("context.blitSprite(RenderType::guiTextured, ", "context.blitSprite(RenderPipelines.GUI_TEXTURED, ") }

			// A player entity answers for the level it is in directly; the command sender wording went
			// with the command source abstraction that no longer sits on the entity.
			string(true) { replace("serverPlayerEntity.getCommandSenderWorld()", "serverPlayerEntity.level()") }

			// The dye an item carries is a data component rather than a property of the item class, so
			// the colour is read from the stack that was clicked with instead of from the item.
			string(true) { replace("convertPIDSColor(dyeItem.getDyeColor())", "convertPIDSColor(itemStack.get(DataComponents.DYE))") }

			// Two NeoForge names went in 26.1, and each is named only inside a block that belongs to one
			// loader, where a version guard would have to sit inside a comment. They are rewritten here
			// for the same reason the event bus attribute above is.
			//
			// A block declares its own render layer in its model now, and the call that used to set one
			// is already compiled out on 26.1, so only its import is left to take away. It is spent on
			// the client packet distributor, which is where sending towards the server moved to. The two
			// import lines are matched together because the second one on its own also names the server
			// side registry, which still sends towards players and keeps what it has.
			string(true) { replace("import net.minecraft.client.renderer.ItemBlockRenderTypes;\nimport net.neoforged.neoforge.network.PacketDistributor;", "import net.neoforged.neoforge.client.network.ClientPacketDistributor;") }
			string(true) { replace("PacketDistributor.sendToServer(", "ClientPacketDistributor.sendToServer(") }

			// GuiGraphics kept its name in the accessor NeoForge offers for it, so this one reader has to
			// be claimed before the blanket rename above reaches into the middle of the method name. A
			// rule that matches earlier in the line wins the span, and this one starts three characters
			// sooner.
			string(true) { replace("event.getGuiGraphics()", "event.getGuiGraphics()") }

			// The level render stages are separate event types now rather than one event carrying a stage,
			// and the camera travels in the render state. The stage chosen is the one the Fabric side of
			// this mod already listens to, so both loaders draw at the same point.
			string(true) { replace("public static void worldRendering(RenderLevelStageEvent event) {\n\t\tif (worldRenderCallback != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {", "public static void worldRendering(RenderLevelStageEvent.AfterOpaqueFeatures event) {\n\t\tif (worldRenderCallback != null) {") }
			string(true) { replace("event.getCamera().getPosition()", "event.getLevelRenderState().cameraRenderState.pos") }

			// The remaining reads of a player's own server field, which is private now.
			string(true) { replace(".accept(serverPlayerEntity.server, serverPlayerEntity)", ".accept(serverPlayerEntity.level().getServer(), serverPlayerEntity)") }

			// A payload handler is no longer wrapped in a pair that picks a side. The registration takes
			// the two handlers itself, so the pair and its import go away.
			//
			// The order is reversed on the way across: the pair was written client first, and the
			// registration takes the serverbound handler first. Registering a payload as bidirectional
			// without a handler for a direction is refused outright from 26.1, with the payload named in
			// the error, so a side left empty has to stay an empty handler rather than become nothing.
			string(true) { replace("import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;\n", "") }

			string(true) { replace("new DirectionalPayloadHandler<>((customPacketC2S, context) -> {\n\t\t}, (customPacketC2S, context) -> {", "(customPacketC2S, context) -> {") }
			string(true) { replace("}, ((ServerPlayer) player).server::execute);\n\t\t\t}\n\t\t})));", "}, ((ServerPlayer) player).level().getServer()::execute);\n\t\t\t}\n\t\t}, (customPacketC2S, context) -> {\n\t\t}));") }

			string(true) { replace("new DirectionalPayloadHandler<>(s2cClientHandler::accept, (customPacketS2C, context) -> {\n\t\t})));", "(customPacketS2C, context) -> {\n\t\t}, s2cClientHandler::accept));") }

			// A player no longer reaches its server through a field of its own.
			string(true) { replace("runServer(((ServerPlayer) player).server, (ServerPlayer) player)", "runServer(((ServerPlayer) player).level().getServer(), (ServerPlayer) player)") }

			// The second texture site, matching the one already rewritten above.
			string(true) { replace("new DynamicTexture(NativeImage.read(byteBuffer))", "new DynamicTexture(() -> \"MTR resource pack preview\", NativeImage.read(byteBuffer))") }
		}
	}
}

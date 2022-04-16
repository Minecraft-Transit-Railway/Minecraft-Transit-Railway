package mtr;

import mtr.block.*;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockEntityTypes {

	BlockEntityType<BlockArrivalProjector1Small.TileEntityArrivalProjector1Small> ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockArrivalProjector1Small.TileEntityArrivalProjector1Small::new, Blocks.ARRIVAL_PROJECTOR_1_SMALL);
	BlockEntityType<BlockArrivalProjector1Medium.TileEntityArrivalProjector1Medium> ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockArrivalProjector1Medium.TileEntityArrivalProjector1Medium::new, Blocks.ARRIVAL_PROJECTOR_1_MEDIUM);
	BlockEntityType<BlockArrivalProjector1Large.TileEntityArrivalProjector1Large> ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockArrivalProjector1Large.TileEntityArrivalProjector1Large::new, Blocks.ARRIVAL_PROJECTOR_1_LARGE);
	BlockEntityType<BlockNode.TileEntityBoatNode> BOAT_NODE_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockNode.TileEntityBoatNode::new, Blocks.BOAT_NODE);
	BlockEntityType<BlockClock.TileEntityClock> CLOCK_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockClock.TileEntityClock::new, Blocks.CLOCK);
	BlockEntityType<BlockPSDTop.TileEntityPSDTop> PSD_TOP_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockPSDTop.TileEntityPSDTop::new, Blocks.PSD_TOP);
	BlockEntityType<BlockAPGGlass.TileEntityAPGGlass> APG_GLASS_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockAPGGlass.TileEntityAPGGlass::new, Blocks.APG_GLASS);
	BlockEntityType<BlockPIDS1.TileEntityBlockPIDS1> PIDS_1_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockPIDS1.TileEntityBlockPIDS1::new, Blocks.PIDS_1);
	BlockEntityType<BlockPIDS2.TileEntityBlockPIDS2> PIDS_2_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockPIDS2.TileEntityBlockPIDS2::new, Blocks.PIDS_2);
	BlockEntityType<BlockPIDS3.TileEntityBlockPIDS3> PIDS_3_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockPIDS3.TileEntityBlockPIDS3::new, Blocks.PIDS_3);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_2_EVEN_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(2, false, pos, state), Blocks.RAILWAY_SIGN_2_EVEN);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_2_ODD_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(2, true, pos, state), Blocks.RAILWAY_SIGN_2_ODD);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_3_EVEN_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(3, false, pos, state), Blocks.RAILWAY_SIGN_3_EVEN);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_3_ODD_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(3, true, pos, state), Blocks.RAILWAY_SIGN_3_ODD);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_4_EVEN_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(4, false, pos, state), Blocks.RAILWAY_SIGN_4_EVEN);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_4_ODD_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(4, true, pos, state), Blocks.RAILWAY_SIGN_4_ODD);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_5_EVEN_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(5, false, pos, state), Blocks.RAILWAY_SIGN_5_EVEN);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_5_ODD_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(5, true, pos, state), Blocks.RAILWAY_SIGN_5_ODD);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_6_EVEN_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(6, false, pos, state), Blocks.RAILWAY_SIGN_6_EVEN);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_6_ODD_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(6, true, pos, state), Blocks.RAILWAY_SIGN_6_ODD);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_7_EVEN_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(7, false, pos, state), Blocks.RAILWAY_SIGN_7_EVEN);
	BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_7_ODD_TILE_ENTITY = RegistryUtilities.getBlockEntityType((pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(7, true, pos, state), Blocks.RAILWAY_SIGN_7_ODD);
	BlockEntityType<BlockRouteSignStandingLight.TileEntityRouteSignStandingLight> ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockRouteSignStandingLight.TileEntityRouteSignStandingLight::new, Blocks.ROUTE_SIGN_STANDING_LIGHT);
	BlockEntityType<BlockRouteSignStandingMetal.TileEntityRouteSignStandingMetal> ROUTE_SIGN_STANDING_METAL_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockRouteSignStandingMetal.TileEntityRouteSignStandingMetal::new, Blocks.ROUTE_SIGN_STANDING_METAL);
	BlockEntityType<BlockRouteSignWallLight.TileEntityRouteSignWallLight> ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockRouteSignWallLight.TileEntityRouteSignWallLight::new, Blocks.ROUTE_SIGN_WALL_LIGHT);
	BlockEntityType<BlockRouteSignWallMetal.TileEntityRouteSignWallMetal> ROUTE_SIGN_WALL_METAL_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockRouteSignWallMetal.TileEntityRouteSignWallMetal::new, Blocks.ROUTE_SIGN_WALL_METAL);
	BlockEntityType<BlockSignalLight1.TileEntitySignalLight1> SIGNAL_LIGHT_1 = RegistryUtilities.getBlockEntityType(BlockSignalLight1.TileEntitySignalLight1::new, Blocks.SIGNAL_LIGHT_1);
	BlockEntityType<BlockSignalLight2.TileEntitySignalLight2> SIGNAL_LIGHT_2 = RegistryUtilities.getBlockEntityType(BlockSignalLight2.TileEntitySignalLight2::new, Blocks.SIGNAL_LIGHT_2);
	BlockEntityType<BlockSignalLight3.TileEntitySignalLight3> SIGNAL_LIGHT_3 = RegistryUtilities.getBlockEntityType(BlockSignalLight3.TileEntitySignalLight3::new, Blocks.SIGNAL_LIGHT_3);
	BlockEntityType<BlockSignalLight4.TileEntitySignalLight4> SIGNAL_LIGHT_4 = RegistryUtilities.getBlockEntityType(BlockSignalLight4.TileEntitySignalLight4::new, Blocks.SIGNAL_LIGHT_4);
	BlockEntityType<BlockSignalSemaphore1.TileEntitySignalSemaphore1> SIGNAL_SEMAPHORE_1 = RegistryUtilities.getBlockEntityType(BlockSignalSemaphore1.TileEntitySignalSemaphore1::new, Blocks.SIGNAL_SEMAPHORE_1);
	BlockEntityType<BlockSignalSemaphore2.TileEntitySignalSemaphore2> SIGNAL_SEMAPHORE_2 = RegistryUtilities.getBlockEntityType(BlockSignalSemaphore2.TileEntitySignalSemaphore2::new, Blocks.SIGNAL_SEMAPHORE_2);
	BlockEntityType<BlockStationNameEntrance.TileEntityStationNameEntrance> STATION_NAME_ENTRANCE_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockStationNameEntrance.TileEntityStationNameEntrance::new, Blocks.STATION_NAME_ENTRANCE);
	BlockEntityType<BlockStationNameWall.TileEntityStationNameWall> STATION_NAME_WALL_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockStationNameWall.TileEntityStationNameWall::new, Blocks.STATION_NAME_WALL);
	BlockEntityType<BlockStationNameTallBlock.TileEntityStationNameTallBlock> STATION_NAME_TALL_BLOCK_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockStationNameTallBlock.TileEntityStationNameTallBlock::new, Blocks.STATION_NAME_TALL_BLOCK);
	BlockEntityType<BlockStationNameTallWall.TileEntityStationNameTallWall> STATION_NAME_TALL_WALL_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockStationNameTallWall.TileEntityStationNameTallWall::new, Blocks.STATION_NAME_TALL_WALL);
	BlockEntityType<BlockTactileMap.TileEntityTactileMap> TACTILE_MAP_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockTactileMap.TileEntityTactileMap::new, Blocks.TACTILE_MAP);
	BlockEntityType<BlockTrainAnnouncer.TileEntityTrainAnnouncer> TRAIN_ANNOUNCER_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockTrainAnnouncer.TileEntityTrainAnnouncer::new, Blocks.TRAIN_ANNOUNCER);
	BlockEntityType<BlockTrainCargoLoader.TileEntityTrainCargoLoader> TRAIN_CARGO_LOADER_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockTrainCargoLoader.TileEntityTrainCargoLoader::new, Blocks.TRAIN_CARGO_LOADER);
	BlockEntityType<BlockTrainCargoUnloader.TileEntityTrainCargoUnloader> TRAIN_CARGO_UNLOADER_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockTrainCargoUnloader.TileEntityTrainCargoUnloader::new, Blocks.TRAIN_CARGO_UNLOADER);
	BlockEntityType<BlockTrainRedstoneSensor.TileEntityTrainRedstoneSensor> TRAIN_REDSTONE_SENSOR_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockTrainRedstoneSensor.TileEntityTrainRedstoneSensor::new, Blocks.TRAIN_REDSTONE_SENSOR);
	BlockEntityType<BlockTrainScheduleSensor.TileEntityTrainScheduleSensor> TRAIN_SCHEDULE_SENSOR_TILE_ENTITY = RegistryUtilities.getBlockEntityType(BlockTrainScheduleSensor.TileEntityTrainScheduleSensor::new, Blocks.TRAIN_SCHEDULE_SENSOR);
}

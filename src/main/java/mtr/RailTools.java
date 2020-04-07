package mtr;

import mods.railcraft.api.tracks.IBlockTrack;
import mods.railcraft.api.tracks.TrackRegistry;
import mods.railcraft.api.tracks.TrackToolsAPI;
import mods.railcraft.api.tracks.TrackType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class RailTools {

	public static TrackType getTrackType(IBlockAccess world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block instanceof IBlockTrack ? ((IBlockTrack) block).getTrackType(world, pos) : TrackRegistry.TRACK_TYPE.get(0);
	}

	public static BlockRailBase.EnumRailDirection getTrackDirection(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		return block instanceof BlockRailBase ? ((BlockRailBase) block).getRailDirection(world, pos, state, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
	}

	public static void traverseConnectedTracks(World world, BlockPos pos, BiFunction<World, BlockPos, Boolean> action) {
		traverseConnectedTracks(world, pos, action, new HashSet<>());
	}

	public static Set<BlockPos> getConnectedTracks(IBlockAccess world, BlockPos pos) {
		BlockRailBase.EnumRailDirection shape;
		if (TrackToolsAPI.isRailBlockAt(world, pos))
			shape = getTrackDirection(world, pos);
		else
			shape = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
		return Arrays.stream(EnumFacing.HORIZONTALS)
				.map(side -> getTrackConnectedTrackAt(world, pos.offset(side), shape))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	public static BlockPos getTrackConnectedTrackAt(IBlockAccess world, BlockPos pos, BlockRailBase.EnumRailDirection shape) {
		if (TrackToolsAPI.isRailBlockAt(world, pos))
			return pos;
		BlockPos up = pos.up();
		if (shape.isAscending() && TrackToolsAPI.isRailBlockAt(world, up))
			return up;
		BlockPos posDown = pos.down();
		if (TrackToolsAPI.isRailBlockAt(world, posDown) && getTrackDirection(world, posDown).isAscending())
			return posDown;
		return null;
	}

	private static void traverseConnectedTracks(World world, BlockPos pos, BiFunction<World, BlockPos, Boolean> action, Set<BlockPos> visited) {
		visited.add(pos);
		if (!action.apply(world, pos))
			return;
		getConnectedTracks(world, pos).stream().filter(p -> !visited.contains(p)).forEach(p -> traverseConnectedTracks(world, p, action, visited));
	}
}


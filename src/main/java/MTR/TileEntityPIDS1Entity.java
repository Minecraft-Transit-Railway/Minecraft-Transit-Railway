package MTR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;

public class TileEntityPIDS1Entity extends TileEntity implements ITickable {

	public int minutes;
	public int platform;
	public int maxMin;

	public static boolean game, turn;
	public static String p1, p2;
	public static List wins = new ArrayList();
	public static long cooldown;

	private static int board[] = new int[9];
	private int tickCounter;
	private int localMax;

	public static void clear() {
		for (int i = 0; i < 9; i++)
			board[i] = 0;
	}

	public static void move(int square, ICommandSender sender) {
		if (board[square - 1] != 0) {
			sender.addChatMessage(new TextComponentString("This square is full!"));
			return;
		}
		board[square - 1] = turn ? 2 : 1;
		int result = checkWin(board);
		if (result > 0) {
			sender.addChatMessage(new TextComponentString((turn ? p2 : p1) + " wins!"));
			cooldown = Calendar.getInstance().getTimeInMillis() + 3000;
			game = false;
			return;
		}
		if (result == -1) {
			sender.addChatMessage(new TextComponentString("Tie game!!"));
			cooldown = Calendar.getInstance().getTimeInMillis() + 3000;
			game = false;
			return;
		}
		if (p2 == "")
			AI(sender);
		else
			turn = !turn;
	}

	public static void AI(ICommandSender sender) {
		List emptySquares = new ArrayList();
		List choose = new ArrayList();
		for (int i = 0; i < 9; i++)
			if (board[i] == 0)
				emptySquares.add(i);

		for (int i = 0; i < emptySquares.size(); i++) {
			int square = (Integer) emptySquares.get(i);
			int future1[] = new int[9];
			for (int j = 0; j < 9; j++)
				future1[j] = board[j];
			future1[square] = 2;
			if (checkWin(future1) == 2)
				choose.add(square);
		}
		if (choose.size() > 0) {
			board[(Integer) choose.get((int) (Math.random() * choose.size()))] = 2;
			sender.addChatMessage(new TextComponentString("AI wins!"));
			cooldown = Calendar.getInstance().getTimeInMillis() + 3000;
			game = false;
			return;
		}

		choose = checkAlmostWin(board);
		if (choose.size() > 0) {
			board[(Integer) choose.get((int) (Math.random() * choose.size()))] = 2;
			return;
		}

		for (int i = 0; i < 9; i++)
			if (board[i] == 0)
				choose.add(i);
		board[(Integer) choose.get((int) (Math.random() * choose.size()))] = 2;
	}

	public static void showBoard(ICommandSender sender) {
		sender.addChatMessage(new TextComponentString("X - " + p1 + ", O - " + (p2 == "" ? "AI" : p2)));
		sender.addChatMessage(new TextComponentString(getSquareString(1) + getSquareString(2) + getSquareString(3)));
		sender.addChatMessage(new TextComponentString(getSquareString(4) + getSquareString(5) + getSquareString(6)));
		sender.addChatMessage(new TextComponentString(getSquareString(7) + getSquareString(8) + getSquareString(9)));
	}

	public static int getSquare(int square) {
		return board[square];
	}

	private static String getSquareString(int square) {
		if (board[square - 1] == 1)
			return "X";
		if (board[square - 1] == 2)
			return "O";
		return "-";
	}

	private static int checkWin(int[] array) {
		int winner = 0;
		int win1 = checkWin2(array, 1, 2, 3);
		int win2 = checkWin2(array, 4, 5, 6);
		int win3 = checkWin2(array, 7, 8, 9);
		int win4 = checkWin2(array, 1, 4, 7);
		int win5 = checkWin2(array, 2, 5, 8);
		int win6 = checkWin2(array, 3, 6, 9);
		int win7 = checkWin2(array, 1, 5, 9);
		int win8 = checkWin2(array, 3, 5, 7);
		if (win1 > 0) {
			wins.add("1");
			winner = win1;
		}
		if (win2 > 0) {
			wins.add("2");
			winner = win2;
		}
		if (win3 > 0) {
			wins.add("3");
			winner = win3;
		}
		if (win4 > 0) {
			wins.add("4");
			winner = win4;
		}
		if (win5 > 0) {
			wins.add("5");
			winner = win5;
		}
		if (win6 > 0) {
			wins.add("6");
			winner = win6;
		}
		if (win7 > 0) {
			wins.add("7");
			winner = win7;
		}
		if (win8 > 0) {
			wins.add("8");
			winner = win8;
		}
		if (wins.size() > 0)
			return winner;
		if (array[0] != 0 && array[1] != 0 && array[2] != 0 && array[3] != 0 && array[4] != 0 && array[5] != 0
				&& array[6] != 0 && array[7] != 0 && array[8] != 0)
			return -1;
		return 0;
	}

	private static int checkWin2(int[] array, int square1, int square2, int square3) {
		if (array[square1 - 1] == 1 && array[square2 - 1] == 1 && array[square3 - 1] == 1)
			return 1;
		if (array[square1 - 1] == 2 && array[square2 - 1] == 2 && array[square3 - 1] == 2)
			return 2;
		return 0;
	}

	private static List checkAlmostWin(int[] array) {
		int square;
		List squares = new ArrayList();
		square = checkAlmostWin2(array, 1, 2, 3);
		if (square > 0)
			squares.add(square - 1);
		square = checkAlmostWin2(array, 4, 5, 6);
		if (square > 0)
			squares.add(square - 1);
		square = checkAlmostWin2(array, 7, 8, 9);
		if (square > 0)
			squares.add(square - 1);
		square = checkAlmostWin2(array, 1, 4, 7);
		if (square > 0)
			squares.add(square - 1);
		square = checkAlmostWin2(array, 2, 5, 8);
		if (square > 0)
			squares.add(square - 1);
		square = checkAlmostWin2(array, 3, 6, 9);
		if (square > 0)
			squares.add(square - 1);
		square = checkAlmostWin2(array, 1, 5, 9);
		if (square > 0)
			squares.add(square - 1);
		square = checkAlmostWin2(array, 3, 5, 7);
		if (square > 0)
			squares.add(square - 1);
		return squares;
	}

	private static int checkAlmostWin2(int[] array, int square1, int square2, int square3) {
		if (array[square1 - 1] == 1 && array[square2 - 1] == 1 && array[square3 - 1] == 0)
			return square3;
		if (array[square1 - 1] == 1 && array[square2 - 1] == 0 && array[square3 - 1] == 1)
			return square2;
		if (array[square1 - 1] == 0 && array[square2 - 1] == 1 && array[square3 - 1] == 1)
			return square1;
		return 0;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("minutes", minutes);
		compound.setInteger("platform", platform);
		compound.setInteger("maxmin", localMax + 1);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		minutes = nbt.getInteger("minutes");
		platform = nbt.getInteger("platform");
		maxMin = nbt.getInteger("maxmin");
		localMax = 0;
	}

	@Override
	public void update() {
		tickCounter++;
		if (tickCounter >= 39 && !worldObj.isRemote) {
			PlatformData data = PlatformData.get(worldObj);
			if (platform > 0) {
				minutes = data.getArrival(platform, 0);
				localMax = Math.max(minutes, localMax);
				worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
				markDirty();
			}
			tickCounter = 0;
		}
	}
}

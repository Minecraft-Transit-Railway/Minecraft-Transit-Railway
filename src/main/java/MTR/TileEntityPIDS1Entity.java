package MTR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;

public class TileEntityPIDS1Entity extends TileEntity {

	public int minutes;

	public static boolean game, turn;
	public static String p1, p2;
	public static List wins = new ArrayList();
	public static long cooldown;

	private static int board[] = new int[9];

	public static void clear() {
		for (int i = 0; i < 9; i++)
			board[i] = 0;
	}

	public static void move(int square, ICommandSender sender) {
		if (board[square - 1] != 0) {
			sender.addChatMessage(new ChatComponentText("This square is full!"));
			return;
		}
		board[square - 1] = turn ? 2 : 1;
		int result = checkWin(board);
		if (result > 0) {
			sender.addChatMessage(new ChatComponentText((turn ? p2 : p1) + " wins!"));
			cooldown = Calendar.getInstance().getTimeInMillis() + 3000;
			game = false;
			return;
		}
		if (result == -1) {
			sender.addChatMessage(new ChatComponentText("Tie game!!"));
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
			sender.addChatMessage(new ChatComponentText("AI wins!"));
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
		sender.addChatMessage(new ChatComponentText("X - " + p1 + ", O - " + (p2 == "" ? "AI" : p2)));
		sender.addChatMessage(new ChatComponentText(getSquareString(1) + getSquareString(2) + getSquareString(3)));
		sender.addChatMessage(new ChatComponentText(getSquareString(4) + getSquareString(5) + getSquareString(6)));
		sender.addChatMessage(new ChatComponentText(getSquareString(7) + getSquareString(8) + getSquareString(9)));
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
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new S35PacketUpdateTileEntity(pos, metadata, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void writeToNBT(NBTTagCompound parentNBTTagCompound) {
		super.writeToNBT(parentNBTTagCompound);
		parentNBTTagCompound.setInteger("minutes", minutes);
	}

	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		super.readFromNBT(parentNBTTagCompound);
		final int NBT_INT_ID = 3;
		int readPosition1 = -1;
		if (parentNBTTagCompound.hasKey("minutes", NBT_INT_ID)) {
			readPosition1 = parentNBTTagCompound.getInteger("minutes");
			if (readPosition1 < 0)
				readPosition1 = -1;
		}
		minutes = readPosition1;
	}
}

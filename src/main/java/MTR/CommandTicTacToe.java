package MTR;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandTicTacToe implements ICommand {

	private final List aliases;

	public CommandTicTacToe() {
		aliases = new ArrayList();
		aliases.add("ttt");
		aliases.add("tic");
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "tictactoe";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "tictactoe <text>";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		String name = sender.getName();

		if (args.length == 0) {
			sendError(sender);
			return;
		}

		// ttt begin
		if (args[0].contains("begin")) {
			if (TileEntityPIDS1Entity.game) {
				sender.addChatMessage(new ChatComponentText("A game is already in progress!"));
				return;
			}
			String player2 = "";
			if (args.length > 1)
				player2 = args[1];
			sender.addChatMessage(new ChatComponentText(
					name + " has begun a game of Tic Tac Toe with " + (player2 == "" ? "AI" : player2)));
			TileEntityPIDS1Entity.game = true;
			TileEntityPIDS1Entity.p1 = name;
			TileEntityPIDS1Entity.p2 = player2;
			TileEntityPIDS1Entity.turn = false;
			TileEntityPIDS1Entity.wins.clear();
			TileEntityPIDS1Entity.clear();
			return;
		}

		// ttt 1-9
		if (args[0].contains("1") || args[0].contains("2") || args[0].contains("3") || args[0].contains("4")
				|| args[0].contains("5") || args[0].contains("6") || args[0].contains("7") || args[0].contains("8")
				|| args[0].contains("9")) {
			if (!TileEntityPIDS1Entity.game) {
				sender.addChatMessage(new ChatComponentText("No game has been started."));
				return;
			}
			if (name.contains(TileEntityPIDS1Entity.p1) && name.length() == TileEntityPIDS1Entity.p1.length()
					&& !TileEntityPIDS1Entity.turn
					|| name.contains(TileEntityPIDS1Entity.p2) && name.length() == TileEntityPIDS1Entity.p2.length()
							&& TileEntityPIDS1Entity.turn) {
				try {
					TileEntityPIDS1Entity.move(Integer.valueOf(args[0]), sender);
				} catch (NumberFormatException e) {
					sendError(sender);
				}
				return;
			}
			sender.addChatMessage(new ChatComponentText("It is "
					+ (TileEntityPIDS1Entity.turn ? TileEntityPIDS1Entity.p2 : TileEntityPIDS1Entity.p1) + "'s turn!"));
			return;
		}

		// ttt show
		if (args[0].contains("show")) {
			if (!TileEntityPIDS1Entity.game) {
				sender.addChatMessage(new ChatComponentText("No game has been started."));
				return;
			}
			TileEntityPIDS1Entity.showBoard(sender);
			return;
		}

		// ttt end
		if (args[0].contains("end")) {
			if (name.contains(TileEntityPIDS1Entity.p1) && name.length() == TileEntityPIDS1Entity.p1.length()
					|| name.contains(TileEntityPIDS1Entity.p2) && name.length() == TileEntityPIDS1Entity.p2.length()) {
				TileEntityPIDS1Entity.game = false;
				sender.addChatMessage(new ChatComponentText("Game ended."));
				return;
			}
			if (!TileEntityPIDS1Entity.game) {
				sender.addChatMessage(new ChatComponentText("No game has been started."));
				return;
			}
		}

		sendError(sender);
	}

	private void sendError(ICommandSender sender) {
		sender.addChatMessage(new ChatComponentText(
				EnumChatFormatting.RED + "Usage: /ttt begin [player] OR /ttt <square> OR /ttt end"));
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}

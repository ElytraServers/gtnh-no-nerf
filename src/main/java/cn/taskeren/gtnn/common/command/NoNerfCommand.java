package cn.taskeren.gtnn.common.command;

import cn.taskeren.gtnn.GTNN;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class NoNerfCommand extends CommandBase {

	private static final URI URI_GTNN_REPO;

	static {
		try {
			URI_GTNN_REPO = new URI("https://github.com/Taskeren/gtnh-no-nerf");
		} catch(URISyntaxException ex) {
			throw new RuntimeException(ex); // not possible
		}
	}

	@Override
	public String getCommandName() {
		return "no-nerf";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Usage: no-nerf";
	}

	private static final String[] WELCOME_MESSAGES = new String[]{
		"/no-nerf             - show the welcome message",
		"/no-nerf disassemble - simulate the disassembling and give the result",
		"/no-nerf github      - go to the project repository",
		"Presented by GTNH-NO-NERF!",
	};

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		var p = sender instanceof EntityPlayer ? ((EntityPlayer) sender) : null;
		if(p == null) return;

		// simulate the disassembling and give the result to the player
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("disassemble")) {
				sender.addChatMessage(new ChatComponentText("This sub command is no longer working."));
				return;
			}

			if(args[0].equalsIgnoreCase("github")) {
				try {
					Desktop.getDesktop().browse(URI_GTNN_REPO);
				} catch(IOException ex) {
					sender.addChatMessage(new ChatComponentText("Failed! Check logs for details."));
					GTNN.logger.error("Unable to browse the repository!", ex);
				}
			}
		}

		for(String line : WELCOME_MESSAGES) {
			sender.addChatMessage(new ChatComponentText(line));
		}
	}
}

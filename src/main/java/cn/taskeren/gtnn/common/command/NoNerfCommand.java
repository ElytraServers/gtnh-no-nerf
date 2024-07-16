package cn.taskeren.gtnn.common.command;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.mixinplugin.Feature;
import cn.taskeren.gtnn.mixinplugin.MixinConfig;
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

			if(args[0].equalsIgnoreCase("mixin")) {
				if(args.length > 1) {
					var featureName = args[1];
					try {
						var feature = Feature.valueOf(featureName);

						if(args.length > 2) {
							var newStatusStr = args[2];
							switch(newStatusStr) {
								case "true", "false" -> {
									try {
										var newStatus = Boolean.parseBoolean(newStatusStr);
										MixinConfig.updateValue(feature, newStatus);
										sender.addChatMessage(new ChatComponentText("Successfully set the enable/disable value of " + feature.name() + " to " + newStatus + "."));
										sender.addChatMessage(new ChatComponentText("It requires restart to take effect!"));
										GTNN.logger.info("Changed the enable/disable value of the Mixin " + feature.name() + " to " + newStatus);
									} catch(Exception ex) {
										sender.addChatMessage(new ChatComponentText("Something goes wrong when changing the value of " + feature.name() + ", check logs for details."));
										GTNN.logger.error("Failed to change the enable/disable value of the Mixin " + feature.name(), ex);
									}
								}
								default -> {
									sender.addChatMessage(new ChatComponentText("Invalid parameter: " + newStatusStr + ", it should be either true or false!"));
								}
							}
						} else {
							sender.addChatMessage(new ChatComponentText(getFeatureStatusMessage(feature)));
						}
					} catch(IllegalArgumentException notFoundEx) {
						sender.addChatMessage(new ChatComponentText("Feature " + featureName + " not found."));
					}
				} else {
					sender.addChatMessage(new ChatComponentText("============[ GTNN Mixins ]============"));
					for(Feature feature : Feature.values()) {
						sender.addChatMessage(new ChatComponentText(getFeatureStatusMessage(feature)));
					}
				}
			}
		}

		for(String line : WELCOME_MESSAGES) {
			sender.addChatMessage(new ChatComponentText(line));
		}
	}

	private static String getFeatureStatusMessage(Feature feature) {
		return String.format(
			"[%s]: %s - %s",
			feature.name(),
			feature.desc,
			feature.isEnabled() ? "enabled" : "disabled"
		);
	}
}

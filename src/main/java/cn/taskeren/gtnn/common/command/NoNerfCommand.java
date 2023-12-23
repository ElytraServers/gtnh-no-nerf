package cn.taskeren.gtnn.common.command;

import cn.taskeren.gtnn.mod.gt5u.util.NNRecipe;
import gregtech.api.enums.GT_Values;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NoNerfCommand extends CommandBase {

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
		"Presented by GTNH-NO-NERF!",
	};

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		var p = sender instanceof EntityPlayer ? ((EntityPlayer) sender) : null;
		if(p == null) return;

		// simulate the disassembling and give the result to the player
		if(args.length > 0 && args[0].equalsIgnoreCase("disassemble")) {
			var hand = p.getHeldItem();

			var r = NNRecipe.Disassembler.getRecipeMap().findRecipe(null, true, GT_Values.V[15], new FluidStack[0], hand);
			var toString = ToStringBuilder.reflectionToString(r, ToStringStyle.MULTI_LINE_STYLE);

			System.out.println(toString);
			for(String line : toString.split("\n")) {
				sender.addChatMessage(new ChatComponentText(line));
			}
			return;
		}

		for(String line : WELCOME_MESSAGES) {
			sender.addChatMessage(new ChatComponentText(line));
		}
	}
}

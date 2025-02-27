package cn.elytra.mod.gtnn.common

import cn.elytra.mod.gtnn.GTNN
import cn.elytra.mod.gtnn.modules.mixins.MixinLoader
import cn.elytra.mod.gtnn.modules.simple.module.disassembler.DisassemblerHelper
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ChatComponentText
import java.awt.Desktop
import java.net.URI

object GtnnCommand : CommandBase() {

	val GithubRepoUri = URI("https://github.com/ElytraServers/gtnh-no-nerf")

	override fun getCommandName(): String {
		return "gtnh-no-nerf"
	}

	override fun getCommandAliases(): List<String> {
		return listOf("gtnn")
	}

	override fun getCommandUsage(sender: ICommandSender): String {
		return "command.gtnn.usage"
	}

	override fun addTabCompletionOptions(
		sender: ICommandSender,
		argsArray: Array<String>,
	): List<String> {
		val args = argsArray.toMutableList()
		return when(args.removeFirstOrNull()) {
			"" -> listOf("github", "loaded-mixins", "help", "disassemble-debug-index")
			else -> listOf()
		}
	}

	override fun processCommand(
		sender: ICommandSender,
		argsArray: Array<String>,
	) {
		val p = sender as? EntityPlayer
		val args = argsArray.toMutableList()

		when(args.removeFirstOrNull()) {
			"github" -> {
				runCatching {
					Desktop.getDesktop().browse(GithubRepoUri)
				}.onFailure {
					sender.addChatMessage(ChatComponentText("Failed to open the URL, see logs for details."))
				}
			}

			"loaded-mixins" -> {
				val loadedMixinNames = MixinLoader.loadedMixinModules.joinToString(", ") { it.id }
				sender.addChatMessage(ChatComponentText("Loaded Mixin Modules: [${loadedMixinNames}]"))
			}

			"disassemble-debug-index" -> {
				val debugIndexStr = args.removeFirstOrNull()
					?: return sender.addChatMessage(ChatComponentText("Debug index required"))
				val debugIndex = debugIndexStr.toIntOrNull()
					?: return sender.addChatMessage(ChatComponentText("Debug index is an integer"))
				val debugIndexToRecipe = DisassemblerHelper.debugIndexToRecipe
				if(debugIndexToRecipe == null) {
					return sender.addChatMessage(ChatComponentText("Debug mode is not enabled"))
				}
				val info = debugIndexToRecipe.getOrElse(debugIndex) {
					return sender.addChatMessage(ChatComponentText("Debug index $debugIndex is not found!"))
				}
				info.getInfo().lineSequence().forEach { GTNN.logger.info(it) }
			}

			else -> {
				arrayOf(
					"/gtnn help - show this help message",
					"/gtnn github - open the repository of gtnn",
					"/gtnn loaded-mixins - list loaded mixin modules",
					"/gtnn disassemble-debug-index - (with disassembler debug mode enabled) get the related recipe info with given debug index"
				).forEach { sender.addChatMessage(ChatComponentText(it)) }
			}
		}
	}
}

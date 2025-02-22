package cn.elytra.mod.gtnn.common

import cn.elytra.mod.gtnn.modules.mixins.MixinLoader
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
			"" -> listOf("github", "loaded-mixins", "help")
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
				sender.addChatMessage(ChatComponentText("Loaded Mixin Modules: [${MixinLoader.loadedMixinModules.joinToString(", ")}]"))
			}
			else -> {
				arrayOf(
					"/gtnn help - show this help message",
					"/gtnn github - open the repository of gtnn",
					"/gtnn loaded-mixins - list loaded mixin modules",
				).forEach { sender.addChatMessage(ChatComponentText(it)) }
			}
		}
	}
}

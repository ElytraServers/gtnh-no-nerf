package cn.elytra.mod.gtnn.modules.mixins

class MustBeEnabledException(mixinModule: IMixinModule) : Exception("${mixinModule.id} must be enabled!")

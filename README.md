# 格雷新视野：憋鸡巴削了！

[English](README_EN.md)

回滚 GTNH 内容的模组，尤其是那些削弱某些机器的改动。

[Modrinth](https://modrinth.com/mod/gtnh-no-nerf)
[MCMOD百科](https://www.mcmod.cn/class/10327.html)

*我玩啥他削啥，这怎么玩！*

![Return of the Disassembler!](docs/disassembler.png)

## 改动列表

### v1.0.0 (GTNH v2.3.3)

- [X] 回滚拆解机 (GTNewHorizons/GT5-Unofficial#1855)
- [X] 避雷针的铁栅栏不再会在发电时被破坏 (GTNewHorizons/GT5-Unofficial#1875)

### v1.1.0 (GTNH v2.3.6)

- [X] 大源质发电机（GoodGenerator）重新支持激光能源仓 (GTNewHorizons/GoodGenerator#168)

![Return of Laser Hatches on LEG](docs/leg-laser-hatch.png)

### v1.2.0

- 修复崩溃

### v1.3.1 (GTNH 2.5.1)

- [X] 回退超临界灵宝和铿铀削弱（#4）
- [X] 添加 Mixin 注入配置文件（#6）

### v1.3.2 (GTNH 2.5.1)

- [X] 回滚火箭燃料修改（GTNewHorizons/GTplusplus#223）（#7）

### v1.4.0 (GTNH v2.6.0)

- 修复崩溃
- 修改部分拆解机配方

### v1.4.1 (GTNH v2.6.0)

- 修复崩溃

### v1.4.2 (GTNH v2.6.0)

- [X] 重写拆解机配方
- [X] 移除 RawInput（J8玩家请使用 [RawInputMod 1.7.10](https://github.com/seanld03/RawInputMod-1.12.2-1.7.10)，J17及以上玩家请使用 LWJGL3ify 提供的 RawInput）

### v1.5.0-v1.5.2 (GTNH v2.7.0)

- [X] 重写GTNN设置
- [X] 量子操纵者的水线（#17）
- [X] 铟蜂窝的简单处理配方（#17）
- [X] 处理阵列
- [X] 无限大小的 ME 输出仓/输出总线（#13）
- [X] 大源质发电机（#18）
  - [ ] 大源质的复制体

## FAQ

### 我希望能够回滚XXX？

你可以开启一个新的 Issue，附上能够找到的相关信息，尤其是该改动的 Pull Request 最佳。

### 怎么文字描述没变？

简而言之，因为懒。只修改代码逻辑部分相对简单，我懒得把时间浪费在改文本上，相信使用本模组的用户们应该也都是知道功能的。

### 设置 `use-gtnn-version` 和 `use-deprecated-recipe` 的区别

- `use-gtnn-version` 为 `true` 时，将会添加 gtnn 内建的这个机器的复制版本，与本来的机器是不同的两个（在复制的当下，应该与本来机器的机制是一样的）。这个复制体可能会造成 MTE Id 的冲突，你可以通过修改 `id` 选项来避免加载崩溃。（在这些机器被完全移除以前，这个选项默认是禁用的。并且对应的 `id` 项是与原本机器是一样的，所以当这个机器被完全移除后，可以直接在原地替换）
- `use-deprecated-recipe` 为 `true` 时，将会重新添加这个机器的合成表，原料和输出都是和原来一模一样的。

## 感谢

第一次下载量达到四位数的模组，非常感谢大家的支持。

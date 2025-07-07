# 捕鱼达人游戏

一个基于Java Swing开发的经典捕鱼达人游戏。

## 技术栈

- **Java 8+**
- **Swing** - 图形用户界面
- **Maven** - 项目构建和依赖管理
- **Lombok** - 简化Java代码
- **SLF4J + Logback** - 日志管理
- **dd-plist** - plist配置文件解析

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/xiaoxuan010/game2507.git
cd game2507
```

### 2. 使用Maven构建

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn exec:java -Dexec.mainClass="top.xiaoxuan010.learn.game.GameMain"
```

### 3. 使用IDE运行

1. 使用IntelliJ IDEA或Eclipse导入Maven项目
2. 等待依赖下载完成
3. 找到 `GameMain.java` 文件
4. 右键选择"运行"或使用快捷键运行

### 4. 打包为可执行JAR

```bash
# 打包项目
mvn clean package

# 运行打包后的JAR文件
java -jar target/game2507-1.0-SNAPSHOT.jar
```

---

**祝你游戏愉快！** 🎣
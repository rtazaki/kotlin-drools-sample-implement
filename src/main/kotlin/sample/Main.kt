package sample

import org.drools.decisiontable.InputType
import org.drools.decisiontable.SpreadsheetCompiler
import org.kie.api.KieServices
import org.kie.api.builder.Message
import org.kie.api.runtime.KieSession
import java.io.File

fun main(args: Array<String>) {
    // リンゴ
    val apples = listOf(
            Apple(size = 1),
            Apple(size = 5),
            Apple(size = 10),
            Apple(size = 30),
            Apple(size = 70),
            Apple(size = 120)
    )

    // 春夏秋冬
    // 季節ごとの価格を、外に出すのが目的。
    val seasons = listOf(
            Season(name = "春"),
            Season(name = "夏"),
            Season(name = "秋"),
            Season(name = "冬")
    )

    // ルールとルールを組み合わせて結果を表示させたい。
    // でもまだできていない。
    val output = Output(sp = 0)

    val kieSession = getKieSession()
    kieSession.let {
        seasons.forEach { season ->
            apples.forEach { apple ->
                it.insert(season)
                it.insert(apple)
                it.insert(output)
                it.fireAllRules()
                println(" サイズ: " + apple.size +
                        " ランク: " + apple.rank +
                        " 元価格: " + apple.price +
                        " 季節: " + season.name +
                        " 係数: " + season.coefficient +
                        " -> 季節を加えた価格(アプリ側で計算): " + apple.price * season.coefficient +
                        " ルール側で計算(これがやりたい): " + output.sp
                )
            }
        }
    }
}

// 1ファイルではなく、複数のルールファイルを読み込ませたいが、 どうやるんだろう?
fun getKieSession(rules: String = "src/main/resources/rule"): KieSession {
    val kieServices = KieServices.Factory.get()
    val kieFileSystem = kieServices.newKieFileSystem()
    lateinit var kieSession: KieSession

    File(rules).listFiles().forEach { file ->
        if (file.extension == "xls") {
            // (debug) xlsをdrl形式で表示する
            file.inputStream().use {
                val sc = SpreadsheetCompiler()
                println(sc.compile(it, InputType.XLS))
            }
            file.inputStream().use {
                // inputStreamからの読み込み
                kieFileSystem.write(
                        "src/main/resources/rules.xls",
                        kieServices.resources.newInputStreamResource(it)
                )
            }
        }
    }

    val kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll()
    // ルールファイル記述内容のチェック
    val results = kieBuilder.results
    if (results.hasMessages(Message.Level.ERROR)) {
        println(results.toString())
        throw IllegalStateException(">>>ルールファイルの記述に問題があります。")
    }
    kieSession = kieServices
            .newKieContainer(kieServices.repository.defaultReleaseId)
            .newKieSession()
    return kieSession
}

data class Apple(val size: Int, var rank: String = "", var price: Int = 0)
data class Season(val name: String, var coefficient: Double = 0.0)
data class Output(var sp: Int)

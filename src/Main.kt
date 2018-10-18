import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        convertTxtToSQLite()
    }

    fun convertTxtToSQLite() {

        val url = "jdbc:sqlite:/Users/quang/IdeaProjects/ConvertTxtToSqlite/data/data.sqlite"
        var conn: Connection? = null
        try {
            conn = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            println(e.message)
        }
        conn!!.autoCommit = false
        val sql = "INSERT INTO anhviet(tu,nghia) VALUES(?,?)"
        val pst = conn.prepareStatement(sql)

        val bufferedReader = File("/Users/quang/IdeaProjects/ConvertTxtToSqlite/data/en_vi.txt").bufferedReader()

        val inputString = bufferedReader.use { it.readText() }
        val regex = Regex("@.*\n")
        val listKey = regex.findAll(inputString).toList()
        val listWord = regex.split(inputString)

        for (i in 0 until listKey.size) {
            val key = listKey[i].value.replace("@", "").replace(Regex("/.*/"), "")
            val word = listWord[i + 1]
            pst.setString(1, key)
            pst.setString(2, word)
            pst.addBatch()
        }
        pst.executeBatch()
        conn.commit()
    }
}
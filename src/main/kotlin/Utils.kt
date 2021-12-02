import java.nio.file.Path

class Utils {

    companion object {
        fun inPath(name: String): Path {
            return Path.of("in", name)
        }
    }
}
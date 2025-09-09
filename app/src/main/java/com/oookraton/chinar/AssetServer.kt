import android.content.Context
import fi.iki.elonen.NanoHTTPD

class AssetServer(context: Context, port: Int = 8080) : NanoHTTPD(port) {
    private val assetManager = context.assets

    override fun serve(session: IHTTPSession): Response {
        var uri = session.uri
        if (uri == "/") uri = "/360.html"

        if (uri.contains("..")) {
            return newFixedLengthResponse(Response.Status.FORBIDDEN, "text/plain", "Forbidden")
        }

        val filePath = uri.removePrefix("/")
        return try {
            val assetStream = assetManager.open(filePath)
            val mimeType = getMimeType(filePath)
            val length = assetStream.available().toLong()

            newFixedLengthResponse(Response.Status.OK, mimeType, assetStream, length).apply {
                addHeader("Access-Control-Allow-Origin", "*")
                addHeader("Cache-Control", "no-cache")
            }
        } catch (e: Exception) {
            newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "File not found: $filePath")
        }
    }

    private fun getMimeType(uri: String): String {
        return when {
            uri.endsWith(".html", true) -> "text/html"
            uri.endsWith(".js", true) -> "application/javascript"
            uri.endsWith(".css", true) -> "text/css"
            uri.endsWith(".jpg", true) || uri.endsWith(".jpeg", true) -> "image/jpeg"
            uri.endsWith(".png", true) -> "image/png"
            uri.endsWith(".json", true) -> "application/json"
            else -> "application/octet-stream"
        }
    }
}
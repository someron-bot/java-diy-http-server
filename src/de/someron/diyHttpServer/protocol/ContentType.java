package de.someron.diyHttpServer.protocol;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

// Made with https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
public enum ContentType {
    DEFAULT("application/octet-stream", null),

    // Text
    CSS("text/css", "css"),
    CSV("text/csv", "csv"),
    HTML("text/html", "html"),
    HTM("text/html", "htm"),
    ICALENDER("text/calendar", "ics"),
    JAVASCRIPT_FILE("text/javascript", "js"),
    JAVASCRIPT_MODULE("text/javascript", "mjs"),
    TEXT_PLAIN("text/plain", "txt"),


    // Fonts
    MS_OPENTYPE_FONTS("application/vnd.ms-fontobject", "eot"),
    OPEN_TYPE_FONT("font/otf", "oft"),
    TRUETYPE_FONT("font/ttf", "ttf"),
    WEB_OPEN_FONT("font/woff", "woff"),
    WEB_OPEN_FONT2("font/woff2", "woff2"),


    // Audio
    AAC_AUDIO("audio/aac", "aac"),
    MIDI("audio/midi", "midi"),
    MID("audio/midi", "mid"),
    MP3("audio/mp3", "mp3"),
    OGG_AUDIO("audio/ogg", "oga"),
    OPUS("audio/opus", "opus"),
    WAV("audio/wav", "wav"),
    WEBM_AUDIO("audio/webm", "weba"),
    AUDIO_3GPP("audio/3gpp", "3gp"),
    AUDIO_3GPP2("audio/3gpp2", "3g2"),


    // Video
    AVI("application/x-msvideo", "avi"),
    MPEG("video/mpeg", "mpeg"),
    OGG_VIDEO("video/ogg", "ogv"),
    MPEG_STREAM("video/mp2t", "ts"),
    WEBM_VIDEO("video/webm", "webm"),
    VIDEO_3GPP("video/3gpp", "3gp"),
    VIDEO_3GPP2("video/3gpp2", "3g2"),


    // Image
    BITMAP("image/bmp", "bmp"),
    GIF("image/gif", "gif"),
    ICON("image/vnd.microsoft.icon", "ico"),
    JPG("image/jpeg", "jpg"),
    JPEG("image/jpeg", "jpeg"),
    PNG("image/png", "otf"),
    SVG("image/svg+xml", "svg"),
    TIFF("tif", "image/tiff"),
    WEBP_IMAGE("image/webp", "webp"),


    // Application
    ARCHIVE_DOCUMENT("application/x-freearc", "arc"),
    BINARY("application/octet-stream", "bin"),
    BZIP("application/x-bzip", "bz"),
    BZIP2("application/x-bzip2", "bz2"),
    C_SHELL("application/x-csh", "csh"),
    EPUB("application/epub+zip", "epub"),
    GZIP("application/gzip", "gz"),
    ZIP("application/zip", "zip"),
    JAR("application/java-archive", "jar"),
    JSON("application/json", "json"),
    JSON_LD("application/ld+json", "jsonld"),
    APPLE_INSTALLER_PACKAGE("application/vnd.apple.installer+xml", "mpkg"),
    OGG("application/ogg", "ogx"),
    PDF("application/pdf", "pdf"),
    PHP("application/x-httpd-php", "php"),
    RAR("application/vdn.rar", "rar"),
    RICH_TEXT("application/rtf", "rtf"),
    BOURNE_SHELL_SCRIPT("application/x-sh", "sh"),
    SHOCKWAFE_FLASH("application/x-shockwave-flash", "swf"),
    TAR("application/x-tar", "tar"),
    MS_VISIO("application/vnd.visio", "vsd"),
    XHTML("application/xhtml+xml", "xhtml"),
    XML("application/xml", "xml"),
    XUL("application/vnd.mozilla.xul+xml", "xul"),
    ARCHIVE_7ZIP("application/x-7z-compressed", "7z"),

    // Office files
    MS_WORD("application/msword", "doc"),
    MS_WORD_OPENXML("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    EXCEL("application/vnd.ms-excel", "xls"),
    EXCEL_OPENXML("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    POWER_POINT("application/vnd.ms-powerpoint", "ppt"),
    POWER_POINT_OPENXML("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx"),
    ABIWORD("application/x-abiword", "abw"),
    OPEN_DOC_PRESENTATION("application/vnd.oasis.opendocument.presentation", "odp"),
    OPEN_DOC_SPREADSHEET("application/vnd.oasis.opendocument.spreadsheet", "ods"),
    OPEN_DOC_TEXTDOC("application/vnd.oasis.opendocument.text", "odt");


    private static HashMap<String, ContentType> rawValueContentTypeMap = new HashMap<>();
    private static HashMap<String, ContentType> extensionContentTypeMap = new HashMap<>();
    private static HashMap<String[], ContentType> prefixContentTypeMap = new HashMap<>();

    public final String rawValue;
    public final String extension;
    ContentType(String rawValue, String extension) {
        this.rawValue = rawValue;
        this.extension = extension;
    }

    /**
     * A ContentType search method applicable to both files and requests.
     * Any nonexistent fields are null
     * @param rawContentType The Content-Type header if present
     * @param content The content if present
     * @param extension The fileextension
     * @return The corresponding ContentType
     */
    public static ContentType getContentType(@Nullable String rawContentType, @Nullable String content, @Nullable String extension) {
        if(rawContentType != null && getByRawHeaderValue(rawContentType) != DEFAULT) {
            return getByRawHeaderValue(rawContentType);
        } else if(extension != null) {
            return getByFileExtension(extension);
        } else {
            return DEFAULT;
        }
    }

    /**
     * @param rawHeaderValue The raw value for the {@code Content-Type} Header
     * @return The corresponding content type
     */
    public static ContentType getByRawHeaderValue(String rawHeaderValue) {
        if(rawValueContentTypeMap.isEmpty()) for(ContentType type : ContentType.values()) rawValueContentTypeMap.put(type.rawValue, type);
        return rawValueContentTypeMap.getOrDefault(rawHeaderValue, DEFAULT);
    }

    /**
     * @param extension The file extension for the file excluding the file separator.
     * @return The corresponding content type
     */
    public static ContentType getByFileExtension(String extension) {
        if(extensionContentTypeMap.isEmpty()) for(ContentType type : ContentType.values()) extensionContentTypeMap.put(type.extension, type);
        return extensionContentTypeMap.getOrDefault(extension, DEFAULT);
    }
}
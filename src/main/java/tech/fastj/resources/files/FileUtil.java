package tech.fastj.resources.files;

import java.nio.file.Path;

/**
 * Class that provides supplementary methods for working with files and {@link Path}s.
 *
 * @author Andrew Dey
 * @since 1.5.0
 */
public class FileUtil {

    /**
     * Gets the file extension of the specified path.
     * <p>
     * This method does <b>not</b> account for file extensions with more than one dot ({@code .}) -- in cases like
     * those, only the last part of the extension will be returned.
     * <p>
     * For paths which contain no file extension, an empty {@code String} will be returned.
     *
     * @param filePath The {@code Path} to get the file extension of.
     * @return The {@code Path}'s file extension.
     */
    public static String getFileExtension(Path filePath) {
        if (filePath.toString().contains(".")) {
            return filePath.toString().substring(filePath.toString().lastIndexOf(".") + 1);
        }
        return "";
    }
}

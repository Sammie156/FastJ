package tech.fastj.resources.models;

import tech.fastj.engine.CrashMessages;
import tech.fastj.math.Maths;
import tech.fastj.math.Pointf;
import tech.fastj.graphics.Boundary;
import tech.fastj.graphics.game.Model2D;
import tech.fastj.graphics.game.Polygon2D;

import tech.fastj.resources.files.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ObjUtil {

    private static final String LineSeparator = System.lineSeparator();

    private ObjUtil() {
        throw new java.lang.IllegalStateException();
    }

    public static Polygon2D[] parse(List<String> lines) {
        return null;
    }

    public static void write(Path destinationPath, Model2D model) {
        StringBuilder fileContents = new StringBuilder();
        writeTimestamp(fileContents);

        Path destinationPathWithoutSpaces = Path.of(destinationPath.toString().replace(' ', '_'));
        int extensionIndex = destinationPathWithoutSpaces.toString().indexOf(FileUtil.getFileExtension(destinationPathWithoutSpaces));
        Path materialPath = Path.of(destinationPathWithoutSpaces.toString().substring(0, extensionIndex) + "mtl");

        writeMaterialLib(fileContents, materialPath);

        int vertexCount = 0;
        for (int i = 0; i < model.getPolygons().length; i++) {
            writeVertexes(fileContents, model.getPolygons()[i], i);
        }
        fileContents.append(LineSeparator);

        for (int i = 0; i < model.getPolygons().length; i++) {
            writeVertexTextures(fileContents, model.getPolygons()[i]);
        }
        fileContents.append(LineSeparator);

        for (int i = 0; i < model.getPolygons().length; i++) {
            Polygon2D polygon = model.getPolygons()[i];
            writeObject(fileContents, i + 1);
            writeMaterial(fileContents, polygon, i + 1, vertexCount);

            vertexCount += polygon.getPoints().length;
        }

        try {
            Files.writeString(destinationPath, fileContents, StandardCharsets.US_ASCII);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    CrashMessages.theGameCrashed("a ." + SupportedModelFormats.Obj + " file writing error."),
                    exception
            );
        }

        MtlUtil.write(materialPath, model);
    }

    private static void writeTimestamp(StringBuilder fileContents) {
        fileContents.append("# Generated by the FastJ Game Engine https://github.com/fastjengine/FastJ")
                .append(LineSeparator)
                .append("# Timestamp: ")
                .append(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                .append(LineSeparator)
                .append(LineSeparator);
    }

    private static void writeMaterialLib(StringBuilder fileContents, Path materialPath) {
        fileContents.append(ParsingKeys.MaterialLib)
                .append(' ')
                .append(materialPath.toString())
                .append(LineSeparator)
                .append(LineSeparator);
    }

    private static void writeVertexes(StringBuilder fileContents, Polygon2D polygon, int polygonIndex) {
        float vertexSpace = polygonIndex / 1000f;
        for (int j = 0; j < polygon.getPoints().length; j++) {
            Pointf vertex = polygon.getPoints()[j];
            fileContents.append(ParsingKeys.Vertex)
                    .append(' ')
                    .append(String.format("%4f", vertex.x))
                    .append(' ')
                    .append(String.format("%4f", vertex.y))
                    .append(' ')
                    .append(String.format("%4f", vertexSpace))
                    .append(LineSeparator);
        }
    }

    private static void writeVertexTextures(StringBuilder fileContents, Polygon2D polygon) {
        Pointf space = Pointf.subtract(polygon.getBound(Boundary.BottomRight), polygon.getBound(Boundary.TopLeft));
        Pointf topLeft = polygon.getBound(Boundary.TopLeft);

        for (int j = 0; j < polygon.getPoints().length; j++) {
            float circleX = Maths.normalize(polygon.getPoints()[j].x - topLeft.x, 0f, space.x);
            float circleY = Maths.normalize(polygon.getPoints()[j].y - topLeft.y, 0f, space.y);
            fileContents.append(ParsingKeys.VertexTexture)
                    .append(' ')
                    .append(String.format("%4f", circleX))
                    .append(' ')
                    .append(String.format("%4f", circleY))
                    .append(LineSeparator);
        }
    }

    private static void writeObject(StringBuilder fileContents, int polygonIndex) {
        fileContents.append(ParsingKeys.ObjectName)
                .append(' ')
                .append("Polygon2D_")
                .append(polygonIndex)
                .append(LineSeparator);
    }

    private static void writeMaterial(StringBuilder fileContents, Polygon2D polygon, int polygonIndex, int vertexCount) {
        switch (polygon.getRenderStyle()) {
            case Fill: {
                writeFillMaterialUsage(fileContents, polygonIndex);
                writeFaces(fileContents, polygon, vertexCount);
                break;
            }
            case Outline: {
                writeOutlineMaterialUsage(fileContents, polygonIndex);
                writeLines(fileContents, polygon, vertexCount);
                break;
            }
            case FillAndOutline: {
                writeFillMaterialUsage(fileContents, polygonIndex);
                writeFaces(fileContents, polygon, vertexCount);
                writeOutlineMaterialUsage(fileContents, polygonIndex);
                writeLines(fileContents, polygon, vertexCount);
                break;
            }
        }
        fileContents.append(LineSeparator);
    }

    private static void writeFillMaterialUsage(StringBuilder fileContents, int polygonIndex) {
        fileContents.append(ParsingKeys.UseMaterial)
                .append(' ')
                .append("Polygon2D_material_fill_")
                .append(polygonIndex)
                .append(LineSeparator);
    }

    private static void writeOutlineMaterialUsage(StringBuilder fileContents, int polygonIndex) {
        fileContents.append(ParsingKeys.UseMaterial)
                .append(' ')
                .append("Polygon2D_material_outline_")
                .append(polygonIndex)
                .append(LineSeparator);
    }

    private static void writeFaces(StringBuilder fileContents, Polygon2D polygon, int vertexCount) {
        fileContents.append(ParsingKeys.ObjectFace);
        for (int i = 1; i <= polygon.getPoints().length; i++) {
            fileContents.append(' ')
                    .append(vertexCount + i)
                    .append('/')
                    .append(vertexCount + i);
        }
        fileContents.append(LineSeparator);
    }

    private static void writeLines(StringBuilder fileContents, Polygon2D polygon, int vertexCount) {
        fileContents.append(ParsingKeys.ObjectLine);
        for (int i = 1; i <= polygon.getPoints().length; i++) {
            fileContents.append(' ').append(vertexCount + i);
        }
        fileContents.append(LineSeparator);
    }

    public static class ParsingKeys {
        private ParsingKeys() {
            throw new java.lang.IllegalStateException();
        }

        public static final String Empty = "";
        public static final String MaterialLib = "mtllib";
        public static final String Vertex = "v";
        public static final String VertexTexture = "vt";
        public static final String ObjectName = "g";
        public static final String UseMaterial = "usemtl";
        public static final String ObjectFace = "f";
        public static final String ObjectLine = "l";
    }
}

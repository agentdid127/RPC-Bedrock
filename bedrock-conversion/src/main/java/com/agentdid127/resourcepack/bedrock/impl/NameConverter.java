package com.agentdid127.resourcepack.bedrock.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.bedrock.util.ImageConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class NameConverter extends Converter {

    protected final Mapping blockMapping = new BlockMapping();
    protected final Mapping blockTGAMapping = new BlockTGAMapping();

    public NameConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path textures_old = pack.getWorkingPath().resolve("assets").resolve("minecraft").resolve("textures");
        Path textures = pack.getWorkingPath().resolve("textures");
        PackConverter.log("      Moving Textures");
        if (textures_old.toFile().exists())
        Util.copyDir(textures_old, textures);


        Path blocks = textures.resolve("block");

        //TODO: Find what images need to be renamed, and rename them

        if (blocks.toFile().exists()) {

            Files.move(blocks, textures.resolve("blocks"));
            renameAll(blockMapping, ".png", textures.resolve("blocks"));
            renameAll(blockTGAMapping, ".png", ".tga", textures.resolve("blocks"));

            //grass_block_side_overlay -> grass_side
            if (textures.resolve("blocks").resolve("grass_block_side_overlay.png").toFile().exists()) {
                ImageConverter grass = new ImageConverter(16, 16, textures.resolve("blocks").resolve("grass_block_side_overlay.png"));
                grass.backgroundImage(textures.resolve("blocks").resolve("dirt.png"));
                System.out.println("Grass Conversion: " + grass.storeTGA());
                Util.renameFile(textures.resolve("blocks").resolve("grass_block_side_overlay.png"), "grass_side.tga");
            }
            }


    }



    protected void renameAll(Mapping mapping, String extension, String newExtension, Path path) throws IOException {
        Files.list(path).forEach(path1 -> {
            if (!path1.toString().endsWith(extension)) return;

            String baseName = path1.getFileName().toString().substring(0, path1.getFileName().toString().length() - extension.length());

            String newName = mapping.remap(baseName);
            if (newName != null && !newName.equals(baseName)) {
                try {
                    System.out.println(baseName + "->" + newName + ", " + extension + "->" + newExtension);
                    if (newExtension.equals(".tga")) {
                        ImageConverter i = new ImageConverter(16, 16, path1);
                        i.storeTGA();
                        //Path path1_old = path1;
                        //path1 = ;

                        Boolean ret = Util.renameFile(path1, newName + newExtension);
                        if (ret == null) return;
                        if (ret && packConverter.DEBUG) {
                            packConverter.log("      Renamed: " + path1.getFileName().toString() + "->" + newName + newExtension);
                        }
                        else if (!ret) {
                            packConverter.log("      Failed to Rename: " + path1.getFileName().toString() + "->" + newName + newExtension);
                        }
                    }
                    else {
                        Boolean ret = Util.renameFile(path1, newName + newExtension);
                        if (ret == null) return;
                        if (ret && packConverter.DEBUG) {
                            packConverter.log("      Renamed: " + path1.getFileName().toString() + "->" + newName + newExtension);
                        }
                        else if (!ret) {
                            packConverter.log("      Failed to Rename: " + path1.getFileName().toString() + "->" + newName + newExtension);
                        }
                    }
                    } catch (IOException e) {
                    packConverter.log("      Failed to Rename: " + path1.getFileName().toString() + "->" + newName + newExtension);
                    e.printStackTrace();
                }

            }
        });
    }
    protected void renameAll(Mapping mapping, String extension, Path path) throws IOException {
        Files.list(path).forEach(path1 -> {
            if (!path1.toString().endsWith(extension)) return;

            String baseName = path1.getFileName().toString().substring(0, path1.getFileName().toString().length() - extension.length());

            String newName = mapping.remap(baseName);
            if (newName != null && !newName.equals(baseName)) {
                Boolean ret = Util.renameFile(path1, newName + extension);
                if (ret == null) return;
                if (ret && packConverter.DEBUG) {
                    packConverter.log("      Renamed: " + path1.getFileName().toString() + "->" + newName + extension);
                }
                else if (!ret) {
                    packConverter.log("      Failed to Rename: " + path1.getFileName().toString() + "->" + newName + extension);
                }
            }
        });
    }

    protected abstract static class Mapping {

        protected final Map<String, String> mapping = new HashMap<>();

        public Mapping() {
            load();
        }

        protected abstract void load();

        /**
         * @return remapped or in if not present
         */
        public String remap(String in) {
            return mapping.getOrDefault(in, in);
        }
    }

    protected class BlockMapping extends Mapping {

        @Override
        protected void load() {
            JsonObject blocks = Util.readJsonResource(packConverter.getGson(), "/blocks.json").getAsJsonObject("blocks");
            if (blocks != null) {
                for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class BlockTGAMapping extends Mapping {

        @Override
        protected void load() {
            JsonObject blocks = Util.readJsonResource(packConverter.getGson(), "/blocks.json").getAsJsonObject("tga");
            if (blocks != null) {
                for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }
}



package com.agentdid127.resourcepack.bedrock.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NameConverter extends Converter {
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

        if (blocks.toFile().exists()) {

            Files.move(blocks, textures.resolve("blocks"));
        }

        //TODO: Find what images need to be renamed, and rename them
    }
}

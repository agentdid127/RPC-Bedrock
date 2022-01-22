package com.agentdid127.resourcepack.bedrock.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.io.IOException;
import java.nio.file.Files;

public class AssetsRemovalConverter extends Converter {
    public AssetsRemovalConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        if (!pack.getWorkingPath().resolve("assets").toFile().exists()) return;

        PackConverter.log("      Removing unused assets");
        Util.deleteDirectoryAndContents(pack.getWorkingPath().resolve("assets"));

        if (!pack.getWorkingPath().resolve("pack.mcmeta").toFile().exists()) return;

        Files.delete(pack.getWorkingPath().resolve("pack.mcmeta"));
    }
}

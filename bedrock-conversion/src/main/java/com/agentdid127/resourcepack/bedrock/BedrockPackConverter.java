package com.agentdid127.resourcepack.bedrock;

import com.agentdid127.resourcepack.bedrock.impl.AssetsRemovalConverter;
import com.agentdid127.resourcepack.bedrock.impl.PackMetaConverter;
import com.agentdid127.resourcepack.bedrock.impl.NameConverter;
import com.agentdid127.resourcepack.bedrock.impl.PackPngConverter;
import com.agentdid127.resourcepack.bedrock.pack.BPack;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class BedrockPackConverter extends PackConverter {
    Path INPUT_DIR;

    public BedrockPackConverter(Path input, boolean debug, boolean minify, PrintStream out) {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        if (!minify) gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        DEBUG = debug;
        PackConverter.out = out;
        this.INPUT_DIR = input;

        log("Java");
        log("Bedrock");

        converterRunner();
    }


    protected void converterRunner() {
        this.registerConverter(new NameConverter(this));
        this.registerConverter(new PackMetaConverter(this));
        this.registerConverter(new PackPngConverter(this));
        this.registerConverter(new AssetsRemovalConverter(this));
    }

    public void runPack(Pack pack) {
        try {
            System.out.println(pack.getClass());
            log("Converting " + pack);

            pack.getHandler().setup();

            log("  Running Converters");
            for (Converter converter : converters.values()) {
                if (DEBUG)
                    log("    Running " + converter.getClass().getSimpleName());
                converter.convert(pack);
            }

            pack.getHandler().finish();
        } catch (Throwable t) {
            log("Failed to convert!");
            Util.propagate(t);
        }
    }

    public void runDir() throws IOException {
        Files.list(INPUT_DIR)
                .map(BPack::parse)
                .filter(Objects::nonNull)
                .forEach(pack -> {
                    runPack(pack);
                });

    }
}

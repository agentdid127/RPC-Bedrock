package com.agentdid127.resourcepack.bedrock.pack;

import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.IOException;
import java.nio.file.Path;

public class BPack extends Pack {

    public BPack(Path path) {
        super(path);
    }

    @Override
    public BPack.Handler createHandler() {
        return new BPack.Handler(this);
    }

    @Override
    public String getFileName() {
        return path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4);
    }

    /**
     * Checks the type of pack it is.
     * @param path
     * @return
     */
    public static Pack parse(Path path) {
        if (!path.toString().contains(CONVERTED_SUFFIX)) {
            if (path.toFile().isDirectory() && path.resolve("pack.mcmeta").toFile().exists()) {
                return new BPack(path);
            } else if (path.toString().endsWith(".zip")) {
                return new BZipPack(path);
            }
        }
        return null;
    }

    public static class Handler extends Pack.Handler {

        public Handler(Pack pack) {
            super(pack);
        }

        public Path getConvertedZipPath() {
            return pack.getWorkingPath().getParent().resolve(pack.getWorkingPath().getFileName() + ".zip");
        }

        /**
         * Runs after program is finished. Zips directory.
         * @throws IOException
         */
        @Override
        public void finish() throws IOException {
            try {
                PackConverter.log("  Zipping working directory");
                ZipFile zipFile = new ZipFile(getConvertedZipPath().toFile());
                ZipParameters parameters = new ZipParameters();
                parameters.setIncludeRootFolder(false);
                zipFile.createSplitZipFileFromFolder(pack.getWorkingPath().toFile(), parameters, false, 65536);
                Util.renameFile(getConvertedZipPath(), pack.getWorkingPath().getFileName() + ".mcpack");
            } catch (ZipException e) {
                Util.propagate(e);
            }

            PackConverter.log("  Deleting working directory");
            Util.deleteDirectoryAndContents(pack.getWorkingPath());
        }

        @Override
        public String toString() {
            return "Handler{} " + super.toString();
        }
    }
}
package com.agentdid127.resourcepack;

import com.agentdid127.resourcepack.bedrock.BedrockPackConverter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) throws IOException {
        PrintStream out = System.out;
        boolean minify = false;

        new BedrockPackConverter(Paths.get("./"), true, minify, out).runDir();
    }
    }

package net.autoignition.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoIgnitionConfig {

    // CONFIG CODEC
    public static final BuilderCodec<AutoIgnitionConfig> CODEC;


    // CONFIG VALUES
    private long UpdateIntervalMs = 1000L;
    private long ScannerIntervalMs = 10000L;
    private boolean EnableOutputTransfer = true;
    private boolean EnableAutoRefuel = true;
    private boolean EnableAutoFuelStart = true;
    private boolean EnableAutoFuelStop = true;
    private List<String> BlacklistedFuelItems = new ArrayList<>(List.of("Wood_Sallow_Trunk"));


    public AutoIgnitionConfig() { }


    public long getUpdateIntervalMs() {
        return Math.max(500L, UpdateIntervalMs);
    }

    public long getScannerIntervalMs() {
        return Math.max(5000L, ScannerIntervalMs);
    }

    public boolean isEnableOutputTransfer() {
        return EnableOutputTransfer;
    }

    public boolean isEnableAutoRefuel() {
        return EnableOutputTransfer;
    }

    public boolean isEnableAutoFuelStart() {
        return EnableAutoFuelStart;
    }

    public boolean isEnableAutoFuelStop() {
        return EnableOutputTransfer;
    }

    public List<String> getBlacklistedFuelItems() {
        return BlacklistedFuelItems;
    }


    static {
        CODEC = BuilderCodec.builder(AutoIgnitionConfig.class, AutoIgnitionConfig::new)
                        .append(new KeyedCodec<>("UpdateIntervalMs", Codec.LONG),
                                (config, value, info) -> config.UpdateIntervalMs = value,
                                (config, info) -> config.UpdateIntervalMs)
                        .add()
                        .append(new KeyedCodec<>("ScannerIntervalMs", Codec.LONG),
                                (config, value, info) -> config.ScannerIntervalMs = value,
                                (config, info) -> config.ScannerIntervalMs)
                        .add()
                        .append(new KeyedCodec<>("EnableOutputTransfer", Codec.BOOLEAN),
                                (config, value, info) -> config.EnableOutputTransfer = value,
                                (config, info) -> config.EnableOutputTransfer)
                        .add()
                        .append(new KeyedCodec<>("EnableAutoRefuel", Codec.BOOLEAN),
                                (config, value, info) -> config.EnableAutoRefuel = value,
                                (config, info) -> config.EnableAutoRefuel)
                        .add()
                    .append(new KeyedCodec<>("EnableAutoFuelStart", Codec.BOOLEAN),
                            (config, value, info) -> config.EnableAutoFuelStart = value,
                            (config, info) -> config.EnableAutoFuelStart)
                    .add()
                    .append(new KeyedCodec<>("EnableAutoFuelStop", Codec.BOOLEAN),
                            (config, value, info) -> config.EnableAutoFuelStop = value,
                            (config, info) -> config.EnableAutoFuelStop)
                    .add()
                        .append(new KeyedCodec<>("BlacklistedFuelItems", Codec.STRING_ARRAY),
                                (config, value, info) -> config.BlacklistedFuelItems = new ArrayList<>(Arrays.asList(value)),
                                (config, info) -> config.BlacklistedFuelItems.toArray(new String[0]))
                        .add()
                        .build();

    }
}

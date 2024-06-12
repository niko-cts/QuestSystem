package net.playlegend.questsystem.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemToBase64ConverterUtil {

    private ItemToBase64ConverterUtil() {
        throw new UnsupportedOperationException("ItemToBase64ConverterUtil is a utility class and cannot be instantiated");
    }

    public static String toBase64(final ItemStack item) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeObject(item);
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    public static ItemStack fromBase64(final String base64Item) throws IOException, ClassCastException, ClassNotFoundException {
        return (ItemStack) new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decode(base64Item))).readObject();
    }
}

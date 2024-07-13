package chatzis.nikolas.questsystem.util;

import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class ItemToBase64ConverterUtil {

    private ItemToBase64ConverterUtil() {
        throw new UnsupportedOperationException("ItemToBase64ConverterUtil is a utility class and cannot be instantiated");
    }

    public static String toBase64(@NonNull final ItemStack item) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeObject(item.serialize());
        dataOutput.close();
        return new String(Base64Coder.encode(outputStream.toByteArray()));
    }

    public static ItemStack fromBase64(@NonNull final String base64Item) throws IOException, ClassCastException, ClassNotFoundException {
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decode(base64Item)));
        ItemStack item = ItemStack.deserialize((Map<String, Object>) dataInput.readObject());
        dataInput.close();
        return item;
    }
}

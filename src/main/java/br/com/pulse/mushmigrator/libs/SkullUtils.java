package br.com.pulse.mushmigrator.libs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtils {
    protected static final MethodHandle PROFILE_GETTER;

    protected static final MethodHandle PROFILE_SETTER;

    private static final String VALUE_PROPERTY = "{\"textures\":{\"SKIN\":{\"url\":\"";

    private static final boolean SUPPORTS_UUID = XMaterial.supports(12);

    private static final String TEXTURES = "https://textures.minecraft.net/texture/";

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle profileSetter = null, profileGetter = null;
        try {
            Class<?> CraftMetaSkull = ReflectionUtils.getCraftClass("inventory.CraftMetaSkull");
            Field profile = CraftMetaSkull.getDeclaredField("profile");
            profile.setAccessible(true);
            profileGetter = lookup.unreflectGetter(profile);
            try {
                Method setProfile = CraftMetaSkull.getDeclaredMethod("setProfile", new Class[] { GameProfile.class });
                setProfile.setAccessible(true);
                profileSetter = lookup.unreflect(setProfile);
            } catch (NoSuchMethodException e) {
                profileSetter = lookup.unreflectSetter(profile);
            }
        } catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
        }
        PROFILE_SETTER = profileSetter;
        PROFILE_GETTER = profileGetter;
    }

    @Nonnull
    public static ItemStack getSkull(@Nonnull UUID id) {
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta)head.getItemMeta();
        if (SUPPORTS_UUID) {
            meta.setOwner(Bukkit.getOfflinePlayer(id).getName());
        } else {
            meta.setOwner(id.toString());
        }
        head.setItemMeta((ItemMeta)meta);
        return head;
    }

    @Nonnull
    public static SkullMeta applySkin(@Nonnull ItemMeta head, @Nonnull OfflinePlayer identifier) {
        SkullMeta meta = (SkullMeta)head;
        if (SUPPORTS_UUID) {
            meta.setOwner(identifier.getName());
        } else {
            meta.setOwner(identifier.getName());
        }
        return meta;
    }

    @Nonnull
    public static SkullMeta applySkin(@Nonnull ItemMeta head, @Nonnull UUID identifier) {
        return applySkin(head, Bukkit.getOfflinePlayer(identifier));
    }

    @Nonnull
    public static SkullMeta applySkin(@Nonnull ItemMeta head, @Nonnull String identifier) {
        SkullMeta meta = (SkullMeta)head;
        if (isUsername(identifier))
            return applySkin(head, Bukkit.getOfflinePlayer(identifier));
        if (identifier.contains("textures.minecraft.net"))
            return getValueFromTextures(meta, identifier);
        if (identifier.length() > 100 && isBase64(identifier))
            return getSkullByValue(meta, identifier);
        return getTexturesFromUrlValue(meta, identifier);
    }

    @Nonnull
    protected static SkullMeta getSkullByValue(@Nonnull SkullMeta head, @Nonnull String value) {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("Skull value cannot be null or empty");
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));
        try {
            PROFILE_SETTER.invoke(head, profile);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return head;
    }

    @Nonnull
    private static SkullMeta getValueFromTextures(@Nonnull SkullMeta head, @Nonnull String url) {
        return getSkullByValue(head, encodeBase64("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}"));
    }

    @Nonnull
    private static SkullMeta getTexturesFromUrlValue(@Nonnull SkullMeta head, @Nonnull String urlValue) {
        return getValueFromTextures(head, "https://textures.minecraft.net/texture/" + urlValue);
    }

    @Nonnull
    private static String encodeBase64(@Nonnull String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    private static boolean isBase64(@Nonnull String base64) {
        try {
            Base64.getDecoder().decode(base64);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    @Nullable
    public static String getSkinValue(@Nonnull ItemMeta skull) {
        Objects.requireNonNull(skull, "Skull ItemStack cannot be null");
        SkullMeta meta = (SkullMeta)skull;
        GameProfile profile = null;
        try {
            profile = (GameProfile) PROFILE_GETTER.invoke(meta);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        if (profile != null && !profile.getProperties().get("textures").isEmpty())
            for (Property property : profile.getProperties().get("textures")) {
                if (!property.value().isEmpty())
                    return property.value();
            }
        return null;
    }

    private static boolean isUsername(@Nonnull String name) {
        int len = name.length();
        if (len < 3 || len > 16)
            return false;
        for (UnmodifiableIterator<Character> unmodifiableIterator = Lists.charactersOf(name).iterator(); unmodifiableIterator.hasNext(); ) {
            char ch = ((Character)unmodifiableIterator.next()).charValue();
            if (ch != '_' && (ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z') && (ch < '0' || ch > '9'))
                return false;
        }
        return true;
    }
}

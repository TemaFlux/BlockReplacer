package me.temaflex.blockreplacer.api;

import org.bukkit.material.MaterialData;

import me.temaflex.blockreplacer.Utils;

import org.bukkit.Bukkit;
import java.util.EnumSet;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

@SuppressWarnings({ "unused", "deprecation" })
public class IDLibrary {
    public final String SEPARATOR = ":";
    private final String Spawn_Egg_Id = "383";
    private final int Spawn_Egg_Id_I;
    private final IDList List;
    
    //www.spigotmc.org/resources/id-library-old-id-support-lib-new-minecraft-ids-1-13-x-1-16-x.62124/
    public IDLibrary() {
        this.Spawn_Egg_Id_I = Integer.parseInt("383");
        this.List = new IDList(this);
    }
    
    private static boolean isLegacy() {
        try {
            Material.valueOf("CONDUIT");
            return false;
        }
        catch (Exception e) {
            return true;
        }
    }
    
    public Material getMaterial(String ID) {
        if (isLegacy()) {
            final ItemStack IS = this.getItemStack(ID);
            return (IS == null) ? null : IS.getType();
        }
        ID = ID.replace(" ", "").toUpperCase();
        Material t = this.List.getMaterial(ID.contains(":") ? ID : (String.valueOf(ID) + ":" + "0"));
        if (t != null) {
            return t;
        }
        try {
            t = Material.valueOf(ID);
            if (this.List.getIDData(t) != null) {
                return t;
            }
        }
        catch (Exception ex) {}
        if (ID.startsWith("383")) {
            return null;
        }
        if (!ID.contains(":") && !Utils.isInteger(ID)) {
            try {
                t = Material.valueOf(ID);
                if (t != null) {
                    return t;
                }
            }
            catch (Exception ex2) {}
        }
        if (ID.contains(":")) {
            final String[] IDs = ID.split(":");
            try {
                return this.getMaterial(Integer.parseInt(IDs[0]), Byte.parseByte(IDs[1]));
            }
            catch (Exception e) {
                final Material m = Material.getMaterial("LEGACY_" + IDs[0], false);
                try {
                    return (m == null) ? m : this.getMaterial(m.getId(), Byte.parseByte(IDs[1]));
                }
                catch (Exception e2) {
                    return null;
                }
            }
        }
        try {
            return this.getMaterial(Integer.parseInt(ID));
        }
        catch (Exception e3) {
            final Material i = Material.getMaterial("LEGACY_" + ID, false);
            return (i == null) ? i : this.getMaterial(i.getId(), (byte)0);
        }
    }
    
    private Material getMaterial(final int ID) {
        return this.getMaterial(ID, (byte)0);
    }
    
    private Material getMaterial(final int ID, final byte Data) {
        for (final Material i : EnumSet.allOf(Material.class)) {
            try {
                if (i.getId() == ID) {
                    final Material m = Bukkit.getUnsafe().fromLegacy(new MaterialData(i, Data));
                    return ((m == Material.AIR && (ID != 0 || Data != 0)) || (Data != 0 && m == Bukkit.getUnsafe().fromLegacy(new MaterialData(i, (byte)0)))) ? this.List.getMaterial(String.valueOf(ID) + ":" + Data) : m;
                }
                continue;
            }
            catch (IllegalArgumentException ex) {}
        }
        return null;
    }
    
    public ItemStack getItemStack(String ID) {
        if (isLegacy()) {
            final ItemStack IS = null;
            ID = ID.replace(" ", "").toUpperCase();
            if (!ID.contains(":")) {
                ID = String.valueOf(ID) + ":" + "0";
            }
            final String[] I = ID.split(":");
            int id = 0;
            try {
                id = Integer.parseInt(I[0]);
            }
            catch (NumberFormatException e) {
                try {
                    id = Material.valueOf(I[0]).getId();
                }
                catch (IllegalArgumentException e2) {
                    return IS;
                }
            }
            try {
                for (final Material i : EnumSet.allOf(Material.class)) {
                    if (i.getId() == id) {
                        return new ItemStack(i, 1, (short)Integer.parseInt(I[1]));
                    }
                }
            }
            catch (Exception ex) {}
            return IS;
        }
        final Material M = this.getMaterial(ID);
        return (M == null) ? null : new ItemStack(this.getMaterial(ID), 1);
    }
    
    public String getIDData(final Material M) {
        final byte d = this.getData(M);
        return String.valueOf(this.getID(M)) + ((d == 0) ? "" : (":" + d));
    }
    
    public int getID(final Material M) {
        if (isLegacy()) {
            M.getId();
        }
        final int d = this.List.getID(M);
        if (d != -1) {
            return d;
        }
        final int i = Bukkit.getUnsafe().toLegacy(M).getId();
        return (i != this.Spawn_Egg_Id_I && (i != 0 || (i == 0 && M == Material.AIR))) ? i : 0;
    }
    
    public byte getData(final Material M) {
        if (isLegacy()) {
            return 0;
        }
        final byte d = this.List.getData(M);
        if (d != -1) {
            return d;
        }
        final int i = Bukkit.getUnsafe().toLegacy(M).getId();
        return (byte)((i != this.Spawn_Egg_Id_I && (i != 0 || (i == 0 && M == Material.AIR))) ? this.getData(M, i, Bukkit.getUnsafe().toLegacy(M)) : 0);
    }
    
    private byte getData(final Material M, final int ID, final Material O) {
        for (final Material i : EnumSet.allOf(Material.class)) {
            try {
                if (i.getId() != ID) {
                    continue;
                }
                for (byte i2 = 0; i2 <= 15; ++i2) {
                    if (M.equals(Bukkit.getUnsafe().fromLegacy(new MaterialData(i, i2)))) {
                        return i2;
                    }
                }
            }
            catch (IllegalArgumentException ex) {}
        }
        return 0;
    }
    
    public String getIDData(final ItemStack IS) {
        final byte d = this.getData(IS);
        return String.valueOf(this.getID(IS)) + ((d == 0) ? "" : (":" + d));
    }
    
    public int getID(final ItemStack IS) {
        if (!isLegacy()) {
            return this.getID(IS.getType());
        }
        return IS.getType().getId();
    }
    
    public byte getData(final ItemStack IS) {
        if (!isLegacy()) {
            return this.getData(IS.getType());
        }
        return IS.getData().getData();
    }
}

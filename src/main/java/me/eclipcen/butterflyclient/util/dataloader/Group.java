package me.eclipcen.butterflyclient.util.dataloader;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import me.eclipcen.butterflyclient.util.image.Texture;

public class Group implements AutoCloseable {
    public final String id;
    public final String name;
    public final Set<UUID> members;
    public final int color;
    public final AtomicReference<Texture> cape = new AtomicReference<>(null);
    public final AtomicReference<Texture> icon = new AtomicReference<>(null);

    public Group(String id, String name, Set<UUID> members, int color, Texture cape, Texture icon) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = name == null ? id : name;
        this.members = Collections.unmodifiableSet(Objects.requireNonNull(members, "members"));
        this.color = color & 16777215;
        this.cape.set(cape);
        this.icon.set(icon);
    }

    public void doWithCapeIfPresent(Consumer<Texture> callback) {
        Texture cape = this.cape.get();
        if (cape != null) {
            callback.accept(cape);
        }
    }

    public void doWithIconIfPresent(Consumer<Texture> callback) {
        Texture icon = this.icon.get();
        if (icon != null) {
            callback.accept(icon);
        }
    }

    @Override
    public void close() {
        this.cape.updateAndGet(loc -> {
            if (loc != null) {
                loc.close();
            }
            return null;
        });
        this.icon.updateAndGet(loc -> {
            if (loc != null) {
                loc.close();
            }
            return null;
        });
    }
}
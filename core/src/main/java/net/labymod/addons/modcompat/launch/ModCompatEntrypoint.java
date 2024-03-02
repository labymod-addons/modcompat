package net.labymod.addons.modcompat.launch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import net.labymod.addons.modcompat.ModCompat;
import net.labymod.addons.modcompat.hook.AddonHooks;
import net.labymod.addons.modcompat.mod.DefaultIncompatibleMod;
import net.labymod.addons.modcompat.mod.DefaultIncompatibleModRegistry;
import net.labymod.addons.modcompat.mod.IncompatibleMod;
import net.labymod.addons.modcompat.mod.IncompatibleModRegistry;
import net.labymod.api.Laby;
import net.labymod.api.addon.entrypoint.Entrypoint;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.modloader.ModLoadEvent;
import net.labymod.api.models.addon.annotation.AddonEntryPoint;
import net.labymod.api.models.version.Version;
import net.labymod.api.models.version.VersionCompatibility;
import net.labymod.api.util.gson.VersionTypeAdapter;
import net.labymod.api.util.version.serial.VersionCompatibilityDeserializer;

@SuppressWarnings("UnstableApiUsage")
@AddonEntryPoint(priority = 100)
public class ModCompatEntrypoint implements Entrypoint {

  private static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(Version.class, new VersionTypeAdapter())
      .registerTypeAdapter(VersionCompatibility.class, new VersionCompatibilityDeserializer())
      .create();
  private static final Type INDEX_TYPE = TypeToken.getParameterized(
      Collection.class,
      DefaultIncompatibleMod.class
  ).getType();

  private static final String INDEX_PATH = "index.json";

  @Override
  public void initialize(Version version) {
    ModCompat.init(new DefaultIncompatibleModRegistry());

    try (InputStream inputStream = this.getClass().getClassLoader()
        .getResourceAsStream(INDEX_PATH)) {
      if (inputStream == null) {
        throw new RuntimeException("Mod compat index was not found on the classpath");
      }

      try (InputStreamReader reader = new InputStreamReader(inputStream)) {
        Collection<IncompatibleMod> modCompatIndex = GSON.fromJson(reader, INDEX_TYPE);

        for (IncompatibleMod incompatibleMod : modCompatIndex) {
          for (String id : incompatibleMod.getIds()) {
            ModCompat.instance().incompatibleModRegistry().register(
                id,
                incompatibleMod
            );
          }
        }
      }
    } catch (IOException exception) {
      throw new RuntimeException("Failed to read mod compat index", exception);
    }

    Laby.references().eventBus().registerListener(this);
    Laby.references().eventBus().registerListener(AddonHooks.instance());
  }

  @Subscribe
  public void onModLoad(ModLoadEvent event) {
    IncompatibleModRegistry incompatibleModRegistry = ModCompat.instance()
        .incompatibleModRegistry();

    if (!incompatibleModRegistry.isModPlayable(event.modInfo().getId())) {
      event.setCancelled(true);
    }
  }
}

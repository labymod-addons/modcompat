package net.labymod.addons.modcompat.fabricapi.accesswidener;

import net.labymod.addons.modcompat.util.HashMultimap;
import net.labymod.api.loader.platform.PlatformEnvironment;
import net.labymod.api.util.collection.map.Multimap;
import net.labymod.api.util.logging.Logging;
import net.minecraftforge.srgutils.IMappingFile;
import net.minecraftforge.srgutils.IMappingFile.IClass;
import net.minecraftforge.srgutils.IMappingFile.IMethod;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AccessWatcherService {

  private static final Multimap<String, String> SIMPLE_MAPPINGS = new HashMultimap<>(
      new ConcurrentHashMap<>()
  );
  private static final Logging LOGGER = Logging.getLogger();
  private static final Map<String, Integer> ACCESSES = new HashMap<>();
  private static final String ID = "fabric";
  private static boolean loaded;

  @Nullable
  public static Collection<String> getValues(String key) {
    load();
    return SIMPLE_MAPPINGS.get(key);
  }

  public static void setAccess(String key, int access) {
    load();
    ACCESSES.put(key, access);
  }

  public static int getAccess(String key) {
    load();
    return ACCESSES.getOrDefault(key, Integer.MAX_VALUE);
  }

  private static void load() {
    if (loaded) {
      return;
    }
    loaded = true;

    String runningVersion = PlatformEnvironment.getRunningVersion();

    ClassLoader loader = AccessWatcherService.class.getClassLoader();
    URL resource = loader.getResource("access_watchers/" + ID + "-" + runningVersion + ".tsrg2");
    if (resource == null) {
      LOGGER.info("No access watcher exists for version {}", runningVersion);
      return;
    }

    try (InputStream stream = resource.openStream()) {
      IMappingFile mappingFile = IMappingFile.load(stream);
      for (IClass cls : mappingFile.getClasses()) {
        for (IMethod method : cls.getMethods()) {
          SIMPLE_MAPPINGS.put(cls.getOriginal(), method.getOriginal() + method.getDescriptor());
        }
      }
    } catch (IOException exception) {
      LOGGER.error("Access watcher cannot be loaded, this can lead to crashes", exception);
    }

    LOGGER.info("{} access watchers have been loaded", SIMPLE_MAPPINGS.size());
  }


}

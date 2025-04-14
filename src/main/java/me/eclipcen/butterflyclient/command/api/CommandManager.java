package me.eclipcen.butterflyclient.command.api;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import me.eclipcen.butterflyclient.command.impl.ModuleCommand;
import me.eclipcen.butterflyclient.module.impl.service.CommandService;
import me.eclipcen.butterflyclient.util.client.AbstractClassLoader;
import me.eclipcen.butterflyclient.util.client.ClassLoaderHelper;

public final class CommandManager extends AbstractClassLoader<Command> {
   private final HashMap<String, Command> commandNames = new HashMap<>();

   @Override
   public Class<Command> getInheritedClass() {
      return Command.class;
   }

   public void registerCommands() {
      String packageDir = "me.eclipcen.butterflyclient.command.impl.*";

      try {
         for (Class<? extends Command> clazz : filterClassPaths(
                 getFMLClassLoader(), ClassLoaderHelper.getClassPathsInPackage(getFMLClassLoader(), "me.eclipcen.butterflyclient.command.impl.*")
         )) {
            Command command = create(clazz);
            if (!(command instanceof ModuleCommand)) {
               registerCommand(command);
            }
            registerCommand(command);
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   public void registerCommand(Command command) {
      if (command == null) {
         return;
      }

      commandNames.put(command.getName(), command);
      if (command.getAliases() != null) {
         for (String alias : command.getAliases()) {
            commandNames.put(alias, command);
         }
      }
   }

   public void runCommand(String command) {
      try {
         String[] split = command.split(" ");
         String commandName = split[0].substring(1);
         Command cmd = commandNames.get(commandName.toLowerCase());
         if (cmd != null) {
            if (split.length < cmd.getMinLength()) {
               printChatMessage("\u00A74Not enough arguments given!");
               return;
            }

            if (split.length > cmd.getMaxLength()) {
               printChatMessage("\u00A74Too many arguments given!");
               return;
            }

            cmd.execute(split);
            return;
         }

         printChatMessage("\u00A74Bad command! Type .help for a list of commands!");
      } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException ignored) {
      }
   }

   public String getSuggestionFor(String input) {
      String[] split = input.split(" ");

      try {
         String commandName = split[0].substring(1);
         Command command = commandNames.get(commandName);
         if (command != null) {
            return CommandService.prefix.getValue() + command.getSuggestion(split);
         }

         for (Entry<String, Command> entry : commandNames.entrySet()) {
            if (entry.getKey().startsWith(commandName)) {
               return CommandService.prefix.getValue() + entry.getKey();
            }
         }
      } catch (StringIndexOutOfBoundsException ignored) {
      }

      return "";
   }

   public HashMap<String, Command> getCommandNames() {
      return commandNames;
   }
}

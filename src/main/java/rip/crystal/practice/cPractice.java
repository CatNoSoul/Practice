package rip.crystal.practice;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.Item;
import rip.crystal.practice.chunk.ChunkRestorationManager;
import rip.crystal.practice.database.MongoConnection;
import rip.crystal.practice.essentials.abilities.AbilityManager;
import rip.crystal.practice.essentials.abilities.command.AbilityCommand;
import rip.crystal.practice.essentials.chat.cPracticeChatFormat;
import rip.crystal.practice.essentials.command.donator.*;
import rip.crystal.practice.essentials.command.player.*;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.ArenaListener;
import rip.crystal.practice.game.arena.command.ArenaCommand;
import rip.crystal.practice.game.arena.command.ArenasCommand;
import rip.crystal.practice.essentials.chat.impl.Chat;
import rip.crystal.practice.essentials.chat.impl.ChatListener;
import rip.crystal.practice.essentials.chat.impl.command.ClearChatCommand;
import rip.crystal.practice.essentials.chat.impl.command.MuteChatCommand;
import rip.crystal.practice.essentials.chat.impl.command.SlowChatCommand;
import rip.crystal.practice.game.tournament.managers.TournamentManager;
import rip.crystal.practice.match.command.*;
import rip.crystal.practice.match.listeners.impl.MatchBuildListener;
import rip.crystal.practice.match.listeners.impl.MatchPearlListener;
import rip.crystal.practice.match.listeners.impl.MatchPlayerListener;
import rip.crystal.practice.match.listeners.impl.MatchSpecialListener;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.clan.ClanListener;
import rip.crystal.practice.player.clan.commands.ClanCommand;
import rip.crystal.practice.player.cosmetics.command.CosmeticsCommand;
import rip.crystal.practice.player.cosmetics.impl.killeffects.command.KillEffectCommand;
import rip.crystal.practice.player.cosmetics.impl.trails.command.TrailEffectCommand;
import rip.crystal.practice.match.duel.command.*;
import rip.crystal.practice.essentials.Essentials;
import rip.crystal.practice.essentials.EssentialsListener;
import rip.crystal.practice.essentials.command.management.AdminInformationCommand;
import rip.crystal.practice.essentials.command.management.SetSlotsCommand;
import rip.crystal.practice.essentials.command.management.SetSpawnCommand;
import rip.crystal.practice.essentials.command.management.cPracticeCommand;
import rip.crystal.practice.essentials.command.staff.*;
import rip.crystal.practice.game.event.Event;
import rip.crystal.practice.game.event.command.EventCommand;
import rip.crystal.practice.game.event.command.EventsCommand;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.EventGameListener;
import rip.crystal.practice.game.event.game.command.EventHostCommand;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.game.event.game.map.vote.command.EventMapVoteCommand;
import rip.crystal.practice.game.event.impl.spleef.SpleefGameLogic;
import rip.crystal.practice.game.event.impl.tntrun.TNTRunGameLogic;
import rip.crystal.practice.game.ffa.FFAListener;
import rip.crystal.practice.game.ffa.FFAManager;
import rip.crystal.practice.game.ffa.command.FFACommand;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.kit.KitEditorListener;
import rip.crystal.practice.game.kit.command.HCFClassCommand;
import rip.crystal.practice.game.kit.command.KitCommand;
import rip.crystal.practice.game.kit.command.KitsCommand;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.player.nametags.cPracticeTags;
import rip.crystal.practice.utilities.EntityHider;
import rip.crystal.practice.utilities.lag.LagRunnable;
import rip.crystal.practice.visual.leaderboard.Leaderboard;
import rip.crystal.practice.visual.leaderboard.LeaderboardListener;
import rip.crystal.practice.visual.leaderboard.PlaceholderAPI;
import rip.crystal.practice.visual.leaderboard.commands.*;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.listeners.MatchListener;
import rip.crystal.practice.player.nametags.GxNameTag;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.party.listeners.PartyListener;
import rip.crystal.practice.player.party.classes.ClassTask;
import rip.crystal.practice.player.party.classes.archer.ArcherClass;
import rip.crystal.practice.player.party.classes.bard.BardEnergyTask;
import rip.crystal.practice.player.party.classes.bard.BardListener;
import rip.crystal.practice.player.party.classes.rogue.RogueClass;
import rip.crystal.practice.player.party.command.PartyCommand;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileListener;
import rip.crystal.practice.player.profile.conversation.command.MessageCommand;
import rip.crystal.practice.player.profile.conversation.command.ReplyCommand;
import rip.crystal.practice.player.profile.file.impl.FlatFileIProfile;
import rip.crystal.practice.player.profile.file.impl.MongoDBIProfile;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.meta.option.command.*;
import rip.crystal.practice.player.profile.modmode.ModmodeListener;
import rip.crystal.practice.player.profile.modmode.commands.StaffModeCommand;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.player.queue.QueueListener;
import rip.crystal.practice.visual.scoreboard.BoardAdapter;
import rip.crystal.practice.shop.ShopSystem;
import rip.crystal.practice.shop.command.CoinsCommand;
import rip.crystal.practice.shop.command.ShopCommand;
import rip.crystal.practice.shop.command.staff.CoinsStaffCommand;
import rip.crystal.practice.visual.tablist.TabAdapter;
import rip.crystal.practice.visual.tablist.impl.TabList;
import rip.crystal.practice.game.tournament.listeners.TournamentListener;
import rip.crystal.practice.game.tournament.commands.TournamentCommand;
import rip.crystal.practice.utilities.Animation;
import rip.crystal.practice.utilities.InventoryUtil;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.language.LanguageConfigurationFile;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import rip.crystal.practice.utilities.menu.MenuListener;
import rip.crystal.practice.utilities.playerversion.PlayerVersionHandler;
import rip.crystal.practice.api.command.CommandManager;
import rip.crystal.practice.api.rank.RankManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;

@Getter @Setter
public class cPractice extends JavaPlugin {

    private String prefix = "&7[&4cPractice&7] &7";

    private LanguageConfigurationFile lang;
    private BasicConfigurationFile mainConfig, databaseConfig, arenasConfig, kitsConfig, eventsConfig,
            scoreboardConfig, coloredRanksConfig, tabLobbyConfig, tabEventConfig, tabSingleFFAFightConfig,
            tabSingleTeamFightConfig, tabPartyFFAFightConfig, tabPartyTeamFightConfig, leaderboardConfig,
            langConfig, hotbarConfig, playersConfig, clansConfig, categoriesConfig, abilityConfig, kiteditorConfig,
            npcConfig, queueConfig, lunarConfig, tabFFAConfig, potionConfig, menuConfig, ffaConfig, pearlConfig;

    private Essentials essentials;

    private RankManager rankManager;
    private AbilityManager abilityManager;
    private FFAManager ffaManager;
    private ShopSystem shopSystem;
    private ChunkRestorationManager chunkRestorationManager;
    private Hotbar hotbar;
    private TournamentManager tournamentManager;

    private EntityHider entityHider;

    private MongoConnection mongoConnection;

    private ProtocolManager protocolManager;

    public boolean placeholderAPI = false;
    public int inQueues, inFights, bridgeRounds, rankedSumoRounds;

    @Override
    public void onEnable() {
        loadConfig();

        loadSaveMethod();
        loadEssentials();
        initManagers();

        registerNameTags();

        registerCommands();
        registerListeners();

        removeCrafting();

        setUpWorld();
        runTasks();

        CC.loadPlugin();
        
        if(!cPractice.get().getDescription().getAuthors().contains("ziue")) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "You edited the plugin.yml, please don't do that"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Please check your plugin.yml and try again."));
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "            &cDisabling cPractice"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!cPractice.get().getDescription().getName().contains("cPractice")) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "You edited the plugin.yml, please don't do that"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Please check your plugin.yml and try again."));
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "            &cDisabling cPractice"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        Profile.getProfiles().values().stream().filter(Profile::isOnline).forEach(Profile::save);
        if (EventGame.getActiveGame() != null) {
            if (EventGame.getActiveGame().getGameLogic() instanceof SpleefGameLogic) {
                SpleefGameLogic event = (SpleefGameLogic) EventGame.getActiveGame().getGameLogic();
                event.endEvent();
            } else if (EventGame.getActiveGame().getGameLogic() instanceof TNTRunGameLogic) {
                TNTRunGameLogic event = (TNTRunGameLogic) EventGame.getActiveGame().getGameLogic();
                event.endEvent();
            }
        }
        Match.cleanup();
        Clan.getClans().values().forEach(clan -> clan.save(false));
        Kit.getKits().forEach(Kit::save);
        Arena.getArenas().forEach(Arena::save);
    }

    private void initManagers() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Initializing all managers for cPractice"));

        this.essentials = new Essentials(this);
        this.rankManager = new RankManager(this);
        this.rankManager.loadRank();

        this.abilityManager = new AbilityManager();
        this.abilityManager.load();

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        this.ffaManager = new FFAManager();
        this.ffaManager.init();

        this.shopSystem = new ShopSystem();
        this.chunkRestorationManager = new ChunkRestorationManager();
        this.hotbar = new Hotbar();

        this.entityHider = new EntityHider(this, EntityHider.Policy.BLACKLIST);
        this.entityHider.init();

        this.tournamentManager = new TournamentManager();

        Kit.init();
        Arena.init();
        Profile.init();
        Match.init();
        Party.init();
        Knockback.init();

        Event.init();
        EventGameMap.init();

        Clan.init();
        Queue.init();
        Animation.init();
        GxNameTag.hook();
        BoardAdapter.hook();
        Leaderboard.init();
        PlayerVersionHandler.init();
        Chat.setChatFormat(new cPracticeChatFormat());
        if (mainConfig.getBoolean("TABLIST_ENABLE")) {
            new TabList(this, new TabAdapter());
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Tablist expansion successfully registered."));
        }
        placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholderAPI) {
            new PlaceholderAPI().register();
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Placeholder API expansion successfully registered."));
        }
    }

    private void loadConfig() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Initializing all configs for cPractice"));

        this.mainConfig = new BasicConfigurationFile(this, "config");
        this.databaseConfig = new BasicConfigurationFile(this, "database");

        this.arenasConfig = new BasicConfigurationFile(this, "cache/arenas");
        this.kitsConfig = new BasicConfigurationFile(this, "cache/kits");

        this.langConfig = new BasicConfigurationFile(this, "lang/global-lang");
        this.lang = new LanguageConfigurationFile(this, "lang/lang");

        this.scoreboardConfig = new BasicConfigurationFile(this, "features/scoreboard");
        this.leaderboardConfig = new BasicConfigurationFile(this, "features/leaderboard");
        this.hotbarConfig = new BasicConfigurationFile(this, "features/hotbar");
        this.abilityConfig = new BasicConfigurationFile(this, "features/ability");
        this.pearlConfig = new BasicConfigurationFile(this, "features/pearl");

        this.kiteditorConfig = new BasicConfigurationFile(this, "settings/kiteditor");
        this.coloredRanksConfig = new BasicConfigurationFile(this, "settings/colored-ranks");
        this.eventsConfig = new BasicConfigurationFile(this, "settings/events");
        this.menuConfig = new BasicConfigurationFile(this, "settings/menu");
        this.npcConfig = new BasicConfigurationFile(this, "settings/npc");

        this.tabEventConfig = new BasicConfigurationFile(this, "tablist/event");
        this.tabLobbyConfig = new BasicConfigurationFile(this, "tablist/lobby");
        this.tabFFAConfig = new BasicConfigurationFile(this, "tablist/ffa");
        this.tabSingleFFAFightConfig = new BasicConfigurationFile(this, "tablist/SingleFFAFight");
        this.tabSingleTeamFightConfig = new BasicConfigurationFile(this, "tablist/SingleTeamFight");
        this.tabPartyFFAFightConfig = new BasicConfigurationFile(this, "tablist/PartyFFAFight");
        this.tabPartyTeamFightConfig = new BasicConfigurationFile(this, "tablist/PartyTeamFight");

        if (mainConfig.getString("SAVE_METHOD").equals("FILE") || mainConfig.getString("SAVE_METHOD").equals("FLATFILE")) {
            this.playersConfig = new BasicConfigurationFile(this, "cache/players");
            this.clansConfig = new BasicConfigurationFile(this, "features/clans");
        }
    }

    private void registerNameTags() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Initializing nametags for cPractice"));
        GxNameTag.registerProvider(new cPracticeTags());
    }

    private void loadSaveMethod() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Initializing save method for cPractice"));

        switch (mainConfig.getString("SAVE_METHOD")) {
            case "MONGO": case "MONGODB":
                Profile.iProfile = new MongoDBIProfile();
                break;
            case "FLATFILE": case "FILE":
                Profile.iProfile = new FlatFileIProfile();
                break;
        }

        if (Profile.getIProfile() instanceof MongoDBIProfile) {
            try {
                if (databaseConfig.getBoolean("MONGO.URI")) {
                    this.mongoConnection = new MongoConnection(databaseConfig.getString("MONGO.URI_LINK"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "         &4&lSuccessfully initialized MongoDB"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "        &cMongoDB initialized using URI String"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "                &4&lLoading cPractice"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));

                } else if (databaseConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
                    this.mongoConnection = new MongoConnection(
                            databaseConfig.getString("MONGO.HOST"),
                            databaseConfig.getInteger("MONGO.PORT"),
                            databaseConfig.getString("MONGO.AUTHENTICATION.USERNAME"),
                            databaseConfig.getString("MONGO.AUTHENTICATION.PASSWORD"),
                            databaseConfig.getString("MONGO.AUTHENTICATION.DATABASE"));

                    Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "         &4&lSuccessfully initialized MongoDB"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "      &cMongoDB initialized using no authentication"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "                &4&lLoading cPractice"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
                } else {
                    this.mongoConnection = new MongoConnection(
                            databaseConfig.getString("MONGO.HOST"),
                            databaseConfig.getInteger("MONGO.PORT"),
                            databaseConfig.getString("MONGO.DATABASE"));

                    Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "         &4&lSuccessfully initialized MongoDB"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "      &cMongoDB initialized using authentication"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "                &4&lLoading cPractice"));
                    Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
                }
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
                Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "            &4&lMongo Internal Error"));
                Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "        &cMongo is not setup correctly!"));
                Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "      &cPlease check your mongo and try again."));
                Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "              &4&lDisabling cPractice"));
                Bukkit.getConsoleSender().sendMessage(CC.translate(CC.CHAT_BAR));
                Bukkit.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
    }

    private void runTasks() {
        TaskUtil.runTimer(() -> Bukkit.getOnlinePlayers().forEach(player -> Bukkit.getOnlinePlayers().forEach(other -> TaskUtil.run(() -> GxNameTag.reloadPlayer(player, other)))), 20L, 20L);
        TaskUtil.runTimer(new ClassTask(), 5L, 5L);
        TaskUtil.runTimer(new BardEnergyTask(), 15L, 20L);
        TaskUtil.runTimer(() ->
            Profile.getProfiles().values().stream()
                    .filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
                    .filter(profile -> profile.getRematchData() != null)
                    .forEach(profile -> profile.getRematchData().validate())
                , 20L, 20L);
    }

    private void setUpWorld() {
        // Set the difficulty for each world to HARD
        // Clear the droppedItems for each world
        getServer().getWorlds().forEach(world -> {
            world.setDifficulty(Difficulty.HARD);
            world.setGameRuleValue("doDaylightCycle", "false");
            getEssentials().clearEntities(world);
        });
    }

    private void removeCrafting() {
        Arrays.asList(
                Material.WORKBENCH,
                Material.STICK,
                Material.WOOD_PLATE,
                Material.WOOD_BUTTON,
                Material.SNOW_BLOCK,
                Material.STONE_BUTTON
        ).forEach(InventoryUtil::removeCrafting);
    }

    private void registerListeners() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Initializing listeners for cPractice"));

        Arrays.asList(
                new KitEditorListener(),
                new PartyListener(),
                new ProfileListener(),
                new MatchListener(),
                new MatchPearlListener(),
                new MatchPlayerListener(),
                new MatchBuildListener(),
                new MatchSpecialListener(),
                new QueueListener(),
                new ArenaListener(),
                new EventGameListener(),
                new BardListener(),
                new ArcherClass(),
                new RogueClass(),
                new ClanListener(),
                new EssentialsListener(),
                new MenuListener(),
                new ChatListener(),
                new LeaderboardListener(),
                new TournamentListener(),
                new FFAListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new LagRunnable(), 100L, 1L);

        if (getMainConfig().getBoolean("MOD_MODE")) getServer().getPluginManager().registerEvents(new ModmodeListener(), this);
    }

    public void registerCommands() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Initializing commands for cPractice"));

        new CommandManager(this);
        if (mainConfig.getBoolean("MESSAGE-REPLY-BOOLEAN")) {
            new MessageCommand();
            new ReplyCommand();
        }
        new CancelAllMatchesCommand();
        new StreamingCommand();
        new CosmeticsCommand();
        new ShopCommand();
        new CoinsCommand();
        new CoinsStaffCommand();
        new KillEffectCommand();
        new MatchListCommand();
        new TrailEffectCommand();
        new TrollCommand();
        new FFACommand();
        new ArenaCommand();
        new ArenasCommand();
        new DuelCommand();
        new DuelRoundCommand();
        new DuelAcceptCommand();
        new EventCommand();
        new EventHostCommand();
        new EventsCommand();
        new EventMapVoteCommand();
        new RematchCommand();
        new SpectateCommand();
        new CancelMatchCommand();
        new StopSpectatingCommand();
        new FlyCommand();
        new ViewMatchCommand();
        new PartyCommand();
        new KitCommand();
        new KitsCommand();
        new ViewInventoryCommand();
        new ToggleScoreboardCommand();
        new ToggleSpectatorsCommand();
        new ToggleDuelRequestsCommand();
        new ClanCommand();
        new TournamentCommand();
        new ClearCommand();
        new DayCommand();
        new GameModeCommand();
        new AbilityCommand();
        new cPracticeCommand();
        new HealCommand();
        new LangCommand();
        new LocationCommand();
        new MoreCommand();
        new NightCommand();
        new PingCommand();
        new RenameCommand();
        new SetSlotsCommand();
        new SetSpawnCommand();
        new ShowAllPlayersCommand();
        new ShowPlayerCommand();
        new SpawnCommand();
        new SudoAllCommand();
        new SudoCommand();
        new SunsetCommand();
        new TeleportWorldCommand();
        new OptionsCommand();
        new ClearChatCommand();
        new SlowChatCommand();
        new MuteChatCommand();
        new EloCommand();
        new SetEloCommand();
        new ResetEloCommand();
        new AdminInformationCommand();
        new CreateWorldCommand();
        new StatsCommand();
        new LeaderboardCommand();
        new RankedCommand();
        new UnRankedCommand();
        new HCFClassCommand();
        new ResetCommand();
        new ToggleGlobalChatCommand();
        new TogglePrivateMessagesCommand();
        new ToggleScoreboardCommand();
        new ToggleSoundsCommand();
        new ToggleSpectatorsCommand();
        new BotFight();
        if (getMainConfig().getBoolean("MOD_MODE")) new StaffModeCommand();
    }

    private void loadEssentials() {
        this.bridgeRounds = getMainConfig().getInteger("MATCH.ROUNDS_BRIDGE");
        this.rankedSumoRounds = getMainConfig().getInteger("MATCH.ROUNDS_RANKED_SUMO");
    }

    public static cPractice get(){
        return getPlugin(cPractice.class);
    }
}

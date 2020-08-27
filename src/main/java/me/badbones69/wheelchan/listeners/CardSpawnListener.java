package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.ClaimedCard;
import me.badbones69.wheelchan.api.objects.DespawnCard;
import me.badbones69.wheelchan.api.objects.SpawnCard;
import me.badbones69.wheelchan.events.SpawnTimer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;

public class CardSpawnListener extends ListenerAdapter {
    
    private WheelChan wheelChan = WheelChan.getInstance();
    private CardTracker cardTracker = CardTracker.getInstance();
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        MessageChannel logging = e.getGuild().getTextChannelById(cardTracker.getLoggingChannelID());
        if (wheelChan.isShoob(user)) {
            SpawnCard card;
            if (!message.getEmbeds().isEmpty()) {
                MessageEmbed embed = message.getEmbeds().get(0);
                //New card spawns
                if (embed.getTitle() != null && isSpawnMessage(embed.getTitle())) {
                    if (SpawnPackListener.isSpawnPackMessage(message.getContentDisplay())) {
                        card = new SpawnCard(embed);
                        cardTracker.newSpawnpackCard(card);
                        System.out.println("Spawn Pack: " + card);
                    } else {
                        card = new SpawnCard(embed);
                        cardTracker.newSpawnCard(card);
                        if (!wheelChan.isTesting()) new SpawnTimer(e.getChannel(), card);
                        System.out.println("Spawned: " + card);
                    }
                    //Card is claimed
                } else if (embed.getDescription() != null && isClaimedMessage(embed.getDescription())) {
                    //Card is normal spawn.
                    if (embed.getDescription().toLowerCase().contains(cardTracker.getLatestSpawnCard().getCharacterName().toLowerCase())) {
                        cardTracker.setSpawnCardClaim(true);
                        card = cardTracker.getLatestSpawnCard();
                        System.out.println("Claimed: " + card);
                        User claimed = wheelChan.getJDA().getUserById(embed.getDescription().split("<@")[1].split(">")[0]);
                        int issue = Integer.parseInt(embed.getDescription().split("#: ")[1].split("\\.")[0].replace("`", ""));
                        if (!wheelChan.isTesting()) logging.sendMessage(new ClaimedCard(card.getCardURL(), card, issue, claimed, true).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
                        //Card is spawn pack
                    } else if (embed.getDescription().toLowerCase().contains(cardTracker.getLatestPackCard().getCharacterName().toLowerCase())) {
                        card = cardTracker.getLatestPackCard();
                        System.out.println("Claimed Spawn Pack: " + card);
                        User claimed = wheelChan.getJDA().getUserById(embed.getDescription().split("<@")[1].split(">")[0]);
                        int issue = Integer.parseInt(embed.getDescription().split("#: ")[1].split("\\.")[0].replace("`", ""));
                        if (!wheelChan.isTesting()) logging.sendMessage(new ClaimedCard(card.getCardURL(), card, issue, claimed, false).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
                    }
                }
                //Card despawns
            } else if (isDespawnMessage(message.getContentDisplay())) {
                cardTracker.setSpawnCardClaim(false);
                card = cardTracker.getLatestSpawnCard();
                System.out.println("Despawned: " + card);
                if (!wheelChan.isTesting()) logging.sendMessage(new DespawnCard(card.getCardURL(), card).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
            }
        }
    }
    
    private boolean isSpawnMessage(String message) {
        return message.contains("Tier: ");
    }
    
    public boolean isClaimedMessage(String message) {
        return message.contains("got the card!");
    }
    
    private boolean isDespawnMessage(String message) {
        return message.contains("Looks like nobody got the card this time.");
    }
    
}
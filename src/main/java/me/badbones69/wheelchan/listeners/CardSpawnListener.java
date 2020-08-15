package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CardSpawnListener extends ListenerAdapter {
    
    private WheelChan wheelChan = WheelChan.getInstance();
    private CardTracker cardTracker = CardTracker.getInstance();

//    @Override
//    public void onMessageReceived(MessageReceivedEvent e) {
//        User user = e.getAuthor();
//        Message message = e.getMessage();
//        MessageChannel logging = e.getGuild().getTextChannelById(cardTracker.getLoggingChannelID());
//        if (wheelChan.isShoob(user) && !SpawnPackListener.isSpawnPackMessage(message.getContentDisplay())) {
//            SpawnCard card;
//            if (!message.getEmbeds().isEmpty()) {
//                MessageEmbed embed = message.getEmbeds().get(0);
//                //New card spawns
//                if (embed.getTitle() != null && isSpawnMessage(embed.getTitle())) {
//                    card = new SpawnCard(embed);
//                    cardTracker.newSpawnCard(card);
//                    System.out.println("Spawned: " + card);
//                    //Card is claimed
//                } else if (embed.getDescription() != null && isClaimedMessage(embed.getDescription())) {
//                    cardTracker.setSpawnCardClaim(true);
//                    card = cardTracker.getLatestCard();
//                    System.out.println("Claimed: " + card);
//                    User claimed = wheelChan.getJDA().getUserById(embed.getDescription().split("<@")[1].split(">")[0]);
//                    int issue = Integer.parseInt(embed.getDescription().split("#: ")[1].split("\\.")[0].replace("`", ""));
//                    logging.sendMessage(new ClaimedCard(card.getCardURL(), card, issue, claimed).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
//                }
//                //Card despawns
//            } else if (isDespawnMessage(message.getContentDisplay())) {
//                cardTracker.setSpawnCardClaim(false);
//                card = cardTracker.getLatestCard();
//                System.out.println("Despawned: " + card);
//                logging.sendMessage(new DespawnCard(card.getCardURL(), card).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
//            }
//        }
//    }
    
    private boolean isSpawnMessage(String message) {
        return message.contains("Tier: ");
    }
    
    private boolean isClaimedMessage(String message) {
        return message.contains("got the card!") && message.toLowerCase().contains(cardTracker.getLatestCard().getCharacterName().toLowerCase());
    }
    
    private boolean isDespawnMessage(String message) {
        return message.contains("Looks like nobody got the card this time.");
    }
    
}
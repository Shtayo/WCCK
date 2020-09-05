package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        Guild guild = e.getGuild();
        EmbedBuilder embed;
        if (server.isSensei(e.getAuthor(), e.getGuild())) {
            wheelChan.clearAllSenpais();
            embed = Messages.CLEAR_SENPAIS.getMessage(guild);
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}
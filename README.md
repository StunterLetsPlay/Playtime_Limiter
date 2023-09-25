## THIS PROJECT IS NO LONGER UPDATED
Searching for a Updated Version ?
Head over to SkinnyDevi's Port: https://github.com/SkinnyDevi/playtimelimiter

# Playtime Limiter
You hate having Players Speedrun the Game in 6.12 Seconds whilst all other 99 Players are still farming Wood?
Do you want to set a limit to how long Players can play on your Server?

Then Playtime Limiter is the Mod you need!

Playtime Limiter is a Server-Side ONLY Plugin, which does absolutely nothing when you install it in your Client.
Just put the Mod File into the /mods/ Folder in your Server, Restart and you're done!

**NOTE!** This Mod ``does not`` keep track of your total Playtime!

## Commands
``/playtime`` > Displays how much time you have left until you get kicked!

``/playtime reload`` > Reloads the Config **You need OP to perform this Command!**

``/playtime reset <Name>`` > Resets the Playtime of an User **You need OP to perform this Command!**

## Config
Located inside of ``/config/playtimelimiter.cfg``
```
# Configuration file

config {

    ##########################################################################################################
    # playtime
    #--------------------------------------------------------------------------------------------------------#
    # General Settings for Playtime
    ##########################################################################################################

    playtime {
        # The Length (IN SECONDS) which a Player can play on your Server before getting Kicked with a Timeout
        # 
        # Default is 3 Hours [range: 1 ~ 604800, default: 10800]
        I:"Playtime Length"=10800

        # The Length (IN SECONDS) which a Player has to wait, after being kicked from the server
        # to be able to join again.
        # 
        # Default is 12 Hours [range: 1 ~ 604800, default: 43200]
        I:"Playtime Timeout"=43200

        # If the Playtime should be reset after once the Server detects
        # that a new (IRL) Day has started since the last time the Player has joined [default: true]
        B:"Reset Playtime at Midnight"=true

        # If the Playtime should be reset after the Player Reconnects [default: false]
        B:"Reset Playtime on Reconnect"=false

        # If Players should get warned before they get kicked
        # They would get warned: {30, 15, 10, 5, 3, 1} Minute(s) before being Kicked! [default: true]
        B:"Warn low Playtime Kick"=true
    }

}
```

## Don't be a Jerk
Don't:

> Claim this Mod has been made by you

> Copy-Paste like 90% of my Source Code (If you want you can copy some, but then please give Credit!)

> Configure the Settings to make your Server a living hell

## Side-Note
This is actually the first Forge Mod I've ever written, and I gotta say, it's different from Spigot Coding, but really nice aswell.

All this happened because [Mareex](https://twitter.com/mareex__) has Commissioned me to do so, thank you very much again! This has been a lot of experience that I was able to gather and I hope you all like this Mod.

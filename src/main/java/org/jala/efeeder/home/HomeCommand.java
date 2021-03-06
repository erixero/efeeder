package org.jala.efeeder.home;

import org.jala.efeeder.api.command.Command;
import org.jala.efeeder.api.command.CommandUnit;
import org.jala.efeeder.api.command.In;
import org.jala.efeeder.api.command.Out;
import org.jala.efeeder.api.command.impl.DefaultOut;

/**
 * Created by alejandro on 09-09-16.
 */
@Command
public class HomeCommand implements CommandUnit {
    @Override
    public Out execute(In parameters) throws Exception {
        return new DefaultOut().forward("home/home.jsp");
    }
}

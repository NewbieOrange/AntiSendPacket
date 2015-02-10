package com.mengcraft.injector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BannerLogger
{
    private final static BannerLogger LOGGER = new BannerLogger();
    
    private final File file = new File("injector-banner.log");
    private Writer writer;
    
    public void log(String in)
    {
        try
        {
            this.writer = new FileWriter(file, true);
            this.writer.write(getDateString() + in);
            this.writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private String getDateString()
    {
        return "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + "] ";
    }
    
    public static BannerLogger getLogger()
    {
        return LOGGER;
    }
}

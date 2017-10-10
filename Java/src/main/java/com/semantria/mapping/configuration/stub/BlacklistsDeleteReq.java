package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.BlacklistItem;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="blacklist")
public final class BlacklistsDeleteReq
{
    private List<String> blacklist = new ArrayList<String>();

    public BlacklistsDeleteReq() {}

    public BlacklistsDeleteReq(List<BlacklistItem> blacklistItems)
    {
        if( blacklistItems != null && !blacklistItems.isEmpty())
        {
            for (BlacklistItem blacklistItem : blacklistItems)
            {
                this.blacklist.add(blacklistItem.getId());
            }
        }
    }

    @XmlElement(name="id")
    public List<String> getBlacklist() { return blacklist; }

    public void setBlacklist(List<String> blacklist) { this.blacklist = blacklist; }
}

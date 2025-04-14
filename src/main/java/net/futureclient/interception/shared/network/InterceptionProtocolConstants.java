package net.futureclient.interception.shared.network;

import java.nio.charset.StandardCharsets;
import net.futureclient.interception.shared.util.ByteUtil;

public final class InterceptionProtocolConstants {
   public static final String MAGIC_HEX = "2446555455524524";
   public static final long MAGIC_LONG = Long.parseLong("2446555455524524", 16);
   public static final String MAGIC_STRING = new String(ByteUtil.hexStringToBytes("2446555455524524"), StandardCharsets.UTF_8);
}

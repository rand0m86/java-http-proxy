package com.github.rand0m86.filters.hop_by_hop;

import com.github.rand0m86.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

abstract class HopByHopHeaderUtil {

    static final Set<String> HOP_BY_HOP_HEADERS = new HashSet<>(Arrays.asList(
            "connection", "keep-alive", "proxy-authenticate", "proxy-authorization",
            "te", "trailer", "transfer-encoding", "upgrade"
    ));

    static boolean isHopByHopHeader(String header) {
        return !StringUtils.isEmpty(header) && HOP_BY_HOP_HEADERS.contains(header.toLowerCase());
    }

}

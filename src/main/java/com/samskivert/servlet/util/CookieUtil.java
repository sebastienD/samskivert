//
// samskivert library - useful routines for java programs
// Copyright (C) 2001-2011 Michael Bayne, et al.
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package com.samskivert.servlet.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility methods for dealing with cookies.
 */
public class CookieUtil
{
    /**
     * Get the cookie of the specified name, or null if not found.
     */
    public static Cookie getCookie (HttpServletRequest req, String name)
    {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int ii=0, nn=cookies.length; ii < nn; ii++) {
                if (cookies[ii].getName().equals(name)) {
                    return cookies[ii];
                }
            }
        }

        return null; // not found
    }

    /**
     * Get the value of the cookie for the cookie of the specified name, or null if not found.
     */
    public static String getCookieValue (HttpServletRequest req, String name)
    {
        Cookie c = getCookie(req, name);
        return (c == null) ? null : c.getValue();
    }

    /**
     * Clear the cookie with the specified name.
     */
    public static void clearCookie (HttpServletResponse rsp, String name)
    {
        Cookie c = new Cookie(name, "x");
        c.setPath("/");
        c.setMaxAge(0);
        rsp.addCookie(c);
    }

    /**
     * Sets the domain of the specified cookie to the server name associated with the supplied
     * request minus the hostname (ie. <code>www.samskivert.com</code> becomes
     * <code>.samskivert.com</code>).
     */
    public static void widenDomain (HttpServletRequest req, Cookie cookie)
    {
        String server = req.getServerName();
        int didx = server.indexOf(".");
        // if no period was found (e.g. localhost) don't set the domain
        if (didx == -1) {
            return;
        }
        // if two or more periods are found (e.g. www.domain.com) strip up to the first one
        if (server.indexOf(".", didx+1) != -1) {
            cookie.setDomain(server.substring(didx));
        } else {
            // ...otherwise prepend a "." because we're seeing something like "domain.com"
            cookie.setDomain("." + server);
        }
    }
}

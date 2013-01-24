/*
 *
 *  * cdoj, UESTC ACMICPC Online Judge
 *  * Copyright (c) 2013 fish <@link lyhypacm@gmail.com>,
 *  * 	mzry1992 <@link muziriyun@gmail.com>
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package cn.edu.uestc.acmicpc.util.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple exception implement for JSON inside.
 *
 * @author <a href="mailto:lyhypacm@gmail.com">fish</a>
 * @version 1
 */
public class ValidatorException extends Exception {
    private Map<String, String> json;

    /**
     * Constructor for multiple keys and values
     *
     * @param json JSON Mapping
     */
    public ValidatorException(Map<String, String> json) {
        super();
        this.json = json;
    }

    public ValidatorException(String key, String value) {
        super();
        json = new HashMap<String, String>();
        json.put(key, value);
    }

    public Map<String, String> getJson() {
        return json;
    }
}
